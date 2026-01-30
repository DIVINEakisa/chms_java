package com.chms.dao;

import com.chms.model.Child;
import com.chms.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Child entity
 */
public class ChildDAO {
    private static final Logger logger = LoggerFactory.getLogger(ChildDAO.class);

    /**
     * Get all children for a specific mother
     */
    public List<Child> getChildrenByMotherId(int motherId) {
        List<Child> children = new ArrayList<>();
        String sql = "SELECT * FROM children WHERE mother_id = ? ORDER BY date_of_birth DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, motherId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                children.add(mapResultSetToChild(rs));
            }
            
            logger.info("Retrieved {} children for mother ID: {}", children.size(), motherId);
            
        } catch (SQLException e) {
            logger.error("Error retrieving children for mother ID: " + motherId, e);
        }
        
        return children;
    }

    /**
     * Get all children under a doctor's care (children with appointments)
     */
    public List<Child> getChildrenByDoctorId(int doctorId) {
        List<Child> children = new ArrayList<>();
        String sql = "SELECT DISTINCT c.* FROM children c " +
                    "JOIN appointments a ON c.child_id = a.child_id " +
                    "WHERE a.doctor_id = ? " +
                    "ORDER BY c.full_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, doctorId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                children.add(mapResultSetToChild(rs));
            }
            
            logger.info("Retrieved {} patients for doctor ID: {}", children.size(), doctorId);
            
        } catch (SQLException e) {
            logger.error("Error retrieving children for doctor ID: " + doctorId, e);
        }
        
        return children;
    }

    /**
     * Get child by ID
     */
    public Child getChildById(int childId) {
        String sql = "SELECT * FROM children WHERE child_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, childId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToChild(rs);
            }
            
        } catch (SQLException e) {
            logger.error("Error retrieving child by ID: " + childId, e);
        }
        
        return null;
    }

    /**
     * Create a new child
     */
    public Child createChild(Child child) {
        String sql = "INSERT INTO children (unique_profile_id, full_name, date_of_birth, gender, " +
                    "birth_weight, birth_height, blood_group, mother_id, father_name, father_phone, " +
                    "emergency_contact, address, medical_history) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, child.getUniqueProfileId());
            pstmt.setString(2, child.getFullName());
            pstmt.setDate(3, child.getDateOfBirth());
            pstmt.setString(4, child.getGender().name());
            pstmt.setDouble(5, child.getBirthWeight());
            pstmt.setDouble(6, child.getBirthHeight());
            pstmt.setString(7, child.getBloodGroup());
            pstmt.setInt(8, child.getMotherId());
            pstmt.setString(9, child.getFatherName());
            pstmt.setString(10, child.getFatherPhone());
            pstmt.setString(11, child.getEmergencyContact());
            pstmt.setString(12, child.getAddress());
            pstmt.setString(13, child.getMedicalHistory());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        child.setChildId(rs.getInt(1));
                        logger.info("Child created successfully with ID: {}", child.getChildId());
                        return child;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating child", e);
        }
        
        return null;
    }

    /**
     * Update an existing child
     */
    public boolean updateChild(Child child) {
        String sql = "UPDATE children SET full_name=?, date_of_birth=?, gender=?, " +
                    "birth_weight=?, birth_height=?, blood_group=?, father_name=?, father_phone=?, " +
                    "emergency_contact=?, address=?, medical_history=? WHERE child_id=?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, child.getFullName());
            pstmt.setDate(2, child.getDateOfBirth());
            pstmt.setString(3, child.getGender().name());
            pstmt.setDouble(4, child.getBirthWeight());
            pstmt.setDouble(5, child.getBirthHeight());
            pstmt.setString(6, child.getBloodGroup());
            pstmt.setString(7, child.getFatherName());
            pstmt.setString(8, child.getFatherPhone());
            pstmt.setString(9, child.getEmergencyContact());
            pstmt.setString(10, child.getAddress());
            pstmt.setString(11, child.getMedicalHistory());
            pstmt.setInt(12, child.getChildId());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                logger.info("Child updated successfully: {}", child.getChildId());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating child", e);
        }
        
        return false;
    }

    /**
     * Map ResultSet to Child object
     */
    private Child mapResultSetToChild(ResultSet rs) throws SQLException {
        Child child = new Child();
        child.setChildId(rs.getInt("child_id"));
        child.setUniqueProfileId(rs.getString("unique_profile_id"));
        child.setFullName(rs.getString("full_name"));
        child.setDateOfBirth(rs.getDate("date_of_birth"));
        child.setGender(Child.Gender.valueOf(rs.getString("gender")));
        child.setBirthWeight(rs.getDouble("birth_weight"));
        child.setBirthHeight(rs.getDouble("birth_height"));
        child.setBloodGroup(rs.getString("blood_group"));
        child.setMotherId(rs.getInt("mother_id"));
        child.setFatherName(rs.getString("father_name"));
        child.setFatherPhone(rs.getString("father_phone"));
        child.setEmergencyContact(rs.getString("emergency_contact"));
        child.setAddress(rs.getString("address"));
        child.setMedicalHistory(rs.getString("medical_history"));
        child.setCreatedAt(rs.getTimestamp("created_at"));
        child.setUpdatedAt(rs.getTimestamp("updated_at"));
        return child;
    }
}
