
package com.erban.common.io;


import android.text.TextUtils;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.channels.FileLock;


public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 删除文件夹
     *
     * @param f
     */
    public static void deleteDir(File f) {
        if (f != null && f.exists() && f.isDirectory()) {
            File[] files = null;
            try {
                files = f.listFiles();
            } catch (Throwable e) {
            }
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory())
                        deleteDir(file);
                    file.delete();
                }
            }
            f.delete();
        }
    }

    public static Object deserializeFromFile(File file) {
        if (file == null || !file.exists())
            return null;

        Object o = null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        FileLock lock = null;

        try {
            fis = new FileInputStream(file);
            lock = fis.getChannel().lock(0L, Long.MAX_VALUE, true);
            ois = new ObjectInputStream(fis);
            o = ois.readUnshared();
            return o;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            file.delete();
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
            // java.lang.NoSuchMethodError: create
            // at java.io.ObjectInputStream.<clinit>(ObjectInputStream.java:2034)
        } finally {
            if (lock != null)
                try {
                    lock.release();
                } catch (IOException e) {
                }
            try {
                if (ois != null)
                    ois.close();
            } catch (IOException e) {
            }
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    public static boolean serializeToFile(Serializable o,
                                          File file) {
        if (o == null || file == null)
            return false;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }

        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        FileLock lock = null;

        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            lock = fos.getChannel().lock();

            oos.writeUnshared(o);
            oos.flush();
            fos.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError oom) {
            oom.printStackTrace();
        } finally {
            if (oos != null)
                try {
                    oos.reset();
                } catch (IOException e) {
                }
            if (lock != null)
                try {
                    lock.release();
                } catch (IOException e) {
                }
            try {
                if (oos != null)
                    oos.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static boolean isFileExist(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return false;
        }


        File file = new File(fileName);
        if (file.exists() && !file.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public static void copy(File src,
                            File dst) {
        try {
            File parent = new File(dst.getParent());
            if (!parent.exists()) {
                parent.mkdirs();
            }
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
