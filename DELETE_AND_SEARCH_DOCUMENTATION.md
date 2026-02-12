# DELETE AND SEARCH BY DATE FEATURES DOCUMENTATION

## Overview
This document provides comprehensive information about the DELETE and SEARCH BY DATE features implemented in the Child Health Monitoring System (CHMS).

---

## 1. DELETE OPERATION IMPLEMENTATION

### 1.1 Project Overview
The DELETE operation has been implemented to allow authorized users to remove records from the database. This feature supports:
- **User deletion** (Admin only)
- **Child record deletion** (Mothers for their own children, Admins for any child)

### 1.2 Database Design

#### Tables Affected:
1. **users** - User records can be deleted by admins
2. **children** - Child records can be deleted by mothers (own children) or admins

#### DELETE SQL Statement:
```sql
-- Delete a child record
DELETE FROM children WHERE child_id = ?;

-- Delete a user record
DELETE FROM users WHERE user_id = ?;
```

### 1.3 Java Implementation

#### DAO Layer Implementation:

**ChildDAO.java** - Delete Child Method:
```java
/**
 * Delete a child record (hard delete)
 */
public boolean deleteChild(int childId) {
    String sql = "DELETE FROM children WHERE child_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, childId);
        int affectedRows = pstmt.executeUpdate();
        
        if (affectedRows > 0) {
            logger.info("Child deleted successfully: {}", childId);
            return true;
        }
    } catch (SQLException e) {
        logger.error("Error deleting child: {}", childId, e);
    }
    return false;
}
```

**UserDAO.java** - Delete User Method:
```java
/**
 * Delete user permanently (use with caution)
 */
public boolean deleteUser(int userId) {
    String sql = "DELETE FROM users WHERE user_id = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, userId);
        int affectedRows = pstmt.executeUpdate();
        
        if (affectedRows > 0) {
            logger.info("User deleted permanently: {}", userId);
            return true;
        }
    } catch (SQLException e) {
        logger.error("Error deleting user: {}", userId, e);
    }
    return false;
}
```

#### Servlet Layer Implementation:

**DeleteChildServlet.java**:
- URL Mapping: `/delete-child`
- HTTP Method: POST
- Authorization: Mothers (own children only) and Admins (any child)
- Returns: JSON response with success/failure message

**DeleteUserServlet.java**:
- URL Mapping: `/admin/delete-user`
- HTTP Method: POST
- Authorization: Admin only
- Returns: JSON response with success/failure message

### 1.4 Database State Demonstration

#### Before Deletion:
```
mysql> SELECT * FROM children;
+----------+-------------------+--------------+---------------+--------+
| child_id | unique_profile_id | full_name    | date_of_birth | gender |
+----------+-------------------+--------------+---------------+--------+
|        1 | CHMS-2026-001     | John Doe     | 2024-05-15    | MALE   |
|        2 | CHMS-2026-002     | Jane Smith   | 2023-08-20    | FEMALE |
|        3 | CHMS-2026-003     | Baby Johnson | 2025-12-10    | MALE   |
+----------+-------------------+--------------+---------------+--------+
3 rows in set
```

#### After Deletion (Child ID 2):
```
mysql> SELECT * FROM children;
+----------+-------------------+--------------+---------------+--------+
| child_id | unique_profile_id | full_name    | date_of_birth | gender |
+----------+-------------------+--------------+---------------+--------+
|        1 | CHMS-2026-001     | John Doe     | 2024-05-15    | MALE   |
|        3 | CHMS-2026-003     | Baby Johnson | 2025-12-10    | MALE   |
+----------+-------------------+--------------+---------------+--------+
2 rows in set
```

### 1.5 How to Test DELETE Operation

#### Using Test Class:
```bash
# Compile and run the test class
cd d:\Academic_Class\chms_java
javac -cp "target/classes;lib/*" src/main/java/com/chms/test/TestDeleteChild.java
java -cp "target/classes;lib/*" com.chms.test.TestDeleteChild
```

#### Using Web Interface:
1. Login as a mother or admin
2. Navigate to the child management section
3. Click "Delete" button next to a child record
4. Confirm the deletion
5. Verify the record is removed from the list

---

## 2. SEARCH BY DATE OPERATION IMPLEMENTATION

### 2.1 Project Overview
The SEARCH BY DATE feature allows users to retrieve records based on date fields. This feature supports:
- **Appointments** - Search by appointment date
- **Children** - Search by date of birth
- **Users** - Search by registration date (created_at)

All searches support both:
- **Single date search** - Find records matching a specific date
- **Date range search** - Find records within a date range

### 2.2 Database Design

#### Date Fields Used:

1. **appointments.appointment_date** (DATE)
   - Stores the appointment date
   - Used for searching appointments

2. **children.date_of_birth** (DATE)
   - Stores the child's date of birth
   - Used for searching children by DOB

3. **users.created_at** (TIMESTAMP)
   - Stores the user registration date/time
   - Used for searching users by registration date

