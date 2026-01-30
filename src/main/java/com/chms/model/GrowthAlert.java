package com.chms.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a Growth Alert in the Child Health Monitoring System
 */
public class GrowthAlert {
    
    // Alert type enumeration
    public enum AlertType {
        UNDERWEIGHT, OVERWEIGHT, STUNTED, RAPID_WEIGHT_LOSS, RAPID_WEIGHT_GAIN, NO_GROWTH
    }
    
    // Severity enumeration
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    // Fields
    private int alertId;
    private int childId;
    private int recordId;
    private AlertType alertType;
    private Severity severity;
    private String description;
    private boolean isResolved;
    private Date resolvedDate;
    private Integer resolvedBy; // Doctor ID, can be null
    private Timestamp createdAt;
    
    // Additional fields for joined queries
    private String childName;
    private Date childDob;
    private String motherName;
    private String motherPhone;
    
    // Constructors
    public GrowthAlert() {
    }
    
    public GrowthAlert(int childId, int recordId, AlertType alertType, Severity severity, String description) {
        this.childId = childId;
        this.recordId = recordId;
        this.alertType = alertType;
        this.severity = severity;
        this.description = description;
        this.isResolved = false;
    }
    
    // Getters and Setters
    public int getAlertId() {
        return alertId;
    }
    
    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }
    
    public int getChildId() {
        return childId;
    }
    
    public void setChildId(int childId) {
        this.childId = childId;
    }
    
    public int getRecordId() {
        return recordId;
    }
    
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
    
    public AlertType getAlertType() {
        return alertType;
    }
    
    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }
    
    public Severity getSeverity() {
        return severity;
    }
    
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isResolved() {
        return isResolved;
    }
    
    public void setResolved(boolean resolved) {
        isResolved = resolved;
    }
    
    public Date getResolvedDate() {
        return resolvedDate;
    }
    
    public void setResolvedDate(Date resolvedDate) {
        this.resolvedDate = resolvedDate;
    }
    
    public Integer getResolvedBy() {
        return resolvedBy;
    }
    
    public void setResolvedBy(Integer resolvedBy) {
        this.resolvedBy = resolvedBy;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getChildName() {
        return childName;
    }
    
    public void setChildName(String childName) {
        this.childName = childName;
    }
    
    public Date getChildDob() {
        return childDob;
    }
    
    public void setChildDob(Date childDob) {
        this.childDob = childDob;
    }
    
    public String getMotherName() {
        return motherName;
    }
    
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
    
    public String getMotherPhone() {
        return motherPhone;
    }
    
    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "GrowthAlert{" +
                "alertId=" + alertId +
                ", childId=" + childId +
                ", alertType=" + alertType +
                ", severity=" + severity +
                ", isResolved=" + isResolved +
                '}';
    }
}
