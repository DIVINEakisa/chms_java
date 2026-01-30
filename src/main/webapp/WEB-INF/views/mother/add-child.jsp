<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.chms.model.User" %>
<%
    User mother = (User) request.getAttribute("mother");
    String error = request.getParameter("error");
    String success = request.getParameter("success");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Child - CHMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 30px 0;
        }
        .form-container {
            max-width: 800px;
            margin: 0 auto;
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 20px;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <div class="card">
            <div class="card-header">
                <h3 class="mb-0"><i class="fas fa-baby"></i> Add New Child</h3>
            </div>
            <div class="card-body p-4">
                <% if (error != null) { %>
                    <div class="alert alert-danger">
                        <% if ("missing_fields".equals(error)) { %>
                            Please fill in all required fields.
                        <% } else if ("invalid_date".equals(error)) { %>
                            Please enter a valid date of birth.
                        <% } else { %>
                            An error occurred. Please try again.
                        <% } %>
                    </div>
                <% } %>

                <% if (success != null && "true".equals(success)) { %>
                    <div class="alert alert-success">
                        Child added successfully! <a href="<%= request.getContextPath() %>/mother/dashboard">View Dashboard</a>
                    </div>
                <% } %>

                <form action="<%= request.getContextPath() %>/mother/add-child" method="post">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="fullName" class="form-label">Child's Full Name <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="fullName" name="fullName" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="dateOfBirth" class="form-label">Date of Birth <span class="text-danger">*</span></label>
                            <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="gender" class="form-label">Gender <span class="text-danger">*</span></label>
                            <select class="form-select" id="gender" name="gender" required>
                                <option value="">Select Gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="OTHER">Other</option>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="bloodGroup" class="form-label">Blood Group</label>
                            <select class="form-select" id="bloodGroup" name="bloodGroup">
                                <option value="">Select Blood Group</option>
                                <option value="A+">A+</option>
                                <option value="A-">A-</option>
                                <option value="B+">B+</option>
                                <option value="B-">B-</option>
                                <option value="O+">O+</option>
                                <option value="O-">O-</option>
                                <option value="AB+">AB+</option>
                                <option value="AB-">AB-</option>
                            </select>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="birthWeight" class="form-label">Birth Weight (kg) <span class="text-danger">*</span></label>
                            <input type="number" step="0.1" class="form-control" id="birthWeight" name="birthWeight" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="birthHeight" class="form-label">Birth Height (cm) <span class="text-danger">*</span></label>
                            <input type="number" step="0.1" class="form-control" id="birthHeight" name="birthHeight" required>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label for="fatherName" class="form-label">Father's Name</label>
                            <input type="text" class="form-control" id="fatherName" name="fatherName">
                        </div>
                        <div class="col-md-6 mb-3">
                            <label for="fatherPhone" class="form-label">Father's Phone</label>
                            <input type="tel" class="form-control" id="fatherPhone" name="fatherPhone">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="emergencyContact" class="form-label">Emergency Contact <span class="text-danger">*</span></label>
                        <input type="tel" class="form-control" id="emergencyContact" name="emergencyContact" required>
                    </div>

                    <div class="mb-3">
                        <label for="address" class="form-label">Address <span class="text-danger">*</span></label>
                        <textarea class="form-control" id="address" name="address" rows="2" required></textarea>
                    </div>

                    <div class="mb-3">
                        <label for="medicalHistory" class="form-label">Medical History / Allergies</label>
                        <textarea class="form-control" id="medicalHistory" name="medicalHistory" rows="3" placeholder="E.g., No known allergies, Asthma, Peanut allergy, etc."></textarea>
                    </div>

                    <div class="d-flex justify-content-between">
                        <a href="<%= request.getContextPath() %>/mother/dashboard" class="btn btn-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Dashboard
                        </a>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save"></i> Add Child
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
