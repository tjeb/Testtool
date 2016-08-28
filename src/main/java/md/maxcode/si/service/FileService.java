/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.domain.CertificateFile;
import md.maxcode.si.domain.FileType;
import md.maxcode.si.domain.SchemaFile;
import md.maxcode.si.domain.UserFile;
import md.maxcode.si.persistence.*;
import md.maxcode.si.smp.galaxygateway.GMetadataProfile;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import md.maxcode.si.xsl.XSLErrorInfo;
import md.maxcode.si.xsl.XSLParseException;
import nu.xom.ParsingException;
import nu.xom.xslt.XSLException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

@Service
public class FileService {
    @Autowired
    ReceivedMetadataMapper receivedMetadataMapper;
    @Autowired
    private TTSettings ttSettings;
    @Autowired
    private UtilsComponent utilsComponent;
    @Autowired(required = true)
    private UserFileMapper fileMapper;
    @Autowired(required = true)
    private SchemaFilesMapper schemaFilesMapper;

    @Autowired(required = true)
    private CertificateFilesMapper certificateFilesMapper;

    @Autowired(required = true)
    private FileTypesMapper fileTypesMapper;

    @Autowired(required = true)
    private GalaxyMetadataProfileMapper galaxyMetadataProfileMapper;

    @Autowired
    private ValidationService validationService;
    private Logger logger = Logger.getLogger(getClass().getName());

    public UserFile getById(final Long id, final Long userId) {
        return fileMapper.getById(id, userId);
    }

    public UserFile getByIdAndUserId(final Long id, final long userId) {
        return fileMapper.getByIdAndUserId(id, userId);
    }

    public CertificateFile getCertificateByIdAndUserId(final Long id, final Long userId) {
        return certificateFilesMapper.getById(id, userId);
    }

    public List<UserFile> getByUserId(final Long id_) {
        return fileMapper.getByUserId(id_);
    }

    public List<UserFile> getLatestByUserId(final Long id_) {
        return fileMapper.getLatestByUserId(id_);
    }

    @Transactional
    public SchemaFile saveAndInsertSchemaFile(final MultipartFile multipartFile_,
                                              final String name_,
                                              final Long typeId_,
                                              final Long userId_) throws IOException {
        String fullFilePath = utilsComponent.getFullFilePath(ttSettings.storeSchemaFiles, typeId_, name_, true);

        if (new File(fullFilePath).exists()) {
            logger.severe("Schema file with this name already exists, " + name_);
            return null;
        }

        RandomAccessFile newFile = new RandomAccessFile(fullFilePath, "rw");
        newFile.write(multipartFile_.getBytes());
        newFile.close();

        SchemaFile schemaFile = new SchemaFile();
        schemaFile.setTypeId(typeId_);
        schemaFile.setUserId(userId_);
        schemaFile.setFileName(name_);
        schemaFile.setName(name_);
        schemaFile.setExtension(FilenameUtils.getExtension(multipartFile_.getOriginalFilename()));

        try {
            schemaFilesMapper.insertFile(schemaFile);
        } catch (Throwable e) {
            new File(fullFilePath).delete();
        }

        return schemaFile;
    }

    @Transactional
    public CertificateFile insertCertificateFile
            (
                    final String fileName_,
                    final Long accessPointId_,
                    final Long userId_
            ) {
        CertificateFile file = new CertificateFile();
        file.setUserId(userId_);
        file.setFileName(fileName_);
        file.setAccessPointId(accessPointId_);

        certificateFilesMapper.insertFile(file);

        return file;
    }

    public UserFile insertFile(final Long typeId_,
                               final Long userId_,
                               final String fileName,
                               final String from_,
                               final String name_,
                               final Long size_,
                               final String extension_,
                               final Long receivedMetadataId,
                               final Boolean validated_, final String validationInfo) {
        UserFile file = new UserFile();
        file.setTypeId(typeId_);
        file.setUserId(userId_);
        file.setName(name_);
        file.setFileName(fileName);
        file.setFrom(from_);
        file.setSize(size_);
        file.setExtension(extension_);
        file.setReceivedMetadataId(receivedMetadataId);
        file.setValidated(validated_);
        file.setValidationInfo(validationInfo);

        fileMapper.insertFile(file);
        file.setAddedTime(fileMapper.getAddedTime(file.getId()));

        return file;
    }

