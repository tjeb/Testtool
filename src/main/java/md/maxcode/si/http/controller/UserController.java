/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.controller;

import md.maxcode.si.domain.FileType;
import md.maxcode.si.service.*;
import md.maxcode.si.smp.galaxygateway.GParticipantIdentifier;
import md.maxcode.si.tools.TTSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/html/user/")
public class UserController extends TTBaseController {

    @Autowired
    FileService fileService;

    @Autowired
    TTSettings ttSettings;

    @Autowired
    InputValidationService inputValidationService;

    @Autowired
    UserService userService;

    @Autowired
    CertificateService certificateService;

    @Autowired
    AccessPointService accessPointService;

    @Autowired
    SMPReceiversService smpReceiversService;

    @RequestMapping(value = "changePassword", method = {RequestMethod.GET})
    public String changePassword(ModelMap model) {
        basicModelInfo(model);

        if (userService.getTempPassword(getUser().getId()) == null) {
            return "redirect:dashBoard";
        }

        return "user/changePassword";
    }

    @RequestMapping(value = "changePassword", method = {RequestMethod.POST})
    public String changePassword_post(final ModelMap map,
                                      @RequestParam("newPassword") final String newPassword,
                                      @RequestParam("newPassword2") final String newPassword2) {
        if (userService.getTempPassword(getUser().getId()) == null) {
            return "redirect:dashBoard";
        }

        basicModelInfo(map);
        Map errors = inputValidationService.newPassword(newPassword, newPassword2);
        map.put("errors", errors);

        if (errors.size() > 0) {
            return "user/changePassword";
        }

        userService.updatePassword(getUser().getId(), newPassword);

        return "redirect:dashBoard";
    }

    @RequestMapping(value = "dashBoard", method = {RequestMethod.GET})
    public String dashboard(ModelMap model) {
        basicModelInfo(model);

        Long userId = getUser().getId();
        model.put("files", fileService.getLatestByUserId(Long.valueOf(userId)));
        model.put("fileTypes", fileService.getAllFileTypes_mapped());
        model.put("accessPoints", accessPointService.getLatestByUserId(Long.valueOf(userId)));
        return "user/dashBoard";
    }

    @RequestMapping(value = "accessPoints/list", method = {RequestMethod.GET})
    public String listAPConfigs(ModelMap model) {
        basicModelInfo(model);
        Long userId = getUser().getId();
        model.put("accessPoints", accessPointService.getAllByUserId(Long.valueOf(userId)));
        model.put("files", fileService.getByUserId(Long.valueOf(userId)));
        model.put("defaultSenderId", getUser().getIdentifier());
        model.put("defaultRecipientId", getUser().getIdentifier());
        return "user/accessPointsList";
    }

    @RequestMapping(value = "accessPoints/add", method = {RequestMethod.GET})
    public String addAPConfigView(ModelMap model,
                                  @RequestParam(value = "error", required = false, defaultValue = "false") Boolean error,
                                  @RequestParam(value = "errorDetails", required = false) String errorDetails_,
                                  @RequestParam(value = "fileId", required = false) String fileId_) {
        basicModelInfo(model);
        model.put("error", error);
        model.put("errorDetails", errorDetails_);
        model.put("fileId", fileId_);
        return "user/accessPointsAdd";
    }

    @RequestMapping(value = "files/add", method = {RequestMethod.GET})
    public String addFile(ModelMap model) {
        List<FileType> fileTypes = fileService.getAllFileTypes();

        basicModelInfo(model);
        model.put("fileTypes", fileTypes);
        model.put("step", "upload");
        return "user/filesAdd";
    }

    @RequestMapping(value = "files/list", method = {RequestMethod.GET})
    public String listFiles(ModelMap model) {
        basicModelInfo(model);

        Long userId = getUser().getId();
        model.put("apConfigs", accessPointService.getAllByUserId(Long.valueOf(userId)));
        model.put("files", fileService.getByUserId(Long.valueOf(userId)));
        model.put("fileTypes", fileService.getAllFileTypes_mapped());
        model.put("defaultSenderId", getUser().getIdentifier());
        model.put("defaultRecipientId", getUser().getIdentifier());
        return "user/filesList";
    }

    @RequestMapping(value = "smpReceivers/list", method = {RequestMethod.GET})
    public String smpReceiversList(ModelMap model) {
        Long userId = getUser().getId();
        model.put("files", fileService.getByUserId(Long.valueOf(userId)));

        List<GParticipantIdentifier> identifiers = smpReceiversService.getSMPReceivers(getUser().getId());
        model.put("identifiers", identifiers);
        model.put("defaultSenderId", getUser().getIdentifier());
        model.put("defaultRecipientId", getUser().getIdentifier());

        basicModelInfo(model);

        return "user/smpReceiversList";
    }

    @RequestMapping(value = "smpReceivers/add", method = {RequestMethod.GET})
    public String smpReceiversAdd(ModelMap model) {
        model.put("endpoints", accessPointService.getAllByUserId(getUser().getId()));

        basicModelInfo(model);

        return "user/smpReceiversAdd";
    }


}