<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.chms.model.Child, com.chms.model.User, java.time.LocalDate, java.time.Period" %>
<%
    Child child = (Child) request.getAttribute("child");
    User mother = (User) request.getAttribute("mother");
    String success = request.getParameter("success");
    
    // Calculate child's age
    LocalDate dob = child.getDateOfBirth().toLocalDate();
    LocalDate now = LocalDate.now();
    Period age = Period.between(dob, now);
    int ageMonths = age.getYears() * 12 + age.getMonths();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= child.getFullName() %> - Child Details</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 30px 0;
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
        }
        .info-label {
            font-weight: 600;
            color: #667eea;
        }
    </style>
</head>
<body>
    <div class="container" style="max-width: 900px;">
        <% if (success != null && "updated".equals(success)) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle"></i> Child information updated successfully!
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <div class="card">
            <div class="card-header d-flex justify-content-between align-items-center">
                <h3 class="mb-0">
                    <i class="fas fa-<%= child.getGender() == Child.Gender.MALE ? "mars" : "venus" %>"></i>
                    <%= child.getFullName() %>
                </h3>
                <span class="badge bg-light text-dark"><%= child.getUniqueProfileId() %></span>
            </div>
            <div class="card-body p-4">
                <div class="row mb-4">
                    <div class="col-md-6">
                        <h5 class="mb-3"><i class="fas fa-user-circle text-primary"></i> Basic Information</h5>
                        <p><span class="info-label">Full Name:</span> <%= child.getFullName() %></p>
                        <p><span class="info-label">Date of Birth:</span> <%= child.getDateOfBirth() %></p>
                        <p><span class="info-label">Age:</span> <%= age.getYears() %> years, <%= age.getMonths() %> months (<%= ageMonths %> months total)</p>
                        <p><span class="info-label">Gender:</span> <%= child.getGender() %></p>
                        <p><span class="info-label">Blood Group:</span> <%= child.getBloodGroup() %></p>
                    </div>
                    <div class="col-md-6">
                        <h5 class="mb-3"><i class="fas fa-baby text-success"></i> Birth Information</h5>
                        <p><span class="info-label">Birth Weight:</span> <%= child.getBirthWeight() %> kg</p>
                        <p><span class="info-label">Birth Height:</span> <%= child.getBirthHeight() %> cm</p>
                        <p><span class="info-label">Profile ID:</span> <%= child.getUniqueProfileId() %></p>
                    </div>
                </div>

                <hr>

                <div class="row mb-4">
                    <div class="col-md-6">
                        <h5 class="mb-3"><i class="fas fa-users text-warning"></i> Parent Information</h5>
                        <p><span class="info-label">Mother:</span> <%= mother.getFullName() %></p>
                        <p><span class="info-label">Mother's Phone:</span> <%= mother.getPhoneNumber() %></p>
                        <% if (child.getFatherName() != null && !child.getFatherName().isEmpty()) { %>
                            <p><span class="info-label">Father:</span> <%= child.getFatherName() %></p>
                            <p><span class="info-label">Father's Phone:</span> <%= child.getFatherPhone() %></p>
                        <% } %>
                    </div>
                    <div class="col-md-6">
                        <h5 class="mb-3"><i class="fas fa-phone text-danger"></i> Contact Information</h5>
                        <p><span class="info-label">Emergency Contact:</span> <%= child.getEmergencyContact() %></p>
                        <p><span class="info-label">Address:</span><br><%= child.getAddress() %></p>
                    </div>
                </div>

                <hr>

                <div class="mb-4">
                    <h5 class="mb-3"><i class="fas fa-notes-medical text-info"></i> Medical Information</h5>
                    <div class="alert alert-light">
                        <%= child.getMedicalHistory() %>
                    </div>
                </div>

                <div class="d-flex justify-content-between">
                    <a href="<%= request.getContextPath() %>/mother/dashboard" class="btn btn-secondary">
                        <i class="fas fa-arrow-left"></i> Back to Dashboard
                    </a>
                    <a href="<%= request.getContextPath() %>/mother/edit-child?id=<%= child.getChildId() %>" class="btn btn-primary">
                        <i class="fas fa-edit"></i> Edit Information
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
