package com.chms.dao;

import com.chms.model.Appointment;
import com.chms.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Appointment entity
 */
public class AppointmentDAO {
    private static final Logger logger = LoggerFactory.getLogger(AppointmentDAO.class);

    /**
     * Get upcoming appointments for a mother's children
     */
    public List<Appointment> getUpcomingAppointmentsByMotherId(int motherId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.full_name as child_name, u.full_name as doctor_name " +
                    "FROM appointments a " +
                    "JOIN children c ON a.child_id = c.child_id " +
                    "JOIN users u ON a.doctor_id = u.user_id " +
                    "WHERE c.mother_id = ? AND a.appointment_date >= CURDATE() " +
                    "AND a.status IN ('SCHEDULED', 'CONFIRMED') " +
                    "ORDER BY a.appointment_date, a.appointment_time LIMIT 10";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, motherId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setChildId(rs.getInt("child_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setAppointmentDate(rs.getDate("appointment_date"));
                appointment.setAppointmentTime(rs.getTime("appointment_time"));
                appointment.setAppointmentType(Appointment.AppointmentType.valueOf(rs.getString("appointment_type")));
                appointment.setStatus(Appointment.AppointmentStatus.valueOf(rs.getString("status")));
                appointment.setNotes(rs.getString("notes"));
                
                // Store child and doctor names as extra attributes
                appointment.setChildName(rs.getString("child_name"));
                appointment.setDoctorName(rs.getString("doctor_name"));
                
                appointments.add(appointment);
            }
            
            logger.info("Retrieved {} upcoming appointments for mother ID: {}", appointments.size(), motherId);
            
        } catch (SQLException e) {
            logger.error("Error retrieving appointments for mother ID: " + motherId, e);
        }
        
        return appointments;
    }

    /**
     * Get upcoming appointments for a doctor
     */
    public List<Appointment> getUpcomingAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.full_name as child_name, u.full_name as mother_name " +
                    "FROM appointments a " +
                    "JOIN children c ON a.child_id = c.child_id " +
                    "JOIN users u ON c.mother_id = u.user_id " +
                    "WHERE a.doctor_id = ? AND a.appointment_date >= CURDATE() " +
                    "AND a.status IN ('SCHEDULED', 'CONFIRMED') " +
                    "ORDER BY a.appointment_date, a.appointment_time LIMIT 20";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setChildId(rs.getInt("child_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setAppointmentDate(rs.getDate("appointment_date"));
                appointment.setAppointmentTime(rs.getTime("appointment_time"));
                appointment.setAppointmentType(Appointment.AppointmentType.valueOf(rs.getString("appointment_type")));
                appointment.setStatus(Appointment.AppointmentStatus.valueOf(rs.getString("status")));
                appointment.setNotes(rs.getString("notes"));
                appointment.setChildName(rs.getString("child_name"));
                appointment.setMotherName(rs.getString("mother_name"));
                
                appointments.add(appointment);
            }
            
            logger.info("Retrieved {} upcoming appointments for doctor ID: {}", appointments.size(), doctorId);
            
        } catch (SQLException e) {
            logger.error("Error retrieving appointments for doctor ID: " + doctorId, e);
        }
        
        return appointments;
    }

    /**
     * Get today's appointments for a doctor
     */
    public List<Appointment> getTodayAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.full_name as child_name, u.full_name as mother_name " +
                    "FROM appointments a " +
                    "JOIN children c ON a.child_id = c.child_id " +
                    "JOIN users u ON c.mother_id = u.user_id " +
                    "WHERE a.doctor_id = ? AND a.appointment_date = CURDATE() " +
                    "ORDER BY a.appointment_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setChildId(rs.getInt("child_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setAppointmentDate(rs.getDate("appointment_date"));
                appointment.setAppointmentTime(rs.getTime("appointment_time"));
                appointment.setAppointmentType(Appointment.AppointmentType.valueOf(rs.getString("appointment_type")));
                appointment.setStatus(Appointment.AppointmentStatus.valueOf(rs.getString("status")));
                appointment.setNotes(rs.getString("notes"));
                appointment.setChildName(rs.getString("child_name"));
                appointment.setMotherName(rs.getString("mother_name"));
                
                appointments.add(appointment);
            }
            
            logger.info("Retrieved {} today's appointments for doctor ID: {}", appointments.size(), doctorId);
            
        } catch (SQLException e) {
            logger.error("Error retrieving today's appointments for doctor ID: " + doctorId, e);
        }
        
        return appointments;
    }

    /**
     * Search appointments by specific date
     */
    public List<Appointment> searchAppointmentsByDate(java.sql.Date searchDate) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.full_name as child_name, u.full_name as doctor_name, " +
                    "m.full_name as mother_name " +
                    "FROM appointments a " +
                    "JOIN children c ON a.child_id = c.child_id " +
                    "JOIN users u ON a.doctor_id = u.user_id " +
                    "JOIN users m ON c.mother_id = m.user_id " +
                    "WHERE a.appointment_date = ? " +
                    "ORDER BY a.appointment_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, searchDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setChildId(rs.getInt("child_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setAppointmentDate(rs.getDate("appointment_date"));
                appointment.setAppointmentTime(rs.getTime("appointment_time"));
                appointment.setAppointmentType(Appointment.AppointmentType.valueOf(rs.getString("appointment_type")));
                appointment.setStatus(Appointment.AppointmentStatus.valueOf(rs.getString("status")));
                appointment.setNotes(rs.getString("notes"));
                appointment.setChildName(rs.getString("child_name"));
                appointment.setDoctorName(rs.getString("doctor_name"));
                appointment.setMotherName(rs.getString("mother_name"));
                
                appointments.add(appointment);
            }
            
            logger.info("Found {} appointments on date: {}", appointments.size(), searchDate);
            
        } catch (SQLException e) {
            logger.error("Error searching appointments by date: " + searchDate, e);
        }
        
        return appointments;
    }

    /**
     * Search appointments by date range
     */
    public List<Appointment> searchAppointmentsByDateRange(java.sql.Date startDate, java.sql.Date endDate) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT a.*, c.full_name as child_name, u.full_name as doctor_name, " +
                    "m.full_name as mother_name " +
                    "FROM appointments a " +
                    "JOIN children c ON a.child_id = c.child_id " +
                    "JOIN users u ON a.doctor_id = u.user_id " +
                    "JOIN users m ON c.mother_id = m.user_id " +
                    "WHERE a.appointment_date BETWEEN ? AND ? " +
                    "ORDER BY a.appointment_date, a.appointment_time";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment();
                appointment.setAppointmentId(rs.getInt("appointment_id"));
                appointment.setChildId(rs.getInt("child_id"));
                appointment.setDoctorId(rs.getInt("doctor_id"));
                appointment.setAppointmentDate(rs.getDate("appointment_date"));
                appointment.setAppointmentTime(rs.getTime("appointment_time"));
                appointment.setAppointmentType(Appointment.AppointmentType.valueOf(rs.getString("appointment_type")));
                appointment.setStatus(Appointment.AppointmentStatus.valueOf(rs.getString("status")));
                appointment.setNotes(rs.getString("notes"));
                appointment.setChildName(rs.getString("child_name"));
                appointment.setDoctorName(rs.getString("doctor_name"));
                appointment.setMotherName(rs.getString("mother_name"));
                
                appointments.add(appointment);
            }
            
            logger.info("Found {} appointments between {} and {}", appointments.size(), startDate, endDate);
            
        } catch (SQLException e) {
            logger.error("Error searching appointments by date range", e);
        }
        
        return appointments;
    }
}
