package com.chms.servlet;

import com.chms.dao.UserDAO;
import com.chms.dao.AuditLogDAO;
import com.chms.model.User;
import com.chms.model.AuditLog;
import com.chms.util.PasswordHasher;
import com.chms.util.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling user login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private UserDAO userDAO;
    private AuditLogDAO auditLogDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        auditLogDAO = new AuditLogDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If already logged in, redirect to dashboard
        if (SessionManager.isUserLoggedIn(request)) {
            redirectToDashboard(request, response);
            return;
        }
        
        // Show login page
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password length: " + (password != null ? password.length() : 0));
        
        // Validate input
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            System.out.println("ERROR: Empty email or password");
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=empty");
            return;
        }
        
        // Authenticate user
        User user = userDAO.getUserByEmail(email.trim());
        System.out.println("User found: " + (user != null));
        if (user != null) {
            System.out.println("User active: " + user.isActive());
            System.out.println("Hash from DB: " + user.getPasswordHash().substring(0, 20) + "...");
            System.out.println("Password verification: " + PasswordHasher.verifyPassword(password, user.getPasswordHash()));
        }
        
        if (user != null && user.isActive() && 
            PasswordHasher.verifyPassword(password, user.getPasswordHash())) {
            
            // Successful login
            SessionManager.createUserSession(request, user);
            
            // Update last login
            userDAO.updateLastLogin(user.getUserId());
            
            // Create audit log
            AuditLog auditLog = new AuditLog(
                user.getUserId(),
                "LOGIN",
                null,
                null,
                "User logged in successfully"
            );
            auditLog.setIpAddress(SessionManager.getClientIpAddress(request));
            auditLog.setUserAgent(SessionManager.getUserAgent(request));
            auditLogDAO.createAuditLog(auditLog);
            
            logger.info("User logged in: {} ({})", user.getEmail(), user.getRole());
            
            // Redirect to appropriate dashboard
            redirectToDashboard(request, response);
            
        } else {
            // Failed login
            logger.warn("Failed login attempt for email: {}", email);
            response.sendRedirect(request.getContextPath() + "/index.jsp?error=invalid");
        }
    }
    
    /**
     * Redirect to appropriate dashboard based on user role
     */
    private void redirectToDashboard(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String role = SessionManager.getLoggedInUserRole(request);
        String contextPath = request.getContextPath();
        
        if ("MOTHER".equals(role)) {
            response.sendRedirect(contextPath + "/mother/dashboard");
        } else if ("DOCTOR".equals(role)) {
            response.sendRedirect(contextPath + "/doctor/dashboard");
        } else if ("ADMIN".equals(role)) {
            response.sendRedirect(contextPath + "/admin/dashboard");
        } else {
            response.sendRedirect(contextPath + "/index.jsp");
        }
    }
}
