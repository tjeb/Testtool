/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import eu.peppol.identifier.PeppolDocumentTypeId;
import eu.peppol.util.GlobalConfiguration;
import md.maxcode.si.domain.ScheduledDocumentSending;
import md.maxcode.si.domain.UserFile;
import md.maxcode.si.persistence.AccessPointMapper;
import md.maxcode.si.tools.SyncPipe;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3._2009._02.ws_tra.FaultMessage;
import org.w3._2009._02.ws_tra.StartException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Scanner;
import java.util.logging.Logger;

@Service
public class FileSendingService {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    FileService fileService;
    @Autowired
    private TTSettings ttSettings;
    @Autowired
    private UtilsComponent utilsComponent;
    @Autowired(required = true)
    private AccessPointMapper accessPointMapper;

    public void sendFileToAP(final Long userId, final String base64Input_, final String providedHMACSignature_) throws Exception {
        String json = new String(Base64.decodeBase64(base64Input_));
        String signature = utilsComponent.toHMACBase64String(json);

        if (!signature.equals(providedHMACSignature_)) {
            throw new Exception("Invalid signature");
        }

        ScheduledDocumentSending sending = (ScheduledDocumentSending) utilsComponent.stringToInstance(json, ScheduledDocumentSending.class);
        sendFileToAP(sending.getApConfigId(),
                userId,
                sending.getFileId(),
                sending.getSenderPeppolId(),
                sending.getRecipientPeppolId());
    }


    private String constructDestinationUrl(final Long apConfigId, final Long userId) {
        String destinationUrl;

        if (apConfigId < 0) {
            destinationUrl = null;
        } else if (apConfigId == 0) {
            destinationUrl = ttSettings.loopbackUrl;
        } else {
            destinationUrl = accessPointMapper.getById(apConfigId, userId).getUrl();
        }

        return destinationUrl;
    }

    public String constructDocumentId(final Element rootElement) throws Exception {
        String uri = null;
        String localName = null;
        String CustomizationID = null;
        String UBLVersionID = null;
        String ProfileID = null;

        try {

            uri = rootElement.getNamespaceURI();
            String[] splitUri = uri.split(":");
            localName = splitUri[splitUri.length - 1];
            localName = localName.substring(0, localName.indexOf("-"));

            Elements childElements = rootElement.getChildElements();
            int size = childElements.size();

            for (int i = 0; i < size; i++) {
                Element child = childElements.get(i);
                if ("CustomizationID".equals(child.getLocalName())) {
                    CustomizationID = child.getValue();

                }

                if ("UBLVersionID".equals(child.getLocalName())) {
                    UBLVersionID = child.getValue();

                }

                if ("ProfileID".equals(child.getLocalName())) {
                    ProfileID = child.getValue();

                }
            }
        } catch (Exception ex) {
            logger.severe("XML parsing error: " + ex.getMessage());
            System.err.println("Error on parsing XMLFile" + ex);
            throw new FaultMessage("Error while sending file, details:\r\n" + ex.getMessage(), new StartException());
        }

        String peppPolDocumentType = uri + "::" + localName + "##" + CustomizationID + "::" + UBLVersionID;

        return peppPolDocumentType;
    }

