package com.toptal.authservice.shared;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    public String getLoggedUserUsername() {
        Authentication authentication = getAuthentication();
        return authentication.getName();
    }

    private Authentication getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
          .orElseThrow(() -> new UnauthorizedUserException("Authentication can't be null"));
    }

}
