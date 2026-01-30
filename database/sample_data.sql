-- ============================================================
-- Child Health Monitoring System - Sample Test Data
-- ============================================================
-- This script populates the database with sample data for testing
-- Execute this after running schema.sql
-- ============================================================

USE chms_db;

-- ============================================================
-- Insert Sample Users
-- Password for all users: "password123" (hashed with BCrypt)
-- Hash: $2a$10$xZQ5v5h8YJZJXHXvHXvHXe5FZG5FZG5FZG5FZG5FZG5FZG5FZG5
-- ============================================================

INSERT INTO users (email, password_hash, full_name, phone_number, role, is_active) VALUES
-- Administrators
('admin@chms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', '+1234567890', 'ADMIN', TRUE),

-- Doctors
('dr.smith@chms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Sarah Smith', '+1234567891', 'DOCTOR', TRUE),
('dr.johnson@chms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Michael Johnson', '+1234567892', 'DOCTOR', TRUE),
('dr.williams@chms.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dr. Emily Williams', '+1234567893', 'DOCTOR', TRUE),

-- Mothers
('mary.wilson@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mary Wilson', '+1234567894', 'MOTHER', TRUE),
('jennifer.brown@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jennifer Brown', '+1234567895', 'MOTHER', TRUE),
('patricia.davis@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Patricia Davis', '+1234567896', 'MOTHER', TRUE),
('linda.miller@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Linda Miller', '+1234567897', 'MOTHER', TRUE),
('barbara.moore@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Barbara Moore', '+1234567898', 'MOTHER', TRUE);

-- ============================================================
-- Insert Sample Children
-- ============================================================

INSERT INTO children (unique_profile_id, full_name, date_of_birth, gender, birth_weight, birth_height, blood_group, mother_id, father_name, father_phone, emergency_contact, address, medical_history) VALUES
-- Mary Wilson's children
('CH2023001234', 'Emma Wilson', '2023-03-15', 'FEMALE', 3.2, 50.0, 'O+', 5, 'Robert Wilson', '+1234567899', '+1234567899', '123 Maple Street, Springfield, IL 62701', 'No known allergies'),
('CH2024002345', 'Oliver Wilson', '2024-06-20', 'MALE', 3.5, 52.0, 'O+', 5, 'Robert Wilson', '+1234567899', '+1234567899', '123 Maple Street, Springfield, IL 62701', 'No known allergies'),

-- Jennifer Brown's children
('CH2023003456', 'Sophia Brown', '2023-01-10', 'FEMALE', 3.0, 49.0, 'A+', 6, 'James Brown', '+1234567810', '+1234567810', '456 Oak Avenue, Chicago, IL 60614', 'Lactose intolerant'),
('CH2024004567', 'Noah Brown', '2024-08-05', 'MALE', 3.8, 53.0, 'A+', 6, 'James Brown', '+1234567810', '+1234567810', '456 Oak Avenue, Chicago, IL 60614', 'No known allergies'),

-- Patricia Davis's children
('CH2022005678', 'Isabella Davis', '2022-11-22', 'FEMALE', 3.3, 51.0, 'B+', 7, 'William Davis', '+1234567811', '+1234567811', '789 Pine Road, Peoria, IL 61602', 'Peanut allergy'),
('CH2025006789', 'Liam Davis', '2025-01-12', 'MALE', 3.6, 51.5, 'B+', 7, 'William Davis', '+1234567811', '+1234567811', '789 Pine Road, Peoria, IL 61602', 'No known allergies'),

-- Linda Miller's children
('CH2023007890', 'Ava Miller', '2023-07-08', 'FEMALE', 3.1, 49.5, 'AB+', 8, 'Richard Miller', '+1234567812', '+1234567812', '321 Elm Street, Rockford, IL 61101', 'Eczema'),

-- Barbara Moore's children
('CH2024008901', 'Ethan Moore', '2024-02-14', 'MALE', 3.4, 51.0, 'O-', 9, 'Charles Moore', '+1234567813', '+1234567813', '654 Birch Lane, Naperville, IL 60540', 'No known allergies');

-- ============================================================
-- Insert Sample Health Records
-- ============================================================

