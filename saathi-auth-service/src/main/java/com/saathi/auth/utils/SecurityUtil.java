package com.saathi.auth.utils;

import com.saathi.auth.security.AuthUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtil {
    public static UUID getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof AuthUserDetails user) {
            return user.user().getId(); // Assuming TestingUser has getUser()
        }
        return null;
    }
}