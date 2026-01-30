package com.chms.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a Health Record in the Child Health Monitoring System
 * Health records are created by doctors during monthly assessments
 */
public class HealthRecord {
    
    // Growth status enumeration
    public enum GrowthStatus {
        NORMAL, UNDERWEIGHT, OVERWEIGHT, STUNTED, WASTED
    }
    
    // Fields
    private int recordId;
    private int childId;
    private int doctorId;
    private Date assessmentDate;
    private int childAgeMonths;
    private double weight;
    private double height;
    private double bmi;
    private double headCircumference;
    private double temperature;
    private GrowthStatus growthStatus;
    private String vaccinationStatus;
    private String nutritionNotes;
    private String healthNotes;
    private String abnormalitiesDetected;
    private Date nextCheckupDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joined queries
    private String childName;
    private Date childDob;
    private String doctorName;
    
    // Constructors
    public HealthRecord() {
    }
    
    public HealthRecord(int childId, int doctorId, Date assessmentDate, int childAgeMonths,
                       double weight, double height, GrowthStatus growthStatus) {
        this.childId = childId;
        this.doctorId = doctorId;
        this.assessmentDate = assessmentDate;
        this.childAgeMonths = childAgeMonths;
        this.weight = weight;
        this.height = height;
        this.growthStatus = growthStatus;
    }
    
    // Getters and Setters
    public int getRecordId() {
        return recordId;
    }
    
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
    
    public int getChildId() {
        return childId;
    }
    
    public void setChildId(int childId) {
        this.childId = childId;
    }
    
    public int getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
    
    public Date getAssessmentDate() {
        return assessmentDate;
    }
    
    public void setAssessmentDate(Date assessmentDate) {
        this.assessmentDate = assessmentDate;
    }
    
    public int getChildAgeMonths() {
        return childAgeMonths;
    }
    
    public void setChildAgeMonths(int childAgeMonths) {
        this.childAgeMonths = childAgeMonths;
    }
    
    public double getWeight() {
        return weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public double getBmi() {
        return bmi;
    }
    
    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
    
    public double getHeadCircumference() {
        return headCircumference;
    }
    
    public void setHeadCircumference(double headCircumference) {
        this.headCircumference = headCircumference;
    }
    
    public double getTemperature() {
        return temperature;
    }
    
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
    
    public GrowthStatus getGrowthStatus() {
        return growthStatus;
    }
    
    public void setGrowthStatus(GrowthStatus growthStatus) {
        this.growthStatus = growthStatus;
    }
    
    public String getVaccinationStatus() {
        return vaccinationStatus;
    }
    
    public void setVaccinationStatus(String vaccinationStatus) {
        this.vaccinationStatus = vaccinationStatus;
    }
    
    public String getNutritionNotes() {
        return nutritionNotes;
    }
    
    public void setNutritionNotes(String nutritionNotes) {
        this.nutritionNotes = nutritionNotes;
    }
    
    public String getHealthNotes() {
        return healthNotes;
    }
    
    public void setHealthNotes(String healthNotes) {
        this.healthNotes = healthNotes;
    }
    
    public String getAbnormalitiesDetected() {
        return abnormalitiesDetected;
    }
    
    public void setAbnormalitiesDetected(String abnormalitiesDetected) {
        this.abnormalitiesDetected = abnormalitiesDetected;
    }
    
    public Date getNextCheckupDate() {
        return nextCheckupDate;
    }
    
    public void setNextCheckupDate(Date nextCheckupDate) {
        this.nextCheckupDate = nextCheckupDate;
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
    
    public Date getChildDob() {
        return childDob;
    }
    
    public void setChildDob(Date childDob) {
        this.childDob = childDob;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    /**
     * Calculate BMI from weight and height
     * BMI = weight(kg) / (height(m))^2
     */
    public void calculateBmi() {
        if (height > 0) {
            double heightInMeters = height / 100.0; // Convert cm to meters
            this.bmi = weight / (heightInMeters * heightInMeters);
            this.bmi = Math.round(this.bmi * 100.0) / 100.0; // Round to 2 decimal places
        }
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "HealthRecord{" +
                "recordId=" + recordId +
                ", childId=" + childId +
                ", assessmentDate=" + assessmentDate +
                ", weight=" + weight +
                ", height=" + height +
                ", bmi=" + bmi +
                ", growthStatus=" + growthStatus +
                '}';
    }
}
