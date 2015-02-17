/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.api;

import md.maxcode.si.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.logging.Logger;

public class TTBaseAPI {
    protected final Logger logger = Logger.getLogger(getClass().getName());

    @ModelAttribute
    protected User getUser() {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getClass() == User.class) {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        return null;
    }
}
