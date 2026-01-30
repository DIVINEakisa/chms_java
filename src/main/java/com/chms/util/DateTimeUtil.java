package com.chms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

/**
 * Date and Time Utility
 * Provides helper methods for date/time operations in the CHMS application
 */
public class DateTimeUtil {
    
    // Date format patterns
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String DISPLAY_DATE_FORMAT = "MMM dd, yyyy";
    public static final String DISPLAY_DATE_TIME_FORMAT = "MMM dd, yyyy hh:mm a";
    
    /**
     * Convert string to java.sql.Date
     * @param dateString Date string in yyyy-MM-dd format
     * @return java.sql.Date object
     */
    public static Date parseDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setLenient(false);
            java.util.Date utilDate = sdf.parse(dateString);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * Convert string to java.sql.Time
     * @param timeString Time string in HH:mm:ss format
     * @return java.sql.Time object
     */
    public static Time parseTime(String timeString) {
        if (timeString == null || timeString.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT);
            sdf.setLenient(false);
            java.util.Date utilDate = sdf.parse(timeString);
            return new Time(utilDate.getTime());
        } catch (ParseException e) {
            return null;
        }
    }
    
    /**
     * Format date for display
     * @param date java.sql.Date object
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_FORMAT);
        return sdf.format(date);
    }
    
    /**
     * Format timestamp for display
     * @param timestamp java.sql.Timestamp object
     * @return Formatted date-time string
     */
    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DISPLAY_DATE_TIME_FORMAT);
        return sdf.format(timestamp);
    }
    
    /**
     * Calculate age in months from date of birth
     * @param dateOfBirth Date of birth
     * @return Age in months
     */
    public static int calculateAgeInMonths(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        
        LocalDate birthDate = dateOfBirth.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        
        return period.getYears() * 12 + period.getMonths();
    }
    
    /**
     * Calculate age in years from date of birth
     * @param dateOfBirth Date of birth
     * @return Age in years
     */
    public static int calculateAgeInYears(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return 0;
        }
        
        LocalDate birthDate = dateOfBirth.toLocalDate();
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(birthDate, currentDate);
        
        return period.getYears();
    }
    
    /**
     * Get current date as java.sql.Date
     * @return Current date
     */
    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    
    /**
     * Get current timestamp
     * @return Current timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     * Check if a date is in the past
     * @param date Date to check
     * @return true if date is in the past, false otherwise
     */
    public static boolean isPastDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.before(getCurrentDate());
    }
    
    /**
     * Check if a date is in the future
     * @param date Date to check
     * @return true if date is in the future, false otherwise
     */
    public static boolean isFutureDate(Date date) {
        if (date == null) {
            return false;
        }
        return date.after(getCurrentDate());
    }
    
    /**
     * Add months to a date
     * @param date Original date
     * @param months Number of months to add
     * @return New date
     */
    public static Date addMonths(Date date, int months) {
        if (date == null) {
            return null;
        }
        LocalDate localDate = date.toLocalDate();
        LocalDate newDate = localDate.plusMonths(months);
        return Date.valueOf(newDate);
    }
    
    /**
     * Format age in a human-readable format
     * @param months Age in months
     * @return Formatted age string (e.g., "2 years 3 months" or "5 months")
     */
    public static String formatAge(int months) {
        if (months < 0) {
            return "N/A";
        }
        
        int years = months / 12;
        int remainingMonths = months % 12;
        
        if (years > 0 && remainingMonths > 0) {
            return String.format("%d year%s %d month%s", 
                years, years > 1 ? "s" : "",
                remainingMonths, remainingMonths > 1 ? "s" : "");
        } else if (years > 0) {
            return String.format("%d year%s", years, years > 1 ? "s" : "");
        } else {
            return String.format("%d month%s", months, months > 1 ? "s" : "");
        }
    }
}
