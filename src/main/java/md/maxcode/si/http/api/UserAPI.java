/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;

import md.maxcode.si.service.InputValidationService;
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

@Controller
@RequestMapping("/api/user/")
public class UserAPI extends TTBaseAPI {

    @Autowired
    UserService userService;

    @Autowired
    InputValidationService inputValidationService;

    @Autowired
    private UtilsComponent utilsComponent;

    @RequestMapping(value = "/password", method = {RequestMethod.POST})
    @ResponseBody
    public Response password(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            @RequestParam(value = "newPassword2", required = false) String newPassword2,
            @RequestParam(value = "currentPassword", required = false) String currentPassword
    ) {
        final Map map = new HashMap();

        if (!currentPassword.isEmpty()) {
            map.putAll(inputValidationService.newPassword(getUser(), currentPassword, newPassword, newPassword2));
        }

        if (!name.isEmpty()) {
            map.putAll(inputValidationService.name(name));
        }

        if (map.size() < 1) {
            if (!name.isEmpty()) {
                userService.updateName(getUser().getId(), name);
                getUser().setName(name);
            }

            if (!currentPassword.isEmpty()) {
                final boolean result = userService.updatePassword(getUser().getId(), newPassword);

                if (!result) {
                    map.put("newPassword", "Error, please contact Administrator!");
                }
            }

            if (map.size() < 1) {
                return new Response(false);
            }
        }

        return new Response(true, map);
    }

}