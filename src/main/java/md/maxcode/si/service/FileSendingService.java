/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import com.sun.xml.ws.transport.http.client.HttpTransportPipe;
import eu.peppol.identifier.PeppolDocumentTypeId;
import eu.peppol.util.GlobalConfiguration;
import md.maxcode.si.domain.ScheduledDocumentSending;
import md.maxcode.si.domain.UserFile;
import md.maxcode.si.persistence.CertificateFilesMapper;
import md.maxcode.si.persistence.UserFileMapper;
import md.maxcode.si.persistence.AccessPointMapper;
import md.maxcode.si.tools.SyncPipe;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import org.apache.commons.codec.binary.Base64;
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

// used to get CN from cert
import md.maxcode.si.domain.CertificateFile;
import java.io.StringReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x500.X500Name;

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
    @Autowired(required = true)
    private CertificateFilesMapper certFileMapper;

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

    private String getAPCertificateFileName(final Long apConfigId, final Long userId) {
        Long fileId;
		System.out.println("[XX] apConfigId: " + apConfigId);
		System.out.println("[XX] userId: " + userId);
        if (apConfigId < 0) {
            return null;
        } else if (apConfigId == 0) {
            return null;
        } else {
            fileId = accessPointMapper.getById(apConfigId, userId).getFileId();
			System.out.println("[XX] FileId: " + fileId);
            CertificateFile certFile = certFileMapper.getById(fileId, userId);
			System.out.println("[XX] userfile object: " + certFile);
            return utilsComponent.getFullFilePath(ttSettings.storeCertificateFiles, certFile.getFileName());
        }
    }

    private String constructAPCommonName(final Long apConfigId, final Long userId) {
        String commonName;
		System.out.println("[XX] get CN from cert");
        String certFileName = getAPCertificateFileName(apConfigId, userId);
        if (certFileName == null) {
			System.out.println("[XX] No certificate file");
            return "null";
        }
        try {
			System.out.println("[XX] cert file: " + certFileName);
			CertificateFactory fact = CertificateFactory.getInstance("X.509");
			X509Certificate x509certificate = (X509Certificate) fact.generateCertificate(new FileInputStream(new File(certFileName)));
			X500Name x500name = new JcaX509CertificateHolder(x509certificate).getSubject();
			System.out.println("[XX] 6");
			RDN cn = x500name.getRDNs(BCStyle.CN)[0];
			System.out.println("[XX] 7");

			String x509name = IETFUtils.valueToString(cn.getFirst().getValue());
			System.out.println("[XX] x509 CN: " + x509name);
			return x509name;
        } catch (Exception exc) {
            System.out.println("Error reading certificate file " + certFileName + ": " + exc.toString());
            return "null";
        }
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
        String systemIdentifier = constructAPCommonName(apConfigId, userId);

        //backwards compatible to old START protocol. Use as default protocol
        String method = "as2";

        //start constructing the command line that we use to send information to Oxalis
        String sslCommand = ttSettings.java_bin + " " +
                "-jar \"" + ttSettings.oxalis_standalone_jar + "\" ";

        if (xmlIsStandardBusinessDocument) {
            //Add mandatory information required by AS2 protocol
            sslCommand += " -f \"" + filePath + "\"" +
                    " -m " + method +
                    " -i " + systemIdentifier;
        } else {
            //Use mandatory parameters for AS2 protocol
            sslCommand = sslCommand +
                    " -d \"" + documentId + "\"" +
                    " -f \"" + filePath + "\"" +
                    " -m \"" + method + "\"" +
                    " -i \"" + systemIdentifier + "\"" + 
                    " -r \"" + recipientId + "\"" +
                    " -s \"" + senderId + "\"";
        }

        if (destinationUrl != null) {
            sslCommand += " -u \"" + destinationUrl + "\"";
        }

        System.out.println("Sending command to Oxalis : " + sslCommand);

        Process p;
        try {
            System.out.println("---------------------------------------------------------------");
            System.out.println("-------------------------  START  -----------------------------");
            System.out.println("---------------------------------------------------------------");
            System.out.println("----------------------Program output:--------------------------");

            p = Runtime.getRuntime().exec(new String[]{ttSettings.terminal}, new String[]{"OXALIS_HOME="+ttSettings.oxalis_home});
            SyncPipe errorPipe = new SyncPipe(p.getErrorStream(), System.err);
            SyncPipe outPipe = new SyncPipe(p.getInputStream(), System.out);
            new Thread(errorPipe).start();
            new Thread(outPipe).start();
            PrintWriter stdin = new PrintWriter(p.getOutputStream());

            stdin.println(sslCommand);
            stdin.close();

            int returnCode = p.waitFor();
            System.out.println("Return code = " + returnCode);

            String errorMessage = errorPipe.getResult().replace("\u0000", "").replace("\t", "\r\n").replace("Caused by:", "\r\nCaused by:");
            errorMessage = URLDecoder.decode(errorMessage, "UTF-8");

            try {
                errorMessage = URLDecoder.decode(errorMessage, "UTF-8");
            } catch (Throwable e) {
            }

            String outMessage = outPipe.getResult().replace("\u0000", "").replace("\t", "\r\n");

            try {
                outMessage = URLDecoder.decode(outMessage, "UTF-8");
            } catch (Throwable e) {
            }

            if (returnCode != 0 || !outMessage.contains("was assigned transmissionId")) {
                int i = 0;
                boolean addAll = false;
                StringBuilder string = new StringBuilder("\r\n\r\n");
                // Check for error messages on stdout
                Scanner scanner = new Scanner(outMessage);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("[XX] SCANNING OUTLINE: " + line);

                    if (line.equals("")) {
                        continue;
                    } else if (line.startsWith("Message failed :") || line.contains("ERROR") || addAll) {
                        string.append(line + "\r\n");
                        addAll = true;
                    } else if (line.contains("ERROR ")) {
                        string.append(line + "\r\n");
                    } else if (line.contains("Exception:") || line.startsWith("Caused by:") || line.startsWith("java.lang.RuntimeException:")) {
                        string.append("  " + (++i) + ".     " + line + "\r\n\r\n------------------------------\r\n\r\n");
                    } else if (addAll) {
                        string.append(line + "\r\n");
                    }

                }

                // Add exceptions from stderr as well
                i = 0;
                addAll = false;

                scanner = new Scanner(errorMessage);
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println("[XX] SCANNING ERRLINE: " + line);

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
