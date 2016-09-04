/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;

import md.maxcode.si.domain.UserFile;
import md.maxcode.si.service.FileService;
import md.maxcode.si.tools.Response;
import md.maxcode.si.tools.TTSettings;
import md.maxcode.si.tools.UtilsComponent;
import md.maxcode.si.xsl.XSLErrorInfo;
import md.maxcode.si.xsl.XSLParseException;
import nu.xom.ParsingException;
import nu.xom.xslt.XSLException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/user/file/")
public class UserFileAPI extends TTBaseAPI {
    @Autowired
    FileService fileService;
    @Autowired
    private UtilsComponent utilsComponent;
    @Autowired
    private TTSettings ttSettings;

    @RequestMapping(method = {RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response delete(@RequestParam("id") Long id_) {
        boolean fileDeleted = fileService.deleteUserFile(getUser().getId(), id_);
        return new Response(!fileDeleted);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Response handleFormUpload(@RequestParam("file") MultipartFile multipartFile_,
                                     @RequestParam("name") String name,
                                     @RequestParam("typeId") Long typeId_) {
        if (name == null || name.trim().equals("")) {
            return new Response("The name should not be empty. Please select a proper file.", true);
        }

        String extension = FilenameUtils.getExtension(multipartFile_.getOriginalFilename());
        String nameHash;
        List<XSLErrorInfo> xslErrorInfos;
        try {
            nameHash = fileService.savePhysicalFile(multipartFile_, ttSettings.storeUserFiles, name);
            xslErrorInfos = fileService.validateFile(utilsComponent.getFullFilePath(ttSettings.storeUserFiles, nameHash), typeId_);
        } catch (IOException | SAXException | XSLException | XSLParseException | ParsingException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            return new Response(e);
        }

        final String validationInfo = utilsComponent.toMessage(xslErrorInfos, true);
        final boolean validated = xslErrorInfos.size() < 1;

        if (!multipartFile_.isEmpty()) {
            UserFile file = fileService.insertFile(
                    typeId_,
                    getUser().getId(),
                    nameHash,
                    null,
                    name,
                    multipartFile_.getSize(),
                    extension,
                    null,
                    validated,
                    validationInfo,
                    false);

            return new Response(false, !validated, utilsComponent.toJsonString(file), validationInfo);
        }

        return new Response(true);
    }

}
