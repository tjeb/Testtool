/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.controller;


import md.maxcode.si.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class LoginController extends TTBaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public String redirectToLogin() {
        if (getUser() == null) {
            return "redirect:/login";
        }

        String tmpPassword = userService.getTempPassword(getUser().getId());
        if (tmpPassword != null) {
            return "redirect:/html/user/changePassword";
        }

        if (getUser().getUsername().equals("admin")) {
            return "redirect:/html/admin/users/list";
        }

        return "redirect:/html/user/dashBoard";
    }

    @RequestMapping(value = "login", method = {RequestMethod.GET})
    public String firstContact(ModelMap map_, @RequestParam(value = "error", required = false, defaultValue = "false") Boolean error) {
        if (getUser() != null) {
            return "redirect:/";
        }

        map_.put("error", error);

        return "login";
    }
}