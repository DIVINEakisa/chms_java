# Child Health Monitoring System - ER Diagram Documentation

## Entity Relationship Diagram

### Database Schema Overview

The CHMS database consists of 8 main entities with carefully designed relationships to ensure data integrity and efficient querying.

## 📊 ER Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CHMS Database Schema                                 │
└─────────────────────────────────────────────────────────────────────────────┘

                                    ┌──────────────┐
                                    │    users     │
                                    ├──────────────┤
                                    │ user_id (PK) │
                                    │ email (UK)   │
                                    │ password_hash│
                                    │ full_name    │
                                    │ phone_number │
                                    │ role [ENUM]  │
                                    │ is_active    │
                                    │ created_at   │
                                    │ updated_at   │
                                    │ last_login   │
                                    └──────┬───────┘
                                           │
                        ┌──────────────────┼──────────────────┐
                        │                  │                  │
                   [MOTHER]           [DOCTOR]           [DOCTOR]
                        │                  │                  │
                        │                  │                  │
         ┌──────────────▼──────────┐      │      ┌───────────▼──────────┐
         │      children           │      │      │   health_records     │
         ├─────────────────────────┤      │      ├──────────────────────┤
         │ child_id (PK)           │      │      │ record_id (PK)       │
         │ unique_profile_id (UK)  │      │      │ child_id (FK) ───────┼───┐
         │ full_name               │      │      │ doctor_id (FK) ──────┼───┘
         │ date_of_birth           │      │      │ assessment_date      │
         │ gender [ENUM]           │      │      │ child_age_months     │
         │ birth_weight            │      │      │ weight               │
         │ birth_height            │      │      │ height               │
         │ blood_group             │      │      │ bmi [COMPUTED]       │
         │ mother_id (FK) ─────────┼──────┘      │ head_circumference   │
         │ father_name             │             │ temperature          │
         │ father_phone            │             │ growth_status [ENUM] │
         │ emergency_contact       │             │ vaccination_status   │
         │ address                 │             │ nutrition_notes      │
         │ medical_history         │             │ health_notes         │
         │ is_active               │             │ abnormalities        │
         │ created_at              │             │ next_checkup_date    │
         │ updated_at              │             │ created_at           │
         └────────┬────────────────┘             │ updated_at           │
                  │                               └──────────┬───────────┘
                  │                                          │
         ┌────────┼────────────┬──────────────┐            │
         │        │            │              │            │
         │        │            │              │            │
   ┌─────▼────────▼──┐  ┌─────▼──────────┐  │  ┌─────────▼──────────┐
   │  appointments   │  │  vaccinations  │  │  │   growth_alerts    │
   ├─────────────────┤  ├────────────────┤  │  ├────────────────────┤
   │appointment_id(PK│  │vaccination_id  │  │  │ alert_id (PK)      │
   │child_id (FK)────┼──┘│ (PK)           │  │  │ child_id (FK) ─────┼──┐
   │doctor_id (FK)   │   │child_id (FK)───┼──┘  │ record_id (FK) ────┼──┘
   │appointment_date │   │vaccine_name    │     │ alert_type [ENUM]  │
   │appointment_time │   │recommended_age │     │ severity [ENUM]    │
   │type [ENUM]      │   │administered_date│    │ description        │
   │status [ENUM]    │   │administered_by │     │ is_resolved        │
   │notes            │   │batch_number    │     │ resolved_date      │
   │created_at       │   │status [ENUM]   │     │ resolved_by (FK)   │
   │updated_at       │   │notes           │     │ created_at         │
   └─────────────────┘   │created_at      │     └────────────────────┘
                         │updated_at      │
                         └────────────────┘

         ┌─────────────────────┐            ┌──────────────────────┐
         │    audit_logs       │            │   notifications      │
         ├─────────────────────┤            ├──────────────────────┤
         │ log_id (PK)         │            │ notification_id (PK) │
         │ user_id (FK)        │            │ user_id (FK)         │
         │ action              │            │ type [ENUM]          │
         │ table_name          │            │ title                │
         │ record_id           │            │ message              │
         │ old_value           │            │ is_read              │
         │ new_value           │            │ related_child_id(FK) │
         │ ip_address          │            │ related_appt_id (FK) │
         │ user_agent          │            │ created_at           │
         │ created_at          │            │ read_at              │
         └─────────────────────┘            └──────────────────────┘

Legend:
PK = Primary Key
FK = Foreign Key
UK = Unique Key
[ENUM] = Enumeration type
[COMPUTED] = Computed/Generated column
```

## 📋 Entity Descriptions

### 1. **users** (System Users)
**Purpose:** Store all system users including mothers, doctors, and administrators

**Key Attributes:**
- `user_id`: Auto-incrementing primary key
- `email`: Unique email for login
- `password_hash`: BCrypt hashed password
- `role`: ENUM('MOTHER', 'DOCTOR', 'ADMIN')
- `is_active`: Soft delete flag

**Relationships:**
- One-to-Many with `children` (as mother)
- One-to-Many with `health_records` (as doctor)
- One-to-Many with `appointments` (as doctor)
- One-to-Many with `audit_logs`
- One-to-Many with `notifications`

### 2. **children** (Child Profiles)
**Purpose:** Store information about registered children

**Key Attributes:**
- `child_id`: Auto-incrementing primary key
- `unique_profile_id`: System-generated unique identifier (e.g., CH2023001234)
- `date_of_birth`: For age calculation
- `gender`: ENUM('MALE', 'FEMALE', 'OTHER')
- `mother_id`: Foreign key to users table

**Relationships:**
- Many-to-One with `users` (mother)
- One-to-Many with `health_records`
- One-to-Many with `appointments`
- One-to-Many with `vaccinations`
- One-to-Many with `growth_alerts`

**Business Rules:**
- Each child must have a mother (cannot be NULL)
- Unique profile ID is auto-generated via trigger
- Mother must have role = 'MOTHER'

### 3. **health_records** (Health Assessments)
**Purpose:** Store monthly health assessments by doctors

**Key Attributes:**
- `record_id`: Auto-incrementing primary key
- `child_id`: Foreign key to children
- `doctor_id`: Foreign key to users (doctor)
- `bmi`: Auto-calculated as weight/(height/100)²
- `growth_status`: ENUM('NORMAL', 'UNDERWEIGHT', 'OVERWEIGHT', 'STUNTED', 'WASTED')

**Relationships:**
- Many-to-One with `children`
- Many-to-One with `users` (doctor)
- One-to-Many with `growth_alerts`

**Business Rules:**
- BMI is automatically calculated (generated column)
- Growth alerts are automatically created via trigger if abnormal
- Doctor must have role = 'DOCTOR'

### 4. **appointments** (Checkup Scheduling)
**Purpose:** Manage scheduled and completed appointments

**Key Attributes:**
- `appointment_type`: ENUM('ROUTINE_CHECKUP', 'VACCINATION', 'FOLLOW_UP', 'EMERGENCY')
- `status`: ENUM('SCHEDULED', 'CONFIRMED', 'COMPLETED', 'CANCELLED', 'NO_SHOW')

**Relationships:**
- Many-to-One with `children`
- Many-to-One with `users` (doctor - optional)

**Business Rules:**
- Notifications are auto-created when appointment is scheduled
- Doctor assignment is optional (can be assigned later)

### 5. **vaccinations** (Vaccination Tracking)
**Purpose:** Track scheduled and administered vaccinations

**Key Attributes:**
- `recommended_age_months`: When vaccine should be given
- `status`: ENUM('PENDING', 'COMPLETED', 'MISSED', 'DELAYED')
- `batch_number`: Vaccine batch for tracking

**Relationships:**
- Many-to-One with `children`
- Many-to-One with `users` (administered_by - doctor)

**Business Rules:**
- Status automatically updated when administered
- Batch number recorded for safety tracking

### 6. **growth_alerts** (Health Alerts)
**Purpose:** Track and manage growth abnormalities

**Key Attributes:**
- `alert_type`: ENUM('UNDERWEIGHT', 'OVERWEIGHT', 'STUNTED', 'RAPID_WEIGHT_LOSS', 'RAPID_WEIGHT_GAIN', 'NO_GROWTH')
- `severity`: ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL')
- `is_resolved`: Flag for alert resolution

**Relationships:**
- Many-to-One with `children`
- Many-to-One with `health_records`
- Many-to-One with `users` (resolved_by - doctor)

**Business Rules:**
- Automatically created by trigger when abnormal growth detected
- Can only be resolved by doctors
- Linked to specific health record that triggered it

### 7. **audit_logs** (Security Logs)
**Purpose:** Track all critical operations for security and compliance

**Key Attributes:**
- `action`: Type of operation (LOGIN, LOGOUT, CREATE, UPDATE, DELETE)
- `table_name`: Affected database table
- `old_value` / `new_value`: For tracking changes
- `ip_address`: Client IP for security
- `user_agent`: Browser information

**Relationships:**
- Many-to-One with `users`

**Business Rules:**
- Created automatically for all critical operations
- Cannot be deleted (append-only)
- Includes system-level actions (user_id can be NULL)

### 8. **notifications** (User Notifications)
**Purpose:** Store notifications for users

**Key Attributes:**
- `type`: ENUM('APPOINTMENT_REMINDER', 'VACCINATION_DUE', 'CHECKUP_DUE', 'GROWTH_ALERT', 'SYSTEM')
- `is_read`: Track notification status
- `related_child_id`: Link to child (optional)
- `related_appointment_id`: Link to appointment (optional)

**Relationships:**
- Many-to-One with `users`
- Many-to-One with `children` (optional)
- Many-to-One with `appointments` (optional)

**Business Rules:**
- Auto-created by triggers (e.g., new appointment)
- Can be marked as read
- Timestamp captured when read

## 🔗 Key Relationships

### 1. **User → Children** (One-to-Many)
- A mother can have multiple children
- Each child has exactly one mother
- Constraint: `mother_id` REFERENCES `users(user_id)`
- On Delete: RESTRICT (cannot delete mother with children)

### 2. **Child → Health Records** (One-to-Many)
- A child can have multiple health assessments
- Each record belongs to one child
- Constraint: `child_id` REFERENCES `children(child_id)`
- On Delete: CASCADE (delete records if child deleted)

### 3. **Doctor → Health Records** (One-to-Many)
- A doctor can create multiple health records
- Each record has one doctor
- Constraint: `doctor_id` REFERENCES `users(user_id)`
- On Delete: RESTRICT (cannot delete doctor with records)

### 4. **Child → Appointments** (One-to-Many)
- A child can have multiple appointments
- Each appointment is for one child
- Constraint: `child_id` REFERENCES `children(child_id)`
- On Delete: CASCADE

### 5. **Health Record → Growth Alerts** (One-to-Many)
- A health record can trigger multiple alerts
- Each alert links to one record
- Constraint: `record_id` REFERENCES `health_records(record_id)`
- On Delete: CASCADE

## 🎯 Database Constraints

### Primary Keys
- All tables have auto-incrementing integer primary keys
- Ensures unique identification of each record

### Foreign Keys
- Enforce referential integrity
- Prevent orphaned records
- Define cascade behaviors

### Unique Constraints
- `users.email`: Prevent duplicate accounts
- `children.unique_profile_id`: Unique child identifier

### Check Constraints
- Weight, height, BMI must be positive
- Temperature within reasonable range (35-42°C)
- Dates validated for logical consistency

### Default Values
- `is_active` defaults to TRUE
- `created_at` defaults to CURRENT_TIMESTAMP
- Enum fields have sensible defaults

## 📈 Database Views

### view_children_with_mothers
Joins children with mother information and calculates age

### view_latest_health_records
Shows the most recent health record for each child

### view_pending_appointments
Lists upcoming appointments with child and mother details

### view_active_alerts
Shows unresolved growth alerts with child information

## 🔐 Security Considerations

1. **Password Storage:** Never store plain text passwords
2. **Audit Trail:** All critical operations logged
3. **Soft Deletes:** `is_active` flags instead of hard deletes
4. **Timestamps:** Track creation and modification times
5. **Foreign Keys:** Prevent data inconsistencies

## 📊 Indexing Strategy

### Indexed Columns
- All primary keys (automatic)
- All foreign keys (for join performance)
- `users.email` (frequent lookups)
- `children.mother_id` (mother's children queries)
- `health_records.child_id` (child's health history)
- `appointments.appointment_date` (date-based queries)
- `audit_logs.created_at` (time-based queries)

## 🎓 Normalization

The database is designed in **3rd Normal Form (3NF)**:

✅ **1NF:** All attributes contain atomic values
✅ **2NF:** No partial dependencies
✅ **3NF:** No transitive dependencies

This ensures:
- Minimal data redundancy
- Data integrity
- Efficient updates
- Scalable design

---

This ER diagram represents a professional, production-ready database schema suitable for a real-world child health monitoring system.
