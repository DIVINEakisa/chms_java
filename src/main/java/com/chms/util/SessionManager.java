package com.chms.util;

import com.chms.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Session Management Utility
 * Provides helper methods for session handling and user authentication state
 */
public class SessionManager {
    
    // Session attribute keys
    public static final String SESSION_USER = "loggedInUser";
    public static final String SESSION_USER_ID = "userId";
    public static final String SESSION_USER_ROLE = "userRole";
    public static final String SESSION_USER_NAME = "userName";
    
    // Session timeout (30 minutes in seconds)
    public static final int SESSION_TIMEOUT = 1800;
    
    /**
     * Create a new session for the user after login
     * @param request HttpServletRequest
     * @param user User object
     */
    public static void createUserSession(HttpServletRequest request, User user) {
        HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(SESSION_TIMEOUT);
        
        // Store user information in session
        session.setAttribute(SESSION_USER, user);
        session.setAttribute(SESSION_USER_ID, user.getUserId());
        session.setAttribute(SESSION_USER_ROLE, user.getRole().name());
        session.setAttribute(SESSION_USER_NAME, user.getFullName());
    }
    
    /**
     * Get the logged in user from session
     * @param request HttpServletRequest
     * @return User object or null if not logged in
     */
    public static User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute(SESSION_USER);
        }
        return null;
    }
    
    /**
     * Get the logged in user's ID
     * @param request HttpServletRequest
     * @return User ID or 0 if not logged in
     */
    public static int getLoggedInUserId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Integer userId = (Integer) session.getAttribute(SESSION_USER_ID);
            return userId != null ? userId : 0;
        }
        return 0;
    }
    
    /**
     * Get the logged in user's role
     * @param request HttpServletRequest
     * @return User role as String or null if not logged in
     */
    public static String getLoggedInUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (String) session.getAttribute(SESSION_USER_ROLE);
        }
        return null;
    }
    
    /**
     * Check if user is logged in
     * @param request HttpServletRequest
     * @return true if user is logged in, false otherwise
     */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(SESSION_USER) != null;
    }
    
    /**
     * Check if logged in user has a specific role
     * @param request HttpServletRequest
     * @param role Role to check (MOTHER, DOCTOR, ADMIN)
     * @return true if user has the role, false otherwise
     */
    public static boolean hasRole(HttpServletRequest request, String role) {
        String userRole = getLoggedInUserRole(request);
        return userRole != null && userRole.equalsIgnoreCase(role);
    }
    
    /**
     * Check if logged in user is a mother
     * @param request HttpServletRequest
     * @return true if user is a mother, false otherwise
     */
    public static boolean isMother(HttpServletRequest request) {
        return hasRole(request, "MOTHER");
    }
    
    /**
     * Check if logged in user is a doctor
     * @param request HttpServletRequest
     * @return true if user is a doctor, false otherwise
     */
    public static boolean isDoctor(HttpServletRequest request) {
        return hasRole(request, "DOCTOR");
    }
    
    /**
     * Check if logged in user is an admin
     * @param request HttpServletRequest
     * @return true if user is an admin, false otherwise
     */
    public static boolean isAdmin(HttpServletRequest request) {
        return hasRole(request, "ADMIN");
    }
    
    /**
     * Invalidate user session (logout)
     * @param request HttpServletRequest
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
    
    /**
     * Get client IP address
     * @param request HttpServletRequest
     * @return IP address as String
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    
    /**
     * Get user agent from request
     * @param request HttpServletRequest
     * @return User agent string
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }
}
