package com.uoc.inmo.gui.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.uoc.inmo.query.entity.user.Role;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.CollectionUtils;

public class SecurityUtils {
    
    private SecurityUtils() {
        // Util methods only
    }

    public static boolean isFrameworkInternalRequest(HttpServletRequest request) { 
        final String requestUri = request.getRequestURI();

        boolean isInternal = requestUri != null && 
            (requestUri.equals("/ui/") ||
        internalUrls().stream().anyMatch(r -> requestUri.startsWith(r)));

        if(!isInternal)
            System.out.println("requestUri: "+requestUri);
        
        return isInternal;
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

    public static boolean isProfesionalLoggedUser(){
        Collection<GrantedAuthority> authorities = getAuthoritiesLoggedUser();

        if(CollectionUtils.isEmpty(authorities))
            return false;

        return authorities.stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase(Role.ADMIN) || 
            r.getAuthority().equalsIgnoreCase(Role.PROFESIONAL));
    }

    public static boolean isParticularLoggedUser(){
        Collection<GrantedAuthority> authorities = getAuthoritiesLoggedUser();

        if(CollectionUtils.isEmpty(authorities))
            return false;

        return authorities.stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase(Role.ADMIN) || 
            r.getAuthority().equalsIgnoreCase(Role.PARTICULAR));
    }

    public static boolean isAdminLoggedUser(){
        Collection<GrantedAuthority> authorities = getAuthoritiesLoggedUser();

        if(CollectionUtils.isEmpty(authorities))
            return false;

        return authorities.stream().anyMatch(r -> r.getAuthority().equalsIgnoreCase(Role.ADMIN));
    }

    public static Collection<GrantedAuthority> getAuthoritiesLoggedUser(){
        User loggedUser = getLoggedUser();

        if(loggedUser != null)
            return loggedUser.getAuthorities();

        return null;
    }

    private static List<String> internalUrls(){

        return new ArrayList<>(Arrays.asList(
            "/ui/VAADIN/",
            "/inmueble",
            "/ui/sw.js",
            "/error",
            "/ui/icons/",
            "/favicon.ico",
            "/robots.txt",
            "/manifest.webmanifest",
            "/sw.js",
            "/offline.html",
            "/icons/**",
            "/images/**",
            "/styles/**",
            "/h2-console/**",
            "/swagger-",
            "/v2/api"));
    }

}
