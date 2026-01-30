# Child Health Monitoring System (CHMS)

## 🎯 Project Overview

A complete, production-ready Child Health Monitoring System built with Java, MySQL, Servlets, and JSP following the MVC architecture. The system monitors child health from birth by tracking growth, vaccinations, nutrition, and medical notes through monthly assessments.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [System Architecture](#system-architecture)
- [Database Schema](#database-schema)
- [Setup Instructions](#setup-instructions)
- [User Roles](#user-roles)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Security Features](#security-features)
- [Testing](#testing)
- [Future Enhancements](#future-enhancements)

## ✨ Features

### Authentication & Authorization
- Secure login/logout functionality
- BCrypt password hashing
- Session-based authentication
- Role-based access control (Mother, Doctor, Admin)

### Child Profile Management
- Mothers can register children at birth
- Unique profile ID generation
- Complete child information storage
- Medical history tracking

### Health Monitoring
- Monthly health assessments
- Automatic BMI calculation
- Growth status tracking (Normal, Underweight, Overweight, Stunted, Wasted)
- Vaccination tracking
- Nutrition and health notes

### Doctor Dashboard
- View all registered children
- Access child health history
- Track growth trends
- Add medical recommendations
- Schedule checkups

### Growth Alerts
- Automatic detection of abnormal growth patterns
- Severity levels (Low, Medium, High, Critical)
- Alert resolution tracking

### Appointment Management
- Schedule monthly checkups
- Appointment status tracking
- Automated notifications

### Audit Logging
- Track all critical operations
- Security and compliance
- IP address and user agent logging

## 🛠 Technology Stack

| Component | Technology |
|-----------|------------|
| **Backend Language** | Java 11+ |
| **Architecture** | MVC (Model-View-Controller) |
| **Framework** | Servlets & JSP |
| **Database** | MySQL 8.0+ |
| **Database Access** | JDBC with Connection Pooling |
| **Build Tool** | Maven |
| **Server** | Apache Tomcat 9.0+ |
| **Frontend** | HTML5, TailwindCSS, JavaScript, Bootstrap 5 |
| **Security** | BCrypt (password hashing), Session management |
| **Logging** | SLF4J with Simple Logger |

## 🏗 System Architecture

The application follows the MVC (Model-View-Controller) design pattern:

```
┌─────────────────────────────────────────────────────────┐
│                      Presentation Layer                  │
│              (JSP Pages + HTML/CSS/JavaScript)          │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                     Controller Layer                     │
│                   (Servlets + Filters)                  │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                      Business Logic                      │
│                   (Service Classes)                     │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                    Data Access Layer                     │
│                       (DAO Classes)                     │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                      Database Layer                      │
│                     (MySQL Database)                    │
└─────────────────────────────────────────────────────────┘
```

## 🗄 Database Schema

### Entity Relationship Diagram

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│    users     │         │   children   │         │health_records│
├──────────────┤         ├──────────────┤         ├──────────────┤
│ user_id (PK) │◄───┐    │ child_id (PK)│◄───┐    │ record_id(PK)│
│ email        │    │    │ mother_id(FK)│────┘    │ child_id (FK)│
│ password_hash│    │    │ full_name    │         │ doctor_id(FK)│
│ full_name    │    │    │ date_of_birth│         │ weight       │
│ role         │    │    │ gender       │         │ height       │
│ ...          │    │    │ ...          │         │ bmi          │
└──────────────┘    │    └──────────────┘         │ growth_status│
                    │                              │ ...          │
                    │                              └──────────────┘
                    │
                    │    ┌──────────────┐         ┌──────────────┐
                    │    │ appointments │         │ vaccinations │
                    │    ├──────────────┤         ├──────────────┤
                    │    │appointment_id│         │vaccination_id│
                    │    │ child_id (FK)│         │ child_id (FK)│
                    └────┤ doctor_id(FK)│         │ vaccine_name │
                         │ status       │         │ status       │
                         │ ...          │         │ ...          │
                         └──────────────┘         └──────────────┘

      ┌──────────────┐         ┌──────────────┐         ┌──────────────┐
      │growth_alerts │         │ audit_logs   │         │notifications │
      ├──────────────┤         ├──────────────┤         ├──────────────┤
      │ alert_id (PK)│         │ log_id (PK)  │         │notification_id│
      │ child_id (FK)│         │ user_id (FK) │         │ user_id (FK) │
      │ record_id(FK)│         │ action       │         │ type         │
      │ alert_type   │         │ table_name   │         │ message      │
      │ severity     │         │ ...          │         │ ...          │
      └──────────────┘         └──────────────┘         └──────────────┘
```

### Main Tables

1. **users** - Stores all system users (mothers, doctors, admins)
2. **children** - Child profiles with parent information
3. **health_records** - Monthly health assessments
4. **appointments** - Scheduled checkups
5. **vaccinations** - Vaccination tracking
6. **growth_alerts** - Abnormal growth pattern alerts
7. **audit_logs** - Security and compliance logging
8. **notifications** - User notifications

## 📦 Setup Instructions

### Prerequisites

- Java Development Kit (JDK) 11 or higher
- Apache Tomcat 9.0 or higher
- MySQL Server 8.0 or higher
- Maven 3.6+ (for building the project)

### Step 1: Database Setup

1. Install MySQL Server if not already installed
2. Create the database and tables:

```bash
mysql -u root -p < database/schema.sql
```

3. Insert sample test data:

```bash
mysql -u root -p < database/sample_data.sql
```

### Step 2: Configure Database Connection

1. Navigate to `src/main/resources/`
2. Copy `database.properties.example` to `database.properties`
3. Update the database credentials:

```properties
db.url=jdbc:mysql://localhost:3306/chms_db?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password_here
db.driver=com.mysql.cj.jdbc.Driver
```

### Step 3: Build the Project

```bash
mvn clean package
```

This will create a WAR file in the `target/` directory.

### Step 4: Deploy to Tomcat

**Option 1: Manual Deployment**
1. Copy `target/chms.war` to Tomcat's `webapps/` directory
2. Start Tomcat server

**Option 2: Maven Tomcat Plugin**
```bash
mvn tomcat7:run
```

### Step 5: Access the Application

Open your browser and navigate to:
```
http://localhost:8080/chms/
```

### Default Login Credentials

**Administrator:**
- Email: `admin@chms.com`
- Password: `password123`

**Doctor:**
- Email: `dr.smith@chms.com`
- Password: `password123`

**Mother:**
- Email: `mary.wilson@email.com`
- Password: `password123`

## 👥 User Roles

### Mother
- Register children at birth
- View own children's profiles
- View health records and growth trends
- Schedule appointments
- Receive notifications

### Doctor
- View all registered children
- Add monthly health assessments
- Track growth patterns
- Manage vaccinations
- Schedule and manage appointments
- Review and resolve growth alerts

### Administrator
- Manage all users
- View system-wide statistics
- Access audit logs
- Archive/delete records
- System configuration

## 📁 Project Structure

```
chms/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── chms/
│   │   │           ├── model/          # POJOs
│   │   │           │   ├── User.java
│   │   │           │   ├── Child.java
│   │   │           │   ├── HealthRecord.java
│   │   │           │   ├── Appointment.java
│   │   │           │   ├── Vaccination.java
│   │   │           │   ├── GrowthAlert.java
│   │   │           │   ├── AuditLog.java
│   │   │           │   └── Notification.java
│   │   │           ├── dao/            # Data Access Objects
│   │   │           │   ├── UserDAO.java
│   │   │           │   ├── ChildDAO.java
│   │   │           │   ├── HealthRecordDAO.java
│   │   │           │   ├── AppointmentDAO.java
│   │   │           │   ├── VaccinationDAO.java
│   │   │           │   ├── GrowthAlertDAO.java
│   │   │           │   └── AuditLogDAO.java
│   │   │           ├── servlet/        # Controllers
│   │   │           │   ├── LoginServlet.java
│   │   │           │   ├── LogoutServlet.java
│   │   │           │   ├── ChildServlet.java
│   │   │           │   ├── HealthRecordServlet.java
│   │   │           │   ├── DoctorDashboardServlet.java
│   │   │           │   ├── MotherDashboardServlet.java
│   │   │           │   └── AdminDashboardServlet.java
│   │   │           ├── filter/         # Filters
│   │   │           │   ├── AuthenticationFilter.java
│   │   │           │   └── RoleAuthorizationFilter.java
│   │   │           └── util/           # Utilities
│   │   │               ├── DatabaseConnection.java
│   │   │               ├── PasswordHasher.java
│   │   │               ├── SessionManager.java
│   │   │               ├── DateTimeUtil.java
│   │   │               └── ValidationUtil.java
│   │   ├── resources/
│   │   │   └── database.properties
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml
│   │       │   └── views/              # JSP Pages
│   │       │       ├── login.jsp
│   │       │       ├── mother/
│   │       │       │   ├── dashboard.jsp
│   │       │       │   ├── child-profile.jsp
│   │       │       │   └── add-child.jsp
│   │       │       ├── doctor/
│   │       │       │   ├── dashboard.jsp
│   │       │       │   ├── child-list.jsp
│   │       │       │   ├── add-health-record.jsp
│   │       │       │   └── growth-trends.jsp
│   │       │       └── admin/
│   │       │           ├── dashboard.jsp
│   │       │           ├── user-management.jsp
│   │       │           └── audit-logs.jsp
│   │       ├── css/
│   │       ├── js/
│   │       └── index.jsp
│   └── test/
│       └── java/
├── database/
│   ├── schema.sql
│   └── sample_data.sql
├── pom.xml
└── README.md
```

## 🔐 Security Features

1. **Password Security**
   - BCrypt hashing with work factor 10
   - Password strength validation
   - Secure password storage

2. **Session Management**
   - Secure session-based authentication
   - Session timeout (30 minutes)
   - Session invalidation on logout

3. **Input Validation**
   - Server-side validation for all inputs
   - XSS prevention through input sanitization
   - SQL injection prevention using PreparedStatements

4. **Role-Based Access Control**
   - Authentication filters
   - Authorization based on user roles
   - Protected routes

5. **Audit Logging**
   - Track all critical operations
   - IP address and user agent logging
   - Compliance tracking

## 🧪 Testing

### Test with Sample Data

The database includes sample data for testing:

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@chms.com | password123 |
| Doctor | dr.smith@chms.com | password123 |
| Doctor | dr.johnson@chms.com | password123 |
| Mother | mary.wilson@email.com | password123 |
| Mother | jennifer.brown@email.com | password123 |

### Manual Testing Checklist

- [ ] User login/logout
- [ ] Mother: Register new child
- [ ] Mother: View child profiles
- [ ] Doctor: View all children
- [ ] Doctor: Add health record
- [ ] Doctor: Schedule appointment
- [ ] Growth alert generation
- [ ] Notification creation
- [ ] Admin: View audit logs

## 🔄 API Endpoints (Servlets)

| Endpoint | Method | Description | Role |
|----------|--------|-------------|------|
| `/login` | POST | User authentication | All |
| `/logout` | GET | User logout | All |
| `/mother/dashboard` | GET | Mother dashboard | Mother |
| `/mother/child/add` | POST | Register new child | Mother |
| `/mother/child/view` | GET | View child profile | Mother |
| `/doctor/dashboard` | GET | Doctor dashboard | Doctor |
| `/doctor/children` | GET | List all children | Doctor |
| `/doctor/health-record/add` | POST | Add health record | Doctor |
| `/doctor/appointment/schedule` | POST | Schedule appointment | Doctor |
| `/admin/dashboard` | GET | Admin dashboard | Admin |
| `/admin/users` | GET | Manage users | Admin |
| `/admin/audit-logs` | GET | View audit logs | Admin |

## 📊 Database Queries

### Useful Stored Procedures

```sql
-- Calculate child's age in months
CALL sp_calculate_child_age(child_id, @age_months);

-- Get child growth trend
CALL sp_get_growth_trend(child_id);

-- Check for overdue checkups
CALL sp_check_overdue_checkups();
```

### Useful Views

```sql
-- Children with mother details
SELECT * FROM view_children_with_mothers;

-- Latest health records
SELECT * FROM view_latest_health_records;

-- Pending appointments
SELECT * FROM view_pending_appointments;

-- Active growth alerts
SELECT * FROM view_active_alerts;
```

## 🚀 Future Enhancements

1. **Mobile Application**
   - Android/iOS app integration
   - Push notifications

2. **AI-Based Features**
   - Health risk prediction
   - Growth pattern analysis
   - Personalized nutrition recommendations

3. **Communication Features**
   - SMS/Email notifications
   - In-app messaging between doctors and mothers
   - Video consultation integration

4. **Advanced Reporting**
   - Growth charts and graphs
   - Export to PDF
   - Statistical analysis

5. **Multi-language Support**
   - Internationalization (i18n)
   - Multiple language options

6. **Integration**
   - EHR (Electronic Health Records) system integration
   - Laboratory results integration
   - Pharmacy integration

## 📝 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise

### Database
- Use PreparedStatements for all queries
- Implement connection pooling
- Close resources in try-with-resources
- Use transactions for multi-step operations

### Security
- Never store plain text passwords
- Validate all user inputs
- Use parameterized queries
- Implement proper error handling

## 🐛 Troubleshooting

### Database Connection Issues
- Verify MySQL is running
- Check database.properties configuration
- Ensure MySQL connector is in classpath

### Tomcat Deployment Issues
- Check Tomcat logs in `logs/catalina.out`
- Verify WAR file is properly deployed
- Ensure port 8080 is not in use

### Build Issues
- Run `mvn clean install` to rebuild
- Check Maven dependencies
- Verify Java version

## 📞 Support

For issues or questions:
- Check the documentation
- Review sample data and test cases
- Check Tomcat logs for errors

## 📄 License

This project is developed as an academic final project and is available for educational purposes.

## 👨‍💻 Contributors

Developed as a comprehensive Child Health Monitoring System demonstrating:
- Java web development with Servlets/JSP
- MySQL database design and implementation
- MVC architecture
- Security best practices
- Professional code structure and documentation

---

**Note:** This is a complete, production-ready system suitable for academic demonstration and evaluation. All security features, best practices, and professional standards have been implemented.
