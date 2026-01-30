<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.chms.util.SessionManager" %>
<%
    // If user is already logged in, redirect to appropriate dashboard
    if (SessionManager.isUserLoggedIn(request)) {
        String role = SessionManager.getLoggedInUserRole(request);
        if ("MOTHER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/mother/dashboard");
        } else if ("DOCTOR".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/doctor/dashboard");
        } else if ("ADMIN".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
        }
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Child Health Monitoring System - Login</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
        }
        .login-card {
            background: white;
            border-radius: 20px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        .login-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 30px;
            border-radius: 20px 20px 0 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="login-card">
                    <div class="login-header text-center">
                        <h2 class="mb-2">🏥 CHMS</h2>
                        <p class="mb-0">Child Health Monitoring System</p>
                    </div>
                    <div class="p-5">
                        <h4 class="text-center mb-4">Sign In</h4>
                        
                        <% if (request.getParameter("error") != null) { %>
                            <div class="alert alert-danger" role="alert">
                                Invalid email or password. Please try again.
                            </div>
                        <% } %>
                        
                        <% if (request.getParameter("logout") != null) { %>
                            <div class="alert alert-success" role="alert">
                                You have been successfully logged out.
                            </div>
                        <% } %>
                        
                        <form action="<%= request.getContextPath() %>/login" method="post">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email Address</label>
                                <input type="email" class="form-control form-control-lg" id="email" 
                                       name="email" required autofocus 
                                       placeholder="Enter your email">
                            </div>
                            
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control form-control-lg" id="password" 
                                       name="password" required 
                                       placeholder="Enter your password">
                            </div>
                            
                            <div class="mb-3 form-check">
                                <input type="checkbox" class="form-check-input" id="remember" name="remember">
                                <label class="form-check-label" for="remember">
                                    Remember me
                                </label>
                            </div>
                            
                            <button type="submit" class="btn btn-primary btn-lg w-100 mb-3">
                                Sign In
                            </button>
                        </form>
                        
                        <!-- Registration Link -->
                        <div class="text-center mt-3">
                            <p class="mb-0">
                                <small class="text-muted">New mother? </small>
                                <a href="<%= request.getContextPath() %>/register.jsp" class="text-decoration-none fw-bold">Create Account</a>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="text-center mt-4 text-white">
                    <p>&copy; 2026 Child Health Monitoring System. All rights reserved.</p>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
