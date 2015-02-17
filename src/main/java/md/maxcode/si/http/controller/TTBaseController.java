/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.controller;

import md.maxcode.si.domain.User;
import md.maxcode.si.tools.TTSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

public class TTBaseController {
    @Autowired
    TTSettings ttSettings;

    protected void basicModelInfo(ModelMap model_, String pageName_) {
        model_.put("pageName", pageName_);
        basicModelInfo(model_);
    }

    protected void basicModelInfo(ModelMap model) {
        model.put("fullName", getUser().getName());
        model.put("identifier", getUser().getIdentifier());
        model.put("userName", getUser().getUsername());
        model.put("companyName", getUser().getCompanyName());
        model.put("user_menu_color", ttSettings.user_menu_color);
        model.put("admin_menu_color", ttSettings.admin_menu_color);
    }

    @ModelAttribute
    protected User getUser() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            return null;
        }

        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getClass() == User.class) {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return null;
    }
}
