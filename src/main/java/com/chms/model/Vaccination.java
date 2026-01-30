package com.chms.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a Vaccination record in the Child Health Monitoring System
 */
public class Vaccination {
    
    // Vaccination status enumeration
    public enum VaccinationStatus {
        PENDING, COMPLETED, MISSED, DELAYED
    }
    
    // Fields
    private int vaccinationId;
    private int childId;
    private String vaccineName;
    private int recommendedAgeMonths;
    private Date administeredDate;
    private Integer administeredBy; // Doctor ID, can be null
    private String batchNumber;
    private VaccinationStatus status;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joined queries
    private String childName;
    private String doctorName;
    
    // Constructors
    public Vaccination() {
    }
    
    public Vaccination(int childId, String vaccineName, int recommendedAgeMonths) {
        this.childId = childId;
        this.vaccineName = vaccineName;
        this.recommendedAgeMonths = recommendedAgeMonths;
        this.status = VaccinationStatus.PENDING;
    }
    
    // Getters and Setters
    public int getVaccinationId() {
        return vaccinationId;
    }
    
    public void setVaccinationId(int vaccinationId) {
        this.vaccinationId = vaccinationId;
    }
    
    public int getChildId() {
        return childId;
    }
    
    public void setChildId(int childId) {
        this.childId = childId;
    }
    
    public String getVaccineName() {
        return vaccineName;
    }
    
    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }
    
    public int getRecommendedAgeMonths() {
        return recommendedAgeMonths;
    }
    
    public void setRecommendedAgeMonths(int recommendedAgeMonths) {
        this.recommendedAgeMonths = recommendedAgeMonths;
    }
    
    public Date getAdministeredDate() {
        return administeredDate;
    }
    
    public void setAdministeredDate(Date administeredDate) {
        this.administeredDate = administeredDate;
    }
    
    public Integer getAdministeredBy() {
        return administeredBy;
    }
    
    public void setAdministeredBy(Integer administeredBy) {
        this.administeredBy = administeredBy;
    }
    
    public String getBatchNumber() {
        return batchNumber;
    }
    
    public void setBatchNumber(String batchNumber) {
        this.batchNumber = batchNumber;
    }
    
    public VaccinationStatus getStatus() {
        return status;
    }
    
    public void setStatus(VaccinationStatus status) {
        this.status = status;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getChildName() {
        return childName;
    }
    
    public void setChildName(String childName) {
        this.childName = childName;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "Vaccination{" +
                "vaccinationId=" + vaccinationId +
                ", childId=" + childId +
                ", vaccineName='" + vaccineName + '\'' +
                ", status=" + status +
                '}';
    }
}