    public String savePhysicalFile(final MultipartFile file_, final String store_, final String name_) throws IOException {
        String currentDate = utilsComponent.getNow();
        String fileName = utilsComponent.toMD5(name_ + currentDate);
        String fullFilePath = utilsComponent.getFullFilePath(store_, fileName);

        RandomAccessFile newFile = new RandomAccessFile(fullFilePath, "rw");
        newFile.write(file_.getBytes());
        newFile.close();

        return fileName;
    }

    public String savePhysicalFile(final String filePath, final String store_, final String name_) throws IOException {
        String currentDate = utilsComponent.getNow();
        String fileName = utilsComponent.toMD5(name_ + currentDate);
        String fullFilePath = utilsComponent.getFullFilePath(store_, fileName);

        Path source = Paths.get(filePath);
        Path dest = Paths.get(fullFilePath);

        Files.copy(source, dest);

        return fileName;
    }

    public String saveStringToPhysicalFile(final String string, final String store) throws IOException {
        int idx = string.length() > 20 ? 20 : string.length();
        String fileName = utilsComponent.toMD5(string.substring(0, idx) + utilsComponent.getNow());
        String fullFilePath = utilsComponent.getFullFilePath(store, fileName);

        RandomAccessFile newFile = new RandomAccessFile(fullFilePath, "rw");
        newFile.write(string.getBytes());
        newFile.close();

        return fileName;
    }

    public boolean deleteUserFile(final Long userId, final Long fileId) {
        UserFile file = fileMapper.getById(fileId, userId);

        if (file != null) {
            return deleteFile(file, ttSettings.storeUserFiles);
        }

        return false;
    }

    public boolean deleteSchemaFile(final Long fileId_) {
        SchemaFile file = schemaFilesMapper.getById(fileId_);

        if (file != null) {
            schemaFilesMapper.deleteFile(file.getId());
            String fullFilePath = utilsComponent.getFullFilePath(ttSettings.storeSchemaFiles, file.getTypeId(), file.getFileName(), false);
            boolean hasBeenDeleted = deletePhysicalFile(fullFilePath);

            if (!hasBeenDeleted) {
                logger.warning("The file has not been deleted! " + file.getFileName());
            }

            return true;
        }

        return false;
    }

    public boolean deleteCertificateFile(final Long fileId, final Long userId) {
        CertificateFile file = certificateFilesMapper.getById(fileId, userId);

        if (file != null) {
            certificateFilesMapper.deleteFile(file.getId());
            deletePhysicalFile(ttSettings.storeCertificateFiles, file.getFileName());
            return true;
        }

        return false;
    }

    private boolean deleteFile(final UserFile file, final String store) {
        fileMapper.deleteFile(file.getId());
        deletePhysicalFile(store, file.getFileName());

        Long receivedMetadataId = file.getReceivedMetadataId();
        if (receivedMetadataId != null) {
            receivedMetadataMapper.remove(receivedMetadataId);
        }

        return true;
    }

    public boolean deletePhysicalFile(final String store_, final String fileName_) {
        return deletePhysicalFile(utilsComponent.getFullFilePath(store_, fileName_));
    }

    public boolean deletePhysicalFile(final String filePath_) {
        File file = new java.io.File(filePath_);
        return file.delete();
    }

    public List<XSLErrorInfo> validateFile(final String filePath_, final Long typeId_) throws IOException, SAXException, ParsingException, XSLException, XSLParseException {
        FileType fileType = fileTypesMapper.getByIdWithAttachments(typeId_);
        Iterator<SchemaFile> fileIterator = fileType.getAttachedFiles().iterator();
        List<XSLErrorInfo> xslErrorInfos = new ArrayList<>();

        while (fileIterator.hasNext()) {
            SchemaFile schemaFile = fileIterator.next();
            String schemaFilePath = utilsComponent.getFullFilePath(ttSettings.storeSchemaFiles, schemaFile.getTypeId(), schemaFile.getFileName(), false);

            if (schemaFile.getExtension().toLowerCase().equals("xsd")) {
                if (schemaFile.isMarked()) {
                    validationService.againstXSD(schemaFilePath, filePath_);
                }
            } else {
                xslErrorInfos = validationService.againstXSL(schemaFilePath, filePath_);
            }
        }

        return xslErrorInfos;
    }

