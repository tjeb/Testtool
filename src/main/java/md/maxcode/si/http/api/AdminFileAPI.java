/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;


import md.maxcode.si.domain.FileType;
import md.maxcode.si.domain.SchemaFile;
import md.maxcode.si.service.FileService;
import md.maxcode.si.service.InputValidationService;
import md.maxcode.si.service.SMPReceiversService;
import md.maxcode.si.service.UserService;
import md.maxcode.si.smp.galaxygateway.GException;
import md.maxcode.si.tools.Response;
import md.maxcode.si.tools.UtilsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/admin/file/")
public class AdminFileAPI extends TTBaseAPI {

    @Autowired
    UserService userService;

    @Autowired
    FileService fileService;

    @Autowired
    UtilsComponent utilsComponent;

    @Autowired
    InputValidationService inputService;

    @Autowired
    SMPReceiversService smpReceiversService;

    @RequestMapping(value = "type", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response addType(@RequestParam(value = "name") String name,
                            @RequestParam(value = "identifier") String identifier,
                            @RequestParam(value = "galaxyMetadataProfile", required = false) String galaxyMetadataProfile_base64
    ) {
        Map errors = inputService.validateDocumentType(name, identifier);

        if (errors.size() == 0) {
            Long fileTypeId = fileService.insertType(name, identifier, galaxyMetadataProfile_base64);
            if (galaxyMetadataProfile_base64 != null && galaxyMetadataProfile_base64 != "") {
                try {
                    smpReceiversService.updateMetadataProfilesForReceivers();
                } catch (GException e) {
                    fileService.deleteType(fileTypeId);
                    logger.severe("An error has occurred while trying to add a new type to Galaxy: " + e.getMessage());
                    return new Response(e);
                }
            }
            return new Response(false);
        }

        return new Response("Validation errors encountered", true, errors);
    }

    @RequestMapping(value = "type", method = {RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response updateType(@RequestParam(value = "id") Long typeId_,
                               @RequestParam(value = "name") String name_,
                               @RequestParam(value = "identifier") String identifier_) {
        fileService.updateType(typeId_, name_, identifier_);
        return new Response(false);
    }

    @RequestMapping(value = "type", method = {RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response deleteType(@RequestParam(value = "id") Long id_) {
        String errorMessage = "";

        try {
            fileService.deleteType(id_);
            smpReceiversService.updateMetadataProfilesForReceivers();
        } catch (GException e) {
            logger.severe("An error has occurred and SMP data has not been updated: " + e.getMessage());
            errorMessage = "An error has occurred and SMP data has not been updated.";
            System.err.println(e.getMessage());
        }

        return new Response(false, errorMessage);
    }

    @RequestMapping(value = "type", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getTypes() {
        List<FileType> allFileTypes = fileService.getAllFileTypes();
        return new Response(false, allFileTypes);
    }

    @RequestMapping(value = "schemaFile", method = RequestMethod.POST)
    @ResponseBody
    public Response addSchemaFile(@RequestParam("file") MultipartFile multipartFile_,
                                  @RequestParam("name") String name_,
                                  @RequestParam("typeId") Long typeId_) {
        if (!multipartFile_.isEmpty()) {
            try {
                SchemaFile file = fileService.saveAndInsertSchemaFile(
                        multipartFile_,
                        name_,
                        typeId_,
                        getUser().getId());

                if (file == null) {
                    return new Response(true);
                }

                return new Response(false, file);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                logger.severe("An error has occurred while trying to add schema file: " + e.getMessage());
                e.printStackTrace();
                return new Response(e);
            }
        }

        return new Response("Multipart file is empty", true);
    }

    @RequestMapping(value = "schemaFile", method = {RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response deleteSchemaFile(@RequestParam(value = "id") Long id_) {
        fileService.deleteSchemaFile(id_);
        return new Response(false);
    }

    @RequestMapping(value = "schemaFile", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response getSchemaFiles() {
        return new Response(false, fileService.getAllSchemas());
    }

    @RequestMapping(value = "schemaFile/mark", method = {RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response markSchemaFile(@RequestParam("id") Long id) {
        return new Response(!fileService.markSchemaFile(id));
    }

}