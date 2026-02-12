<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.chms.model.Child, com.chms.model.User, com.chms.model.Appointment, com.chms.model.Notification, java.util.List" %>
<%
    User mother = (User) request.getAttribute("mother");
    @SuppressWarnings("unchecked")
    List<Child> children = (List<Child>) request.getAttribute("children");
    @SuppressWarnings("unchecked")
    List<Appointment> upcomingAppointments = (List<Appointment>) request.getAttribute("upcomingAppointments");
    @SuppressWarnings("unchecked")
    List<Notification> notifications = (List<Notification>) request.getAttribute("notifications");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mother Dashboard - CHMS</title>
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
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 15px 20px;
            font-weight: 600;
        }
        .stat-card {
            text-align: center;
            padding: 20px;
        }
        .stat-card i {
            font-size: 2.5rem;
            margin-bottom: 10px;
        }
        .stat-card h3 {
            font-size: 2rem;
            font-weight: bold;
            margin: 10px 0;
        }
        .child-card {
            transition: transform 0.3s;
        }
        .child-card:hover {
            transform: translateY(-5px);
        }
        .badge-status {
            padding: 8px 15px;
            border-radius: 20px;
            font-size: 0.85rem;
        }
        .notification-item {
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
                <i class="fas fa-baby"></i> CHMS
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="<%= request.getContextPath() %>/search-by-date">
                            <i class="fas fa-search"></i> Search by Date
                        </a>
                    </li>
                    <li class="nav-item">
                        <span class="nav-link"><i class="fas fa-user"></i> <%= mother.getFullName() %></span>
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

    <div class="dashboard-container">
        <div class="container">
            <!-- Welcome Header -->
            <div class="row mb-4">
                <div class="col-12">
                    <div class="card">
                        <div class="card-body">
                            <h2 class="mb-0">
                                <i class="fas fa-heart text-danger"></i> 
                                Welcome back, <%= mother.getFullName() %>!
                            </h2>
                            <p class="text-muted mb-0">Here's an overview of your children's health</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Statistics Row -->
            <div class="row mb-4">
                <div class="col-md-4">
                    <div class="card stat-card">
                        <i class="fas fa-child text-primary"></i>
                        <h3><%= children != null ? children.size() : 0 %></h3>
                        <p class="text-muted mb-0">Total Children</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card">
                        <i class="fas fa-calendar-check text-success"></i>
                        <h3><%= upcomingAppointments != null ? upcomingAppointments.size() : 0 %></h3>
                        <p class="text-muted mb-0">Upcoming Appointments</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card stat-card">
                        <i class="fas fa-bell text-warning"></i>
                        <h3><%= notifications != null ? notifications.size() : 0 %></h3>
                        <p class="text-muted mb-0">New Notifications</p>
                    </div>
                </div>
            </div>

            <!-- Main Content Row -->
            <div class="row">
                <!-- Children Section -->
                <div class="col-lg-8">
                    <div class="card">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <span><i class="fas fa-children"></i> My Children</span>
                            <a href="<%= request.getContextPath() %>/mother/add-child" class="btn btn-sm btn-light">
                                <i class="fas fa-plus"></i> Add Child
                            </a>
                        </div>
                        <div class="card-body">
                            <% if (children == null || children.isEmpty()) { %>
                                <div class="text-center py-5">
                                    <i class="fas fa-baby fa-3x text-muted mb-3"></i>
                                    <p class="text-muted">No children registered yet</p>
                                    <a href="<%= request.getContextPath() %>/mother/add-child" class="btn btn-primary">
                                        <i class="fas fa-plus"></i> Add Your First Child
                                    </a>
                                </div>
                            <% } else { %>
                                <div class="row">
                                    <% for (Child child : children) { %>
                                        <div class="col-md-6 mb-3">
                                            <div class="card child-card h-100">
                                                <div class="card-body">
                                                    <h5 class="card-title">
                                                        <i class="fas fa-<%= child.getGender() == Child.Gender.MALE ? "mars text-primary" : "venus text-danger" %>"></i>
                                                        <%= child.getFullName() %>
                                                    </h5>
                                                    <p class="text-muted small mb-2">ID: <%= child.getUniqueProfileId() %></p>
                                                    <p class="mb-1"><strong>DOB:</strong> <%= child.getDateOfBirth() %></p>
                                                    <p class="mb-1"><strong>Blood Group:</strong> <%= child.getBloodGroup() %></p>
                                                    <p class="mb-3"><strong>Birth Weight:</strong> <%= child.getBirthWeight() %> kg</p>
                                                    <div class="d-flex gap-2">
                                                        <a href="<%= request.getContextPath() %>/mother/view-child?id=<%= child.getChildId() %>" class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-eye"></i> View Details
                                                        </a>
                                                        <a href="<%= request.getContextPath() %>/mother/edit-child?id=<%= child.getChildId() %>" class="btn btn-sm btn-outline-secondary">
                                                            <i class="fas fa-edit"></i> Edit
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    <% } %>
                                </div>
                            <% } %>
                        </div>
                    </div>

                    <!-- Upcoming Appointments -->
                    <div class="card mt-4">
                        <div class="card-header">
                            <i class="fas fa-calendar-alt"></i> Upcoming Appointments
                        </div>
                        <div class="card-body">
                            <% if (upcomingAppointments == null || upcomingAppointments.isEmpty()) { %>
                                <p class="text-muted text-center">No upcoming appointments</p>
                            <% } else { %>
                                <div class="table-responsive">
                                    <table class="table table-hover">
                                        <thead>
                                            <tr>
                                                <th>Child</th>
                                                <th>Doctor</th>
                                                <th>Date & Time</th>
                                                <th>Type</th>
                                                <th>Status</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <% for (Appointment apt : upcomingAppointments) { %>
                                                <tr>
                                                    <td><%= apt.getChildName() %></td>
                                                    <td><%= apt.getDoctorName() %></td>
                                                    <td><%= apt.getAppointmentDate() %> at <%= apt.getAppointmentTime() %></td>
                                                    <td><span class="badge bg-info"><%= apt.getAppointmentType() %></span></td>
                                                    <td>
                                                        <span class="badge <%= apt.getStatus() == Appointment.AppointmentStatus.CONFIRMED ? "bg-success" : "bg-warning" %>">
                                                            <%= apt.getStatus() %>
                                                        </span>
                                                    </td>
                                                </tr>
                                            <% } %>
                                        </tbody>
                                    </table>
                                </div>
                            <% } %>
                        </div>
                    </div>
                </div>

                <!-- Notifications Section -->
                <div class="col-lg-4">
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-bell"></i> Recent Notifications
                        </div>
                        <div class="card-body">
                            <% if (notifications == null || notifications.isEmpty()) { %>
                                <p class="text-muted text-center">No new notifications</p>
                            <% } else { %>
                                <% for (Notification notif : notifications) { %>
                                    <div class="notification-item">
                                        <h6 class="mb-1"><%= notif.getTitle() %></h6>
                                        <p class="small mb-1"><%= notif.getMessage() %></p>
                                        <small class="text-muted"><%= notif.getCreatedAt() %></small>
                                    </div>
                                <% } %>
                            <% } %>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
