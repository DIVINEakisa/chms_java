# QUICK START GUIDE - Testing DELETE and SEARCH BY DATE Features

## Prerequisites
1. Java 17 or higher installed
2. Maven configured
3. MySQL database running
4. Database schema and sample data loaded

---

## Step 1: Compile the Project

```bash
cd d:\Academic_Class\chms_java
mvn clean compile
```

---

## Step 2: Test DELETE Operation

### Option A: Using Test Class (Recommended for Lab Report)

```bash
# Run the delete test
mvn exec:java -Dexec.mainClass="com.chms.test.TestDeleteChild"
```

**What This Test Does:**
1. Shows all children records BEFORE deletion
2. Performs a DELETE operation on a child record
3. Shows all children records AFTER deletion
4. Verifies the deletion was successful

**Expected Output:**
```
================================================================================
CHILD HEALTH MONITORING SYSTEM - DELETE OPERATION TEST
================================================================================

✓ Database connection successful!
Database URL: jdbc:mysql://localhost:3306/chms

--------------------------------------------------------------------------------
STEP 1: VIEWING EXISTING CHILDREN RECORDS (Before Deletion)
--------------------------------------------------------------------------------
Children for Mother ID 2:

ID       Profile ID      Full Name                      Date of Birth   Gender
--------------------------------------------------------------------------------
1        CHMS-2026-001   John Doe                       2024-05-15      MALE
2        CHMS-2026-002   Jane Smith                     2023-08-20      FEMALE

Total children records: 2

--------------------------------------------------------------------------------
STEP 2: PERFORMING DELETE OPERATION
--------------------------------------------------------------------------------

Attempting to delete child:
  Child ID: 1
  Name: John Doe
  Profile ID: CHMS-2026-001

✓ Child record deleted successfully!

--------------------------------------------------------------------------------
STEP 3: VERIFICATION (Database State After Deletion)
--------------------------------------------------------------------------------

✓ VERIFICATION SUCCESSFUL: Child record no longer exists in database

Remaining children for Mother ID 2: 1

ID       Profile ID      Full Name                      Date of Birth   Gender
--------------------------------------------------------------------------------
2        CHMS-2026-002   Jane Smith                     2023-08-20      FEMALE

================================================================================
DELETE OPERATION TEST COMPLETED
================================================================================

Summary:
  - Records before deletion: 2
  - Records after deletion: 1
  - Deletion successful: true
  - Verification passed: true
```

### Option B: Using Web Interface

1. Start the server:
```bash
mvn tomcat7:run
```

2. Open browser: http://localhost:8080/chms
3. Login as a mother or admin
4. Navigate to child management
5. Click "Delete" button on a child record

---

## Step 3: Test SEARCH BY DATE Operation

### Option A: Using Test Class (Recommended for Lab Report)

```bash
# Run the search by date test
mvn exec:java -Dexec.mainClass="com.chms.test.TestSearchByDate"
```

**Interactive Menu:**
```
SELECT SEARCH TYPE:
--------------------------------------------------------------------------------
1. Search Appointments by Date
2. Search Appointments by Date Range
3. Search Children by Date of Birth
4. Search Children by Date of Birth Range
5. Search Users by Registration Date
6. Search Users by Registration Date Range
0. Exit

Enter your choice:
```

**Example: Testing Search Appointments by Date**

1. Select option 1
2. Enter date: `2026-03-15`
3. View results

**Expected Output:**
```
--------------------------------------------------------------------------------
SEARCH APPOINTMENTS BY SPECIFIC DATE
--------------------------------------------------------------------------------

Enter date (YYYY-MM-DD) [e.g., 2026-03-15]: 2026-03-15

Searching appointments for date: 2026-03-15

Found 3 appointment(s)

ID    Date         Time      Child              Doctor             Type            Status
--------------------------------------------------------------------------------
101   2026-03-15   09:00:00  John Doe          Dr. Smith          CHECKUP         SCHEDULED
102   2026-03-15   10:30:00  Jane Smith        Dr. Johnson        VACCINATION     CONFIRMED
103   2026-03-15   14:00:00  Baby Johnson      Dr. Smith          FOLLOW_UP       SCHEDULED
```

