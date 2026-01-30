<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Child Health Monitoring System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .hero-section {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 100px 0;
            text-align: center;
        }
        .hero-section h1 {
            font-size: 3.5rem;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .hero-section p {
            font-size: 1.3rem;
            margin-bottom: 30px;
        }
        .feature-card {
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            height: 100%;
            transition: transform 0.3s;
        }
        .feature-card:hover {
            transform: translateY(-10px);
            box-shadow: 0 8px 12px rgba(0,0,0,0.15);
        }
        .feature-icon {
            font-size: 3rem;
            margin-bottom: 20px;
            color: #667eea;
        }
        .stats-section {
            background: #f8f9fa;
            padding: 60px 0;
        }
        .stat-card {
            text-align: center;
            padding: 30px;
        }
        .stat-number {
            font-size: 3rem;
            font-weight: bold;
            color: #667eea;
        }
        .stat-label {
            font-size: 1.2rem;
            color: #6c757d;
        }
        .cta-section {
            padding: 80px 0;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-align: center;
        }
        .btn-custom {
            padding: 15px 40px;
            font-size: 1.2rem;
            border-radius: 50px;
            margin: 10px;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light bg-white shadow-sm">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-heartbeat text-primary"></i>
                <strong>CHMS</strong>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="#features">Features</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="#about">About</a>
                    </li>
                    <li class="nav-item">
                        <a class="btn btn-primary btn-sm ms-3" href="<%= request.getContextPath() %>/index.jsp">
                            <i class="fas fa-sign-in-alt"></i> Login
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <i class="fas fa-baby fa-4x mb-4"></i>
            <h1>Child Health Monitoring System</h1>
            <p>Comprehensive healthcare management for mothers, doctors, and administrators</p>
            <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-light btn-lg btn-custom">
                <i class="fas fa-sign-in-alt"></i> Login to Your Account
            </a>
            <a href="<%= request.getContextPath() %>/register.jsp" class="btn btn-outline-light btn-lg btn-custom">
                <i class="fas fa-user-plus"></i> Create Account
            </a>
        </div>
    </section>

    <!-- Features Section -->
    <section id="features" class="py-5">
        <div class="container">
            <div class="text-center mb-5">
                <h2 class="display-4 fw-bold">System Features</h2>
                <p class="lead text-muted">Everything you need to monitor and manage child health</p>
            </div>
            
            <div class="row">
                <div class="col-md-4">
                    <div class="feature-card text-center">
                        <i class="fas fa-user-friends feature-icon"></i>
                        <h4>For Mothers</h4>
                        <p>Register your children, track their health records, schedule appointments, and receive vaccination reminders.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card text-center">
                        <i class="fas fa-user-md feature-icon"></i>
                        <h4>For Doctors</h4>
                        <p>Manage patient appointments, add health records, track growth charts, and monitor vaccination schedules.</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="feature-card text-center">
                        <i class="fas fa-user-cog feature-icon"></i>
                        <h4>For Administrators</h4>
                        <p>Manage users, generate reports, monitor system usage, and maintain comprehensive audit logs.</p>
                    </div>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-md-3">
                    <div class="feature-card text-center">
                        <i class="fas fa-calendar-check feature-icon"></i>
                        <h5>Appointment Management</h5>
                        <p>Easy scheduling and tracking of medical appointments</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="feature-card text-center">
                        <i class="fas fa-syringe feature-icon"></i>
                        <h5>Vaccination Tracking</h5>
                        <p>Complete immunization history and reminders</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="feature-card text-center">
                        <i class="fas fa-chart-line feature-icon"></i>
                        <h5>Growth Monitoring</h5>
                        <p>Track weight, height, and developmental milestones</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="feature-card text-center">
                        <i class="fas fa-notes-medical feature-icon"></i>
                        <h5>Health Records</h5>
                        <p>Comprehensive medical history and records</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Stats Section -->
    <section class="stats-section">
        <div class="container">
            <div class="row">
                <div class="col-md-3">
                    <div class="stat-card">
                        <div class="stat-number"><i class="fas fa-users"></i> 500+</div>
                        <div class="stat-label">Registered Families</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <div class="stat-number"><i class="fas fa-user-md"></i> 50+</div>
                        <div class="stat-label">Healthcare Providers</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <div class="stat-number"><i class="fas fa-baby"></i> 1000+</div>
                        <div class="stat-label">Children Monitored</div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stat-card">
                        <div class="stat-number"><i class="fas fa-calendar-check"></i> 5000+</div>
                        <div class="stat-label">Appointments Scheduled</div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- About Section -->
    <section id="about" class="py-5">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h2 class="display-5 fw-bold mb-4">About CHMS</h2>
                    <p class="lead">The Child Health Monitoring System is a comprehensive platform designed to streamline pediatric healthcare management.</p>
                    <p>Our system connects mothers, healthcare providers, and administrators in one integrated platform, ensuring better health outcomes for children through:</p>
                    <ul class="list-unstyled">
                        <li class="mb-2"><i class="fas fa-check-circle text-success me-2"></i> Real-time health tracking and monitoring</li>
                        <li class="mb-2"><i class="fas fa-check-circle text-success me-2"></i> Automated vaccination reminders</li>
                        <li class="mb-2"><i class="fas fa-check-circle text-success me-2"></i> Easy appointment scheduling</li>
                        <li class="mb-2"><i class="fas fa-check-circle text-success me-2"></i> Comprehensive health records</li>
                        <li class="mb-2"><i class="fas fa-check-circle text-success me-2"></i> Growth and development tracking</li>
                        <li class="mb-2"><i class="fas fa-check-circle text-success me-2"></i> Secure and HIPAA compliant</li>
                    </ul>
                </div>
                <div class="col-md-6 text-center">
                    <i class="fas fa-heartbeat text-primary" style="font-size: 15rem; opacity: 0.1;"></i>
                </div>
            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
        <div class="container">
            <h2 class="display-4 fw-bold mb-4">Ready to Get Started?</h2>
            <p class="lead mb-4">Join hundreds of families managing their children's health with CHMS</p>
            <a href="<%= request.getContextPath() %>/register.jsp" class="btn btn-light btn-lg btn-custom">
                <i class="fas fa-user-plus"></i> Create Your Account
            </a>
            <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-outline-light btn-lg btn-custom">
                <i class="fas fa-sign-in-alt"></i> Login
            </a>
        </div>
    </section>

    <!-- Footer -->
    <footer class="bg-dark text-white py-4">
        <div class="container text-center">
            <p class="mb-0">&copy; 2026 Child Health Monitoring System. All rights reserved.</p>
            <p class="mb-0"><i class="fas fa-heartbeat"></i> Caring for children's health, one click at a time.</p>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