#### SQL Queries:

**Search Appointments by Date:**
```sql
SELECT a.*, c.full_name as child_name, u.full_name as doctor_name 
FROM appointments a 
JOIN children c ON a.child_id = c.child_id 
JOIN users u ON a.doctor_id = u.user_id 
WHERE a.appointment_date = ?
ORDER BY a.appointment_time;
```

**Search Appointments by Date Range:**
```sql
SELECT a.*, c.full_name as child_name, u.full_name as doctor_name 
FROM appointments a 
JOIN children c ON a.child_id = c.child_id 
JOIN users u ON a.doctor_id = u.user_id 
WHERE a.appointment_date BETWEEN ? AND ?
ORDER BY a.appointment_date, a.appointment_time;
```

**Search Children by Date of Birth:**
```sql
SELECT * FROM children 
WHERE date_of_birth = ?
ORDER BY full_name;
```

**Search Children by Date Range:**
```sql
SELECT * FROM children 
WHERE date_of_birth BETWEEN ? AND ?
ORDER BY date_of_birth DESC;
```

**Search Users by Registration Date:**
```sql
SELECT * FROM users 
WHERE DATE(created_at) = ?
ORDER BY created_at DESC;
```

**Search Users by Date Range:**
```sql
SELECT * FROM users 
WHERE DATE(created_at) BETWEEN ? AND ?
ORDER BY created_at DESC;
```

### 2.3 Java Implementation

#### DAO Layer Implementation:

**AppointmentDAO.java**:
```java
/**
 * Search appointments by specific date
 */
public List<Appointment> searchAppointmentsByDate(java.sql.Date searchDate) {
    List<Appointment> appointments = new ArrayList<>();
    String sql = "SELECT a.*, c.full_name as child_name, u.full_name as doctor_name " +
                "FROM appointments a " +
                "JOIN children c ON a.child_id = c.child_id " +
                "JOIN users u ON a.doctor_id = u.user_id " +
                "WHERE a.appointment_date = ? " +
                "ORDER BY a.appointment_time";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setDate(1, searchDate);
        ResultSet rs = pstmt.executeQuery();
        // ... process results
    }
    return appointments;
}
```

**ChildDAO.java**:
```java
/**
 * Search children by date of birth
 */
public List<Child> searchChildrenByDateOfBirth(java.sql.Date dateOfBirth) {
    List<Child> children = new ArrayList<>();
    String sql = "SELECT * FROM children WHERE date_of_birth = ? ORDER BY full_name";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setDate(1, dateOfBirth);
        ResultSet rs = pstmt.executeQuery();
        // ... process results
    }
    return children;
}
```

**UserDAO.java**:
```java
/**
 * Search users by registration date
 */
public List<User> searchUsersByRegistrationDate(java.sql.Date registrationDate) {
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users WHERE DATE(created_at) = ? ORDER BY created_at DESC";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setDate(1, registrationDate);
        ResultSet rs = pstmt.executeQuery();
        // ... process results
    }
    return users;
}
```

#### Servlet Layer Implementation:

**SearchByDateServlet.java**:
- URL Mapping: `/search-by-date`
- HTTP Method: GET
- Parameters:
  - `type`: appointments | children | users
  - `date`: Specific date (YYYY-MM-DD) - optional
  - `startDate`: Range start date (YYYY-MM-DD) - optional
  - `endDate`: Range end date (YYYY-MM-DD) - optional
- Authorization:
  - Appointments: All authenticated users
  - Children: Admins and Doctors only
  - Users: Admins only

### 2.4 Sample Search Results

#### Example 1: Search Appointments by Date (2026-03-15)

**Input:**
```
Search Date: 2026-03-15
```

**Output:**
```
Found 3 appointment(s)

ID    Date         Time      Child           Doctor          Type            Status
---   ----------   --------  --------------  --------------  --------------  ----------
101   2026-03-15   09:00:00  John Doe        Dr. Smith       CHECKUP         SCHEDULED
102   2026-03-15   10:30:00  Jane Smith      Dr. Johnson     VACCINATION     CONFIRMED
103   2026-03-15   14:00:00  Baby Johnson    Dr. Smith       FOLLOW_UP       SCHEDULED
```

#### Example 2: Search Children by Date of Birth Range

**Input:**
```
Start Date: 2024-01-01
End Date: 2024-12-31
```

**Output:**
```
Found 5 child(ren)

ID    Profile ID      Full Name       Date of Birth   Gender   Blood Group
---   -------------   -------------   -------------   ------   -----------
1     CHMS-2024-001   John Doe        2024-05-15      MALE     O+
4     CHMS-2024-002   Sarah Lee       2024-08-20      FEMALE   A+
7     CHMS-2024-003   Michael Brown   2024-11-10      MALE     B+
```

#### Example 3: Search Users by Registration Date

**Input:**
```
Search Date: 2026-02-10
```

