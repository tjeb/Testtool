/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;

import md.maxcode.si.domain.User;
import md.maxcode.si.service.AccessPointService;
import md.maxcode.si.service.InputValidationService;
import md.maxcode.si.service.SMPReceiversService;
import md.maxcode.si.service.UserService;
import md.maxcode.si.tools.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/api/admin/user")
public class AdminUserAPI extends TTBaseAPI {

    @Autowired
    UserService userService;

    @Autowired
    SMPReceiversService smpReceiversService;

    @Autowired
    InputValidationService inputService;

    @Autowired
    AccessPointService accessPointService;

    @RequestMapping(method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response post(@RequestParam(value = "name") String name_,
                         @RequestParam(value = "companyName") String companyName_,
                         @RequestParam(value = "username") String username_,
                         @RequestParam(value = "identifier") String identifier,
                         @RequestParam(value = "password") String password_,
                         @RequestParam(value = "email") String email,
                         @RequestParam(value = "enabled") Boolean enabled_,
                         @RequestParam(value = "type") String type_
    ) {
        Map errors = inputService.validateUserData(name_, companyName_, username_, identifier, password_, email, type_, true, true);

        if (errors.size() == 0) {
            User user = userService.insertUser(name_, companyName_, identifier, username_, password_, enabled_, type_, false, true, email);
            return new Response(false);
        }

        return new Response("Validation errors encountered", true, errors);
    }

    @RequestMapping(method = {RequestMethod.PUT}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response put(@RequestParam(value = "name") String name_,
                        @RequestParam(value = "companyName") String companyName_,
                        @RequestParam(value = "username") String username_,
                        @RequestParam(value = "identifier") String identifier,
                        @RequestParam(value = "enabled") Boolean enabled_,
                        @RequestParam(value = "type") String type_,
                        @RequestParam(value = "email") String email,
                        @RequestParam(value = "password", required = false) String password,
                        @RequestParam(value = "id") Long id_
    ) {
        Map<String, String> errors = inputService.validateUserData(name_, companyName_, username_, identifier, password, email, type_, false, false);

        if (errors.size() == 0) {
            userService.updateUser(name_, companyName_, username_, identifier, enabled_, type_, email, password, id_);
            return new Response(false);
        }

        return new Response("Validation errors encountered", true, errors);
    }

    @RequestMapping(method = {RequestMethod.DELETE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response delete(@RequestParam("userId") Long userId_) {
        try {
            userService.remove(userId_);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new Response(e);
        }
        return new Response(false);
    }

    @RequestMapping(method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Response get() {
        return new Response(false, userService.getAllNonEditable());
    }

}