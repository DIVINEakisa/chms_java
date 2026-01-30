<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.chms.model.Child, com.chms.model.User, com.chms.model.Appointment, java.util.List, java.text.SimpleDateFormat" %>
<%
    User doctor = (User) request.getAttribute("doctor");
    @SuppressWarnings("unchecked")
    List<Appointment> todayAppointments = (List<Appointment>) request.getAttribute("todayAppointments");
    @SuppressWarnings("unchecked")
    List<Appointment> upcomingAppointments = (List<Appointment>) request.getAttribute("upcomingAppointments");
    @SuppressWarnings("unchecked")
    List<Child> patients = (List<Child>) request.getAttribute("patients");
    
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Dashboard - CHMS</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .dashboard-container {
            padding: 30px 0;
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .stat-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 15px;
            margin-bottom: 20px;
        }
        .appointment-item {
            padding: 15px;
            border-left: 4px solid #667eea;
            margin-bottom: 10px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-heartbeat"></i> CHMS Doctor Portal
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <span class="nav-link"><i class="fas fa-user-md"></i> Dr. <%= doctor.getFullName() %></span>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/logout">
                            <i class="fas fa-sign-out-alt"></i> Logout
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Main Content -->
    <div class="container dashboard-container">
        <div class="row">
            <div class="col-12 mb-4">
                <h2 class="text-white"><i class="fas fa-tachometer-alt"></i> Doctor Dashboard</h2>
                <p class="text-white">Welcome back, Dr. <%= doctor.getFullName() %>!</p>
            </div>
        </div>

        <!-- Statistics -->
        <div class="row">
            <div class="col-md-4">
                <div class="stat-card text-center">
                    <h3><%= todayAppointments != null ? todayAppointments.size() : 0 %></h3>
                    <p><i class="fas fa-calendar-day"></i> Today's Appointments</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card text-center">
                    <h3><%= upcomingAppointments != null ? upcomingAppointments.size() : 0 %></h3>
                    <p><i class="fas fa-calendar-alt"></i> Upcoming Appointments</p>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card text-center">
                    <h3><%= patients != null ? patients.size() : 0 %></h3>
                    <p><i class="fas fa-users"></i> Total Patients</p>
                </div>
            </div>
        </div>

        <!-- Today's Appointments -->
        <div class="row">
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-calendar-check"></i> Today's Appointments</h5>
                    </div>
                    <div class="card-body">
                        <% if (todayAppointments == null || todayAppointments.isEmpty()) { %>
                            <div class="text-center text-muted py-4">
                                <i class="fas fa-calendar-times fa-3x mb-3"></i>
                                <p>No appointments scheduled for today</p>
                            </div>
                        <% } else { %>
                            <% for (Appointment appt : todayAppointments) { %>
                                <div class="appointment-item">
                                    <h6><i class="fas fa-child"></i> <%= appt.getChildName() != null ? appt.getChildName() : "N/A" %></h6>
                                    <p class="mb-1 text-muted"><i class="fas fa-user"></i> Mother: <%= appt.getMotherName() != null ? appt.getMotherName() : "N/A" %></p>
                                    <p class="mb-1"><i class="fas fa-clock"></i> <%= appt.getAppointmentTime() != null ? timeFormatter.format(appt.getAppointmentTime()) : "N/A" %></p>
                                    <p class="mb-0"><i class="fas fa-stethoscope"></i> <%= appt.getAppointmentType() %></p>
                                </div>
                            <% } %>
                        <% } %>
                    </div>
                </div>
            </div>

            <!-- Upcoming Appointments -->
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0"><i class="fas fa-calendar-alt"></i> Upcoming Appointments</h5>
                    </div>
                    <div class="card-body">
                        <% if (upcomingAppointments == null || upcomingAppointments.isEmpty()) { %>
                            <div class="text-center text-muted py-4">
                                <i class="fas fa-calendar-times fa-3x mb-3"></i>
                                <p>No upcoming appointments</p>
                            </div>
                        <% } else { %>
                            <% for (Appointment appt : upcomingAppointments) { %>
                                <div class="appointment-item">
                                    <h6><i class="fas fa-child"></i> <%= appt.getChildName() != null ? appt.getChildName() : "N/A" %></h6>
                                    <p class="mb-1 text-muted"><i class="fas fa-user"></i> Mother: <%= appt.getMotherName() != null ? appt.getMotherName() : "N/A" %></p>
                                    <p class="mb-1"><i class="fas fa-calendar"></i> <%= appt.getAppointmentDate() != null ? dateFormatter.format(appt.getAppointmentDate()) : "N/A" %></p>
                                    <p class="mb-1"><i class="fas fa-clock"></i> <%= appt.getAppointmentTime() != null ? timeFormatter.format(appt.getAppointmentTime()) : "N/A" %></p>
                                    <p class="mb-0"><i class="fas fa-stethoscope"></i> <%= appt.getAppointmentType() %></p>
                                </div>
                            <% } %>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <!-- My Patients -->
        <div class="row">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-users"></i> My Patients</h5>
                    </div>
                    <div class="card-body">
                        <% if (patients == null || patients.isEmpty()) { %>
                            <div class="text-center text-muted py-4">
                                <i class="fas fa-user-slash fa-3x mb-3"></i>
                                <p>No patients assigned yet</p>
                            </div>
                        <% } else { %>
                            <div class="row">
                                <% for (Child child : patients) { %>
                                    <div class="col-md-6 col-lg-4 mb-3">
                                        <div class="card">
                                            <div class="card-body">
                                                <h6 class="card-title"><i class="fas fa-child text-info"></i> <%= child.getFullName() %></h6>
                                                <p class="card-text">
                                                    <small class="text-muted">Profile ID: <%= child.getProfileId() %></small><br>
                                                    <small>DOB: <%= child.getDateOfBirth() != null ? dateFormatter.format(child.getDateOfBirth()) : "N/A" %></small><br>
                                                    <small>Gender: <%= child.getGender() %></small>
                                                    <% if (child.getBloodType() != null && !child.getBloodType().isEmpty()) { %>
                                                        <br><small>Blood: <%= child.getBloodType() %></small>
                                                    <% } %>
                                                </p>
                                                <button class="btn btn-sm btn-outline-info w-100">
                                                    <i class="fas fa-eye"></i> View Details
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                <% } %>
                            </div>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
