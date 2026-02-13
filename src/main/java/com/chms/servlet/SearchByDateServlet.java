package com.chms.servlet;

import com.chms.dao.AppointmentDAO;
import com.chms.dao.ChildDAO;
import com.chms.dao.UserDAO;
import com.chms.model.Appointment;
import com.chms.model.Child;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Servlet for searching records by date
 * Supports searching appointments, children (by DOB), and users (by registration date)
 */
@WebServlet("/search-by-date")
public class SearchByDateServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SearchByDateServlet.class);
    
    private final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private final ChildDAO childDAO = new ChildDAO();
    private final UserDAO userDAO = new UserDAO();
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is logged in
        if (!SessionManager.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        User loggedInUser = SessionManager.getLoggedInUser(request);
        
        // Get search parameters
        String searchType = request.getParameter("type"); // appointments, children, users
        String dateStr = request.getParameter("date");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");
        String nameFilter = request.getParameter("name"); // Filter by name
        String startTime = request.getParameter("startTime"); // Filter by time range
        String endTime = request.getParameter("endTime");
        
        try {
            if (searchType == null || searchType.isEmpty()) {
                // Default view - show search form
                request.setAttribute("searchType", "appointments");
                request.getRequestDispatcher("/WEB-INF/views/search-by-date.jsp").forward(request, response);
                return;
            }
            
            // Perform search based on type
            switch (searchType.toLowerCase()) {
                case "appointments":
                    searchAppointments(request, dateStr, startDateStr, endDateStr, nameFilter, startTime, endTime);
                    break;
                    
                case "children":
                    // Only admin and doctors can search all children
                    if (!loggedInUser.getRole().equals(User.Role.ADMIN) && 
                        !loggedInUser.getRole().equals(User.Role.DOCTOR)) {
                        request.setAttribute("error", "Unauthorized access");
                        break;
                    }
                    searchChildren(request, dateStr, startDateStr, endDateStr, nameFilter);
                    break;
                    
                case "users":
                    // Only admin can search users
                    if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
                        request.setAttribute("error", "Unauthorized: Only admins can search users");
                        break;
                    }
                    searchUsers(request, dateStr, startDateStr, endDateStr, nameFilter);
                    break;
                    
                default:
                    request.setAttribute("error", "Invalid search type");
            }
            
            request.setAttribute("searchType", searchType);
            request.setAttribute("searchDate", dateStr);
            request.setAttribute("startDate", startDateStr);
            request.setAttribute("endDate", endDateStr);
            request.setAttribute("nameFilter", nameFilter);
            request.setAttribute("startTime", startTime);
            request.setAttribute("endTime", endTime);
            
        } catch (Exception e) {
            logger.error("Error performing date search", e);
            request.setAttribute("error", "Error: " + e.getMessage());
        }
        
        request.getRequestDispatcher("/WEB-INF/views/search-by-date.jsp").forward(request, response);
    }
    
    /**
     * Search appointments by date or date range
     */
    private void searchAppointments(HttpServletRequest request, String dateStr, 
                                   String startDateStr, String endDateStr, String nameFilter,
                                   String startTime, String endTime) {
        try {
            List<Appointment> appointments;
            
            if (dateStr != null && !dateStr.isEmpty()) {
                // Search by specific date
                Date searchDate = Date.valueOf(dateStr);
                appointments = appointmentDAO.searchAppointmentsByDate(searchDate);
                request.setAttribute("searchMode", "single");
                logger.info("Searched appointments for date: {}, found: {}", dateStr, appointments.size());
            } else if (startDateStr != null && !startDateStr.isEmpty() && 
                       endDateStr != null && !endDateStr.isEmpty()) {
                // Search by date range
                Date startDate = Date.valueOf(startDateStr);
                Date endDate = Date.valueOf(endDateStr);
                appointments = appointmentDAO.searchAppointmentsByDateRange(startDate, endDate);
                request.setAttribute("searchMode", "range");
                logger.info("Searched appointments from {} to {}, found: {}", 
                          startDateStr, endDateStr, appointments.size());
            } else {
                request.setAttribute("error", "Please provide either a specific date or a date range");
                return;
            }
            
            // Apply name filter if provided
            if (nameFilter != null && !nameFilter.trim().isEmpty()) {
                String filter = nameFilter.trim().toLowerCase();
                appointments = appointments.stream()
                    .filter(a -> {
                        if (a.getChildName() != null && a.getChildName().toLowerCase().contains(filter)) return true;
                        if (a.getMotherName() != null && a.getMotherName().toLowerCase().contains(filter)) return true;
                        if (a.getDoctorName() != null && a.getDoctorName().toLowerCase().contains(filter)) return true;
                        return false;
                    })
                    .collect(java.util.stream.Collectors.toList());
                logger.info("Applied name filter '{}', remaining: {}", nameFilter, appointments.size());
            }
            
            // Apply time filter if provided
            if (startTime != null && !startTime.isEmpty() && endTime != null && !endTime.isEmpty()) {
                java.sql.Time filterStartTime = java.sql.Time.valueOf(startTime + ":00");
                java.sql.Time filterEndTime = java.sql.Time.valueOf(endTime + ":00");
                appointments = appointments.stream()
                    .filter(a -> a.getAppointmentTime() != null && 
                                !a.getAppointmentTime().before(filterStartTime) && 
                                !a.getAppointmentTime().after(filterEndTime))
                    .collect(java.util.stream.Collectors.toList());
                logger.info("Applied time filter {} to {}, remaining: {}", startTime, endTime, appointments.size());
            }
            
            request.setAttribute("appointments", appointments);
            request.setAttribute("resultCount", appointments.size());
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid date format", e);
            request.setAttribute("error", "Invalid date format. Use YYYY-MM-DD");
        }
    }
    
    /**
     * Search children by date of birth or date range
     */
    private void searchChildren(HttpServletRequest request, String dateStr, 
                               String startDateStr, String endDateStr, String nameFilter) {
        try {
            List<Child> children;
            
            if (dateStr != null && !dateStr.isEmpty()) {
                // Search by specific date of birth
                Date searchDate = Date.valueOf(dateStr);
                children = childDAO.searchChildrenByDateOfBirth(searchDate);
                request.setAttribute("searchMode", "single");
                logger.info("Searched children by DOB: {}, found: {}", dateStr, children.size());
            } else if (startDateStr != null && !startDateStr.isEmpty() && 
                       endDateStr != null && !endDateStr.isEmpty()) {
                // Search by date range
                Date startDate = Date.valueOf(startDateStr);
                Date endDate = Date.valueOf(endDateStr);
                children = childDAO.searchChildrenByDateRange(startDate, endDate);
                request.setAttribute("searchMode", "range");
                logger.info("Searched children from {} to {}, found: {}", 
                          startDateStr, endDateStr, children.size());
            } else {
                request.setAttribute("error", "Please provide either a specific date or a date range");
                return;
            }
            
            // Apply name filter if provided
            if (nameFilter != null && !nameFilter.trim().isEmpty()) {
                String filter = nameFilter.trim().toLowerCase();
                children = children.stream()
                    .filter(c -> c.getFullName() != null && c.getFullName().toLowerCase().contains(filter))
                    .collect(java.util.stream.Collectors.toList());
                logger.info("Applied name filter '{}', remaining: {}", nameFilter, children.size());
            }
            
            request.setAttribute("children", children);
            request.setAttribute("resultCount", children.size());
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid date format", e);
            request.setAttribute("error", "Invalid date format. Use YYYY-MM-DD");
        }
    }
    
    /**
     * Search users by registration date or date range
     */
    private void searchUsers(HttpServletRequest request, String dateStr, 
                            String startDateStr, String endDateStr, String nameFilter) {
        try {
            List<User> users;
            
            if (dateStr != null && !dateStr.isEmpty()) {
                // Search by specific registration date
                Date searchDate = Date.valueOf(dateStr);
                users = userDAO.searchUsersByRegistrationDate(searchDate);
                request.setAttribute("searchMode", "single");
                logger.info("Searched users by registration date: {}, found: {}", dateStr, users.size());
            } else if (startDateStr != null && !startDateStr.isEmpty() && 
                       endDateStr != null && !endDateStr.isEmpty()) {
                // Search by date range
                Date startDate = Date.valueOf(startDateStr);
                Date endDate = Date.valueOf(endDateStr);
                users = userDAO.searchUsersByDateRange(startDate, endDate);
                request.setAttribute("searchMode", "range");
                logger.info("Searched users from {} to {}, found: {}", 
                          startDateStr, endDateStr, users.size());
            } else {
                request.setAttribute("error", "Please provide either a specific date or a date range");
                return;
            }
            
            // Apply name filter if provided
            if (nameFilter != null && !nameFilter.trim().isEmpty()) {
                String filter = nameFilter.trim().toLowerCase();
                users = users.stream()
                    .filter(u -> (u.getFullName() != null && u.getFullName().toLowerCase().contains(filter)) ||
                                 (u.getEmail() != null && u.getEmail().toLowerCase().contains(filter)))
                    .collect(java.util.stream.Collectors.toList());
                logger.info("Applied name filter '{}', remaining: {}", nameFilter, users.size());
            }
            
            request.setAttribute("users", users);
            request.setAttribute("resultCount", users.size());
            
        } catch (IllegalArgumentException e) {
            logger.error("Invalid date format", e);
            request.setAttribute("error", "Invalid date format. Use YYYY-MM-DD");
        }
    }
}
