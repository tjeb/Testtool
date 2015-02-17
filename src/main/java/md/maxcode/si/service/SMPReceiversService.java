/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.service;

import md.maxcode.si.domain.AccessPoint;
import md.maxcode.si.domain.CertificateFile;
import md.maxcode.si.persistence.AccessPointMapper;
import md.maxcode.si.persistence.GParticipantIdMapper;
import md.maxcode.si.persistence.GalaxyMetadataProfileMapper;
import md.maxcode.si.smp.galaxygateway.*;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@Service("smpReceiversService")
public class SMPReceiversService {

    private final String DATE_TIME_ZONE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
    @Autowired
    GUrls gUrls;

    @Autowired
    UtilsComponent utilsComponent;

    @Autowired
    SMPMessagingService smpMessagingService;

    @Autowired
    FileService fileService;

    @Autowired
    CertificateService certificateService;
    @Autowired(required = true)
    private GalaxyMetadataProfileMapper galaxyMetadataProfileMapper;
    @Autowired(required = true)
    private AccessPointMapper accessPointMapper;
    @Autowired(required = true)
    private GParticipantIdMapper gParticipantIdMapper;
    @Autowired
    private TTSettings ttSettings;
    private Logger logger = Logger.getLogger(SMPReceiversService.class.getName());

    public GParticipantIdentifierRequest addSMPReceiver(Long userId, Long accessPointId, String participantName)
            throws GException {
        GParticipantIdentifierReference participantIdRef = new GParticipantIdentifierReference();
        participantIdRef.setUserId(userId);

        gParticipantIdMapper.insert(participantIdRef);

        AccessPoint accessPoint = accessPointMapper.getById(accessPointId, userId);
        Integer endpointId = registerEndpoint(accessPoint);

        if (endpointId == null) {
            gParticipantIdMapper.remove(participantIdRef.getId());
            logger.severe("Error occurred while trying to register an endpoint on Tickstar");
            throw new GException("Error occurred while creating endpoint in the SMP");
        }

        String participantId_value = ttSettings.galaxy_prefix + utilsComponent.get6digits(String.valueOf(participantIdRef.getId())) + ttSettings.galaxy_suffix;

        GAccessPointConfiguration accessPointConfiguration = new GAccessPointConfiguration();
        accessPointConfiguration.setEndpointId(endpointId);
        accessPointConfiguration.setMetadataProfileIds(galaxyMetadataProfileMapper.getAllProfileIds());

        GParticipantIdentifier participantIdentifier = new GParticipantIdentifier();
        participantIdentifier.setName(participantName);
        participantIdentifier.setValue(participantId_value);
        participantIdentifier.setScheme("iso6523-actorid-upis");

        participantIdentifier.getAccessPointConfigurations().add(accessPointConfiguration);

        GParticipantIdentifierRequest identifierRequest = new GParticipantIdentifierRequest();
        identifierRequest.getParticipantIdentifiers().add(participantIdentifier);

        String httpResult = smpMessagingService.makePostRequest(utilsComponent.toJsonString(identifierRequest), gUrls.getParticipantIdentifierUrl());

        boolean hasErrors = utilsComponent.hasGalaxyErrors(httpResult,
                "Error occurred while trying to create identifier in the SMP",
                logger);

        if (hasErrors) {
            gParticipantIdMapper.remove(participantIdRef.getId());
            throw new GException("Error occurred while creating participant in the SMP");
        }

        participantIdRef.setParticipantIdValue(participantIdentifier.getValue());
        participantIdRef.setEndpointId(endpointId);
        gParticipantIdMapper.update(participantIdRef);

        return identifierRequest;
    }

