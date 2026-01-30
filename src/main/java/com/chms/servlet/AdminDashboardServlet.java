package com.chms.servlet;

import com.chms.dao.UserDAO;
import com.chms.dao.ChildDAO;
import com.chms.dao.AppointmentDAO;
import com.chms.dao.AuditLogDAO;
import com.chms.model.User;
import com.chms.model.Child;
import com.chms.model.Appointment;
import com.chms.model.AuditLog;
import com.chms.util.SessionManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Admin Dashboard Servlet
 * Displays system statistics and user management
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AdminDashboardServlet.class);
    private final UserDAO userDAO = new UserDAO();
    private final ChildDAO childDAO = new ChildDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final AuditLogDAO auditLogDAO = new AuditLogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is an admin
        if (!SessionManager.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        User loggedInUser = SessionManager.getLoggedInUser(request);
        if (loggedInUser == null || !loggedInUser.getRole().equals(User.Role.ADMIN)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=unauthorized");
            return;
        }

        try {
            // Get all users
            List<User> allUsers = userDAO.getAllUsers();
            
            // Get statistics
            int totalUsers = allUsers != null ? allUsers.size() : 0;
            int totalMothers = 0;
            int totalDoctors = 0;
            int totalAdmins = 0;
            
            if (allUsers != null) {
                for (User user : allUsers) {
                    switch (user.getRole()) {
                        case MOTHER:
                            totalMothers++;
                            break;
                        case DOCTOR:
                            totalDoctors++;
                            break;
                        case ADMIN:
                            totalAdmins++;
                            break;
                    }
                }
            }
            
            // Get recent audit logs
            List<AuditLog> recentLogs = auditLogDAO.getRecentAuditLogs(10);
            
            // Set attributes for JSP
            request.setAttribute("admin", loggedInUser);
            request.setAttribute("allUsers", allUsers);
            request.setAttribute("totalUsers", totalUsers);
            request.setAttribute("totalMothers", totalMothers);
            request.setAttribute("totalDoctors", totalDoctors);
            request.setAttribute("totalAdmins", totalAdmins);
            request.setAttribute("recentLogs", recentLogs);
            
            logger.info("Admin dashboard loaded for: {} (ID: {})", loggedInUser.getEmail(), loggedInUser.getUserId());
            
            // Forward to dashboard JSP
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading admin dashboard", e);
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=system");
        }
    }
}
