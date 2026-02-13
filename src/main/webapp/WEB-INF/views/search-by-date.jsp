<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.chms.model.User" %>
<%@ page import="com.chms.model.Child" %>
<%@ page import="com.chms.model.Appointment" %>
<%@ page import="com.chms.util.SessionManager" %>
<%
    User loggedInUser = SessionManager.getLoggedInUser(request);
    if (loggedInUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    String searchType = (String) request.getAttribute("searchType");
    if (searchType == null) searchType = "appointments";
    
    String error = (String) request.getAttribute("error");
    Integer resultCount = (Integer) request.getAttribute("resultCount");
    String searchMode = (String) request.getAttribute("searchMode");
    
    List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
    List<Child> children = (List<Child>) request.getAttribute("children");
    List<User> users = (List<User>) request.getAttribute("users");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search by Date - CHMS</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            padding: 30px;
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
        }
        
        .nav-links {
            margin-bottom: 20px;
        }
        
        .nav-links a {
            color: #667eea;
            text-decoration: none;
            margin-right: 15px;
        }
        
        .nav-links a:hover {
            text-decoration: underline;
        }
        
        .search-form {
            background: #f8f9fa;
            padding: 25px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
            font-weight: 500;
        }
        
        select, input[type="date"] {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        
        .date-inputs {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 15px;
        }
        
        .or-divider {
            text-align: center;
            margin: 20px 0;
            color: #666;
            font-weight: 500;
        }
        
        button {
            background: #667eea;
            color: white;
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background 0.3s;
        }
        
        button:hover {
            background: #5568d3;
        }
        
        .alert {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-error {
            background: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }
        
        .alert-success {
            background: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }
        
        .results-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            padding-bottom: 10px;
            border-bottom: 2px solid #667eea;
        }
        
        .results-header h2 {
            color: #333;
        }
        
        .result-count {
            background: #667eea;
            color: white;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 14px;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            background: #f8f9fa;
            color: #333;
            font-weight: 600;
        }
        
        tr:hover {
            background: #f8f9fa;
        }
        
        .no-results {
            text-align: center;
            padding: 40px;
            color: #666;
        }
        
        .badge {
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            font-weight: 500;
        }
        
        .badge-scheduled {
            background: #fff3cd;
            color: #856404;
        }
        
        .badge-confirmed {
            background: #d1ecf1;
            color: #0c5460;
        }
        
        .badge-completed {
            background: #d4edda;
            color: #155724;
        }
        
        .badge-cancelled {
            background: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="nav-links">
            <a href="<%= request.getContextPath() %>/<%= loggedInUser.getRole().name().toLowerCase() %>/dashboard">← Back to Dashboard</a>
        </div>
        
        <h1>🔍 Advanced Search</h1>
        <p class="subtitle">Search appointments, children, or users by date, time, and name</p>
        
        <% if (error != null) { %>
            <div class="alert alert-error">
                <strong>Error:</strong> <%= error %>
            </div>
        <% } %>
        
        <div class="search-form">
            <form method="get" action="<%= request.getContextPath() %>/search-by-date">
                <div class="form-group">
                    <label for="type">Search Type:</label>
                    <select name="type" id="type" required>
                        <option value="appointments" <%= "appointments".equals(searchType) ? "selected" : "" %>>
                            Appointments by Date
                        </option>
                        <% if (loggedInUser.getRole().equals(User.Role.ADMIN) || 
                               loggedInUser.getRole().equals(User.Role.DOCTOR)) { %>
                            <option value="children" <%= "children".equals(searchType) ? "selected" : "" %>>
                                Children by Date of Birth
                            </option>
                        <% } %>
                        <% if (loggedInUser.getRole().equals(User.Role.ADMIN)) { %>
                            <option value="users" <%= "users".equals(searchType) ? "selected" : "" %>>
                                Users by Registration Date
                            </option>
                        <% } %>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="date">Search by Specific Date:</label>
                    <input type="date" name="date" id="date" 
                           value="<%= request.getAttribute("searchDate") != null ? request.getAttribute("searchDate") : "" %>">
                </div>
                
                <div class="or-divider">- OR -</div>
                
                <div class="form-group">
                    <label>Search by Date Range:</label>
                    <div class="date-inputs">
                        <div>
                            <label for="startDate">From:</label>
                            <input type="date" name="startDate" id="startDate"
                                   value="<%= request.getAttribute("startDate") != null ? request.getAttribute("startDate") : "" %>">
                        </div>
                        <div>
                            <label for="endDate">To:</label>
                            <input type="date" name="endDate" id="endDate"
                                   value="<%= request.getAttribute("endDate") != null ? request.getAttribute("endDate") : "" %>">
                        </div>
                    </div>
                </div>
                
                <!-- Additional Filters -->
                <div style="border-top: 2px solid #ddd; margin: 25px 0; padding-top: 20px;">
                    <h3 style="color: #667eea; margin-bottom: 15px;">📋 Additional Filters (Optional)</h3>
                    
                    <div class="form-group">
                        <label for="name">Filter by Name:</label>
                        <input type="text" name="name" id="name" placeholder="Enter name to filter (child, mother, doctor, or user name)"
                               style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;"
                               value="<%= request.getAttribute("nameFilter") != null ? request.getAttribute("nameFilter") : "" %>">
                        <small style="color: #666;">Enter any part of the name to filter results</small>
                    </div>
                    
                    <div class="form-group" id="timeFilterGroup" style="<%= !"appointments".equals(searchType) ? "display:none;" : "" %>">
                        <label>Filter by Time Range (for appointments only):</label>
                        <div class="date-inputs">
                            <div>
                                <label for="startTime">From Time:</label>
                                <input type="time" name="startTime" id="startTime"
                                       style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;"
                                       value="<%= request.getAttribute("startTime") != null ? request.getAttribute("startTime") : "" %>">
                            </div>
                            <div>
                                <label for="endTime">To Time:</label>
                                <input type="time" name="endTime" id="endTime"
                                       style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;"
                                       value="<%= request.getAttribute("endTime") != null ? request.getAttribute("endTime") : "" %>">
                            </div>
                        </div>
                        <small style="color: #666;">Leave empty to show all appointments regardless of time</small>
                    </div>
                </div>
                
                <button type="submit">🔍 Search with Filters</button>
            </form>
        </div>
        
        <script>
            // Show/hide time filter based on search type
            document.getElementById('type').addEventListener('change', function() {
                var timeFilterGroup = document.getElementById('timeFilterGroup');
                if (this.value === 'appointments') {
                    timeFilterGroup.style.display = 'block';
                } else {
                    timeFilterGroup.style.display = 'none';
                }
            });
        </script>
        
        <% if (resultCount != null) { %>
            <div class="results-header">
                <h2>Search Results</h2>
                <span class="result-count"><%= resultCount %> record(s) found</span>
            </div>
            
            <!-- Show active filters -->
            <div style="background: #f0f8ff; padding: 15px; border-radius: 5px; margin-bottom: 20px; border-left: 4px solid #667eea;">
                <strong>Active Filters:</strong> 
                <% if (request.getAttribute("searchDate") != null && !request.getAttribute("searchDate").toString().isEmpty()) { %>
                    <span style="background: #667eea; color: white; padding: 4px 10px; border-radius: 15px; margin: 0 5px; font-size: 13px;">
                        📅 Date: <%= request.getAttribute("searchDate") %>
                    </span>
                <% } %>
                <% if (request.getAttribute("startDate") != null && !request.getAttribute("startDate").toString().isEmpty()) { %>
                    <span style="background: #667eea; color: white; padding: 4px 10px; border-radius: 15px; margin: 0 5px; font-size: 13px;">
                        📅 <%= request.getAttribute("startDate") %> to <%= request.getAttribute("endDate") %>
                    </span>
                <% } %>
                <% if (request.getAttribute("nameFilter") != null && !request.getAttribute("nameFilter").toString().isEmpty()) { %>
                    <span style="background: #28a745; color: white; padding: 4px 10px; border-radius: 15px; margin: 0 5px; font-size: 13px;">
                        👤 Name: <%= request.getAttribute("nameFilter") %>
                    </span>
                <% } %>
                <% if (request.getAttribute("startTime") != null && !request.getAttribute("startTime").toString().isEmpty()) { %>
                    <span style="background: #ffc107; color: #333; padding: 4px 10px; border-radius: 15px; margin: 0 5px; font-size: 13px;">
                        🕐 Time: <%= request.getAttribute("startTime") %> - <%= request.getAttribute("endTime") %>
                    </span>
                <% } %>
            </div>
            
            <% if ("appointments".equals(searchType) && appointments != null) { %>
                <% if (appointments.isEmpty()) { %>
                    <div class="no-results">No appointments found for the selected date(s).</div>
                <% } else { %>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Child</th>
                                <th>Doctor</th>
                                <th>Mother</th>
                                <th>Type</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Appointment apt : appointments) { %>
                                <tr>
                                    <td><%= apt.getAppointmentId() %></td>
                                    <td><%= apt.getAppointmentDate() %></td>
                                    <td><%= apt.getAppointmentTime() %></td>
                                    <td><%= apt.getChildName() %></td>
                                    <td><%= apt.getDoctorName() %></td>
                                    <td><%= apt.getMotherName() %></td>
                                    <td><%= apt.getAppointmentType() %></td>
                                    <td>
                                        <span class="badge badge-<%= apt.getStatus().name().toLowerCase() %>">
                                            <%= apt.getStatus() %>
                                        </span>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            <% } %>
            
            <% if ("children".equals(searchType) && children != null) { %>
                <% if (children.isEmpty()) { %>
                    <div class="no-results">No children found for the selected date(s).</div>
                <% } else { %>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Profile ID</th>
                                <th>Full Name</th>
                                <th>Date of Birth</th>
                                <th>Gender</th>
                                <th>Blood Group</th>
                                <th>Mother ID</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Child child : children) { %>
                                <tr>
                                    <td><%= child.getChildId() %></td>
                                    <td><%= child.getUniqueProfileId() %></td>
                                    <td><%= child.getFullName() %></td>
                                    <td><%= child.getDateOfBirth() %></td>
                                    <td><%= child.getGender() %></td>
                                    <td><%= child.getBloodGroup() %></td>
                                    <td><%= child.getMotherId() %></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            <% } %>
            
            <% if ("users".equals(searchType) && users != null) { %>
                <% if (users.isEmpty()) { %>
                    <div class="no-results">No users found for the selected date(s).</div>
                <% } else { %>
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Full Name</th>
                                <th>Email</th>
                                <th>Phone</th>
                                <th>Role</th>
                                <th>Status</th>
                                <th>Registered At</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (User user : users) { %>
                                <tr>
                                    <td><%= user.getUserId() %></td>
                                    <td><%= user.getFullName() %></td>
                                    <td><%= user.getEmail() %></td>
                                    <td><%= user.getPhoneNumber() %></td>
                                    <td><%= user.getRole() %></td>
                                    <td><%= user.isActive() ? "Active" : "Inactive" %></td>
                                    <td><%= user.getCreatedAt() %></td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                <% } %>
            <% } %>
        <% } %>
    </div>
</body>
</html>
