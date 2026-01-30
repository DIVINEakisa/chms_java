<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.chms.model.User, com.chms.model.AuditLog, java.util.List, java.text.SimpleDateFormat" %>
<%
    User admin = (User) request.getAttribute("admin");
    @SuppressWarnings("unchecked")
    List<User> allUsers = (List<User>) request.getAttribute("allUsers");
    @SuppressWarnings("unchecked")
    List<AuditLog> recentLogs = (List<AuditLog>) request.getAttribute("recentLogs");
    
    Integer totalUsers = (Integer) request.getAttribute("totalUsers");
    Integer totalMothers = (Integer) request.getAttribute("totalMothers");
    Integer totalDoctors = (Integer) request.getAttribute("totalDoctors");
    Integer totalAdmins = (Integer) request.getAttribute("totalAdmins");
    
    SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - CHMS</title>
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
            text-align: center;
        }
        .stat-card h3 {
            font-size: 2.5rem;
            font-weight: bold;
        }
        .user-row {
            padding: 10px;
            border-bottom: 1px solid #e9ecef;
            transition: background 0.2s;
        }
        .user-row:hover {
            background: #f8f9fa;
        }
        .badge-role {
            padding: 5px 10px;
            border-radius: 20px;
            font-size: 0.75rem;
        }
        .log-item {
            padding: 10px;
            border-left: 3px solid #667eea;
            margin-bottom: 8px;
            background: #f8f9fa;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">
                <i class="fas fa-user-shield"></i> CHMS Admin Portal
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <span class="nav-link"><i class="fas fa-user-shield"></i> <%= admin.getFullName() %></span>
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
                <h2 class="text-white"><i class="fas fa-tachometer-alt"></i> Admin Dashboard</h2>
                <p class="text-white">Welcome back, <%= admin.getFullName() %>!</p>
            </div>
        </div>

        <!-- Statistics -->
        <div class="row">
            <div class="col-md-3">
                <div class="stat-card">
                    <h3><%= totalUsers != null ? totalUsers : 0 %></h3>
                    <p><i class="fas fa-users"></i> Total Users</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <h3><%= totalMothers != null ? totalMothers : 0 %></h3>
                    <p><i class="fas fa-user-friends"></i> Mothers</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <h3><%= totalDoctors != null ? totalDoctors : 0 %></h3>
                    <p><i class="fas fa-user-md"></i> Doctors</p>
                </div>
            </div>
            <div class="col-md-3">
                <div class="stat-card">
                    <h3><%= totalAdmins != null ? totalAdmins : 0 %></h3>
                    <p><i class="fas fa-user-shield"></i> Admins</p>
                </div>
            </div>
        </div>

        <!-- User Management -->
        <div class="row">
            <div class="col-lg-8">
                <div class="card">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-users-cog"></i> User Management</h5>
                    </div>
                    <div class="card-body">
                        <% if (allUsers == null || allUsers.isEmpty()) { %>
                            <div class="text-center text-muted py-4">
                                <i class="fas fa-users-slash fa-3x mb-3"></i>
                                <p>No users found</p>
                            </div>
                        <% } else { %>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Name</th>
                                            <th>Email</th>
                                            <th>Role</th>
                                            <th>Status</th>
                                            <th>Last Login</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% for (User user : allUsers) { %>
                                            <tr class="user-row">
                                                <td><%= user.getUserId() %></td>
                                                <td><%= user.getFullName() %></td>
                                                <td><%= user.getEmail() %></td>
                                                <td>
                                                    <span class="badge 
                                                        <% if ("MOTHER".equals(user.getRole().name())) { %>
                                                            bg-info
                                                        <% } else if ("DOCTOR".equals(user.getRole().name())) { %>
                                                            bg-success
                                                        <% } else { %>
                                                            bg-danger
                                                        <% } %>">
                                                        <%= user.getRole() %>
                                                    </span>
                                                </td>
                                                <td>
                                                    <% if (user.isActive()) { %>
                                                        <span class="badge bg-success">Active</span>
                                                    <% } else { %>
                                                        <span class="badge bg-secondary">Inactive</span>
                                                    <% } %>
                                                </td>
                                                <td>
                                                    <small>
                                                        <%= user.getLastLogin() != null ? 
                                                            dateFormatter.format(user.getLastLogin()) : "Never" %>
                                                    </small>
                                                </td>
                                                <td>
                                                    <% if (user.getUserId() != admin.getUserId()) { %>
                                                        <button class="btn btn-sm btn-danger" 
                                                                onclick="deleteUser(<%= user.getUserId() %>, '<%= user.getFullName() %>')">
                                                            <i class="fas fa-trash"></i> Delete
                                                        </button>
                                                    <% } else { %>
                                                        <span class="text-muted"><small>Current User</small></span>
                                                    <% } %>
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

            <!-- Recent Activity -->
            <div class="col-lg-4">
                <div class="card">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0"><i class="fas fa-history"></i> Recent Activity</h5>
                    </div>
                    <div class="card-body" style="max-height: 500px; overflow-y: auto;">
                        <% if (recentLogs == null || recentLogs.isEmpty()) { %>
                            <div class="text-center text-muted py-4">
                                <i class="fas fa-clipboard-list fa-3x mb-3"></i>
                                <p>No recent activity</p>
                            </div>
                        <% } else { %>
                            <% for (AuditLog log : recentLogs) { %>
                                <div class="log-item">
                                    <strong><%= log.getAction() %></strong><br>
                                    <small class="text-muted">
                                        User ID: <%= log.getUserId() %><br>
                                        <%= log.getCreatedAt() != null ? dateFormatter.format(log.getCreatedAt()) : "N/A" %>
                                    </small>
                                    <% if (log.getDescription() != null && !log.getDescription().isEmpty()) { %>
                                        <br><small><%= log.getDescription() %></small>
                                    <% } %>
                                </div>
                            <% } %>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <!-- System Information -->
        <div class="row mt-3">
            <div class="col-12">
                <div class="card">
                    <div class="card-header bg-info text-white">
                        <h5 class="mb-0"><i class="fas fa-info-circle"></i> System Information</h5>
                    </div>
                    <div class="card-body">
                        <div class="row">
                            <div class="col-md-3">
                                <p><strong>System Version:</strong> 1.0.0</p>
                            </div>
                            <div class="col-md-3">
                                <p><strong>Database:</strong> MySQL/MariaDB</p>
                            </div>
                            <div class="col-md-3">
                                <p><strong>Server:</strong> Apache Tomcat 11</p>
                            </div>
                            <div class="col-md-3">
                                <p><strong>Status:</strong> <span class="badge bg-success">Online</span></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function deleteUser(userId, userName) {
            if (confirm('Are you sure you want to delete user "' + userName + '"? This action cannot be undone.')) {
                fetch('<%= request.getContextPath() %>/admin/delete-user?userId=' + userId, {
                    method: 'POST'
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('User deleted successfully');
                        location.reload();
                    } else {
                        alert('Error: ' + (data.message || 'Failed to delete user'));
                    }
                })
                .catch(error => {
                    alert('Error deleting user: ' + error);
                });
            }
        }
    </script>
</body>
</html>
