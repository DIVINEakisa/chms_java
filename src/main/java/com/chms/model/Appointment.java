package com.chms.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model class representing an Appointment in the Child Health Monitoring System
 */
public class Appointment {
    
    // Appointment type enumeration
    public enum AppointmentType {
        ROUTINE_CHECKUP, VACCINATION, FOLLOW_UP, EMERGENCY
    }
    
    // Appointment status enumeration
    public enum AppointmentStatus {
        SCHEDULED, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
    }
    
    // Fields
    private int appointmentId;
    private int childId;
    private Integer doctorId; // Can be null
    private Date appointmentDate;
    private Time appointmentTime;
    private AppointmentType appointmentType;
    private AppointmentStatus status;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields for joined queries
    private String childName;
    private Date childDob;
    private String motherName;
    private String motherPhone;
    private String doctorName;
    
    // Constructors
    public Appointment() {
    }
    
    public Appointment(int childId, Date appointmentDate, Time appointmentTime, AppointmentType appointmentType) {
        this.childId = childId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentType = appointmentType;
        this.status = AppointmentStatus.SCHEDULED;
    }
    
    // Getters and Setters
    public int getAppointmentId() {
        return appointmentId;
    }
    
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    
    public int getChildId() {
        return childId;
    }
    
    public void setChildId(int childId) {
        this.childId = childId;
    }
    
    public Integer getDoctorId() {
        return doctorId;
    }
    
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
    
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    
    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }
    
    public Time getAppointmentTime() {
        return appointmentTime;
    }
    
    public void setAppointmentTime(Time appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
    
    public AppointmentType getAppointmentType() {
        return appointmentType;
    }
    
    public void setAppointmentType(AppointmentType appointmentType) {
        this.appointmentType = appointmentType;
    }
    
    public AppointmentStatus getStatus() {
        return status;
    }
    
    public void setStatus(AppointmentStatus status) {
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
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }
    
    // Utility methods
    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", childId=" + childId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                ", appointmentType=" + appointmentType +
                ", status=" + status +
                '}';
    }
}
