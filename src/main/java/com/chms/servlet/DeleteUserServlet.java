package com.chms.servlet;

import com.chms.dao.UserDAO;
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
import java.io.PrintWriter;

/**
 * Servlet for deleting users (Admin only)
 */
@WebServlet("/admin/delete-user")
public class DeleteUserServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteUserServlet.class);
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Check if user is logged in and is an admin
        if (!SessionManager.isUserLoggedIn(request)) {
            out.print("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }

        User loggedInUser = SessionManager.getLoggedInUser(request);
        if (loggedInUser == null || !loggedInUser.getRole().equals(User.Role.ADMIN)) {
            out.print("{\"success\": false, \"message\": \"Unauthorized access\"}");
            return;
        }

        try {
            String userIdStr = request.getParameter("userId");
            
            if (userIdStr == null || userIdStr.isEmpty()) {
                out.print("{\"success\": false, \"message\": \"User ID is required\"}");
                return;
            }
            
            int userId = Integer.parseInt(userIdStr);
            
            // Prevent admin from deleting themselves
            if (userId == loggedInUser.getUserId()) {
                out.print("{\"success\": false, \"message\": \"Cannot delete your own account\"}");
                return;
            }
            
            // Delete the user
            boolean deleted = userDAO.deleteUser(userId);
            
            if (deleted) {
                logger.info("Admin {} deleted user ID: {}", loggedInUser.getUserId(), userId);
                out.print("{\"success\": true, \"message\": \"User deleted successfully\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Failed to delete user\"}");
            }
            
        } catch (NumberFormatException e) {
            logger.error("Invalid user ID format", e);
            out.print("{\"success\": false, \"message\": \"Invalid user ID\"}");
        } catch (Exception e) {
            logger.error("Error deleting user", e);
            out.print("{\"success\": false, \"message\": \"Server error\"}");
        }
    }
}