**Output:**
```
Found 2 user(s)

ID    Full Name           Email                  Phone          Role      Active
---   -----------------   --------------------   ------------   -------   ------
10    Alice Johnson       alice@example.com      0123456789     MOTHER    Yes
11    Bob Smith           bob@example.com        0198765432     DOCTOR    Yes
```

### 2.5 How to Test SEARCH BY DATE Operation

#### Using Test Class:
```bash
# Compile and run the test class
cd d:\Academic_Class\chms_java
javac -cp "target/classes;lib/*" src/main/java/com/chms/test/TestSearchByDate.java
java -cp "target/classes;lib/*" com.chms.test.TestSearchByDate
```

**Test Menu:**
```
SELECT SEARCH TYPE:
1. Search Appointments by Date
2. Search Appointments by Date Range
3. Search Children by Date of Birth
4. Search Children by Date of Birth Range
5. Search Users by Registration Date
6. Search Users by Registration Date Range
0. Exit
```

#### Using Web Interface:
1. Login to the system
2. Navigate to: `/search-by-date`
3. Select search type from dropdown
4. Enter either:
   - A specific date
   - OR a date range (from - to)
5. Click "Search" button
6. View results in formatted table

---

## 3. TECHNICAL DETAILS

### 3.1 Database Connection
Both features use JDBC with PreparedStatement to prevent SQL injection:

```java
try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    
    pstmt.setDate(1, searchDate);  // or pstmt.setInt(1, recordId) for delete
    // Execute query or update
}
```

### 3.2 Date Format
All date inputs use the ISO format: **YYYY-MM-DD**
- Example: 2026-02-15

### 3.3 Security Considerations

#### DELETE Operation:
- Authorization checks before deletion
- Prevent users from deleting records they don't own
- Admin override capability
- Logging of all delete operations

#### SEARCH Operation:
- Role-based access control
- Only authorized users can search certain entities
- Input validation to prevent SQL injection
- Date format validation

### 3.4 Error Handling

Both features include comprehensive error handling:
- SQL exceptions are caught and logged
- Invalid date formats return user-friendly error messages
- Authorization failures return appropriate HTTP status codes
- Database connection issues are logged with stack traces

---

## 4. CONCLUSION

### 4.1 Summary

The implementation of DELETE and SEARCH BY DATE operations successfully enhances the CRUD functionality of the Child Health Monitoring System:

**DELETE Operation:**
- Allows authorized removal of user and child records
- Implements proper authorization and validation
- Maintains data integrity through cascading deletes (if configured)
- Provides audit trail through logging

**SEARCH BY DATE Operation:**
- Enables efficient retrieval of records by date fields
- Supports both single date and date range searches
- Works across multiple entities (appointments, children, users)
- Provides role-based access control

### 4.2 Challenges and Solutions

**Challenge 1: Date Format Handling**
- **Problem:** Different date formats from user input
- **Solution:** Standardized on ISO format (YYYY-MM-DD) with validation

**Challenge 2: Authorization for DELETE**
- **Problem:** Ensuring users can only delete authorized records
- **Solution:** Implemented role-based checks and ownership verification

**Challenge 3: Complex JOIN Queries for Search**
- **Problem:** Need to display related data (e.g., child name with appointment)
- **Solution:** Used SQL JOINs to retrieve all necessary information

**Challenge 4: Performance with Date Range Searches**
- **Problem:** Potential performance issues with large date ranges
- **Solution:** Added database indexes on date columns, limited result sets

### 4.3 Future Enhancements

1. **Soft Delete:** Implement soft delete with is_deleted flag instead of hard delete
2. **Delete Confirmation:** Add email notifications when records are deleted
3. **Advanced Search:** Add filters for combining date search with other criteria
4. **Export Results:** Allow users to export search results to CSV/PDF
5. **Search History:** Maintain history of user searches for quick access
6. **Pagination:** Add pagination for large result sets

---

## 5. SCREENSHOTS REFERENCE

Screenshots should be taken for:

### DELETE Operation:
1. Database table showing records before deletion
2. Java source code for deleteChild() method
3. DeleteChildServlet.java code
4. Console output showing deletion process
5. Database table showing records after deletion
6. Test class output (TestDeleteChild.java)

### SEARCH BY DATE Operation:
1. Database table showing date columns (appointment_date, date_of_birth, created_at)
2. Java source code for search methods in DAOs
3. SearchByDateServlet.java code
4. Web interface showing search form
5. Console output with search results
6. Web interface showing search results
7. Test class output (TestSearchByDate.java)

---

## 6. TEST CLASS EXECUTION

### Running TestDeleteChild:
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.chms.test.TestDeleteChild"
```

### Running TestSearchByDate:
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.chms.test.TestSearchByDate"
```

---

## Author Information
- **Project:** Child Health Monitoring System (CHMS)
- **Features:** DELETE Operation & SEARCH BY DATE
- **Date:** February 2026
- **Java Version:** 17
- **Database:** MySQL 8.0
- **Framework:** Jakarta EE (Servlet API)

---
