/*
 * Copyright (c) 2015 Maxcode B.V.
 */

package md.maxcode.si.security;

import md.maxcode.si.domain.User;
import md.maxcode.si.service.UserService;
import md.maxcode.si.tools.UtilsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("ttAuthenticationProvider")
public class TTAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private UtilsComponent utilsComponent;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        User user = userService.getByUsername(username);

        if (user == null) {
            throw new BadCredentialsException("Try again.");
        }

        String passwordHash = utilsComponent.getPasswordHash(password, user.getSalt());
        String tmpPassword = userService.getTempPassword(user.getId());
        Boolean authenticated = false;

        if (user.getPassword() != null && passwordHash.equals(user.getPassword())) {
            authenticated = true;

            if (tmpPassword != null) {
                userService.deleteTempPassword(user.getId());
            }
        } else if (passwordHash.equals(tmpPassword)) {
            authenticated = true;
        }

        if (!authenticated) {
            throw new BadCredentialsException("Try again.");
        }

        user.setAuthorities(userService.getRolesById(user.getId()));
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }
}