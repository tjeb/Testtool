/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.controller;

import md.maxcode.si.service.FileService;
import md.maxcode.si.service.SMPReceiversService;
import md.maxcode.si.service.UserService;
import md.maxcode.si.smp.galaxygateway.GMetadataProfile;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("/html/admin/")
public class AdminController extends TTBaseController {
    @Autowired
    FileService fileService;
    @Autowired
    SMPReceiversService smpReceiversService;
    @Autowired
    UserService userService;
    @Autowired
    private UtilsComponent utilsComponent;
    @Autowired
    private TTSettings ttSettings;

    @RequestMapping(value = "users/list", method = {RequestMethod.GET})
    public String listUsers(ModelMap model) {
        basicModelInfo(model, "users");
        return "admin/usersList";
    }

    @RequestMapping(value = "users/add", method = {RequestMethod.GET})
    public String addUser(ModelMap model) {
        basicModelInfo(model, "users");
        return "admin/usersAdd";
    }

    @RequestMapping(value = "documentTypes/list", method = {RequestMethod.GET})
    public String listDocumentTypes(ModelMap model) {
        basicModelInfo(model, "documentTypes");
        return "admin/documentTypesList";
    }

    @RequestMapping(value = "documentTypes/add", method = {RequestMethod.GET})
    public String addDocumentType(ModelMap modelMap) {
        List<GMetadataProfile> metadataProfiles = smpReceiversService.getMetadataProfiles(true);
        modelMap.put("metadataProfiles", metadataProfiles);

        basicModelInfo(modelMap, "documentTypes");
        return "admin/documentTypesAdd";
    }
}