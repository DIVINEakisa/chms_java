package com.chms.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Input Validation Utility
 * Provides server-side input validation methods for security
 */
public class ValidationUtil {
    
    /**
     * Check if a string is null or empty
     * @param str String to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Validate email format
     * @param email Email address to validate
     * @return true if valid email format, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    /**
     * Validate phone number format
     * @param phone Phone number to validate
     * @return true if valid phone format, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        // Accepts formats: +1234567890, 1234567890, 123-456-7890, (123) 456-7890
        String phoneRegex = "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$";
        return phone.matches(phoneRegex);
    }
    
    /**
     * Validate that a string contains only letters and spaces
     * @param str String to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidName(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return str.matches("^[a-zA-Z\\s]+$");
    }
    
    /**
     * Validate that a number is positive
     * @param value Number to validate
     * @return true if positive, false otherwise
     */
    public static boolean isPositiveNumber(double value) {
        return value > 0;
    }
    
    /**
     * Sanitize input to prevent XSS attacks
     * @param input Input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (isEmpty(input)) {
            return input;
        }
        
        // Replace potentially dangerous characters
        return input.replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;")
                   .replace("/", "&#x2F;");
    }
    
    /**
     * Validate weight is in reasonable range (kg)
     * @param weight Weight in kg
     * @return true if valid, false otherwise
     */
    public static boolean isValidWeight(double weight) {
        return weight > 0 && weight <= 200;
    }
    
    /**
     * Validate height is in reasonable range (cm)
     * @param height Height in cm
     * @return true if valid, false otherwise
     */
    public static boolean isValidHeight(double height) {
        return height > 0 && height <= 250;
    }
    
    /**
     * Validate temperature is in reasonable range (Celsius)
     * @param temperature Temperature in Celsius
     * @return true if valid, false otherwise
     */
    public static boolean isValidTemperature(double temperature) {
        return temperature >= 35 && temperature <= 42;
    }
    
    /**
     * Get safe parameter value from request
     * @param request HttpServletRequest
     * @param paramName Parameter name
     * @return Sanitized parameter value or empty string if null
     */
    public static String getSafeParameter(HttpServletRequest request, String paramName) {
        String value = request.getParameter(paramName);
        return value != null ? sanitizeInput(value.trim()) : "";
    }
    
    /**
     * Get integer parameter from request with default value
     * @param request HttpServletRequest
     * @param paramName Parameter name
     * @param defaultValue Default value if parsing fails
     * @return Integer value or default
     */
    public static int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
        String value = request.getParameter(paramName);
        if (isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Get double parameter from request with default value
     * @param request HttpServletRequest
     * @param paramName Parameter name
     * @param defaultValue Default value if parsing fails
     * @return Double value or default
     */
    public static double getDoubleParameter(HttpServletRequest request, String paramName, double defaultValue) {
        String value = request.getParameter(paramName);
        if (isEmpty(value)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
