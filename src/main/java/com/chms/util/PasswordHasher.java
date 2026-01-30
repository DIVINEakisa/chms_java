package com.chms.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password Hashing Utility using BCrypt
 * Provides secure password hashing and verification for the CHMS application
 */
public class PasswordHasher {
    
    // BCrypt work factor (log rounds) - higher is more secure but slower
    private static final int WORK_FACTOR = 10;
    
    /**
     * Hash a plain text password using BCrypt
     * @param plainTextPassword The plain text password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainTextPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(WORK_FACTOR));
    }
    
    /**
     * Verify a plain text password against a hashed password
     * @param plainTextPassword The plain text password to verify
     * @param hashedPassword The hashed password to compare against
     * @return true if the passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || plainTextPassword.isEmpty()) {
            return false;
        }
        if (hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            // Invalid hash format or other error
            return false;
        }
    }
    
    /**
     * Check if a password meets minimum security requirements
     * @param password The password to check
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        // Check for at least one uppercase, one lowercase, one digit
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        
        return hasUppercase && hasLowercase && hasDigit;
    }
    
    /**
     * Get password strength message
     * @param password The password to evaluate
     * @return A message describing password strength
     */
    public static String getPasswordStrengthMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }
        
        if (password.length() < 8) {
            return "Password must be at least 8 characters long";
        }
        
        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
        boolean hasLowercase = password.chars().anyMatch(Character::isLowerCase);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch));
        
        if (!hasUppercase) {
            return "Password must contain at least one uppercase letter";
        }
        if (!hasLowercase) {
            return "Password must contain at least one lowercase letter";
        }
        if (!hasDigit) {
            return "Password must contain at least one digit";
        }
        
        if (password.length() >= 12 && hasSpecial) {
            return "Strong password";
        } else if (password.length() >= 10) {
            return "Good password";
        } else {
            return "Acceptable password (consider making it longer)";
        }
    }
}
