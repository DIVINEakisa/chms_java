package com.chms.dao;

import com.chms.model.AuditLog;
import com.chms.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AuditLog entity
 */
public class AuditLogDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(AuditLogDAO.class);
    
    /**
     * Create a new audit log entry
     * @param auditLog AuditLog object
     * @return The created audit log with generated ID, or null if failed
     */
    public AuditLog createAuditLog(AuditLog auditLog) {
        String sql = "INSERT INTO audit_logs (user_id, action, table_name, record_id, " +
                    "old_value, new_value, ip_address, user_agent) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setObject(1, auditLog.getUserId());
            pstmt.setString(2, auditLog.getAction());
            pstmt.setString(3, auditLog.getTableName());
            pstmt.setObject(4, auditLog.getRecordId());
            pstmt.setString(5, auditLog.getOldValue());
            pstmt.setString(6, auditLog.getNewValue());
            pstmt.setString(7, auditLog.getIpAddress());
            pstmt.setString(8, auditLog.getUserAgent());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        auditLog.setLogId(rs.getInt(1));
                        return auditLog;
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating audit log", e);
        }
        return null;
    }
    
    /**
     * Get all audit logs with pagination
     * @param offset Starting position
     * @param limit Number of records to fetch
     * @return List of audit logs
     */
    public List<AuditLog> getAuditLogs(int offset, int limit) {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT al.*, u.full_name as user_name FROM audit_logs al " +
                    "LEFT JOIN users u ON al.user_id = u.user_id " +
                    "ORDER BY al.created_at DESC LIMIT ? OFFSET ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractAuditLogFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting audit logs", e);
        }
        return logs;
    }
    
    /**
     * Get audit logs for a specific user
     * @param userId User ID
     * @return List of audit logs for the user
     */
    public List<AuditLog> getAuditLogsByUser(int userId) {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT al.*, u.full_name as user_name FROM audit_logs al " +
                    "LEFT JOIN users u ON al.user_id = u.user_id " +
                    "WHERE al.user_id = ? ORDER BY al.created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractAuditLogFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting audit logs for user: {}", userId, e);
        }
        return logs;
    }
    
    /**
     * Get recent audit logs
     * @param limit Number of recent logs to fetch
     * @return List of recent audit logs
     */
    public List<AuditLog> getRecentAuditLogs(int limit) {
        List<AuditLog> logs = new ArrayList<>();
        String sql = "SELECT al.*, u.full_name as user_name FROM audit_logs al " +
                    "LEFT JOIN users u ON al.user_id = u.user_id " +
                    "ORDER BY al.created_at DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    logs.add(extractAuditLogFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting recent audit logs", e);
        }
        return logs;
    }
    
    /**
     * Extract AuditLog object from ResultSet
     */
    private AuditLog extractAuditLogFromResultSet(ResultSet rs) throws SQLException {
        AuditLog log = new AuditLog();
        log.setLogId(rs.getInt("log_id"));
        log.setUserId((Integer) rs.getObject("user_id"));
        log.setAction(rs.getString("action"));
        log.setTableName(rs.getString("table_name"));
        log.setRecordId((Integer) rs.getObject("record_id"));
        log.setOldValue(rs.getString("old_value"));
        log.setNewValue(rs.getString("new_value"));
        log.setIpAddress(rs.getString("ip_address"));
        log.setUserAgent(rs.getString("user_agent"));
        log.setCreatedAt(rs.getTimestamp("created_at"));
        log.setUserName(rs.getString("user_name"));
        return log;
    }
}