    public List<FileType> getAllFileTypes() {
        return fileTypesMapper.getAllWithTheirAttachments();
    }

    public Map<Long, FileType> getAllFileTypes_mapped() {
        List<FileType> fileTypes = fileTypesMapper.getAllWithTheirAttachments();

        Map<Long, FileType> result = new HashMap<>();

        for (FileType type : fileTypes) {
            result.put(type.getId(), type);
        }

        return result;
    }

    public Long insertType(final String name_, final String identifier_, final String galaxyMetadataProfile_base64) {
        final FileType fileType = new FileType();
        fileType.setName(name_);
        fileType.setIdentifier(identifier_);
        fileTypesMapper.insertType(fileType);

        if (galaxyMetadataProfile_base64 != null && !galaxyMetadataProfile_base64.isEmpty()) {
            GMetadataProfile profile = new GMetadataProfile(galaxyMetadataProfile_base64);
            galaxyMetadataProfileMapper.insert(profile, fileType.getId());
        }

        return fileType.getId();
    }

    public void updateType(final Long typeId_, final String name_, final String identifier_) {
        FileType fileType = fileTypesMapper.getById(typeId_);
        fileType.setName(name_);
        fileType.setIdentifier(identifier_);

        fileTypesMapper.updateType(fileType);
    }

    public void deleteType(final Long typeId) {
        List<UserFile> files = fileMapper.getAllBytTypeId(typeId);
        Iterator<UserFile> iterator = files.iterator();
        if (files.size() > 0) {
            while (iterator.hasNext()) {
                final UserFile file = iterator.next();
                file.setTypeId(null);
                fileMapper.updateFile(file);
            }

        }

        List<SchemaFile> schemaFiles = schemaFilesMapper.getAllBytTypeId(typeId);
        Iterator<SchemaFile> iterator1 = schemaFiles.iterator();

        if (schemaFiles.size() > 0) {
            while (iterator1.hasNext()) {
                final SchemaFile schemaFile = iterator1.next();
                deleteSchemaFile(schemaFile.getId());
            }

        }

        fileTypesMapper.deleteType(typeId);
        galaxyMetadataProfileMapper.removeByFileTypeId(typeId);
    }

    public List<SchemaFile> getAllSchemas() {
        return schemaFilesMapper.getAllSchemas();
    }

    public String getSchemaContent(final Long fileId) {
        SchemaFile file = schemaFilesMapper.getById(fileId);

        if (file != null) {
            String path = utilsComponent.getFullFilePath(ttSettings.storeSchemaFiles, file.getTypeId(), file.getFileName(), false);
            return utilsComponent.getFileContent(path);
        }

        return null;
    }


    public String getFileContent(final String filename) {
        String path = utilsComponent.getFullFilePath(ttSettings.storeUserFiles, filename);

        return utilsComponent.getFileContent(path);
    }

    public Boolean markSchemaFile(final Long fileId) {
        SchemaFile file = schemaFilesMapper.getById(fileId);

        if (!file.getExtension().toLowerCase().equals("xsd")) {
            logger.warning("Trying to mark as root file an schema file, " + file.getId() + ", " + file.getName());
            return false;
        }

        SchemaFile markedFile = schemaFilesMapper.getMarkedSchemaFile(file.getTypeId());

        if (markedFile != null) {
            if (markedFile.getId() == fileId) {
                logger.warning("Trying to mark as root file an already marked schema file, " + file.getId() + ", " + file.getName());
                return false;
            }

            markedFile.setMarked(false);
            schemaFilesMapper.updateFile(markedFile);
        }

        file.setMarked(true);
        schemaFilesMapper.updateFile(file);

        return true;
    }
}
