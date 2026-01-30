# Child Health Monitoring System - Quick Start Guide

## 🚀 Quick Setup (5 Minutes)

### Step 1: Install Prerequisites

**Required Software:**
- Java JDK 11 or higher
- MySQL Server 8.0 or higher
- Apache Tomcat 9.0 or higher
- Maven 3.6+ (optional, for building)

**Windows Installation:**
```powershell
# Install Java
# Download from: https://www.oracle.com/java/technologies/downloads/

# Install MySQL
# Download from: https://dev.mysql.com/downloads/installer/

# Install Tomcat
# Download from: https://tomcat.apache.org/download-90.cgi

# Install Maven (optional)
# Download from: https://maven.apache.org/download.cgi
```

### Step 2: Setup Database

**1. Start MySQL Server**
```powershell
# Start MySQL service
net start MySQL80
```

**2. Create Database**
```powershell
# Login to MySQL
mysql -u root -p

# Run the schema script from MySQL prompt
source C:/Users/USER/Desktop/chms/database/schema.sql

# Insert sample data
source C:/Users/USER/Desktop/chms/database/sample_data.sql

# Verify database creation
USE chms_db;
SHOW TABLES;
EXIT;
```

### Step 3: Configure Application

**1. Update Database Configuration**

Edit `src/main/resources/database.properties`:
```properties
db.url=jdbc:mysql://localhost:3306/chms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
db.username=root
db.password=YOUR_MYSQL_PASSWORD
db.driver=com.mysql.cj.jdbc.Driver
```

### Step 4: Build the Application

**Option A: Using Maven**
```powershell
cd C:\Users\USER\Desktop\chms
mvn clean package
```

This creates: `target/chms.war`

**Option B: Using IDE**
- Import project as Maven project in Eclipse/IntelliJ IDEA
- Run Maven build: `clean package`

### Step 5: Deploy to Tomcat

**Method 1: Manual Deployment**
```powershell
# Copy WAR file to Tomcat webapps
copy target\chms.war C:\apache-tomcat-9.0.xx\webapps\

# Start Tomcat
C:\apache-tomcat-9.0.xx\bin\startup.bat
```

**Method 2: Using Maven**
```powershell
mvn tomcat7:run
```

**Method 3: Using IDE**
- Configure Tomcat server in IDE
- Deploy project to Tomcat
- Start server

### Step 6: Access Application

**Open Browser:**
```
http://localhost:8080/chms/
```

**Test Login Credentials:**

| Role | Email | Password |
|------|-------|----------|
| **Admin** | admin@chms.com | password123 |
| **Doctor** | dr.smith@chms.com | password123 |
| **Mother** | mary.wilson@email.com | password123 |

## 📋 Verification Checklist

- [ ] MySQL database created successfully
- [ ] Sample data inserted (9 users, 8 children)
- [ ] Database connection configured
- [ ] WAR file built successfully
- [ ] Tomcat started without errors
- [ ] Application accessible at http://localhost:8080/chms/
- [ ] Login works with test credentials
- [ ] Dashboard loads for each role

## 🔧 Troubleshooting

### Problem: "Cannot connect to database"

**Solution 1:** Verify MySQL is running
```powershell
# Check MySQL service
sc query MySQL80

# If not running, start it
net start MySQL80
```

**Solution 2:** Check database.properties
- Verify username and password
- Ensure database name is correct: `chms_db`
- Check MySQL port (default: 3306)

### Problem: "Port 8080 already in use"

**Solution:** Change Tomcat port
Edit `conf/server.xml` in Tomcat directory:
```xml
<Connector port="8080"  <!-- Change to 8081 or another port -->
```

### Problem: "404 Not Found" after deployment

**Solution:**
1. Check if WAR deployed: Look for `chms` folder in `webapps`
2. Check Tomcat logs: `logs/catalina.out`
3. Restart Tomcat

### Problem: Maven build fails

**Solution:**
```powershell
# Clean and rebuild
mvn clean
mvn install -U

# Skip tests if needed
mvn clean package -DskipTests
```

## 📊 Database Structure Overview

