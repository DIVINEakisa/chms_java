package com.chms.servlet;

import com.chms.dao.ChildDAO;
import com.chms.dao.AppointmentDAO;
import com.chms.model.Child;
import com.chms.model.Appointment;
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
 * Doctor Dashboard Servlet
 * Displays doctor's appointments and patients
 */
@WebServlet("/doctor/dashboard")
public class DoctorDashboardServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboardServlet.class);
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ChildDAO childDAO = new ChildDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is logged in and is a doctor
        if (!SessionManager.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }

        User loggedInUser = SessionManager.getLoggedInUser(request);
        if (loggedInUser == null || !loggedInUser.getRole().equals(User.Role.DOCTOR)) {
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=unauthorized");
            return;
        }

        try {
            int doctorId = loggedInUser.getUserId();
            
            // Get doctor's upcoming appointments
            List<Appointment> upcomingAppointments = appointmentDAO.getUpcomingAppointmentsByDoctorId(doctorId);
            
            // Get today's appointments
            List<Appointment> todayAppointments = appointmentDAO.getTodayAppointmentsByDoctorId(doctorId);
            
            // Get all children under this doctor's care
            List<Child> patients = childDAO.getChildrenByDoctorId(doctorId);
            
            // Set attributes for JSP
            request.setAttribute("doctor", loggedInUser);
            request.setAttribute("upcomingAppointments", upcomingAppointments);
            request.setAttribute("todayAppointments", todayAppointments);
            request.setAttribute("patients", patients);
            
            logger.info("Doctor dashboard loaded for: {} (ID: {})", loggedInUser.getEmail(), doctorId);
            
            // Forward to dashboard JSP
            request.getRequestDispatcher("/WEB-INF/views/doctor/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.error("Error loading doctor dashboard", e);
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=system");
        }
    }
}
