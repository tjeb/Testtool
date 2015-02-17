/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.http.controller;

import md.maxcode.si.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserRequestInterceptor extends TTBaseController implements HandlerInterceptor {

    @Autowired
    UserService userService;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String requestUri = request.getRequestURI();
        String tempPassword = userService.getTempPassword(getUser().getId());

        if (tempPassword == null) {
            if (requestUri.endsWith("/html/user/changePassword")) {
                redirectStrategy.sendRedirect(request, response, "/");
                return false;
            }
        } else {
            if (!requestUri.endsWith("/html/user/changePassword")) {
                redirectStrategy.sendRedirect(request, response, "/html/user/changePassword");
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {


    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {


    }
}