### Option B: Using Web Interface

1. Start the server (if not already running):
```bash
mvn tomcat7:run
```

2. Open browser: http://localhost:8080/chms/search-by-date
3. Login with any role
4. Select search type from dropdown
5. Enter date or date range
6. Click "Search" button
7. View results in table format

---

## Step 4: Verify in Database

### Check DELETE Operation:

```sql
-- Connect to MySQL
mysql -u root -p chms

-- View children records
SELECT child_id, unique_profile_id, full_name, date_of_birth, gender 
FROM children 
ORDER BY child_id;

-- Check if specific child was deleted
SELECT * FROM children WHERE child_id = 1;
-- Should return: Empty set (0.00 sec)
```

### Check SEARCH BY DATE:

```sql
-- Search appointments by date
SELECT a.appointment_id, a.appointment_date, a.appointment_time, 
       c.full_name as child_name, u.full_name as doctor_name
FROM appointments a
JOIN children c ON a.child_id = c.child_id
JOIN users u ON a.doctor_id = u.user_id
WHERE a.appointment_date = '2026-03-15';

-- Search children by date of birth
SELECT child_id, full_name, date_of_birth, gender
FROM children
WHERE date_of_birth = '2024-05-15';

-- Search users by registration date
SELECT user_id, full_name, email, role, DATE(created_at) as reg_date
FROM users
WHERE DATE(created_at) = '2026-02-10';
```

---

## Step 5: Taking Screenshots for Lab Report

### DELETE Operation Screenshots:

1. **Database Before Deletion:**
   ```bash
   mysql -u root -p chms
   SELECT * FROM children;
   ```
   Take screenshot of the SELECT result

2. **Java Source Code:**
   - Open: `src/main/java/com/chms/dao/ChildDAO.java`
   - Scroll to the `deleteChild()` method
   - Take screenshot

3. **Servlet Code:**
   - Open: `src/main/java/com/chms/servlet/DeleteChildServlet.java`
   - Take screenshot of the doPost() method

4. **Test Execution:**
   - Run: `mvn exec:java -Dexec.mainClass="com.chms.test.TestDeleteChild"`
   - Take screenshot of the console output

5. **Database After Deletion:**
   ```bash
   SELECT * FROM children;
   ```
   Take screenshot showing the record is gone

### SEARCH BY DATE Screenshots:

1. **Database showing Date Columns:**
   ```bash
   DESC appointments;  -- Show appointment_date column
   DESC children;      -- Show date_of_birth column
   DESC users;         -- Show created_at column
   ```
   Take screenshots

2. **Sample Data:**
   ```bash
   SELECT * FROM appointments WHERE appointment_date >= CURDATE() LIMIT 5;
   SELECT * FROM children LIMIT 5;
   SELECT user_id, full_name, created_at FROM users LIMIT 5;
   ```
   Take screenshots

3. **Java Source Code:**
   - Open: `src/main/java/com/chms/dao/AppointmentDAO.java`
   - Scroll to `searchAppointmentsByDate()` method
   - Take screenshot
   - Do the same for ChildDAO and UserDAO

4. **Servlet Code:**
   - Open: `src/main/java/com/chms/servlet/SearchByDateServlet.java`
   - Take screenshot of search methods

5. **Test Execution:**
   - Run: `mvn exec:java -Dexec.mainClass="com.chms.test.TestSearchByDate"`
   - Select option 1 (Search Appointments by Date)
   - Enter a date with existing appointments
   - Take screenshot of results
   - Repeat for options 3 and 5

6. **Web Interface:**
   - Access: http://localhost:8080/chms/search-by-date
   - Take screenshot of the search form
   - Perform a search
   - Take screenshot of the results table

---

## Step 6: Common Test Scenarios

### Scenario 1: Delete Child Record

**Purpose:** Demonstrate hard delete of a child record

**Steps:**
1. Run TestDeleteChild
2. Note the child ID being deleted
3. Verify deletion in database
4. Document in report

