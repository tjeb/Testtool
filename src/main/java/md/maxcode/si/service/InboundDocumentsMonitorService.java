/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.domain.FileType;
import md.maxcode.si.domain.ReceivedFileMetadata;
import md.maxcode.si.domain.User;
import md.maxcode.si.persistence.FileTypesMapper;
import md.maxcode.si.persistence.ReceivedMetadataMapper;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import md.maxcode.si.tools.WatchDir;
import md.maxcode.si.xsl.XSLErrorInfo;
import md.maxcode.si.xsl.XSLParseException;
import nu.xom.ParsingException;
import nu.xom.xslt.XSLException;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import sun.nio.cs.ext.JISAutoDetect;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class InboundDocumentsMonitorService {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    FileTypesMapper fileTypesMapper;
    @Autowired
    ReceivedMetadataMapper receivedMetadataMapper;
    InboundDocumentsMonitorService instance;
    @Autowired
    private UserService userService;
    @Autowired
    private FileService fileService;
    @Autowired
    private TTSettings ttSettings;
    @Autowired
    private UtilsComponent utilsComponent;
    private Thread thread;
    private int maxRetriesNr = 10;

    public void startMonitoring() throws Exception {
        instance = this;
        startThread();
    }

    private void startThread() {
        thread = new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Path path = Paths.get(ttSettings.inbound_documents_folder);    // Get the directory to be monitored
                            new WatchDir(path, true).processEvents(instance);
                            maxRetriesNr = 10;
                        } catch (Throwable e) {
                            System.err.println("Error occurred while trying to scan oxalis folder for input documents, e --> " + e.getMessage());
                            logger.severe("Could not scan Oxalis document for files: " + e.getMessage());
                            maxRetriesNr--;
                            thread.interrupt();

                            if (maxRetriesNr < 0) {
                                return;
                            }

                            startThread();
                        }
                    }
                });

        thread.setDaemon(true);
        thread.start();
    }

    public void checkNewFile(final Path child) throws JSONException {
        final String filePath = child.toString();
        if (filePath.endsWith(".txt")) {
            System.out.println("Parsing new metadata file, file path --> " + filePath);
            Properties properties = new Properties();

            Charset cs = new JISAutoDetect();
            List<String> strings;

            try {
                strings = Files.readAllLines(child, cs);
            } catch (IOException e) {
                System.err.println("Error occurred while reading metadata file, " + e.getMessage());
                logger.severe("Error occurred while reading metadata file: " + e.getMessage());
                return;
            }
            StringBuilder fileContent = new StringBuilder();

            for (String line : strings) {
                fileContent.append(line.replace("\\", "\\\\") + "\r\n");
            }
            try {
                properties.load(new StringReader(fileContent.toString()));
            } catch (IOException e) {
                System.err.println("Error occurred while loading properties from metadata file, " + e.getMessage());
                logger.severe("Error occurred while loading properties from metadata file: " + e.getMessage());
                return;
            }
            JSONObject json = null;

            try {
                json = new JSONObject(fileContent.toString());
            } catch (JSONException jex) {
                System.err.println("Trying to parse the following JSON: " + fileContent.toString());
                System.err.println("- -- -- -- -- -- -- -- ");
                System.err.println("An error has occurred while trying to load JSON data from string " + jex);
                logger.severe("An error has occurred while trying to parse the following JSON content: " + fileContent.toString() + ". The generated error is: " + jex.getMessage());
                return;
            }
            ReceivedFileMetadata metadataFile;

            try {
                metadataFile = new ReceivedFileMetadata(json, filePath, ttSettings.channelId);
            } catch (Exception ex) {
                System.err.println("General exception while loading properties from file :" + ex);
                logger.severe("An error has occurred while loading properties from the file: " + ex.getMessage());
                return;
            }

            User user = userService.getByIdentifier(metadataFile.getRecipientIdentifier());

            if (user == null) {
                System.err.println("*  *  ** * ** **  * * * ** ** * * * * ** * ");
                System.err.println("User not found with identifier: " + metadataFile.getRecipientIdentifier());
                System.err.println("content of the metadata file is: " + fileContent.toString());
                System.err.println("- -- -- -- -- -- -- -- ");
                return;
            }

            String nameHash = "";
            try {
                nameHash = fileService.savePhysicalFile(
                        metadataFile.getMessageFileName(),
                        ttSettings.storeUserFiles,
                        metadataFile.getMessageIdentifier());
            } catch (IOException e) {
                System.err.println("Error occurred while saving physical file, " + e.getMessage());
                System.err.println("Trying to save with the following name hash :" + nameHash);
                logger.severe("Could not save file to disk: " + e.getMessage());
                return;
            }

            FileType fileType = fileTypesMapper.getByIdentifier(metadataFile.getDocumentIdentifier());
            Long typeId = null;
            boolean validated = true;
            String validationInfo = null;

            if (fileType != null) {
                typeId = fileType.getId();

                try {
                    String filePathForValidation = utilsComponent.getFullFilePath(ttSettings.storeUserFiles, nameHash);
                    List<XSLErrorInfo> xslErrorInfos = fileService.validateFile(filePathForValidation, fileType.getId());
                    validated = xslErrorInfos.size() < 1;
                    validationInfo = utilsComponent.toMessage(xslErrorInfos, true);
                } catch (IOException | SAXException | XSLException | XSLParseException | ParsingException e) {
                    System.err.println("Error occurred while validating file, " + e.getMessage());
                    validated = false;
                    validationInfo = e.getMessage();
                }
            }

            receivedMetadataMapper.write(metadataFile);

            String extension = FilenameUtils.getExtension(metadataFile.getMessageFileName());
            fileService.insertFile(
                    typeId,
                    user.getId(),
                    nameHash,
                    metadataFile.getSenderIdentifier(),
                    metadataFile.getMessageIdentifier(),
                    new File(filePath).length(),
                    extension,
                    metadataFile.geId(),
                    validated, validationInfo);

            System.out.println("File successfully added --> " + filePath);
            System.out.println("RecipientIdentifier --> " + metadataFile.getRecipientIdentifier());
        }
    }
}
