/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;

import md.maxcode.si.domain.User;
import md.maxcode.si.service.EmailSendingService;
import md.maxcode.si.service.UserService;
import md.maxcode.si.tools.Response;
import md.maxcode.si.tools.UtilsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/api/login/")
public class LoginAPI extends TTBaseAPI {
    @Autowired
    UserService userService;

    @Autowired
    EmailSendingService emailSendingService;

    @Autowired
    private UtilsComponent utilsComponent;
    private Logger logger = Logger.getLogger(getClass().getName());

    @RequestMapping(value = "passwordReset", method = {RequestMethod.POST})
    @ResponseBody
    public Response passwordReset(@RequestParam("username") String username) {
        User user = userService.getByUsername(username);
        Map<String, String> map = new HashMap<>();
        if (user == null) {
            map.put("username", "Username not found!");
        }

        if (map.size() > 0) {
            return new Response(true, map);
        }

        String tempPassword = utilsComponent.getRandomString();
        String passwordHash = utilsComponent.getPasswordHash(tempPassword, user.getSalt());

        userService.updateTempPassword(passwordHash, user.getId());
        boolean b = emailSendingService.sendPasswordReset(user.getEmail(), tempPassword);

        if (!b) {
            logger.severe("Error encountered while trying to send email to: " + user.getEmail());
            userService.deleteTempPassword(user.getId());
            map.put("password", "Error encountered while trying to send the email");
            return new Response(true, map);
        }

        return new Response(false);
    }

    @RequestMapping(value = "usernameReset", method = {RequestMethod.POST})
    @ResponseBody
    public Response usernameReset(@RequestParam("email") String email) {
        User user = userService.getByEmail(email);
        Map<String, String> map = new HashMap<>();
        if (user == null) {
            map.put("username", "E-mail address not found!");
        }

        if (map.size() > 0) {
            return new Response(true, map);
        }

        boolean b = emailSendingService.sendUsernameReminder(user.getEmail(), user.getUsername());

        if (!b) {
            logger.severe("Error encountered while trying to send email to: " + user.getEmail());
            userService.deleteTempPassword(user.getId());
            map.put("username", "Error encountered while trying to send email");
            return new Response(true, map);
        }

        return new Response(false);
    }
}