**Success Criteria:**
- Record removed from database
- No errors in console
- Verification shows record doesn't exist

### Scenario 2: Search Appointments by Date

**Purpose:** Find all appointments on a specific date

**Steps:**
1. Run TestSearchByDate
2. Choose option 1
3. Enter date (e.g., tomorrow's date)
4. View results

**Success Criteria:**
- All appointments for that date are shown
- No duplicates
- Related data (child name, doctor name) is included

### Scenario 3: Search Children by Date Range

**Purpose:** Find children born within a date range

**Steps:**
1. Run TestSearchByDate
2. Choose option 4
3. Enter start date: 2024-01-01
4. Enter end date: 2024-12-31
5. View results

**Success Criteria:**
- All children born in 2024 are shown
- Results are ordered by date of birth
- No records outside the range

### Scenario 4: Search Users by Registration Date

**Purpose:** Find users registered on a specific date

**Steps:**
1. Run TestSearchByDate
2. Choose option 5
3. Enter today's date
4. View results

**Success Criteria:**
- All users registered today are shown
- Email and role information is included
- Only active users or all users based on implementation

---

## Troubleshooting

### Issue 1: Database Connection Failed
**Error:** `Cannot connect to database`
**Solution:**
1. Check if MySQL is running
2. Verify database.properties file exists and is configured
3. Test connection: `mysql -u root -p chms`

### Issue 2: No Records Found
**Error:** `No appointments/children/users found`
**Solution:**
1. Check if sample data is loaded: Run `database/sample_data.sql`
2. Verify date format is correct (YYYY-MM-DD)
3. Check if dates in database match search criteria

### Issue 3: Test Class Not Found
**Error:** `ClassNotFoundException: com.chms.test.TestDeleteChild`
**Solution:**
1. Recompile: `mvn clean compile`
2. Check classpath includes compiled classes
3. Verify test class exists in correct package

### Issue 4: Servlet Returns 404
**Error:** `HTTP 404 - /search-by-date not found`
**Solution:**
1. Check if server is running
2. Verify @WebServlet annotation is correct
3. Restart server: `mvn tomcat7:run`

### Issue 5: Access Denied
**Error:** `Unauthorized access` or `Access Denied`
**Solution:**
1. Ensure you're logged in
2. Check user role permissions
3. Some searches require admin or doctor role

---

## Lab Report Checklist

### DELETE Operation Report:
- [ ] Project overview describing the delete feature
- [ ] Database schema showing children and users tables
- [ ] Database state BEFORE deletion (screenshot)
- [ ] Java code for deleteChild() method (screenshot)
- [ ] Java code for DeleteChildServlet (screenshot)
- [ ] Test execution showing deletion (screenshot)
- [ ] Database state AFTER deletion (screenshot)
- [ ] Verification results
- [ ] Challenges faced and solutions

### SEARCH BY DATE Report:
- [ ] Feature overview explaining search functionality
- [ ] Database schema showing date columns (screenshot)
- [ ] Sample data with different dates (screenshot)
- [ ] Java code for search methods in DAOs (screenshots)
- [ ] Java code for SearchByDateServlet (screenshot)
- [ ] Test execution for appointments search (screenshot)
- [ ] Test execution for children search (screenshot)
- [ ] Test execution for users search (screenshot)
- [ ] Web interface screenshots (optional)
- [ ] Explanation of SQL queries used
- [ ] How date parameters are passed
- [ ] How results are displayed
- [ ] Challenges faced and solutions

---

## Additional Resources

- Full Documentation: `DELETE_AND_SEARCH_DOCUMENTATION.md`
- Database Schema: `database/schema.sql`
- Sample Data: `database/sample_data.sql`
- Project README: `README.md`

---

## Contact & Support

If you encounter issues:
1. Check the troubleshooting section above
2. Review the full documentation
3. Check console logs for error messages
4. Verify database connection and data

---

**Last Updated:** February 2026
**Project:** Child Health Monitoring System (CHMS)
**Java Version:** 17
**Database:** MySQL 8.0
