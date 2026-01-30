<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - CHMS</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="min-h-screen flex items-center justify-center bg-gradient-to-br from-purple-500 to-indigo-600">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6 col-lg-5">
                <div class="card shadow-2xl">
                    <div class="card-body p-5">
                        <!-- Header -->
                        <div class="text-center mb-4">
                            <h2 class="h4 mb-2">👶 CHMS</h2>
                            <p class="text-muted">Child Health Monitoring System</p>
                            <h3 class="h5 mt-3">Create Your Account</h3>
                        </div>

                        <!-- Error Message -->
                        <% if (request.getParameter("error") != null) { %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <% if ("email_exists".equals(request.getParameter("error"))) { %>
                                    Email already registered. Please login.
                                <% } else if ("invalid".equals(request.getParameter("error"))) { %>
                                    Invalid input. Please check all fields.
                                <% } else { %>
                                    Registration failed. Please try again.
                                <% } %>
                            </div>
                        <% } %>

                        <!-- Success Message -->
                        <% if (request.getParameter("success") != null) { %>
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                Registration successful! Please <a href="index.jsp" class="alert-link">login</a>.
                            </div>
                        <% } %>

                        <!-- Registration Form -->
                        <form method="post" action="<%= request.getContextPath() %>/register">
                            <div class="mb-3">
                                <label for="fullName" class="form-label">Full Name</label>
                                <input type="text" class="form-control" id="fullName" name="fullName" 
                                       placeholder="Enter your full name" required>
                            </div>

                            <div class="mb-3">
                                <label for="email" class="form-label">Email Address</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       placeholder="Enter your email" required>
                            </div>

                            <div class="mb-3">
                                <label for="phone" class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" id="phone" name="phone" 
                                       placeholder="+1234567890" required>
                            </div>

                            <div class="mb-3">
                                <label for="role" class="form-label">I am a</label>
                                <select class="form-select" id="role" name="role" required>
                                    <option value="" selected disabled>Select your role</option>
                                    <option value="MOTHER">Mother (Parent/Guardian)</option>
                                    <option value="DOCTOR">Doctor (Healthcare Provider)</option>
                                    <option value="ADMIN">Admin (System Administrator)</option>
                                </select>
                                <small class="text-muted">Choose the role that best describes you</small>
                            </div>

                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" name="password" 
                                       placeholder="Create a password" minlength="6" required>
                            </div>

                            <div class="mb-3">
                                <label for="confirmPassword" class="form-label">Confirm Password</label>
                                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                                       placeholder="Confirm your password" minlength="6" required>
                            </div>

                            <div class="d-grid">
                                <button type="submit" class="btn btn-primary btn-lg">
                                    Create Account
                                </button>
                            </div>
                        </form>

                        <!-- Login Link -->
                        <div class="text-center mt-4">
                            <p class="text-muted mb-0">
                                Already have an account? 
                                <a href="<%= request.getContextPath() %>/index.jsp" class="text-primary">Sign In</a>
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
