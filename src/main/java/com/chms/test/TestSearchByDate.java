package com.chms.test;

import com.chms.dao.AppointmentDAO;
import com.chms.dao.ChildDAO;
import com.chms.dao.UserDAO;
import com.chms.model.Appointment;
import com.chms.model.Child;
import com.chms.model.User;
import com.chms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Test class for demonstrating SEARCH BY DATE functionality
 * 
 * This test demonstrates:
 * 1. Searching appointments by specific date
 * 2. Searching appointments by date range
 * 3. Searching children by date of birth
 * 4. Searching users by registration date
 */
public class TestSearchByDate {
    
    private static final AppointmentDAO appointmentDAO = new AppointmentDAO();
    private static final ChildDAO childDAO = new ChildDAO();
    private static final UserDAO userDAO = new UserDAO();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("CHILD HEALTH MONITORING SYSTEM - SEARCH BY DATE TEST");
        System.out.println("=".repeat(80));
        System.out.println();
        
        // Test database connection
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✓ Database connection successful!");
            System.out.println("Database URL: " + conn.getMetaData().getURL());
            System.out.println();
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            return;
        }
        
        boolean continueSearching = true;
        
        while (continueSearching) {
            System.out.println("-".repeat(80));
            System.out.println("SELECT SEARCH TYPE:");
            System.out.println("-".repeat(80));
            System.out.println("1. Search Appointments by Date");
            System.out.println("2. Search Appointments by Date Range");
            System.out.println("3. Search Children by Date of Birth");
            System.out.println("4. Search Children by Date of Birth Range");
            System.out.println("5. Search Users by Registration Date");
            System.out.println("6. Search Users by Registration Date Range");
            System.out.println("0. Exit");
            System.out.println();
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.println();
            
            switch (choice) {
                case 1:
                    searchAppointmentsByDate();
                    break;
                case 2:
                    searchAppointmentsByDateRange();
                    break;
                case 3:
                    searchChildrenByDateOfBirth();
                    break;
                case 4:
                    searchChildrenByDateRange();
                    break;
                case 5:
                    searchUsersByRegistrationDate();
                    break;
                case 6:
                    searchUsersByDateRange();
                    break;
                case 0:
                    continueSearching = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (continueSearching && choice != 0) {
                System.out.println();
                System.out.print("Press Enter to continue...");
                scanner.nextLine();
                System.out.println("\n\n");
            }
        }
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("SEARCH BY DATE TEST COMPLETED");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Search appointments by a specific date
     */
    private static void searchAppointmentsByDate() {
        System.out.println("-".repeat(80));
        System.out.println("SEARCH APPOINTMENTS BY SPECIFIC DATE");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.print("Enter date (YYYY-MM-DD) [e.g., 2026-03-15]: ");
        String dateStr = scanner.nextLine();
        
        try {
            Date searchDate = Date.valueOf(dateStr);
            
            System.out.println();
            System.out.println("Searching appointments for date: " + searchDate);
            System.out.println();
            
            List<Appointment> appointments = appointmentDAO.searchAppointmentsByDate(searchDate);
            
            System.out.println("Found " + appointments.size() + " appointment(s)");
            System.out.println();
            
            if (!appointments.isEmpty()) {
                System.out.printf("%-5s %-12s %-10s %-20s %-20s %-15s %-12s%n",
                                "ID", "Date", "Time", "Child", "Doctor", "Type", "Status");
                System.out.println("-".repeat(80));
                
                for (Appointment apt : appointments) {
                    System.out.printf("%-5d %-12s %-10s %-20s %-20s %-15s %-12s%n",
                                    apt.getAppointmentId(),
                                    apt.getAppointmentDate(),
                                    apt.getAppointmentTime(),
                                    apt.getChildName(),
                                    apt.getDoctorName(),
                                    apt.getAppointmentType(),
                                    apt.getStatus());
                }
            } else {
                System.out.println("No appointments found for the specified date.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD");
        }
    }
    
    /**
     * Search appointments by date range
     */
    private static void searchAppointmentsByDateRange() {
        System.out.println("-".repeat(80));
        System.out.println("SEARCH APPOINTMENTS BY DATE RANGE");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        
        try {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            
            System.out.println();
            System.out.println("Searching appointments from " + startDate + " to " + endDate);
            System.out.println();
            
            List<Appointment> appointments = appointmentDAO.searchAppointmentsByDateRange(startDate, endDate);
            
            System.out.println("Found " + appointments.size() + " appointment(s)");
            System.out.println();
            
            if (!appointments.isEmpty()) {
                System.out.printf("%-5s %-12s %-10s %-20s %-20s %-15s %-12s%n",
                                "ID", "Date", "Time", "Child", "Doctor", "Type", "Status");
                System.out.println("-".repeat(80));
                
                for (Appointment apt : appointments) {
                    System.out.printf("%-5d %-12s %-10s %-20s %-20s %-15s %-12s%n",
                                    apt.getAppointmentId(),
                                    apt.getAppointmentDate(),
                                    apt.getAppointmentTime(),
                                    apt.getChildName(),
                                    apt.getDoctorName(),
                                    apt.getAppointmentType(),
                                    apt.getStatus());
                }
            } else {
                System.out.println("No appointments found in the specified date range.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD");
        }
    }
    
    /**
     * Search children by date of birth
     */
    private static void searchChildrenByDateOfBirth() {
        System.out.println("-".repeat(80));
        System.out.println("SEARCH CHILDREN BY DATE OF BIRTH");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.print("Enter date of birth (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        
        try {
            Date searchDate = Date.valueOf(dateStr);
            
            System.out.println();
            System.out.println("Searching children born on: " + searchDate);
            System.out.println();
            
            List<Child> children = childDAO.searchChildrenByDateOfBirth(searchDate);
            
            System.out.println("Found " + children.size() + " child(ren)");
            System.out.println();
            
            if (!children.isEmpty()) {
                System.out.printf("%-5s %-15s %-25s %-12s %-8s %-12s%n",
                                "ID", "Profile ID", "Full Name", "DOB", "Gender", "Blood Group");
                System.out.println("-".repeat(80));
                
                for (Child child : children) {
                    System.out.printf("%-5d %-15s %-25s %-12s %-8s %-12s%n",
                                    child.getChildId(),
                                    child.getUniqueProfileId(),
                                    child.getFullName(),
                                    child.getDateOfBirth(),
                                    child.getGender(),
                                    child.getBloodGroup());
                }
            } else {
                System.out.println("No children found with the specified date of birth.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD");
        }
    }
    
    /**
     * Search children by date of birth range
     */
    private static void searchChildrenByDateRange() {
        System.out.println("-".repeat(80));
        System.out.println("SEARCH CHILDREN BY DATE OF BIRTH RANGE");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        
        try {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            
            System.out.println();
            System.out.println("Searching children born between " + startDate + " and " + endDate);
            System.out.println();
            
            List<Child> children = childDAO.searchChildrenByDateRange(startDate, endDate);
            
            System.out.println("Found " + children.size() + " child(ren)");
            System.out.println();
            
            if (!children.isEmpty()) {
                System.out.printf("%-5s %-15s %-25s %-12s %-8s %-12s%n",
                                "ID", "Profile ID", "Full Name", "DOB", "Gender", "Blood Group");
                System.out.println("-".repeat(80));
                
                for (Child child : children) {
                    System.out.printf("%-5d %-15s %-25s %-12s %-8s %-12s%n",
                                    child.getChildId(),
                                    child.getUniqueProfileId(),
                                    child.getFullName(),
                                    child.getDateOfBirth(),
                                    child.getGender(),
                                    child.getBloodGroup());
                }
            } else {
                System.out.println("No children found in the specified date range.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD");
        }
    }
    
    /**
     * Search users by registration date
     */
    private static void searchUsersByRegistrationDate() {
        System.out.println("-".repeat(80));
        System.out.println("SEARCH USERS BY REGISTRATION DATE");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.print("Enter registration date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        
        try {
            Date searchDate = Date.valueOf(dateStr);
            
            System.out.println();
            System.out.println("Searching users registered on: " + searchDate);
            System.out.println();
            
            List<User> users = userDAO.searchUsersByRegistrationDate(searchDate);
            
            System.out.println("Found " + users.size() + " user(s)");
            System.out.println();
            
            if (!users.isEmpty()) {
                System.out.printf("%-5s %-25s %-30s %-15s %-10s %-8s%n",
                                "ID", "Full Name", "Email", "Phone", "Role", "Active");
                System.out.println("-".repeat(80));
                
                for (User user : users) {
                    System.out.printf("%-5d %-25s %-30s %-15s %-10s %-8s%n",
                                    user.getUserId(),
                                    user.getFullName(),
                                    user.getEmail(),
                                    user.getPhoneNumber(),
                                    user.getRole(),
                                    user.isActive() ? "Yes" : "No");
                }
            } else {
                System.out.println("No users found registered on the specified date.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD");
        }
    }
    
    /**
     * Search users by registration date range
     */
    private static void searchUsersByDateRange() {
        System.out.println("-".repeat(80));
        System.out.println("SEARCH USERS BY REGISTRATION DATE RANGE");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.print("Enter start date (YYYY-MM-DD): ");
        String startDateStr = scanner.nextLine();
        System.out.print("Enter end date (YYYY-MM-DD): ");
        String endDateStr = scanner.nextLine();
        
        try {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            
            System.out.println();
            System.out.println("Searching users registered between " + startDate + " and " + endDate);
            System.out.println();
            
            List<User> users = userDAO.searchUsersByDateRange(startDate, endDate);
            
            System.out.println("Found " + users.size() + " user(s)");
            System.out.println();
            
            if (!users.isEmpty()) {
                System.out.printf("%-5s %-25s %-30s %-15s %-10s %-8s%n",
                                "ID", "Full Name", "Email", "Phone", "Role", "Active");
                System.out.println("-".repeat(80));
                
                for (User user : users) {
                    System.out.printf("%-5d %-25s %-30s %-15s %-10s %-8s%n",
                                    user.getUserId(),
                                    user.getFullName(),
                                    user.getEmail(),
                                    user.getPhoneNumber(),
                                    user.getRole(),
                                    user.isActive() ? "Yes" : "No");
                }
            } else {
                System.out.println("No users found in the specified date range.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD");
        }
    }
}
