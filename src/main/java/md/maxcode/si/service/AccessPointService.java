/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.domain.AccessPoint;
import md.maxcode.si.domain.CertificateFile;
import md.maxcode.si.persistence.AccessPointMapper;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Logger;

@Service
public class AccessPointService {
    protected final Logger logger = Logger.getLogger(getClass().getName());
    @Autowired
    FileService fileService;
    @Autowired
    CertificateService certificateService;
    @Autowired
    private TTSettings ttSettings;
    @Autowired
    private UtilsComponent utilsComponent;
    @Autowired(required = true)
    private AccessPointMapper accessPointMapper;

    public List<AccessPoint> getAllByUserId(Long id_) {
        return accessPointMapper.getAllByUserId(id_);
    }

    public List<AccessPoint> getLatestByUserId(Long id_) {
        return accessPointMapper.getLatestByUserId(id_);
    }

    @Transactional
    public AccessPoint insert(final Long ownerId,
                              final String name,
                              final String serviceUrl,
                              final String description,
                              final String contactEmail,
                              final String technicalUrl) {
        AccessPoint ap = new AccessPoint();
        ap.setUserId(ownerId);
        ap.setName(name);
        ap.setUrl(serviceUrl);
        ap.setDescription(description);
        ap.setContactEmail(contactEmail);
        ap.setTechnicalUrl(technicalUrl);

        accessPointMapper.insert(ap);
        return ap;
    }

    public void removeAP(final Long id, final Long userId) {
        AccessPoint accessPoint = accessPointMapper.getById(id, userId);
        accessPointMapper.remove(id, userId);
        fileService.deleteCertificateFile(accessPoint.getFileId(), userId);
    }

    public void update(final AccessPoint accessPoint_) {
        accessPointMapper.update(accessPoint_);
    }

    public Long add(final Long userId,
                    final String name,
                    final String url,
                    final String certificate,
                    final String description,
                    final String contactEmail,
                    final String technicalUrl) throws Exception {
        final CertificateFile file;
        AccessPoint accessPoint = null;
        String fileName = null;
        final String store = ttSettings.storeCertificateFiles;

        try {
            fileName = fileService.saveStringToPhysicalFile(certificate, store);
            accessPoint = insert(userId, name, url, description, contactEmail, technicalUrl);
            file = fileService.insertCertificateFile(fileName, accessPoint.getId(), userId);

            accessPoint.setFileId(file.getId());
            update(accessPoint);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            logger.severe("An exception has occurred while trying to add AP: " + e.getMessage());
            if (accessPoint != null) {
                removeAP(accessPoint.getId(), userId);
            }

            if (fileName != null) {
                fileService.deletePhysicalFile(store, fileName);
            }

            String errorDetails = "null";
            try {
                if (e.getMessage() != null) {
                    errorDetails = URLEncoder.encode(e.getMessage(), "UTF-8");
                }

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                logger.severe("An unsupported encoding exception has been caught: " + e1.getMessage());
            }

            throw new Exception(errorDetails);
        }

        return file.getId();
    }
}
