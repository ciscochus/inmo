package com.uoc.inmo.gui.security;

import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import com.vaadin.flow.server.HandlerHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class SecurityUtils {
    
    private SecurityUtils() {
        // Util methods only
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) { 
        final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
            && Stream.of(RequestType.values())
            .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

    public static boolean isUserLoggedIn() { 
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
            && !(authentication instanceof AnonymousAuthenticationToken)
            && authentication.isAuthenticated();
    }

    public static User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if(isUserLoggedIn() && 
            authentication.getPrincipal() instanceof User)
                return (User) authentication.getPrincipal();

        return null;
    }

    public static String getEmailLoggedUser() {
        User loggedUser = getLoggedUser();

        if(loggedUser != null)
            return loggedUser.getUsername();

        return null;
    }

}
