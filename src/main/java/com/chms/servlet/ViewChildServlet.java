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

/**
 * Servlet for viewing child details
 */
@WebServlet("/mother/view-child")
public class ViewChildServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(ViewChildServlet.class);
    private final ChildDAO childDAO = new ChildDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check authentication
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
            String childIdStr = request.getParameter("id");
            if (childIdStr == null || childIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/mother/dashboard?error=invalid_child");
                return;
            }

            int childId = Integer.parseInt(childIdStr);
            Child child = childDAO.getChildById(childId);

            // Verify child belongs to this mother
            if (child == null || child.getMotherId() != loggedInUser.getUserId()) {
                response.sendRedirect(request.getContextPath() + "/mother/dashboard?error=unauthorized");
                return;
            }

            request.setAttribute("child", child);
            request.setAttribute("mother", loggedInUser);
            request.getRequestDispatcher("/WEB-INF/views/mother/view-child.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Error viewing child details", e);
            response.sendRedirect(request.getContextPath() + "/mother/dashboard?error=system");
        }
    }
}