    public void sendFileToAP(final Long apConfigId,
                             final Long userId,
                             final Long fileId,
                             final String senderPeppolId,
                             final String recipientPeppolId) throws Exception {
        UserFile baseFile = fileService.getById(fileId, userId);

        String filePath = utilsComponent.getFullFilePath(ttSettings.storeUserFiles, baseFile.getFileName());
        String recipientId = recipientPeppolId;
        String senderId = senderPeppolId;

        File xmlInvoice = new File(filePath);
        Builder parser = new Builder();
        Document doc = parser.build(xmlInvoice);
        Element rootElement = doc.getRootElement();

        Boolean xmlIsStandardBusinessDocument = false;
        PeppolDocumentTypeId documentId = null;

        if (rootElement.getLocalName() == "StandardBusinessDocument") {
            //we need to send only the file to Oxalis. All information will be parsed from Oxalis
            xmlIsStandardBusinessDocument = true;
        } else {
            xmlIsStandardBusinessDocument = false;

            try {
                documentId = PeppolDocumentTypeId.valueOf(constructDocumentId(rootElement));
            } catch (Exception ex) {
                System.err.println("Something went wrong while trying to load peppol document id");
                return;
            }
        }

        HttpTransportPipe.dump = true;

        File keystoreLocation;
        keystoreLocation = new File(GlobalConfiguration.getInstance().getKeyStoreFileName());

        if (!keystoreLocation.isFile() || !keystoreLocation.canRead()) {
            throw new IllegalStateException("Keystore file not found or not readable: " + keystoreLocation.getAbsolutePath());
        }

        String destinationUrl = constructDestinationUrl(apConfigId, userId);

        Boolean isAS2 = destinationUrl == null || destinationUrl.contains("as2");
        String systemIdentifier = "";

        //backwards compatible to old START protocol. Use as default protocol
        String method = "start";
        if (isAS2) {
            method = "as2";
            systemIdentifier = " -i \"" + ttSettings.AS2SystemIdentifier + "\"";
        }

        //start constructing the command line that we use to send information to Oxalis
        String sslCommand = ttSettings.java_bin + " " +
                "-jar \"" + ttSettings.oxalis_standalone_jar + "\" ";

        if (xmlIsStandardBusinessDocument) {
            //Add mandatory information required by AS2 protocol
            sslCommand += " -f \"" + filePath + "\"" +
                    " -m \"" + method + "\"" +
                    systemIdentifier;
        } else {
            //Use mandatory parameters for AS2 protocol
            sslCommand = sslCommand +
                    " -d \"" + documentId + "\"" +
                    " -f \"" + filePath + "\"" +
                    " -m \"" + method + "\"" +
                    systemIdentifier +
                    " -r \"" + recipientId + "\"" +
                    " -s \"" + senderId + "\"";
        }

        if (destinationUrl != null) {
            sslCommand += " -u \"" + destinationUrl + "\"";
        }

        System.out.println("Sending command to Oxalis : " + sslCommand);

        Process p;
        try {
            p = Runtime.getRuntime().exec(new String[]{ttSettings.terminal}, new String[]{"OXALIS_HOME="+ttSettings.oxalis_home});
            PrintWriter stdin = new PrintWriter(p.getOutputStream());
            stdin.println(sslCommand);
            stdin.close();
            int returnCode = p.waitFor();
            System.out.println("Return code: " + returnCode);
            String stdout = IOUtils.toString(p.getInputStream(), "UTF-8");
            String stderr = IOUtils.toString(p.getErrorStream(), "UTF-8");
            System.out.println("---------------- Process stdout ----------------------");
            System.out.println(stdout);
            System.out.println("---------------- Process stderr ----------------------");
            System.out.println(stderr);
            
            if (returnCode != 0 || !stdout.contains("was assigned transmissionId")) {
                // make something somewhat readable from the error
                StringBuilder error = new StringBuilder();
                for (String line : stdout.split("\n")) {
                    if (line.startsWith("Message failed")) {
                        error.append(line);
                        error.append("\r\n");
                    }
                }

                // Just append stderr in its entirety
                error.append(stderr);
                System.out.println("------------ Converted error message -----------------");
                System.out.println(error.toString());

                    if (line.equals("")) {
                        continue;
                    }

                    if (line.startsWith("Caused by:") || line.startsWith("java.lang.RuntimeException:")) {
                        string.append("  " + (++i) + ".     " + line + "\r\n\r\n------------------------------\r\n\r\n");
                    }

                }
                scanner.close();

                System.out.println("------------------Modified error message:----------------------");
                System.out.println(errorMessage);
                System.out.println("---------------------------------------------------------------");
                System.out.println("---------------------------  END  -----------------------------");
                System.out.println("---------------------------------------------------------------");
                throw new FaultMessage("Error while sending file, details:\r\n" + string, new StartException());
            }

            System.out.println("---------------- End of process ----------------------");
        } catch (IOException e) {
            logger.severe(e.getMessage());
            e.printStackTrace();
            throw new FaultMessage("Error while executing java command", new StartException());
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.severe(e.getMessage());
            throw new FaultMessage("Error while executing java command", new StartException());
        }
    }

    public String getFileSendingTriggerParams(final Long apConfigId_, final Long fileId_, final String senderPeppolId_, final String recipientPeppolId_) throws SignatureException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        String triggerParams;

        ScheduledDocumentSending sending = new ScheduledDocumentSending();
        sending.setApConfigId(apConfigId_);
        sending.setFileId(fileId_);
        sending.setSenderPeppolId(senderPeppolId_);
        sending.setRecipientPeppolId(recipientPeppolId_);

        String json = utilsComponent.toJsonString(sending);
        String signature = utilsComponent.toHMACBase64String(json);

        json = new String(Base64.encodeBase64(json.getBytes()));
        triggerParams = "i=" + URLEncoder.encode(json, "UTF-8") + "&s=" + URLEncoder.encode(signature, "UTF-8");

        return triggerParams;
    }

}
