/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.tools;

import java.io.InputStream;
import java.io.OutputStream;

public class SyncPipe implements Runnable {
    private final OutputStream outputStream;
    private final InputStream inputStream;

    private final StringBuffer result = new StringBuffer();

    public SyncPipe(InputStream istrm, OutputStream ostrm) {
        inputStream = istrm;
        outputStream = ostrm;
    }

    public void run() {
        try {
            final byte[] buffer = new byte[1024];
            for (int length; (length = inputStream.read(buffer)) != -1; ) {
                outputStream.write(buffer, 0, length);
                result.append(new String(buffer));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getResult() {
        return result.toString();
    }
}