/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;

import md.maxcode.si.service.FileSendingService;
import md.maxcode.si.service.FileService;
import md.maxcode.si.service.InputValidationService;
import md.maxcode.si.service.SMPReceiversService;
import md.maxcode.si.smp.galaxygateway.GException;
import md.maxcode.si.smp.galaxygateway.GParticipantIdentifierRequest;
import md.maxcode.si.tools.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;


@Controller
@RequestMapping("/api/user/smp/")
public class SMPReceiversAPI extends TTBaseAPI {
    @Autowired
    FileService fileService;

    @Autowired
    FileSendingService fileSendingService;

    @Autowired
    SMPReceiversService smpReceiversService;

    @Autowired
    InputValidationService inputService;


    @RequestMapping(method = {RequestMethod.POST})
    @ResponseBody
    public Response addAPConfig(@RequestParam(value = "endPointId", required = false, defaultValue = "") Long endPointId,
                                @RequestParam(value = "participantName", required = false, defaultValue = "") String participantName) {
        Map errors = inputService.validateGSMP(endPointId, participantName);

        GParticipantIdentifierRequest identifierRequest;

        if (errors.size() == 0) {
            try {
                identifierRequest = smpReceiversService.addSMPReceiver(getUser().getId(), endPointId, participantName);
            } catch (GException e) {
                System.err.println(e.getMessage());
                return new Response(e);
            }

            return new Response(false, identifierRequest);
        }

        return new Response(true, errors);
    }

    @RequestMapping(value = "sendFileToAP", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response sendFileToAP(@RequestParam(value = "i", required = true) String base64Input_,
                                 @RequestParam(value = "s", required = true) String base64HMACSignature_) {

        return new Response(false);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response sendFileToAP(@RequestParam(value = "participantId", required = true) String participantId) {
        try {
            smpReceiversService.deleteReceiver(participantId);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            logger.severe("An error has occurred while trying delete file from AP: " + e.getMessage());
            return new Response(e);
        }

        return new Response(false);
    }
}