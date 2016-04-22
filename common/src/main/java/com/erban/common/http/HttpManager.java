package com.erban.common.http;

import android.text.TextUtils;
import android.util.SparseArray;

import com.erban.common.http.HttpMsg.HttpMsgListener;
import com.erban.common.http.HttpMsg.Method;
import com.erban.common.http.HttpMsg.ResponseType;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * HTTP 请求任务的封装。使用HttpURLConnection实现。
 * 具有4个工作线程执行HTTP请求任务队列。
 */
public class HttpManager {

    private boolean running = true;
    private final static int WORKER_NUMBER = 4;
    private Worker[] workers = new Worker[WORKER_NUMBER];
    public static boolean PROCESS_PROXY = true;
    private static final int CONNECT_TIME_OUT = 1 * 20 * 1000;
    private static final int READ_TIME_OUT = 1 * 20 * 1000;
    private static final String ENCODING = "UTF-8";
    private static final int MAX_REDIRECT_NUM = 5;

    private static HttpManager sInstance = null;

    public static HttpManager getInstance() {
        if (null == sInstance) {
            synchronized (HttpManager.class) {
                if (null == sInstance) {
                    sInstance = new HttpManager();
                }
            }
        }

        return sInstance;
    }

    private HttpManager() {
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    private PriorityQueue<HttpMsg> queue = new PriorityQueue<HttpMsg>(11,
            new Comparator<HttpMsg>() {
                @Override
                public int compare(HttpMsg lhs,
                                   HttpMsg rhs) {
                    if (lhs.getPriority() > rhs.getPriority()) {
                        return -1;
                    } else if (lhs.getPriority() < rhs.getPriority()) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
    private SparseArray<HttpMsg> array = new SparseArray<HttpMsg>();
    private static int idGenerator = 1000;
    private Object lock = new Object();

    /**
     * 异步网络请求。每次请求创建新的HTTP connection。
     */
    public int send(HttpMsg msg) {
        synchronized (lock) {
            int id = idGenerator++;
            msg.setId(id);
            array.put(id, msg);
            queue.add(msg);
            lock.notify();
            return id;
        }
    }

    /**
     * 同步网络请求。每次请求创建新的HTTP connection。
     */
    public static void sendMsg(HttpMsg msg) throws HttpException {
        sendMsgInternal(msg);
    }

    public void destroy() {
        sInstance = null;
        running = false;
        cancelAll();
        workers = null;
    }

    public void cancel(int msgId) {
        synchronized (lock) {
            HttpMsg msg = array.get(msgId);
            if (msg != null) {
                array.remove(msgId);
                queue.remove(msg);
            } else {
                for (int i = 0; i < workers.length; i++) {
                    if (workers[i].curMsg != null && workers[i].curMsg.getId() == msgId) {
                        workers[i].curMsg.setCanceled(true);
                    }
                }
            }
            lock.notify();
        }

    }

    public void cancelMsgInQuery(int msgId) {
        synchronized (lock) {
            HttpMsg msg = array.get(msgId);
            if (msg != null) {
                array.remove(msgId);
                queue.remove(msg);
            }
            lock.notify();
        }

    }

    public void cancelAll() {
        synchronized (lock) {
            array.clear();
            queue.clear();
            for (int i = 0; i < workers.length; i++) {
                if (workers[i].curMsg != null) {
                    workers[i].curMsg.setCanceled(true);
                }
            }
            lock.notifyAll();
        }
    }

    private static void sendMsgInternal(HttpMsg msg) throws HttpException {
        HttpURLConnection con = null;
        OutputStream os = null;
        InputStream is = null;
        HttpMsgListener listener = msg.getListener();
        try {
            int redirectNum = 0;
            int responseCode;
            while (true) {
                con = getConnection(msg);
                if (msg.isCanceled()) {
                    return;
                }
                if (msg.getMethod() == Method.POST || msg.getMethod() == Method.PUT) {
                    os = con.getOutputStream();
                    makeRequestData(os, msg);
                }
                if (msg.isCanceled()) {
                    return;
                }
                responseCode = con.getResponseCode();
                if (msg.isCanceled()) {
                    return;
                }
                if ((responseCode == HttpURLConnection.HTTP_MOVED_TEMP || responseCode == HttpURLConnection.HTTP_MOVED_PERM)
                        && redirectNum < MAX_REDIRECT_NUM) {
                    String loc = con.getHeaderField("Location");
                    if(!msg.isNeedAutoRedirect()){
                        listener.onResponse(responseCode, null, loc.length(), loc);
                        listener.onResponse(responseCode, null, loc.length(), loc, 0, null);
                        return;
                    }
                    if (loc != null) {
                        try {
                            if (os != null) {
                                os.close();
                            }
                            con.disconnect();
                        } catch (Exception t) {
                        }
                        short type = URI.getRequestType(loc);
                        loc = URI.getFullUrl(loc, type, msg.getRedirectUrl() == null ? msg.getUrl()
                                : msg.getRedirectUrl());
                        msg.setRedirectUrl(loc);
                        redirectNum++;
                        if (msg.isCanceled()) {
                            return;
                        }
                        continue;
                    } else {
                        throw new IOException("Redirect failed!");
                    }
                } else {
                    break;
                }
            }

            if (responseCode == HttpURLConnection.HTTP_OK
                    || responseCode == HttpURLConnection.HTTP_PARTIAL
                    || responseCode == HttpURLConnection.HTTP_NOT_MODIFIED) {
                HashMap<String, String> respHeaders = new HashMap<String, String>();
                int responseLength = 0;
                String key = null;
                int i = 1;
                while ((key = con.getHeaderFieldKey(i++)) != null) {
                    String value = con.getHeaderField(key);
                    respHeaders.put(key, value);
                }
                responseLength = con.getContentLength();
                is = con.getInputStream();
                if (listener != null && msg.getType() == ResponseType.STREAM) {
                    if (msg.isCanceled()) {
                        return;
                    }
                    listener.onResponse(responseCode, respHeaders, responseLength, is);
                    return;
                }

                if (responseCode != HttpURLConnection.HTTP_NOT_MODIFIED) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    int n = 0;
                    while ((n = is.read(buf)) >= 0) {
                        if (msg.isCanceled()) {
                            return;
                        }
                        baos.write(buf, 0, n);
                    }
                    baos.flush();
                    byte[] data = baos.toByteArray();

                    responseLength = data.length;
                    if (msg.getType() == ResponseType.TEXT) {
                        String encode = ENCODING;
                        if (con.getContentType() != null) {
                            encode = getContentCharSet(con.getContentType());
                        }
                        String dataStr = new String(data, encode);
                        if (msg.isCanceled()) {
                            return;
                        }
                        int code = -1;
                        String message = null;
                        if (!TextUtils.isEmpty(dataStr)) {
                            JSONObject response = new JSONObject(dataStr);
                            try {
                                code = response.getInt("code");
                                message = response.optString("msg");
                            }catch (Exception e){

                            }
                            if (code == -7) {
                                //com.erban.broadcast.ACTION_LOGOUT
//                                Intent broadcastIntent = new Intent("com.erban.broadcast.ACTION_LOGOUT");
//                                ApplicationDelegate.getApplication().getApplicationContext().sendBroadcast(broadcastIntent);
                            }
                        }
                        listener.onResponse(responseCode, respHeaders, responseLength, dataStr);
                        listener.onResponse(responseCode, respHeaders, responseLength, dataStr, code, message);

                    } else if (msg.getType() == ResponseType.BINARY) {
                        if (msg.isCanceled()) {
                            return;
                        }

                        listener.onResponse(responseCode, respHeaders, responseLength,
                                data);
                    }
                    return;
                } else {
                    if (listener != null) {
                        if (msg.isCanceled()) {
                            return;
                        }
                        listener.onError(new RespErrCodeException(responseCode, null));
                    }
                    return;
                }
            } else {
                if (listener != null) {
                    listener.onError(new RespErrCodeException(responseCode, null));
                }
                return;
            }
        } catch (SocketTimeoutException e) {
            listener.onSocketTimeOut();
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                //wifi认证返回
                listener.onError(new RespErrCodeException(9999, null));
            }
            throw new NetException(e.toString());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            throw new NetException(e.toString());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }

    private static String getContentCharSet(String contentType) {
        String charset = ENCODING;
        if (contentType != null) {
            String[] s = contentType.split(";");
            if (s != null && s.length > 0) {
                for (String v : s) {
                    if (v.contains("charset") && v.contains("=")) {
                        String[] r = v.split("=");
                        if (r != null && r.length > 1) {
                            if (r[1] == null || r[1].trim().equals("")) {
                                return charset;
                            } else {
                                return r[1].trim();
                            }
                        }
                    }
                }
            }
        }
        return charset;
    }

    /**
     * 获得真正的HTTP 链接。
     */
    private static HttpURLConnection getConnection(HttpMsg msg) throws MalformedURLException, IOException {
        HttpURLConnection conn = null;
        String url = msg.getRedirectUrl();
        if (TextUtils.isEmpty(url)) {
            url = msg.getUrl();
        }

        conn = (HttpURLConnection) new URL(url).openConnection();
        Method method = msg.getMethod();
        conn.setRequestMethod(method.name());
        if (Method.POST.equals(method)
                || Method.PUT.equals(method)) {
            conn.setDoOutput(true);
        }
        conn.setDoInput(true);
        conn.setConnectTimeout(CONNECT_TIME_OUT);
        conn.setReadTimeout(READ_TIME_OUT);
        conn.setInstanceFollowRedirects(false);
        HashMap<String, String> header = new HashMap<>();
        header.put("Connection", "close");
        msg.setHeaders(header);
        Map<String, String> headers = msg.getHeaders();
        if (headers != null && !headers.isEmpty()) {
            Set<Entry<String, String>> entrys = headers.entrySet();
            for (Entry<String, String> entry : entrys) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return conn;
    }

    /**
     * 组装真正的HTTP请求数据。
     */
    private static void makeRequestData(OutputStream os,
                                        HttpMsg msg) throws UnsupportedEncodingException,
            IOException {
        if (msg.getReqTextData() != null && msg.getReqTextData().length() > 0) {
            os.write(msg.getReqTextData().getBytes(ENCODING));
        } else if (msg.getReqBinaryData() != null && msg.getReqBinaryData().length > 0) {
            os.write(msg.getReqBinaryData());
        }
    }

    /**
     * 工作线程的封装。
     */
    private class Worker extends Thread {

        private HttpMsg curMsg = null;

        @Override
        public void run() {
            while (running) {
                HttpMsg msg = null;
                synchronized (lock) {
                    while (queue.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!running) {
                            return;
                        }
                    }
                    msg = queue.poll();
                    array.remove(msg.getId());
                    curMsg = msg;
                }
                if (msg.getListener() != null) {
                    msg.getListener().beforeSend();
                }
                int retry = 0;
                HttpException exception = null;
                boolean taskDone = false;
                while (retry <= msg.getRetry()) {
                    try {
                        HttpManager.sendMsgInternal(msg);
                        taskDone = true;
                        break;
                    } catch (HttpException e) {
                        exception = e;
                        retry++;
                    }
                }

                if (msg.getListener() != null) {
                    msg.getListener().afterSend();
                }

                if (!taskDone) {
                    HttpMsgListener listener = msg.getListener();
                    if (listener != null) {
                        listener.onError(exception);
                    }
                }
                curMsg = null;
            }
        }
    }

    /*private static HttpResponse sendPOSTRequestHttpClient(String path,
                                                          Map<String, String> params) throws Exception {
        // 封装请求参数
        List<NameValuePair> pair = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pair.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        // 把请求参数变成请求体部分
        UrlEncodedFormEntity uee = new UrlEncodedFormEntity(pair, "utf-8");
        // 使用HttpPost对象设置发送的URL路径
        HttpPost post = new HttpPost(path);
        // 发送请求体
        post.setEntity(uee);

        post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        // 创建一个浏览器对象，以把POST对象向服务器发送，并返回响应消息
        HttpResponse response = new DefaultHttpClient().execute(post);
        //如果想要在DefaultHttpClient里设置一些必要的参数可以调用以下的getHttpClient方法
        // DefaultHttpClient dhc = new DefaultHttpClient();
        if (response.getStatusLine().getStatusCode() == 200) {
        }
        return response;
    }

    public static HttpClient getHttpClient() {
        // 创建 HttpParams 以用来设置 HTTP 参数（这一部分不是必需的）
        HttpParams httpParams = new BasicHttpParams();
        // 设置连接超时和 Socket 超时，以及 Socket 缓存大小
        HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
        HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        // 设置重定向，缺省为 true
        HttpClientParams.setRedirecting(httpParams, true);
        // 设置 user agent
        String userAgent = "Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16";
        HttpProtocolParams.setUserAgent(httpParams, userAgent);
        // 创建一个 HttpClient 实例
        // 注意 HttpClient httpClient = new HttpClient(); 是Commons HttpClient
        // 中的用法，在 Android 1.5 中我们需要使用 Apache 的缺省实现 DefaultHttpClient
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        return httpClient;
    }*/

}
