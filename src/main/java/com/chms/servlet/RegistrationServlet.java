package com.chms.servlet;

import com.chms.dao.UserDAO;
import com.chms.model.User;
import com.chms.util.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for handling user registration (All roles)
 */
@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
    
    private static final Logger logger = LoggerFactory.getLogger(RegistrationServlet.class);
    private final UserDAO userDAO = new UserDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/register.jsp");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String role = request.getParameter("role");
        
        System.out.println("=== REGISTRATION ATTEMPT ===");
        System.out.println("Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phone);
        
        // Validate input
        if (fullName == null || fullName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty() ||
            role == null || role.trim().isEmpty()) {
            System.out.println("ERROR: Missing required fields");
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=invalid");
            return;
        }
        
        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            System.out.println("ERROR: Passwords do not match");
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=password_mismatch");
            return;
        }
        
        // Check if email already exists
        User existingUser = userDAO.getUserByEmail(email.trim());
        if (existingUser != null) {
            System.out.println("ERROR: Email already exists");
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=email_exists");
            return;
        }
        
        try {
            // Hash the password
            String hashedPassword = PasswordHasher.hashPassword(password);
            System.out.println("Password hashed successfully");
            System.out.println("Hash: " + hashedPassword.substring(0, 20) + "...");
            
            // Create new user
            User newUser = new User();
            newUser.setEmail(email.trim());
            newUser.setPasswordHash(hashedPassword);
            newUser.setFullName(fullName.trim());
            newUser.setPhoneNumber(phone.trim());
            newUser.setRole(User.Role.valueOf(role.trim()));
            newUser.setActive(true);
            
            // Save to database
            User createdUser = userDAO.createUser(newUser);
            
            if (createdUser != null) {
                logger.info("New {} account registered: {}", role, email);
                System.out.println("✅ User registered successfully with ID: " + createdUser.getUserId() + " as " + role);
                response.sendRedirect(request.getContextPath() + "/index.jsp?registered=true");
            } else {
                logger.error("Failed to create user account: {}", email);
                System.out.println("❌ Failed to create user in database");
                response.sendRedirect(request.getContextPath() + "/register.jsp?error=db_error");
            }
            
        } catch (Exception e) {
            logger.error("Registration error", e);
            System.out.println("❌ Registration exception: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=server_error");
        }
    }
}
