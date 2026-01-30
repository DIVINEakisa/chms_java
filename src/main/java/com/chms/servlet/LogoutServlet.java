package com.chms.servlet;

import com.chms.dao.AuditLogDAO;
import com.chms.model.AuditLog;
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
 * Servlet for handling user logout
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(LogoutServlet.class);
    private AuditLogDAO auditLogDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        auditLogDAO = new AuditLogDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get user ID before invalidating session
        int userId = SessionManager.getLoggedInUserId(request);
        
        if (userId > 0) {
            // Create audit log
            AuditLog auditLog = new AuditLog(
                userId,
                "LOGOUT",
                null,
                null,
                "User logged out"
            );
            auditLog.setIpAddress(SessionManager.getClientIpAddress(request));
            auditLog.setUserAgent(SessionManager.getUserAgent(request));
            auditLogDAO.createAuditLog(auditLog);
            
            logger.info("User logged out: {}", userId);
        }
        
        // Invalidate session
        SessionManager.invalidateSession(request);
        
        // Redirect to login page with logout message
        response.sendRedirect(request.getContextPath() + "/index.jsp?logout=success");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
