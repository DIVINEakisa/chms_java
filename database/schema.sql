-- ============================================================
-- Child Health Monitoring System - Database Schema
-- ============================================================
-- This script creates the complete database structure for CHMS
-- Execute this script to set up the database
-- ============================================================

-- Drop database if exists and create fresh
DROP DATABASE IF EXISTS chms_db;
CREATE DATABASE chms_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE chms_db;

-- ============================================================
-- Table: users
-- Purpose: Store all system users (mothers, doctors, admins)
-- ============================================================
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    role ENUM('MOTHER', 'DOCTOR', 'ADMIN') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login TIMESTAMP NULL,
    
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_active (is_active)
) ENGINE=InnoDB;

-- ============================================================
-- Table: children
-- Purpose: Store child profiles registered by mothers
-- ============================================================
CREATE TABLE children (
    child_id INT PRIMARY KEY AUTO_INCREMENT,
    unique_profile_id VARCHAR(50) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
    birth_weight DECIMAL(5,2) COMMENT 'Weight at birth in kg',
    birth_height DECIMAL(5,2) COMMENT 'Height at birth in cm',
    blood_group VARCHAR(5),
    mother_id INT NOT NULL,
    father_name VARCHAR(100),
    father_phone VARCHAR(20),
    emergency_contact VARCHAR(20),
    address TEXT,
    medical_history TEXT COMMENT 'Pre-existing conditions, allergies',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (mother_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_mother_id (mother_id),
    INDEX idx_dob (date_of_birth),
    INDEX idx_active (is_active)
) ENGINE=InnoDB;

-- ============================================================
-- Table: health_records
-- Purpose: Store monthly health assessments by doctors
-- ============================================================
CREATE TABLE health_records (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    child_id INT NOT NULL,
    doctor_id INT NULL COMMENT 'Doctor who performed assessment - NULL if doctor deleted',
    assessment_date DATE NOT NULL,
    child_age_months INT NOT NULL COMMENT 'Age in months at assessment',
    weight DECIMAL(5,2) NOT NULL COMMENT 'Weight in kg',
    height DECIMAL(5,2) NOT NULL COMMENT 'Height in cm',
    bmi DECIMAL(5,2) GENERATED ALWAYS AS (weight / POWER(height/100, 2)) STORED COMMENT 'Auto-calculated BMI',
    head_circumference DECIMAL(5,2) COMMENT 'Head circumference in cm',
    temperature DECIMAL(4,1) COMMENT 'Body temperature in Celsius',
    growth_status ENUM('NORMAL', 'UNDERWEIGHT', 'OVERWEIGHT', 'STUNTED', 'WASTED') NOT NULL,
    vaccination_status TEXT COMMENT 'Vaccines administered',
    nutrition_notes TEXT COMMENT 'Dietary recommendations and notes',
    health_notes TEXT COMMENT 'General health observations',
    abnormalities_detected TEXT COMMENT 'Any health concerns or abnormalities',
    next_checkup_date DATE COMMENT 'Recommended next visit',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_assessment_date (assessment_date),
    INDEX idx_growth_status (growth_status)
) ENGINE=InnoDB;

-- ============================================================
-- Table: appointments
-- Purpose: Schedule and track monthly checkups
-- ============================================================
CREATE TABLE appointments (
    appointment_id INT PRIMARY KEY AUTO_INCREMENT,
    child_id INT NOT NULL,
    doctor_id INT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    appointment_type ENUM('ROUTINE_CHECKUP', 'VACCINATION', 'FOLLOW_UP', 'EMERGENCY') DEFAULT 'ROUTINE_CHECKUP',
    status ENUM('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'NO_SHOW') DEFAULT 'SCHEDULED',
    notes TEXT COMMENT 'Appointment notes or reason',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_doctor_id (doctor_id),
    INDEX idx_appointment_date (appointment_date),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- ============================================================
-- Table: vaccinations
-- Purpose: Track vaccination schedule and completion
-- ============================================================
CREATE TABLE vaccinations (
    vaccination_id INT PRIMARY KEY AUTO_INCREMENT,
    child_id INT NOT NULL,
    vaccine_name VARCHAR(100) NOT NULL,
    recommended_age_months INT NOT NULL,
    administered_date DATE,
    administered_by INT COMMENT 'Doctor who administered',
    batch_number VARCHAR(50),
    status ENUM('PENDING', 'COMPLETED', 'MISSED', 'DELAYED') DEFAULT 'PENDING',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE,
    FOREIGN KEY (administered_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- ============================================================
-- Table: growth_alerts
-- Purpose: Track abnormal growth patterns and alerts
-- ============================================================
CREATE TABLE growth_alerts (
    alert_id INT PRIMARY KEY AUTO_INCREMENT,
    child_id INT NOT NULL,
    record_id INT NOT NULL,
    alert_type ENUM('UNDERWEIGHT', 'OVERWEIGHT', 'STUNTED', 'RAPID_WEIGHT_LOSS', 'RAPID_WEIGHT_GAIN', 'NO_GROWTH') NOT NULL,
    severity ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') DEFAULT 'MEDIUM',
    description TEXT NOT NULL,
    is_resolved BOOLEAN DEFAULT FALSE,
    resolved_date DATE,
    resolved_by INT COMMENT 'Doctor who resolved the alert',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (child_id) REFERENCES children(child_id) ON DELETE CASCADE,
    FOREIGN KEY (record_id) REFERENCES health_records(record_id) ON DELETE CASCADE,
    FOREIGN KEY (resolved_by) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_alert_type (alert_type),
    INDEX idx_resolved (is_resolved)
) ENGINE=InnoDB;

-- ============================================================
-- Table: audit_logs
-- Purpose: Track all critical operations for security and compliance
-- ============================================================
CREATE TABLE audit_logs (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    action VARCHAR(100) NOT NULL COMMENT 'CREATE, UPDATE, DELETE, LOGIN, LOGOUT',
    table_name VARCHAR(50) COMMENT 'Affected table',
    record_id INT COMMENT 'Affected record ID',
    old_value TEXT COMMENT 'Previous value (for updates/deletes)',
    new_value TEXT COMMENT 'New value (for creates/updates)',
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_table_name (table_name),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- ============================================================
-- Table: notifications
-- Purpose: Store system notifications for users
-- ============================================================
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    notification_type ENUM('APPOINTMENT_REMINDER', 'VACCINATION_DUE', 'CHECKUP_DUE', 'GROWTH_ALERT', 'SYSTEM') NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    related_child_id INT,
    related_appointment_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (related_child_id) REFERENCES children(child_id) ON DELETE CASCADE,
    FOREIGN KEY (related_appointment_id) REFERENCES appointments(appointment_id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- ============================================================
-- Views for easier data access
-- ============================================================

-- View: Child with mother details
CREATE VIEW view_children_with_mothers AS
SELECT 
    c.*,
    u.full_name AS mother_name,
    u.email AS mother_email,
    u.phone_number AS mother_phone,
    TIMESTAMPDIFF(MONTH, c.date_of_birth, CURDATE()) AS age_months,
    TIMESTAMPDIFF(YEAR, c.date_of_birth, CURDATE()) AS age_years
FROM children c
INNER JOIN users u ON c.mother_id = u.user_id
WHERE c.is_active = TRUE;

-- View: Latest health record for each child
CREATE VIEW view_latest_health_records AS
SELECT 
    hr.*,
    c.full_name AS child_name,
    c.date_of_birth,
    u.full_name AS doctor_name
FROM health_records hr
INNER JOIN children c ON hr.child_id = c.child_id
INNER JOIN users u ON hr.doctor_id = u.user_id
WHERE hr.record_id IN (
    SELECT MAX(record_id) 
    FROM health_records 
    GROUP BY child_id
);

-- View: Pending appointments
CREATE VIEW view_pending_appointments AS
SELECT 
    a.*,
    c.full_name AS child_name,
    c.date_of_birth,
    u.full_name AS mother_name,
    u.phone_number AS mother_phone,
    d.full_name AS doctor_name
FROM appointments a
INNER JOIN children c ON a.child_id = c.child_id
INNER JOIN users u ON c.mother_id = u.user_id
LEFT JOIN users d ON a.doctor_id = d.user_id
WHERE a.status IN ('SCHEDULED', 'CONFIRMED')
    AND a.appointment_date >= CURDATE();

-- View: Active growth alerts
CREATE VIEW view_active_alerts AS
SELECT 
    ga.*,
    c.full_name AS child_name,
    c.date_of_birth,
    u.full_name AS mother_name,
    u.phone_number AS mother_phone
FROM growth_alerts ga
INNER JOIN children c ON ga.child_id = c.child_id
INNER JOIN users u ON c.mother_id = u.user_id
WHERE ga.is_resolved = FALSE
ORDER BY ga.severity DESC, ga.created_at DESC;

-- ============================================================
-- Stored Procedures
-- ============================================================

-- Calculate child's age in months
DELIMITER //
CREATE PROCEDURE sp_calculate_child_age(
    IN p_child_id INT,
    OUT p_age_months INT
)
BEGIN
    SELECT TIMESTAMPDIFF(MONTH, date_of_birth, CURDATE())
    INTO p_age_months
    FROM children
    WHERE child_id = p_child_id;
END //
DELIMITER ;

-- Get child growth trend
DELIMITER //
CREATE PROCEDURE sp_get_growth_trend(
    IN p_child_id INT
)
BEGIN
    SELECT 
        record_id,
        assessment_date,
        child_age_months,
        weight,
        height,
        bmi,
        growth_status,
        ROUND(weight - LAG(weight) OVER (ORDER BY assessment_date), 2) AS weight_change,
        ROUND(height - LAG(height) OVER (ORDER BY assessment_date), 2) AS height_change
    FROM health_records
    WHERE child_id = p_child_id
    ORDER BY assessment_date;
END //
DELIMITER ;

-- Check for overdue checkups
DELIMITER //
CREATE PROCEDURE sp_check_overdue_checkups()
BEGIN
    SELECT 
        c.child_id,
        c.unique_profile_id,
        c.full_name AS child_name,
        u.full_name AS mother_name,
        u.email AS mother_email,
        u.phone_number AS mother_phone,
        MAX(hr.assessment_date) AS last_checkup,
        DATEDIFF(CURDATE(), MAX(hr.assessment_date)) AS days_since_checkup,
        TIMESTAMPDIFF(MONTH, c.date_of_birth, CURDATE()) AS child_age_months
    FROM children c
    INNER JOIN users u ON c.mother_id = u.user_id
    LEFT JOIN health_records hr ON c.child_id = hr.child_id
    WHERE c.is_active = TRUE
    GROUP BY c.child_id, c.unique_profile_id, c.full_name, u.full_name, u.email, u.phone_number, c.date_of_birth
    HAVING days_since_checkup > 35 OR days_since_checkup IS NULL
    ORDER BY days_since_checkup DESC;
END //
DELIMITER ;

-- ============================================================
-- Triggers
-- ============================================================

-- Trigger: Generate unique profile ID for new children
DELIMITER //
CREATE TRIGGER before_child_insert
BEFORE INSERT ON children
FOR EACH ROW
BEGIN
    IF NEW.unique_profile_id IS NULL OR NEW.unique_profile_id = '' THEN
        SET NEW.unique_profile_id = CONCAT(
            'CH',
            YEAR(NEW.date_of_birth),
            LPAD(FLOOR(RAND() * 999999), 6, '0')
        );
    END IF;
END //
DELIMITER ;

-- Trigger: Create growth alert on abnormal health record
DELIMITER //
CREATE TRIGGER after_health_record_insert
AFTER INSERT ON health_records
FOR EACH ROW
BEGIN
    DECLARE alert_desc TEXT;
    DECLARE alert_severity VARCHAR(10);
    
    IF NEW.growth_status IN ('UNDERWEIGHT', 'OVERWEIGHT', 'STUNTED', 'WASTED') THEN
        SET alert_desc = CONCAT(
            'Child shows ', NEW.growth_status, ' status. ',
            'Weight: ', NEW.weight, 'kg, Height: ', NEW.height, 'cm, BMI: ', NEW.bmi
        );
        
        SET alert_severity = CASE
            WHEN NEW.growth_status IN ('STUNTED', 'WASTED') THEN 'HIGH'
            WHEN NEW.growth_status = 'UNDERWEIGHT' THEN 'MEDIUM'
            ELSE 'LOW'
        END;
        
        INSERT INTO growth_alerts (child_id, record_id, alert_type, severity, description)
        VALUES (NEW.child_id, NEW.record_id, NEW.growth_status, alert_severity, alert_desc);
    END IF;
END //
DELIMITER ;

-- Trigger: Create notification for appointment
DELIMITER //
CREATE TRIGGER after_appointment_insert
AFTER INSERT ON appointments
FOR EACH ROW
BEGIN
    DECLARE mother_user_id INT;
    
    -- Get mother's user ID
    SELECT mother_id INTO mother_user_id
    FROM children
    WHERE child_id = NEW.child_id;
    
    -- Create notification for mother
    INSERT INTO notifications (user_id, notification_type, title, message, related_child_id, related_appointment_id)
    VALUES (
        mother_user_id,
        'APPOINTMENT_REMINDER',
        'Appointment Scheduled',
        CONCAT('An appointment has been scheduled for ', DATE_FORMAT(NEW.appointment_date, '%M %d, %Y'), ' at ', TIME_FORMAT(NEW.appointment_time, '%h:%i %p')),
        NEW.child_id,
        NEW.appointment_id
    );
END //
DELIMITER ;

-- ============================================================
-- Database is ready!
-- ============================================================

COMMIT;
