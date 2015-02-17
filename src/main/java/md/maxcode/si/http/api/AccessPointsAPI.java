/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;


import md.maxcode.si.service.AccessPointService;
import md.maxcode.si.service.FileSendingService;
import md.maxcode.si.service.FileService;
import md.maxcode.si.service.InputValidationService;
import md.maxcode.si.tools.Response;
import md.maxcode.si.tools.TTSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/api/user/ap/")
public class AccessPointsAPI extends TTBaseAPI {
    @Autowired
    FileService fileService;
    @Autowired
    FileSendingService fileSendingService;
    @Autowired
    AccessPointService accessPointService;
    @Autowired
    InputValidationService inputService;
    @Autowired
    private TTSettings ttSettings;

    @RequestMapping(method = {RequestMethod.POST})
    @ResponseBody
    public Response addAPConfig(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                                @RequestParam(value = "url", required = false, defaultValue = "") String url,
                                @RequestParam(value = "description", required = false, defaultValue = "") String description,
                                @RequestParam(value = "certificate", required = false, defaultValue = "") String certificate,
                                @RequestParam(value = "contactEmail", required = false, defaultValue = "") String contactEmail,
                                @RequestParam(value = "technicalUrl", required = false, defaultValue = "") String technicalUrl) {
        Long fileId;

        Map errors = inputService.validateAPData(name, url, certificate, getUser().getId(),
                description, contactEmail, technicalUrl, true);

        if (errors.size() == 0) {
            try {
                fileId = accessPointService.add(getUser().getId(), name, url, certificate,
                        description, contactEmail, technicalUrl);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                logger.severe("An error has occurred while trying to addAPConfig: " + e.getMessage());
                return new Response(true, errors);
            }

            return new Response(false, fileId);
        }

        return new Response(true, errors);
    }

    @RequestMapping(value = "sendFileToAP", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response sendFileToAP(@RequestParam(value = "apConfigId", required = true) Long apConfigId_,
                                 @RequestParam(value = "fileId", required = true) Long fileId_,
                                 @RequestParam(value = "recipientPeppolId", required = true) String recipientPeppolId_,
                                 @RequestParam(value = "generateTriggerUrl", required = false) boolean generateTriggerUrl_) {
        if (recipientPeppolId_.isEmpty() || apConfigId_ < -1 || fileId_ < 1) {
            return new Response("Invalid parameters for sending a file to AP", true);
        }

        try {
            if (generateTriggerUrl_) {
                String triggerParams = fileSendingService.getFileSendingTriggerParams(apConfigId_, fileId_, getUser().getIdentifier(), recipientPeppolId_);
                String triggerUrl = ttSettings.triggerUrl + triggerParams;
                return new Response(false, triggerUrl);
            } else {
                fileSendingService.sendFileToAP(apConfigId_, getUser().getId(), fileId_, getUser().getIdentifier(), recipientPeppolId_);
            }
        } catch (Throwable e_) {
            System.err.println(e_.getMessage());
            logger.severe("An error has occurred while trying to send file to access point using POST: " + e_.getMessage());
            return new Response(e_);
        }

        return new Response(false);
    }

    @RequestMapping(value = "sendFileToAP", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response sendFileToAP(@RequestParam(value = "i", required = true) String base64Input_,
                                 @RequestParam(value = "s", required = true) String base64HMACSignature_) {
        try {
            fileSendingService.sendFileToAP(getUser().getId(), base64Input_, base64HMACSignature_);
        } catch (Exception e_) {
            System.err.println(e_.getMessage());
            logger.severe("An error has occurred while trying to send file to access point: " + e_.getMessage());
            return new Response(e_);
        }

        return new Response(false);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response sendFileToAP(@RequestParam(value = "id", required = true) Long id) {
        try {
            accessPointService.removeAP(id, getUser().getId());
        } catch (Exception e_) {
            System.err.println(e_.getMessage());
            logger.severe("An error has occurred while trying to delete access point configuration: " + e_.getMessage());
            return new Response(e_);
        }

        return new Response(false);
    }

    @RequestMapping(value = "getAll", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getAll(@RequestParam(value = "userId", required = true) Long userId) {
        try {
            accessPointService.getAllByUserId(userId);
        } catch (Exception e_) {
            System.err.println(e_.getMessage());
            logger.severe("An error has occurred while trying to get all access point configurations: " + e_.getMessage());
            return new Response(e_);
        }

        return new Response(false);
    }
}