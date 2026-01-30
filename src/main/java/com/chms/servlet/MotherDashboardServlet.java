package com.chms.servlet;

import com.chms.dao.ChildDAO;
import com.chms.dao.AppointmentDAO;
import com.chms.dao.NotificationDAO;
import com.chms.model.Child;
import com.chms.model.Appointment;
import com.chms.model.Notification;
import com.chms.model.User;
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
 * Mother Dashboard Servlet
 * Displays mother's children, appointments, and notifications
 */
@WebServlet("/mother/dashboard")
public class MotherDashboardServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(MotherDashboardServlet.class);
    private final ChildDAO childDAO = new ChildDAO();
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is a mother
        if (!SessionManager.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        User loggedInUser = SessionManager.getLoggedInUser(request);
        if (loggedInUser == null || !loggedInUser.getRole().equals(User.Role.MOTHER)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=unauthorized");
            return;
        }

        try {
            int motherId = loggedInUser.getUserId();
            
            // Get mother's children
            List<Child> children = childDAO.getChildrenByMotherId(motherId);
            
            // Get upcoming appointments for all children
            List<Appointment> upcomingAppointments = appointmentDAO.getUpcomingAppointmentsByMotherId(motherId);
            
            // Get unread notifications
            List<Notification> notifications = notificationDAO.getUnreadNotificationsByUserId(motherId);
            
            // Set attributes for JSP
            request.setAttribute("mother", loggedInUser);
            request.setAttribute("children", children);
            request.setAttribute("upcomingAppointments", upcomingAppointments);
            request.setAttribute("notifications", notifications);
            
            logger.info("Mother dashboard loaded for: {} (ID: {})", loggedInUser.getEmail(), motherId);
            
            // Forward to dashboard JSP
            request.getRequestDispatcher("/WEB-INF/views/mother/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading mother dashboard", e);
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=system");
        }
    }
}
