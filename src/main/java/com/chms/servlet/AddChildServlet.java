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
import java.sql.Date;
import java.time.LocalDate;
import java.time.Year;

/**
 * Servlet for adding a new child
 */
@WebServlet("/mother/add-child")
public class AddChildServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(AddChildServlet.class);
    private final ChildDAO childDAO = new ChildDAO();

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

        request.setAttribute("mother", loggedInUser);
        request.getRequestDispatcher("/WEB-INF/views/mother/add-child.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
            // Get form parameters
            String fullName = request.getParameter("fullName");
            String dateOfBirthStr = request.getParameter("dateOfBirth");
            String genderStr = request.getParameter("gender");
            String birthWeightStr = request.getParameter("birthWeight");
            String birthHeightStr = request.getParameter("birthHeight");
            String bloodGroup = request.getParameter("bloodGroup");
            String fatherName = request.getParameter("fatherName");
            String fatherPhone = request.getParameter("fatherPhone");
            String emergencyContact = request.getParameter("emergencyContact");
            String address = request.getParameter("address");
            String medicalHistory = request.getParameter("medicalHistory");

            // Validate required fields
            if (fullName == null || fullName.trim().isEmpty() ||
                dateOfBirthStr == null || dateOfBirthStr.trim().isEmpty() ||
                genderStr == null || genderStr.trim().isEmpty() ||
                birthWeightStr == null || birthWeightStr.trim().isEmpty() ||
                birthHeightStr == null || birthHeightStr.trim().isEmpty() ||
                emergencyContact == null || emergencyContact.trim().isEmpty() ||
                address == null || address.trim().isEmpty()) {
                
                response.sendRedirect(request.getContextPath() + "/mother/add-child?error=missing_fields");
                return;
            }

            // Parse date
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            Date sqlDateOfBirth = Date.valueOf(dateOfBirth);

            // Generate unique profile ID
            String uniqueProfileId = generateUniqueProfileId(dateOfBirth);

            // Create Child object
            Child newChild = new Child();
            newChild.setUniqueProfileId(uniqueProfileId);
            newChild.setFullName(fullName.trim());
            newChild.setDateOfBirth(sqlDateOfBirth);
            newChild.setGender(Child.Gender.valueOf(genderStr));
            newChild.setBirthWeight(Double.parseDouble(birthWeightStr));
            newChild.setBirthHeight(Double.parseDouble(birthHeightStr));
            newChild.setBloodGroup(bloodGroup != null && !bloodGroup.isEmpty() ? bloodGroup : "Unknown");
            newChild.setMotherId(loggedInUser.getUserId());
            newChild.setFatherName(fatherName != null ? fatherName.trim() : "");
            newChild.setFatherPhone(fatherPhone != null ? fatherPhone.trim() : "");
            newChild.setEmergencyContact(emergencyContact.trim());
            newChild.setAddress(address.trim());
            newChild.setMedicalHistory(medicalHistory != null ? medicalHistory.trim() : "No known medical history");
            newChild.setActive(true);

            // Save to database
            Child createdChild = childDAO.createChild(newChild);

            if (createdChild != null) {
                logger.info("Child added successfully by mother ID: {} - Child: {}", 
                           loggedInUser.getUserId(), createdChild.getFullName());
                response.sendRedirect(request.getContextPath() + "/mother/dashboard?success=child_added");
            } else {
                logger.error("Failed to add child for mother ID: {}", loggedInUser.getUserId());
                response.sendRedirect(request.getContextPath() + "/mother/add-child?error=db_error");
            }

        } catch (Exception e) {
            logger.error("Error adding child", e);
            response.sendRedirect(request.getContextPath() + "/mother/add-child?error=system");
        }
    }

    /**
     * Generate a unique profile ID for the child
     * Format: CHYYYYnnnnnn (CH + year + 6-digit random number)
     */
    private String generateUniqueProfileId(LocalDate dateOfBirth) {
        int year = dateOfBirth.getYear();
        int randomNum = (int) (Math.random() * 900000) + 100000; // 6-digit random number
        return String.format("CH%d%06d", year, randomNum);
    }
}
