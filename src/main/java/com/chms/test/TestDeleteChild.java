package com.chms.test;

import com.chms.dao.ChildDAO;
import com.chms.model.Child;
import com.chms.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * Test class for demonstrating DELETE operation on Child records
 * 
 * This test demonstrates:
 * 1. Viewing existing children records before deletion
 * 2. Deleting a child record
 * 3. Verifying the deletion by checking the database state
 */
public class TestDeleteChild {
    
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("CHILD HEALTH MONITORING SYSTEM - DELETE OPERATION TEST");
        System.out.println("=".repeat(80));
        System.out.println();
        
        ChildDAO childDAO = new ChildDAO();
        
        // Test database connection
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("✓ Database connection successful!");
            System.out.println("Database URL: " + conn.getMetaData().getURL());
            System.out.println();
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            return;
        }
        
        System.out.println("-".repeat(80));
        System.out.println("STEP 1: VIEWING EXISTING CHILDREN RECORDS (Before Deletion)");
        System.out.println("-".repeat(80));
        
        // Get a mother ID to test (assuming mother_id = 2 exists)
        int testMotherId = 2;
        List<Child> childrenBefore = childDAO.getChildrenByMotherId(testMotherId);
        
        System.out.println("Children for Mother ID " + testMotherId + ":");
        System.out.println();
        
        if (childrenBefore.isEmpty()) {
            System.out.println("No children found for this mother. Cannot perform delete test.");
            System.out.println("Please add children records first using TestCreateChild.java");
            return;
        }
        
        System.out.printf("%-8s %-15s %-30s %-15s %-10s%n", 
                         "ID", "Profile ID", "Full Name", "Date of Birth", "Gender");
        System.out.println("-".repeat(80));
        
        for (Child child : childrenBefore) {
            System.out.printf("%-8d %-15s %-30s %-15s %-10s%n",
                            child.getChildId(),
                            child.getUniqueProfileId(),
                            child.getFullName(),
                            child.getDateOfBirth(),
                            child.getGender());
        }
        
        System.out.println();
        System.out.println("Total children records: " + childrenBefore.size());
        System.out.println();
        
        // Select the first child to delete
        if (childrenBefore.isEmpty()) {
            System.out.println("No children available to delete.");
            return;
        }
        
        Child childToDelete = childrenBefore.get(0);
        int childIdToDelete = childToDelete.getChildId();
        
        System.out.println("-".repeat(80));
        System.out.println("STEP 2: PERFORMING DELETE OPERATION");
        System.out.println("-".repeat(80));
        System.out.println();
        System.out.println("Attempting to delete child:");
        System.out.println("  Child ID: " + childIdToDelete);
        System.out.println("  Name: " + childToDelete.getFullName());
        System.out.println("  Profile ID: " + childToDelete.getUniqueProfileId());
        System.out.println();
        
        // Perform the deletion
        boolean deleted = childDAO.deleteChild(childIdToDelete);
        
        if (deleted) {
            System.out.println("✓ Child record deleted successfully!");
        } else {
            System.out.println("✗ Failed to delete child record.");
        }
        System.out.println();
        
        System.out.println("-".repeat(80));
        System.out.println("STEP 3: VERIFICATION (Database State After Deletion)");
        System.out.println("-".repeat(80));
        System.out.println();
        
        // Verify by checking if the child still exists
        Child verifyChild = childDAO.getChildById(childIdToDelete);
        
        if (verifyChild == null) {
            System.out.println("✓ VERIFICATION SUCCESSFUL: Child record no longer exists in database");
        } else {
            System.out.println("✗ VERIFICATION FAILED: Child record still exists in database");
        }
        System.out.println();
        
        // Show remaining children
        List<Child> childrenAfter = childDAO.getChildrenByMotherId(testMotherId);
        System.out.println("Remaining children for Mother ID " + testMotherId + ": " + childrenAfter.size());
        
        if (!childrenAfter.isEmpty()) {
            System.out.println();
            System.out.printf("%-8s %-15s %-30s %-15s %-10s%n", 
                             "ID", "Profile ID", "Full Name", "Date of Birth", "Gender");
            System.out.println("-".repeat(80));
            
            for (Child child : childrenAfter) {
                System.out.printf("%-8d %-15s %-30s %-15s %-10s%n",
                                child.getChildId(),
                                child.getUniqueProfileId(),
                                child.getFullName(),
                                child.getDateOfBirth(),
                                child.getGender());
            }
        }
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("DELETE OPERATION TEST COMPLETED");
        System.out.println("=".repeat(80));
        System.out.println();
        System.out.println("Summary:");
        System.out.println("  - Records before deletion: " + childrenBefore.size());
        System.out.println("  - Records after deletion: " + childrenAfter.size());
        System.out.println("  - Deletion successful: " + deleted);
        System.out.println("  - Verification passed: " + (verifyChild == null));
        System.out.println();
    }
}
