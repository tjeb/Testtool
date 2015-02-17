/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.tools.SyncPipe;
import md.maxcode.si.tools.TTSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;

@Service
public class CertificateService {
    @Autowired
    private TTSettings ttSettings;

    public void generateUserCERTFile(String csrFilePath_, String crtFilePath_, String companyName_, String commonName_) throws IOException, InterruptedException {
        String sslCommand = ttSettings.openSSL_bin + " ca " +
                " -in \"" + csrFilePath_ + "\"" +
                " -out \"" + crtFilePath_ + "\"" +
                " -subj \"/C=NL/O=" + companyName_ + "/CN=" + commonName_ + "\"" +
                " -config " + ttSettings.openSSL_conf +
                " -batch -key " + ttSettings.privateKey_pass;

        Process p = Runtime.getRuntime().exec(new String[]{ttSettings.terminal});
        SyncPipe errorPipe = new SyncPipe(p.getErrorStream(), System.err);
        SyncPipe outPipe = new SyncPipe(p.getInputStream(), System.out);
        new Thread(errorPipe).start();
        new Thread(outPipe).start();
        PrintWriter stdin = new PrintWriter(p.getOutputStream());

        stdin.println(sslCommand);
        stdin.close();

        int returnCode = p.waitFor();
        System.out.println("Return code = " + returnCode);

        if (returnCode != 0 || errorPipe.getResult().contains("Error") || outPipe.getResult().contains("Error")) {
            throw new IOException("There was an error parsing the certificate signing request file, please check server logs!");
        }
    }

}
