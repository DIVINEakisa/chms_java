package com.chms.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class representing a Child in the Child Health Monitoring System
 * Children are registered by mothers and monitored by doctors
 */
public class Child {
    
    // Gender enumeration
    public enum Gender {
        MALE, FEMALE, OTHER
    }
    
    // Fields
    private int childId;
    private String uniqueProfileId;
    private String fullName;
    private Date dateOfBirth;
    private Gender gender;
    private double birthWeight;
    private double birthHeight;
    private String bloodGroup;
    private int motherId;
    private String fatherName;
    private String fatherPhone;
    private String emergencyContact;
    private String address;
    private String medicalHistory;
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joined queries
    private String motherName;
    private String motherEmail;
    private String motherPhone;
    private int ageMonths;
    private int ageYears;
    
    // Constructors
    public Child() {
    }
    
    public Child(String fullName, Date dateOfBirth, Gender gender, int motherId) {
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.motherId = motherId;
        this.isActive = true;
    }
    
    // Getters and Setters
    public int getChildId() {
        return childId;
    }
    
    public void setChildId(int childId) {
        this.childId = childId;
    }
    
    public String getUniqueProfileId() {
        return uniqueProfileId;
    }
    
    public void setUniqueProfileId(String uniqueProfileId) {
        this.uniqueProfileId = uniqueProfileId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public double getBirthWeight() {
        return birthWeight;
    }
    
    public void setBirthWeight(double birthWeight) {
        this.birthWeight = birthWeight;
    }
    
    public double getBirthHeight() {
        return birthHeight;
    }
    
    public void setBirthHeight(double birthHeight) {
        this.birthHeight = birthHeight;
    }
    
    public String getBloodGroup() {
        return bloodGroup;
    }
    
    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
    
    public int getMotherId() {
        return motherId;
    }
    
    public void setMotherId(int motherId) {
        this.motherId = motherId;
    }
    
    public String getFatherName() {
        return fatherName;
    }
    
    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }
    
    public String getFatherPhone() {
        return fatherPhone;
    }
    
    public void setFatherPhone(String fatherPhone) {
        this.fatherPhone = fatherPhone;
    }
    
    public String getEmergencyContact() {
        return emergencyContact;
    }
    
    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getMedicalHistory() {
        return medicalHistory;
    }
    
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
    
    public String getMotherName() {
        return motherName;
    }
    
    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }
    
    public String getMotherEmail() {
        return motherEmail;
    }
    
    public void setMotherEmail(String motherEmail) {
        this.motherEmail = motherEmail;
    }
    
    public String getMotherPhone() {
        return motherPhone;
    }
    
    public void setMotherPhone(String motherPhone) {
        this.motherPhone = motherPhone;
    }
    
    public int getAgeMonths() {
        return ageMonths;
    }
    
    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }
    
    public int getAgeYears() {
        return ageYears;
    }
    
    public void setAgeYears(int ageYears) {
        this.ageYears = ageYears;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "Child{" +
                "childId=" + childId +
                ", uniqueProfileId='" + uniqueProfileId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", motherId=" + motherId +
                '}';
    }
}