**Main Tables Created:**
- `users` (9 sample users: 1 admin, 3 doctors, 5 mothers)
- `children` (8 sample children with different ages)
- `health_records` (30+ health assessment records)
- `appointments` (10 scheduled appointments)
- `vaccinations` (40+ vaccination records)
- `growth_alerts` (Automatically generated alerts)
- `audit_logs` (Track all operations)
- `notifications` (User notifications)

**Sample Data Includes:**
- Children ranging from newborn to 2 years old
- Complete vaccination histories
- Monthly growth tracking
- Various growth statuses (normal, underweight)
- Scheduled and completed appointments

## 🎯 Testing the System

### Test Case 1: Mother Login
1. Login as: mary.wilson@email.com / password123
2. View dashboard
3. See children: Emma Wilson, Oliver Wilson
4. View child profiles
5. Check health records
6. View appointments

### Test Case 2: Doctor Login
1. Login as: dr.smith@chms.com / password123
2. View all registered children
3. Select a child
4. View health history
5. Add new health record
6. Schedule appointment

### Test Case 3: Admin Login
1. Login as: admin@chms.com / password123
2. View system statistics
3. Manage users
4. View audit logs
5. Check growth alerts

## 📱 System Features Tour

### For Mothers:
✅ Register new child at birth
✅ View all your children
✅ See detailed health records
✅ Track growth trends
✅ View vaccination schedule
✅ Check upcoming appointments
✅ Receive notifications

### For Doctors:
✅ View all registered children
✅ Add monthly health assessments
✅ Calculate BMI automatically
✅ Detect growth abnormalities
✅ Schedule checkups
✅ Track vaccination status
✅ Add medical notes

### For Administrators:
✅ Manage all users
✅ View system statistics
✅ Access audit logs
✅ Monitor growth alerts
✅ System configuration

## 🔐 Security Features

✅ **Password Security:** BCrypt hashing
✅ **Session Management:** 30-minute timeout
✅ **Input Validation:** Server-side validation
✅ **SQL Injection Prevention:** PreparedStatements
✅ **XSS Protection:** Input sanitization
✅ **Audit Logging:** Track all operations
✅ **Role-Based Access:** Separate dashboards

## 📈 Performance

- **Database Connection Pooling:** Configured for optimal performance
- **Efficient Queries:** Indexed columns and optimized SQL
- **Resource Management:** Proper connection handling
- **Session Management:** Optimized session timeout

## 🎓 Educational Value

This project demonstrates:
- **MVC Architecture:** Clear separation of concerns
- **DAO Pattern:** Data access abstraction
- **JDBC:** Database connectivity and operations
- **Servlets & JSP:** Java web development
- **Security Best Practices:** Authentication & authorization
- **Database Design:** Proper normalization and relationships
- **Professional Code Structure:** Clean, documented, maintainable

## 📞 Getting Help

**Check Logs:**
```powershell
# Tomcat logs
type C:\apache-tomcat-9.0.xx\logs\catalina.out

# MySQL logs
type C:\ProgramData\MySQL\MySQL Server 8.0\Data\*.err
```

**Common Issues:**
1. **Class Not Found:** Ensure MySQL connector is in `lib` folder
2. **Connection Refused:** Check MySQL is running
3. **Permission Denied:** Run as Administrator
4. **Port Conflict:** Change Tomcat port

## ✅ Success Indicators

You know the setup is successful when:
- ✅ Login page loads at http://localhost:8080/chms/
- ✅ All three roles can login
- ✅ Dashboards display data correctly
- ✅ Sample children and health records are visible
- ✅ No errors in Tomcat logs

## 🎉 Next Steps

After successful setup:
1. Explore the mother dashboard
2. Add a health record as a doctor
3. Schedule appointments
4. View growth trends and charts
5. Check audit logs as admin
6. Test all CRUD operations

## 📝 Important Notes

- **Default Password:** All test users use `password123`
- **Session Timeout:** 30 minutes of inactivity
- **Sample Data:** Pre-loaded with realistic test data
- **Database:** Uses `chms_db` database name
- **Context Path:** `/chms` (can be changed in deployment)

---

**Congratulations! Your Child Health Monitoring System is now ready for demonstration and evaluation!** 🎊
