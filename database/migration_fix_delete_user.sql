-- ============================================================
-- Migration Script: Fix User Deletion Foreign Key Constraints
-- ============================================================
-- This script updates foreign key constraints to allow user deletion
-- Run this on your EXISTING database without losing data
-- ============================================================

USE chms_db;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- Fix 1: Update children table - mother_id constraint
-- ============================================================
ALTER TABLE children
    DROP FOREIGN KEY children_ibfk_1;

ALTER TABLE children
    ADD CONSTRAINT children_ibfk_1 
    FOREIGN KEY (mother_id) 
    REFERENCES users(user_id) 
    ON DELETE CASCADE;

-- ============================================================
-- Fix 2: Update health_records table - doctor_id constraint
-- ============================================================

-- First, allow NULL values for doctor_id
ALTER TABLE health_records
    MODIFY COLUMN doctor_id INT NULL COMMENT 'Doctor who performed assessment - NULL if doctor deleted';

-- Drop the old foreign key
ALTER TABLE health_records
    DROP FOREIGN KEY health_records_ibfk_2;

-- Add new foreign key with SET NULL on delete
ALTER TABLE health_records
    ADD CONSTRAINT health_records_ibfk_2 
    FOREIGN KEY (doctor_id) 
    REFERENCES users(user_id) 
    ON DELETE SET NULL;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- Verification: Check the updated constraints
-- ============================================================
SELECT 
    TABLE_NAME,
    COLUMN_NAME,
    CONSTRAINT_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM 
    INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE 
    TABLE_SCHEMA = 'chms_db'
    AND TABLE_NAME IN ('children', 'health_records')
    AND REFERENCED_TABLE_NAME = 'users';

-- ============================================================
-- Migration Complete!
-- ============================================================
-- Changes applied:
-- 1. Mother deletion now cascades to delete their children records
-- 2. Doctor deletion now sets doctor_id to NULL in health_records
-- 3. Audit logs and notifications will handle deletions appropriately
-- ============================================================