    private Integer registerEndpoint(AccessPoint accessPoint) throws GException {
        Integer id = Integer.parseInt(String.valueOf(accessPoint.getId()));

        if (accessPoint.isUsedForGalaxyGateway()) {
            return id;
        }

        GEndPoint endPoint = new GEndPoint();

        CertificateFile file = fileService.getCertificateByIdAndUserId(accessPoint.getFileId(), accessPoint.getUserId());
        String filePath = utilsComponent.getFullFilePath(ttSettings.storeCertificateFiles, file.getFileName());
        String certificateContents;

        try {
            certificateContents = FileUtils.readFileToString(new File(filePath));
        } catch (IOException e) {
            logger.severe("Was not able to read certificate contents");
            return null;
        }

        endPoint.setCertificate(certificateContents);

        final Date startDate = new Date();

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.YEAR, 1);
        final Date expirationDate = cal.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_ZONE_PATTERN);
        endPoint.setServiceActivationDate(simpleDateFormat.format(startDate));
        endPoint.setServiceExpirationDate(simpleDateFormat.format(expirationDate));

        endPoint.setEndpointId(id);
        endPoint.setEndpointAddress(accessPoint.getUrl());
        endPoint.setServiceDescription(accessPoint.getDescription());
        endPoint.setTechnicalContactUrl(accessPoint.getContactEmail());
        endPoint.setTechnicalInformationUrl(accessPoint.getTechnicalUrl());

        GEndpointRequest endpointRequest = new GEndpointRequest();
        endpointRequest.getEndpoints().add(endPoint);

        String httpResult = smpMessagingService.makePostRequest(utilsComponent.toJsonString(endpointRequest), gUrls.getEndpointsUrl());

        boolean hasErrors = utilsComponent.hasGalaxyErrors(httpResult,
                "Errors encountered while registering endpoint: accessPointId --> " + id,
                logger);

        if (hasErrors) {
            return null;
        }

        if (!accessPoint.isUsedForGalaxyGateway()) {
            accessPoint.setUsedForGalaxyGateway(true);
            accessPointMapper.setUsedForGalaxyGateway(accessPoint);
        }

        return id;
    }

    public List<GParticipantIdentifier> getSMPReceivers(final Long userId) {

        List<String> participantIds = gParticipantIdMapper.getParticipantIds(userId);
        String smpResponse = smpMessagingService.makeGetRequest(gUrls.getParticipantIdentifierUrl());

        Map map = (Map) utilsComponent.jsonToObject(smpResponse, Map.class);
        List<Map> profiles = (List) map.get("participantIdentifiers");
        List<GParticipantIdentifier> result = new ArrayList<GParticipantIdentifier>();

        for (Map item : profiles) {
            if (participantIds.contains(String.valueOf(item.get("value")))) {
                result.add(new GParticipantIdentifier(item, new String(Base64.encodeBase64String(utilsComponent.toJsonString(item).getBytes()))));
            }
        }

        return result;
    }

    public List<GMetadataProfile> getMetadataProfiles() {
        String smpResponse = smpMessagingService.makeGetRequest(gUrls.getMetadataProfilesUrl());

        Map map = (Map) utilsComponent.jsonToObject(smpResponse, Map.class);
        List<Map> profiles = (List) map.get("metadataprofiles");

        List<GMetadataProfile> result = new ArrayList<GMetadataProfile>();

        for (Map item : profiles) {
            String base64 = new String(Base64.encodeBase64String(utilsComponent.toJsonString(item).getBytes()));
            result.add(new GMetadataProfile(item, base64));
        }

        return result;
    }

    public List<GMetadataProfile> getMetadataProfiles(final boolean filterExistent) {
        List<GMetadataProfile> metadataProfiles = getMetadataProfiles();

        if (!filterExistent) {
            return metadataProfiles;
        }

        List<GMetadataProfile> result = new ArrayList<GMetadataProfile>();
        List<Integer> registeredProfiles = galaxyMetadataProfileMapper.getAllProfileIds();

        for (GMetadataProfile profile : metadataProfiles) {
            if (!registeredProfiles.contains(profile.getProfileId())) {
                result.add(profile);
            }
        }

        return result;
    }

    public void deleteReceiver(String participantId) throws IOException {
        String result = smpMessagingService.makeDeleteRequest(gUrls.getParticipantIdentifierUrl() + "?pid=" + URLEncoder.encode("iso6523-actorid-upis::" + participantId, "UTF-8"));

        Map map = (Map) utilsComponent.jsonToObject(result, Map.class);
        map = (Map) map.get("response");
        List errors = (ArrayList) map.get("errors");

        if (errors.size() > 0) {
            throw new IOException("Errors encountered while trying to remove participant id");
        }

        GParticipantIdentifierReference gref = gParticipantIdMapper.getByParticipantId(participantId);
        if (gref.getEndpointId() != null) {
            result = smpMessagingService.makeDeleteRequest(gUrls.getEndpointsUrl() + "?eid=" + gref.getEndpointId());

            map = (Map) utilsComponent.jsonToObject(result, Map.class);
            map = (Map) map.get("response");
            errors = (ArrayList) map.get("errors");

            if (errors.size() > 0) {
                throw new IOException("Errors encountered while trying to remove participant's endpoint");
            }
        }

        gParticipantIdMapper.removeByParticipantId(participantId);
    }

    public void updateMetadataProfilesForReceivers() throws GException {
        List<Integer> profileIds = galaxyMetadataProfileMapper.getAllProfileIds();

        String smpResponse = smpMessagingService.makeGetRequest(gUrls.getParticipantIdentifierUrl());

        Map map = (Map) utilsComponent.jsonToObject(smpResponse, Map.class);
        List<Map> profiles = (List) map.get("participantIdentifiers");
        List<GParticipantIdentifier> result = new ArrayList<GParticipantIdentifier>();

        for (Map item : profiles) {
            GParticipantIdentifier identifier = new GParticipantIdentifier(item, new String(Base64.encodeBase64String(utilsComponent.toJsonString(item).getBytes())));
            identifier.setBase64("");


            GAccessPointConfiguration accessPointConfiguration = new GAccessPointConfiguration();

            if (identifier.getAccessPointConfigurations().size() > 0) {
                accessPointConfiguration = identifier.getAccessPointConfigurations().get(0);
                System.err.println("Identifier Name/Code " + identifier.getName() + " / " + identifier.getValue() + "    Endpoint ID: " + accessPointConfiguration.getEndpointId());

                if (accessPointConfiguration.getEndpointId() == 0)
                    continue;
            } else {
                GParticipantIdentifierReference identifierReference = gParticipantIdMapper.getByParticipantId(identifier.getValue());

                if (identifierReference == null) {
                    System.err.println("WARNING, unregistered SMP receiver id --> " + identifier.getValue());
                    continue;
                }
                accessPointConfiguration.setEndpointId(identifierReference.getEndpointId());
            }

            accessPointConfiguration.setMetadataProfileIds(profileIds);

            result.add(identifier);
        }

        GParticipantIdentifierRequest identifierRequest = new GParticipantIdentifierRequest();
        identifierRequest.setParticipantIdentifiers(result);

        System.err.println("Trying to make POST with identifierRequest: " + identifierRequest);
        System.err.println("The following URL is used: " + gUrls.getParticipantIdentifierUrl());

        String httpResult = smpMessagingService.makePostRequest(utilsComponent.toJsonString(identifierRequest), gUrls.getParticipantIdentifierUrl());

        boolean hasErrors = utilsComponent.hasGalaxyErrors(httpResult,
                "Error occurred while trying to update an endpoint on Tickstar",
                logger);

        if (hasErrors) {
            throw new GException("Error occurred while updating metadata profiles in the SMP");
        }
    }
}