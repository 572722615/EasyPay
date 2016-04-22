package com.erban.common.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class IOUtils {

    private static final String TAG = "IOUtils";

    public static void touchFile(File file) {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            file.setLastModified(System.currentTimeMillis());
        }
    }

    public static FileInputStream openAlwaysFileInputStream(File file)
            throws FileNotFoundException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            touchFile(file);
            return new FileInputStream(file);
        }
    }

    public static int readFully(InputStream inputStream,
                                byte[] buffer)
            throws IOException {
        return readFully(inputStream, buffer, 0, buffer.length);
    }

    public static int readFully(InputStream inputStream,
                                byte[] buffer,
                                int offset,
                                int byteCount) throws IOException {
        int totalRead = 0;
        while (totalRead < byteCount) {
            int read = inputStream.read(buffer, offset + totalRead, byteCount
                    - totalRead);
            if (read < 0)
                return -1;

            totalRead += read;
        }

        return totalRead;
    }

    public static void skipFully(InputStream inputStream,
                                 long byteCount)
            throws IOException {
        while (byteCount > 0) {
            long skipped = inputStream.skip(byteCount);
            byteCount -= skipped;
        }
    }

    public static void closeQuietly(Closeable c) {
        if (c == null)
            return;

        try {
            c.close();
        } catch (Exception e) {
        }
    }

    public static String toString(InputStream inputStream,
                                  String string)
            throws IOException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }

            return sb.toString();

        } finally {
            IOUtils.closeQuietly(reader);
            reader = null;
        }
    }

    public static void write(String text,
                             OutputStream outputStream)
            throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(text);
        } finally {
            IOUtils.closeQuietly(writer);
            writer = null;
        }
    }

    public static void copy(InputStream inputStream,
                            OutputStream outputStream)
            throws IOException {
        byte[] buffer = new byte[8 * 1024];

        int read = inputStream.read(buffer);
        while (read >= 0) {
            outputStream.write(buffer, 0, read);
            read = inputStream.read(buffer);
        }
    }

    public static byte[] readFully(InputStream is) {
        if (is == null) {
            return null;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 5];
        try {
            while (is.read(buffer) != -1) {
                bos.write(buffer);
            }
        } catch (IOException e) {
        }
        return bos.toByteArray();
    }


}
