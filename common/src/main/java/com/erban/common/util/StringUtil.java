package com.erban.common.util;


import com.erban.common.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * 判断传入的字符串是否为空串。
     *
     * @param s
     * @return
     */
    public static boolean isEmpty(String s) {
        return (s == null || s.length() == 0);
    }

    /**
     * 将数据流转换成对应字符集的字符串
     *
     * @param stream
     * @param charset
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String inputStream2String(InputStream stream,
                                            String charset)
            throws UnsupportedEncodingException {
        String dataStr = "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 8];
        int n = 0;
        try {
            while ((n = stream.read(buf)) >= 0) {
                baos.write(buf, 0, n);
            }
            baos.flush();
            byte[] data = baos.toByteArray();
            dataStr = new String(data, charset);
        } catch (EOFException e) {
            try {
                baos.flush();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            byte[] data = baos.toByteArray();
            dataStr = new String(data, charset);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(baos);
        }

        return dataStr;
    }

    /**
     * 检测给定的字符串，与给定的正则式，是否匹配。
     */
    public static boolean matcher(String s,
                                  String pattern) {
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);
        Matcher matcher = p.matcher(s);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Checks if the string contains only ASCII printable characters.</p>
     * <p/>
     * <p><code>null</code> will return <code>false</code>.
     * An empty String ("") will return <code>true</code>.</p>
     * <p/>
     * <pre>
     * StringUtils.isAsciiPrintable(null)     = false
     * StringUtils.isAsciiPrintable("")       = true
     * StringUtils.isAsciiPrintable(" ")      = true
     * StringUtils.isAsciiPrintable("Ceki")   = true
     * StringUtils.isAsciiPrintable("ab2c")   = true
     * StringUtils.isAsciiPrintable("!ab-c~") = true
     * StringUtils.isAsciiPrintable("\u0020") = true
     * StringUtils.isAsciiPrintable("\u0021") = true
     * StringUtils.isAsciiPrintable("\u007e") = true
     * StringUtils.isAsciiPrintable("\u007f") = false
     * StringUtils.isAsciiPrintable("Ceki G\u00fclc\u00fc") = false
     * </pre>
     *
     * @param str the string to check, may be null
     * @return <code>true</code> if every character is in the range
     * 32 thru 126
     * @since 2.1
     */
    public static boolean isAsciiPrintable(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (isAsciiPrintable(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks whether the character is ASCII 7 bit printable.</p>
     * <p/>
     * <pre>
     *   CharUtils.isAsciiPrintable('a')  = true
     *   CharUtils.isAsciiPrintable('A')  = true
     *   CharUtils.isAsciiPrintable('3')  = true
     *   CharUtils.isAsciiPrintable('-')  = true
     *   CharUtils.isAsciiPrintable('\n') = false
     *   CharUtils.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(char ch) {
        return ch >= 32 && ch < 127;
    }

    public static String join(Object[] elements,
                              CharSequence separator) {
        return join(Arrays.asList(elements), separator);
    }

    public static String join(Iterable<? extends Object> elements,
                              CharSequence separator) {
        StringBuilder builder = new StringBuilder();

        if (elements != null) {
            Iterator<? extends Object> iter = elements.iterator();
            if (iter.hasNext()) {
                builder.append(String.valueOf(iter.next()));
                while (iter.hasNext()) {
                    builder.append(separator).append(String.valueOf(iter.next()));
                }
            }
        }

        return builder.toString();
    }

    public static long string2Long(String sData, long def) {
        long data = def;
        if (sData != null) {
            try {
                data = Long.valueOf(sData);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return data;
    }
}
