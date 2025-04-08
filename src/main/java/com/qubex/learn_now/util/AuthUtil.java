package com.qubex.learn_now.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthUtil {



    private AuthUtil() {
        throw new UnsupportedOperationException("Utility class - instantiation not allowed");
    }

    public static String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }
            return principal.toString();
        }
        return null;
    }

}