-- Emma Wilson (22 months old)
INSERT INTO health_records (child_id, doctor_id, assessment_date, child_age_months, weight, height, head_circumference, temperature, growth_status, vaccination_status, nutrition_notes, health_notes, abnormalities_detected, next_checkup_date) VALUES
(1, 2, '2023-04-15', 1, 4.2, 54.0, 36.5, 36.8, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Exclusive breastfeeding recommended', 'Healthy newborn, all reflexes normal', NULL, '2023-05-15'),
(1, 2, '2023-07-15', 4, 6.1, 62.0, 40.5, 36.7, 'NORMAL', 'DPT (1st), Polio (1st), Hib (1st)', 'Continue breastfeeding, start vitamin D drops', 'Good weight gain, meeting developmental milestones', NULL, '2023-08-15'),
(1, 2, '2023-10-15', 7, 7.8, 67.0, 43.0, 36.9, 'NORMAL', 'DPT (2nd), Polio (2nd)', 'Introduce solid foods gradually', 'Rolling over, sitting with support', NULL, '2023-11-15'),
(1, 2, '2024-01-15', 10, 9.2, 72.0, 44.5, 36.8, 'NORMAL', 'DPT (3rd), Polio (3rd), Measles (1st)', 'Varied diet with fruits and vegetables', 'Crawling, starting to stand with support', NULL, '2024-02-15'),
(1, 3, '2024-07-15', 16, 11.0, 79.0, 46.0, 36.7, 'NORMAL', 'MMR, Varicella', 'Well-balanced diet, limit sugary foods', 'Walking independently, speaking simple words', NULL, '2024-08-15'),
(1, 3, '2025-01-15', 22, 12.3, 84.0, 47.0, 36.8, 'NORMAL', 'All vaccines up to date', 'Healthy eating habits established', 'Active and playful, meeting all milestones', NULL, '2025-02-15'),

-- Sophia Brown (24 months old)
(3, 2, '2023-02-10', 1, 3.8, 52.0, 35.5, 36.9, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Lactose-free formula recommended', 'Healthy, lactose intolerance noted', NULL, '2023-03-10'),
(3, 2, '2023-05-10', 4, 5.9, 61.0, 39.5, 36.7, 'NORMAL', 'DPT (1st), Polio (1st)', 'Continue lactose-free formula', 'Adapting well to formula', NULL, '2023-06-10'),
(3, 3, '2023-11-10', 10, 8.8, 71.0, 43.5, 36.8, 'NORMAL', 'DPT (3rd), Polio (3rd)', 'Lactose-free diet maintained', 'Good growth, active baby', NULL, '2023-12-10'),
(3, 3, '2024-05-10', 16, 10.5, 78.0, 45.5, 36.8, 'NORMAL', 'MMR, Varicella', 'Continue lactose-free products', 'Walking well, very social', NULL, '2024-06-10'),
(3, 2, '2024-11-10', 22, 11.8, 83.0, 46.8, 36.7, 'NORMAL', 'All vaccines current', 'Maintaining dietary restrictions well', 'Developing normally, very verbal', NULL, '2024-12-10'),
(3, 2, '2025-01-10', 24, 12.5, 86.0, 47.2, 36.8, 'NORMAL', 'Complete for age', 'Continue current diet plan', 'Excellent development, no concerns', NULL, '2025-02-10'),

-- Isabella Davis (26 months old) - Shows UNDERWEIGHT
(5, 3, '2022-12-22', 1, 3.0, 49.0, 35.0, 37.0, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Breastfeeding established', 'Small but healthy newborn', NULL, '2023-01-22'),
(5, 3, '2023-03-22', 4, 5.2, 59.0, 38.5, 36.8, 'UNDERWEIGHT', 'DPT (1st), Polio (1st)', 'Increase feeding frequency, monitor weight', 'Weight gain slower than expected', 'Low weight percentile', '2023-04-22'),
(5, 2, '2023-06-22', 7, 6.5, 64.0, 41.0, 36.9, 'UNDERWEIGHT', 'DPT (2nd), Polio (2nd)', 'High-calorie supplementation recommended', 'Still below growth curve', 'Continued underweight status', '2023-07-22'),
(5, 2, '2023-12-22', 13, 8.2, 72.0, 43.5, 36.7, 'NORMAL', 'DPT (3rd), Measles', 'Improved nutrition showing results', 'Weight improving, catching up', NULL, '2024-01-22'),
(5, 3, '2024-06-22', 19, 10.0, 78.0, 45.0, 36.8, 'NORMAL', 'MMR, Varicella', 'Continue current diet', 'Good progress, normal range now', NULL, '2024-07-22'),
(5, 3, '2024-12-22', 25, 11.5, 84.0, 46.5, 36.8, 'NORMAL', 'All current', 'Maintaining healthy weight', 'Thriving, peanut allergy managed well', NULL, '2025-01-22'),

-- Oliver Wilson (7 months old)
(2, 2, '2024-07-20', 1, 4.0, 53.0, 37.0, 36.8, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Breastfeeding established', 'Healthy newborn', NULL, '2024-08-20'),
(2, 2, '2024-10-20', 4, 6.5, 63.0, 41.0, 36.7, 'NORMAL', 'DPT (1st), Polio (1st), Hib (1st)', 'Exclusive breastfeeding', 'Excellent weight gain', NULL, '2024-11-20'),
(2, 3, '2025-01-20', 7, 8.0, 68.0, 43.5, 36.9, 'NORMAL', 'DPT (2nd), Polio (2nd)', 'Starting solid foods', 'Active, reaching milestones', NULL, '2025-02-20'),

-- Noah Brown (5 months old)
(4, 3, '2024-09-05', 1, 4.3, 54.0, 37.5, 36.8, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Breastfeeding', 'Strong, healthy baby', NULL, '2024-10-05'),
(4, 3, '2024-12-05', 4, 6.8, 64.0, 41.5, 36.7, 'NORMAL', 'DPT (1st), Polio (1st)', 'Continue breastfeeding', 'Good development', NULL, '2025-01-05'),

-- Liam Davis (newborn - 0 months)
(6, 2, '2025-01-12', 0, 3.6, 51.5, 36.0, 36.9, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Breastfeeding initiated', 'Healthy newborn, all assessments normal', NULL, '2025-02-12'),

-- Ava Miller (18 months old)
(7, 2, '2023-08-08', 1, 3.5, 50.0, 36.0, 36.8, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Breastfeeding, eczema cream prescribed', 'Mild eczema present', 'Eczema on cheeks', '2023-09-08'),
(7, 3, '2023-11-08', 4, 6.2, 62.0, 40.0, 36.7, 'NORMAL', 'DPT (1st), Polio (1st)', 'Eczema improving with treatment', 'Skin condition managed', NULL, '2023-12-08'),
(7, 3, '2024-05-08', 10, 9.0, 72.0, 44.0, 36.8, 'NORMAL', 'DPT (3rd), Polio (3rd)', 'Balanced diet, moisturize skin regularly', 'Eczema well controlled', NULL, '2024-06-08'),
(7, 2, '2024-11-08', 16, 10.8, 78.0, 45.8, 36.7, 'NORMAL', 'MMR, Varicella', 'Continue skincare routine', 'Minimal eczema flare-ups', NULL, '2024-12-08'),
(7, 2, '2025-01-08', 18, 11.2, 80.0, 46.2, 36.8, 'NORMAL', 'Up to date', 'Healthy eating habits', 'Active, eczema minimal', NULL, '2025-02-08'),

-- Ethan Moore (11 months old)
(8, 3, '2024-03-14', 1, 3.9, 52.0, 36.8, 36.8, 'NORMAL', 'BCG, Hepatitis B (1st dose)', 'Breastfeeding', 'Healthy baby', NULL, '2024-04-14'),
(8, 3, '2024-06-14', 4, 6.6, 63.0, 40.8, 36.7, 'NORMAL', 'DPT (1st), Polio (1st)', 'Continue breastfeeding', 'Growing well', NULL, '2024-07-14'),
(8, 2, '2024-09-14', 7, 8.1, 68.0, 43.2, 36.8, 'NORMAL', 'DPT (2nd), Polio (2nd)', 'Introducing solids', 'Meeting milestones', NULL, '2024-10-14'),
(8, 2, '2024-12-14', 10, 9.5, 73.0, 44.8, 36.7, 'NORMAL', 'DPT (3rd), Polio (3rd)', 'Varied diet', 'Crawling and cruising', NULL, '2025-01-14');

-- ============================================================
-- Insert Sample Appointments
-- ============================================================

INSERT INTO appointments (child_id, doctor_id, appointment_date, appointment_time, appointment_type, status, notes) VALUES
-- Upcoming appointments
(1, 3, '2025-02-15', '10:00:00', 'ROUTINE_CHECKUP', 'SCHEDULED', '23-month checkup'),
(2, 3, '2025-02-20', '11:00:00', 'ROUTINE_CHECKUP', 'CONFIRMED', '8-month checkup'),
(3, 2, '2025-02-10', '09:30:00', 'ROUTINE_CHECKUP', 'CONFIRMED', '2-year checkup'),
(4, 3, '2025-02-05', '14:00:00', 'VACCINATION', 'SCHEDULED', 'DPT 2nd dose'),
(5, 3, '2025-02-22', '10:30:00', 'ROUTINE_CHECKUP', 'SCHEDULED', '26-month checkup'),
(6, 2, '2025-02-12', '09:00:00', 'ROUTINE_CHECKUP', 'SCHEDULED', '1-month checkup'),
(7, 2, '2025-02-08', '15:00:00', 'ROUTINE_CHECKUP', 'CONFIRMED', '19-month checkup'),
(8, 2, '2025-02-14', '11:30:00', 'ROUTINE_CHECKUP', 'SCHEDULED', '11-month checkup'),

-- Past completed appointments
(1, 2, '2025-01-15', '10:00:00', 'ROUTINE_CHECKUP', 'COMPLETED', '22-month checkup completed'),
(3, 2, '2025-01-10', '09:30:00', 'ROUTINE_CHECKUP', 'COMPLETED', '24-month checkup completed');

-- ============================================================
-- Insert Sample Vaccinations
-- ============================================================

INSERT INTO vaccinations (child_id, vaccine_name, recommended_age_months, administered_date, administered_by, batch_number, status, notes) VALUES
-- Emma Wilson vaccinations
(1, 'BCG', 0, '2023-03-15', 2, 'BCG2023-001', 'COMPLETED', 'Birth dose'),
(1, 'Hepatitis B - 1st Dose', 0, '2023-03-15', 2, 'HB2023-001', 'COMPLETED', 'Birth dose'),
(1, 'DPT - 1st Dose', 2, '2023-05-15', 2, 'DPT2023-101', 'COMPLETED', NULL),
(1, 'Polio - 1st Dose', 2, '2023-05-15', 2, 'POLIO2023-101', 'COMPLETED', NULL),
(1, 'DPT - 2nd Dose', 4, '2023-07-15', 2, 'DPT2023-102', 'COMPLETED', NULL),
(1, 'Polio - 2nd Dose', 4, '2023-07-15', 2, 'POLIO2023-102', 'COMPLETED', NULL),
(1, 'DPT - 3rd Dose', 6, '2023-09-15', 2, 'DPT2023-103', 'COMPLETED', NULL),
(1, 'Polio - 3rd Dose', 6, '2023-09-15', 2, 'POLIO2023-103', 'COMPLETED', NULL),
(1, 'MMR - 1st Dose', 12, '2024-03-15', 3, 'MMR2024-001', 'COMPLETED', NULL),
(1, 'Varicella', 12, '2024-03-15', 3, 'VAR2024-001', 'COMPLETED', NULL),

-- Oliver Wilson vaccinations
(2, 'BCG', 0, '2024-06-20', 2, 'BCG2024-050', 'COMPLETED', 'Birth dose'),
(2, 'Hepatitis B - 1st Dose', 0, '2024-06-20', 2, 'HB2024-050', 'COMPLETED', 'Birth dose'),
(2, 'DPT - 1st Dose', 2, '2024-08-20', 2, 'DPT2024-201', 'COMPLETED', NULL),
(2, 'Polio - 1st Dose', 2, '2024-08-20', 2, 'POLIO2024-201', 'COMPLETED', NULL),
(2, 'DPT - 2nd Dose', 4, '2024-10-20', 2, 'DPT2024-202', 'COMPLETED', NULL),
(2, 'Polio - 2nd Dose', 4, '2024-10-20', 2, 'POLIO2024-202', 'COMPLETED', NULL),
(2, 'DPT - 3rd Dose', 6, NULL, NULL, NULL, 'PENDING', 'Due soon'),
(2, 'Polio - 3rd Dose', 6, NULL, NULL, NULL, 'PENDING', 'Due soon'),

-- Sophia Brown vaccinations (all current)
(3, 'BCG', 0, '2023-01-10', 2, 'BCG2023-010', 'COMPLETED', 'Birth dose'),
(3, 'Hepatitis B - 1st Dose', 0, '2023-01-10', 2, 'HB2023-010', 'COMPLETED', 'Birth dose'),
(3, 'DPT - Complete Series', 2, '2023-07-10', 2, 'DPT2023-150', 'COMPLETED', 'All doses completed'),
(3, 'Polio - Complete Series', 2, '2023-07-10', 2, 'POLIO2023-150', 'COMPLETED', 'All doses completed'),
(3, 'MMR - 1st Dose', 12, '2024-01-10', 3, 'MMR2024-010', 'COMPLETED', NULL),
(3, 'Varicella', 12, '2024-01-10', 3, 'VAR2024-010', 'COMPLETED', NULL),

-- Liam Davis (newborn)
(6, 'BCG', 0, '2025-01-12', 2, 'BCG2025-001', 'COMPLETED', 'Birth dose'),
(6, 'Hepatitis B - 1st Dose', 0, '2025-01-12', 2, 'HB2025-001', 'COMPLETED', 'Birth dose'),
(6, 'DPT - 1st Dose', 2, NULL, NULL, NULL, 'PENDING', 'Scheduled for next month'),
(6, 'Polio - 1st Dose', 2, NULL, NULL, NULL, 'PENDING', 'Scheduled for next month');

-- ============================================================
-- Insert Some Audit Logs
-- ============================================================

INSERT INTO audit_logs (user_id, action, table_name, record_id, new_value, ip_address, user_agent) VALUES
(1, 'LOGIN', NULL, NULL, 'Admin logged in', '192.168.1.100', 'Mozilla/5.0'),
(2, 'LOGIN', NULL, NULL, 'Dr. Smith logged in', '192.168.1.101', 'Mozilla/5.0'),
(5, 'LOGIN', NULL, NULL, 'Mary Wilson logged in', '192.168.1.102', 'Mozilla/5.0'),
(5, 'CREATE', 'children', 1, 'Created child profile: Emma Wilson', '192.168.1.102', 'Mozilla/5.0'),
(2, 'CREATE', 'health_records', 1, 'Added health record for Emma Wilson', '192.168.1.101', 'Mozilla/5.0'),
(3, 'CREATE', 'appointments', 1, 'Scheduled appointment for Emma Wilson', '192.168.1.103', 'Mozilla/5.0');

-- ============================================================
-- Insert Some Notifications
-- ============================================================

INSERT INTO notifications (user_id, notification_type, title, message, is_read, related_child_id, related_appointment_id) VALUES
(5, 'APPOINTMENT_REMINDER', 'Upcoming Appointment', 'Emma Wilson has a checkup scheduled for February 15, 2025 at 10:00 AM', FALSE, 1, 1),
(5, 'VACCINATION_DUE', 'Vaccination Due', 'Oliver Wilson is due for DPT 3rd dose vaccination', FALSE, 2, NULL),
(6, 'APPOINTMENT_REMINDER', 'Appointment Confirmed', 'Sophia Brown checkup confirmed for February 10, 2025 at 9:30 AM', TRUE, 3, 3),
(7, 'GROWTH_ALERT', 'Growth Alert Resolved', 'Isabella Davis growth status has improved to normal range', TRUE, 5, NULL),
(9, 'CHECKUP_DUE', 'Monthly Checkup Due', 'Ethan Moore is due for his 11-month checkup', FALSE, 8, NULL);

-- ============================================================
-- Test Data Inserted Successfully!
-- ============================================================

-- Verify data
SELECT 'Users' AS Table_Name, COUNT(*) AS Record_Count FROM users
UNION ALL
SELECT 'Children', COUNT(*) FROM children
UNION ALL
SELECT 'Health Records', COUNT(*) FROM health_records
UNION ALL
SELECT 'Appointments', COUNT(*) FROM appointments
UNION ALL
SELECT 'Vaccinations', COUNT(*) FROM vaccinations
UNION ALL
SELECT 'Growth Alerts', COUNT(*) FROM growth_alerts
UNION ALL
SELECT 'Audit Logs', COUNT(*) FROM audit_logs
UNION ALL
SELECT 'Notifications', COUNT(*) FROM notifications;

COMMIT;
