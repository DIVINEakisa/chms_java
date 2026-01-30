package com.chms.dao;

import com.chms.model.Notification;
import com.chms.util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Notification entity
 */
public class NotificationDAO {
    private static final Logger logger = LoggerFactory.getLogger(NotificationDAO.class);

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = FALSE " +
                    "ORDER BY created_at DESC LIMIT 20";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Notification notification = new Notification();
                notification.setNotificationId(rs.getInt("notification_id"));
                notification.setUserId(rs.getInt("user_id"));
                notification.setNotificationType(Notification.NotificationType.valueOf(rs.getString("notification_type")));
                notification.setTitle(rs.getString("title"));
                notification.setMessage(rs.getString("message"));
                notification.setRead(rs.getBoolean("is_read"));
                notification.setRelatedChildId(rs.getInt("related_child_id"));
                notification.setRelatedAppointmentId(rs.getInt("related_appointment_id"));
                notification.setCreatedAt(rs.getTimestamp("created_at"));
                
                notifications.add(notification);
            }
            
            logger.info("Retrieved {} unread notifications for user ID: {}", notifications.size(), userId);
            
        } catch (SQLException e) {
            logger.error("Error retrieving notifications for user ID: " + userId, e);
        }
        
        return notifications;
    }
}
