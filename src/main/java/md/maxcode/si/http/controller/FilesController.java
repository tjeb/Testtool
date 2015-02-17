/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.controller;

import md.maxcode.si.domain.CertificateFile;
import md.maxcode.si.domain.UserFile;
import md.maxcode.si.service.FileService;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/file/")
public class FilesController extends TTBaseController {
    @Autowired
    FileService fileService;
    @Autowired
    private TTSettings ttSettings;
    @Autowired
    private UtilsComponent utilsComponent;

    @RequestMapping(value = "user/download", method = {RequestMethod.GET})
    @ResponseBody
    public void downloadFile(@RequestParam("fileId") Long fileId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws IOException {
        UserFile file = fileService.getByIdAndUserId(fileId, getUser().getId());

        if (file == null) {
            return;
        }

        String filePath = utilsComponent.getFullFilePath(ttSettings.storeUserFiles, file.getFileName());
        utilsComponent.performDownload(request, response, filePath, file.getName());
    }

    @RequestMapping(value = "user/download/certificate", method = {RequestMethod.GET})
    @ResponseBody
    public void downloadCertificate(@RequestParam("fileId") Long fileId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws IOException {
        CertificateFile file = fileService.getCertificateByIdAndUserId(fileId, getUser().getId());
        String filePath = utilsComponent.getFullFilePath(ttSettings.storeCertificateFiles, file.getFileName());
        utilsComponent.performDownload(request, response, filePath, file.getFileName());
    }

    @RequestMapping(value = "user/show", method = {RequestMethod.GET})
    public String showUserFile(@RequestParam("fileId") final Long fileId, ModelMap modelMap) {
        UserFile file = fileService.getByIdAndUserId(fileId, getUser().getId());
        final String fileContent = fileService.getFileContent(file.getFileName());
        modelMap.put("xml", StringEscapeUtils.escapeXml(fileContent));
        modelMap.put("fileName", file.getName());
        basicModelInfo(modelMap);

        return "user/fileShow";
    }

    @RequestMapping(value = "admin/show", method = {RequestMethod.GET})
    public String showFile(@RequestParam("id") final Long fileId, ModelMap modelMap) {
        final String fileContent = fileService.getSchemaContent(fileId);

        if (fileContent == null) {
            modelMap.put("xml", "File not found");
        } else {
            modelMap.put("xml", StringEscapeUtils.escapeXml(fileContent));
        }

        basicModelInfo(modelMap);

        return "admin/fileShow";
    }
}