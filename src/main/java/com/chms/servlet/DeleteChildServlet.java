package com.chms.servlet;

import com.chms.dao.ChildDAO;
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
import java.io.PrintWriter;

/**
 * Servlet for deleting child records
 * Only mothers can delete their own children and admins can delete any child
 */
@WebServlet("/delete-child")
public class DeleteChildServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DeleteChildServlet.class);
    private final ChildDAO childDAO = new ChildDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        // Check if user is logged in
        if (!SessionManager.isUserLoggedIn(request)) {
            out.print("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }

        User loggedInUser = SessionManager.getLoggedInUser(request);
        if (loggedInUser == null) {
            out.print("{\"success\": false, \"message\": \"User not found\"}");
            return;
        }

        try {
            String childIdStr = request.getParameter("childId");
            
            if (childIdStr == null || childIdStr.isEmpty()) {
                out.print("{\"success\": false, \"message\": \"Child ID is required\"}");
                return;
            }
            
            int childId = Integer.parseInt(childIdStr);
            
            // Get the child to verify ownership
            Child child = childDAO.getChildById(childId);
            if (child == null) {
                out.print("{\"success\": false, \"message\": \"Child not found\"}");
                return;
            }
            
            // Check authorization: mother can only delete their own children, admin can delete any
            if (loggedInUser.getRole().equals(User.Role.MOTHER)) {
                if (child.getMotherId() != loggedInUser.getUserId()) {
                    out.print("{\"success\": false, \"message\": \"Unauthorized: You can only delete your own children\"}");
                    return;
                }
            } else if (!loggedInUser.getRole().equals(User.Role.ADMIN)) {
                out.print("{\"success\": false, \"message\": \"Unauthorized: Only mothers and admins can delete children\"}");
                return;
            }
            
            // Delete the child
            boolean deleted = childDAO.deleteChild(childId);
            
            if (deleted) {
                logger.info("User {} deleted child ID: {}", loggedInUser.getUserId(), childId);
                out.print("{\"success\": true, \"message\": \"Child deleted successfully\"}");
            } else {
                out.print("{\"success\": false, \"message\": \"Failed to delete child\"}");
            }
            
        } catch (NumberFormatException e) {
            logger.error("Invalid child ID format", e);
            out.print("{\"success\": false, \"message\": \"Invalid child ID\"}");
        } catch (Exception e) {
            logger.error("Error deleting child", e);
            out.print("{\"success\": false, \"message\": \"Server error: " + e.getMessage() + "\"}");
        }
    }
}
