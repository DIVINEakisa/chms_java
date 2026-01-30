package com.chms.model;

import java.sql.Timestamp;

/**
 * Model class representing a Notification in the Child Health Monitoring System
 */
public class Notification {
    
    // Notification type enumeration
    public enum NotificationType {
        APPOINTMENT_REMINDER, VACCINATION_DUE, CHECKUP_DUE, GROWTH_ALERT, SYSTEM
    }
    
    // Fields
    private int notificationId;
    private int userId;
    private NotificationType notificationType;
    private String title;
    private String message;
    private boolean isRead;
    private Integer relatedChildId;
    private Integer relatedAppointmentId;
    private Timestamp createdAt;
    private Timestamp readAt;
    
    // Constructors
    public Notification() {
    }
    
    public Notification(int userId, NotificationType notificationType, String title, String message) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.title = title;
        this.message = message;
        this.isRead = false;
    }
    
    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }
    
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public NotificationType getNotificationType() {
        return notificationType;
    }
    
    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean read) {
        isRead = read;
    }
    
    public Integer getRelatedChildId() {
        return relatedChildId;
    }
    
    public void setRelatedChildId(Integer relatedChildId) {
        this.relatedChildId = relatedChildId;
    }
    
    public Integer getRelatedAppointmentId() {
        return relatedAppointmentId;
    }
    
    public void setRelatedAppointmentId(Integer relatedAppointmentId) {
        this.relatedAppointmentId = relatedAppointmentId;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getReadAt() {
        return readAt;
    }
    
    public void setReadAt(Timestamp readAt) {
        this.readAt = readAt;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", notificationType=" + notificationType +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
