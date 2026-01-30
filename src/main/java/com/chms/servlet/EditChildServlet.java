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

/**
 * Servlet for editing and updating child information
 */
@WebServlet("/mother/edit-child")
public class EditChildServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(EditChildServlet.class);
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
            request.getRequestDispatcher("/WEB-INF/views/mother/edit-child.jsp").forward(request, response);

        } catch (Exception e) {
            logger.error("Error loading edit child page", e);
            response.sendRedirect(request.getContextPath() + "/mother/dashboard?error=system");
        }
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
            // Get child ID
            String childIdStr = request.getParameter("childId");
            int childId = Integer.parseInt(childIdStr);
            
            // Get existing child to verify ownership
            Child existingChild = childDAO.getChildById(childId);
            if (existingChild == null || existingChild.getMotherId() != loggedInUser.getUserId()) {
                response.sendRedirect(request.getContextPath() + "/mother/dashboard?error=unauthorized");
                return;
            }

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
                
                response.sendRedirect(request.getContextPath() + "/mother/edit-child?id=" + childId + "&error=missing_fields");
                return;
            }

            // Parse date
            LocalDate dateOfBirth = LocalDate.parse(dateOfBirthStr);
            Date sqlDateOfBirth = Date.valueOf(dateOfBirth);

            // Update child object
            existingChild.setFullName(fullName.trim());
            existingChild.setDateOfBirth(sqlDateOfBirth);
            existingChild.setGender(Child.Gender.valueOf(genderStr));
            existingChild.setBirthWeight(Double.parseDouble(birthWeightStr));
            existingChild.setBirthHeight(Double.parseDouble(birthHeightStr));
            existingChild.setBloodGroup(bloodGroup != null && !bloodGroup.isEmpty() ? bloodGroup : "Unknown");
            existingChild.setFatherName(fatherName != null ? fatherName.trim() : "");
            existingChild.setFatherPhone(fatherPhone != null ? fatherPhone.trim() : "");
            existingChild.setEmergencyContact(emergencyContact.trim());
            existingChild.setAddress(address.trim());
            existingChild.setMedicalHistory(medicalHistory != null ? medicalHistory.trim() : "No known medical history");

            // Update in database
            boolean updated = childDAO.updateChild(existingChild);

            if (updated) {
                logger.info("Child updated successfully by mother ID: {} - Child: {}", 
                           loggedInUser.getUserId(), existingChild.getFullName());
                response.sendRedirect(request.getContextPath() + "/mother/view-child?id=" + childId + "&success=updated");
            } else {
                logger.error("Failed to update child for mother ID: {}", loggedInUser.getUserId());
                response.sendRedirect(request.getContextPath() + "/mother/edit-child?id=" + childId + "&error=db_error");
            }

        } catch (Exception e) {
            logger.error("Error updating child", e);
            response.sendRedirect(request.getContextPath() + "/mother/dashboard?error=system");
        }
    }
}
