package com.example;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Load the JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        // Establish a database connection
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_management", "root", "root");

        // Create a Scanner object for user input
        Scanner sc = new Scanner(System.in);

        // Variable to store the user's menu choice
        int operation;

        do {
            // Display the menu options
            System.out.println("***********************************");
            System.out.println();
            System.out.println("|| Hospital Management System ||");
            System.out.println();
            System.out.println("***********************************");
            System.out.println();
            System.out.println("------------------------------");
            System.out.println("1. Manage Patient Information");
            System.out.println("------------------------------");
            System.out.println("2. View Doctor Information");
            System.out.println("------------------------------");
            System.out.println("3. Manage Appointments");
            System.out.println("------------------------------");
            System.out.println("5. Exit");
            System.out.println("------------------------------");
            System.out.println();

            // Prompt the user to choose an operation
            System.out.println("Choose an Option:");
            operation = sc.nextInt();

            // Consume the newline character
            sc.nextLine();

            switch (operation) {
                case 1:
                    managePatients(con, sc);
                    break;

                case 2:
                    viewDoctors(con);
                    break;

                case 3:
                    manageAppointments(con, sc);
                    break;
                    
                case 4:
                	// Exit the program
                    System.out.println("Thank you for visiting our system!!");
                    System.out.println();
                    break;
                    

                default:
                    System.out.println("Invalid choice. Please try again!!");
                    System.out.println();
            }
        } while (operation != 4);

        sc.close();
        con.close();
    }

    private static void managePatients(Connection con, Scanner sc) throws SQLException {
        int choice;
        do {
            System.out.println("1. Add Patient");
            System.out.println("2. Update Patient");
            System.out.println("3. Delete Patient");
            System.out.println("4. Check Patient");
            System.out.println("5. View Patients");
            System.out.println("6. Back to Main Menu");
            System.out.println();
            System.out.println("Choose an Option:");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addPatient(con, sc);
                    break;
                case 2:
                    updatePatient(con, sc);
                    break;
                case 3:
                    deletePatient(con, sc);
                    break;
                case 4:
                    checkPatient(con, sc);
                    break;
                case 5:
                    viewPatients(con);
                    break;
                case 6:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again!!");
            }
        } while (choice != 6);
    }

    private static void addPatient(Connection con, Scanner sc) throws SQLException {
        System.out.println("Enter Patient's Name:");
        String name = sc.nextLine();
        System.out.println("Enter Patient's Age:");
        int age = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Patient's Gender:");
        String gender = sc.nextLine();
        System.out.println("Enter Patient's Contact No:");
        String phoneno = sc.nextLine();

        String createQuery = "INSERT INTO patients (name, age, gender, phoneno) VALUES (?, ?, ?, ?)";
        try (PreparedStatement createStmt = con.prepareStatement(createQuery)) {
            createStmt.setString(1, name);
            createStmt.setInt(2, age);            
            createStmt.setString(3, gender);
            createStmt.setString(4, phoneno);
            int rowsAffected = createStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Record added successfully!!");
            } else {
                System.out.println("Failed to add the record!!");
            }
        }
    }

    private static void updatePatient(Connection con, Scanner sc) throws SQLException {
        System.out.println("Enter Patient's ID to update info:");
        int patientIdToUpdate = sc.nextInt();
        sc.nextLine();

        System.out.println("Choose an option to update:");
        System.out.println("1. Update Name");
        System.out.println("2. Update Age");
        System.out.println("3. Update Contact No");
        System.out.println("4. Update Gender");
        System.out.print("Enter your choice: ");
        int updateChoice = sc.nextInt();
        sc.nextLine();

        String updateQuery;
        PreparedStatement updateStmt;

        switch (updateChoice) {
            case 1:
                System.out.println("Enter New First Name:");
                String newName = sc.nextLine();
                updateQuery = "UPDATE patients SET name = ? WHERE id = ?";
                updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setString(1, newName);
                updateStmt.setInt(2, patientIdToUpdate);
                break;
            case 2:
                System.out.print("Enter new age: ");
                int newAge = sc.nextInt();
                updateQuery = "UPDATE patients SET age = ? WHERE id = ?";
                updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setInt(1, newAge);
                updateStmt.setInt(2, patientIdToUpdate);
                break;
            case 3:
                System.out.print("Enter new contact No: ");
                String newPhoneno = sc.nextLine();
                updateQuery = "UPDATE patients SET phoneno = ? WHERE id = ?";
                updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setString(1, newPhoneno);
                updateStmt.setInt(2, patientIdToUpdate);
                break;
            case 4:
                System.out.print("Enter Correct Gender: ");
                String gender = sc.nextLine();
                updateQuery = "UPDATE patients SET gender = ? WHERE id = ?";
                updateStmt = con.prepareStatement(updateQuery);
                updateStmt.setString(1, gender);
                updateStmt.setInt(2, patientIdToUpdate);
                break;
            default:
                System.out.println("Invalid choice for update. Please try again.");
                return;
        }

        int rowsAffected = updateStmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Patient information updated successfully.");
        } else {
            System.out.println("Patient not found or update failed.");
        }
    }

    private static void deletePatient(Connection con, Scanner sc) throws SQLException {
        System.out.println("Enter Patient ID to delete record:");
        int patientIdToDelete = sc.nextInt();

        String deleteQuery = "DELETE FROM patients WHERE id = ?";
        try (PreparedStatement deleteStmt = con.prepareStatement(deleteQuery)) {
            deleteStmt.setInt(1, patientIdToDelete);
            int rowsAffected = deleteStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient deleted successfully!!");
            } else {
                System.out.println("Patient not found or delete failed!!");
            }
        }
        catch (SQLIntegrityConstraintViolationException e) {
            // Catch the foreign key constraint violation
            System.out.println("Cannot delete patient because they have scheduled appointments.");
        }
    }

    private static void checkPatient(Connection con, Scanner sc) throws SQLException {
        System.out.println("Enter Patient ID to check:");
        int patientIdToCheck = sc.nextInt();

        String checkQuery = "SELECT * FROM patients WHERE id = ?";
        try (PreparedStatement checkStmt = con.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, patientIdToCheck);
            try (ResultSet rs = checkStmt.executeQuery()) {
            	System.out.println("+------------+--------------------+----------+------------+---------------+");          
                System.out.println("| Patient ID |        Name        |    Age   |   Gender   |    Phone No   |");
                System.out.println("+------------+--------------------+----------+------------+---------------+"); 
                while (rs.next()) {
                    int patientId = rs.getInt("id");
                    String patientName = rs.getString("name");
                    int patientAge = rs.getInt("age");
                    String patientgender = rs.getString("gender");
                    String patientcontno = rs.getString("phoneno");
                    
                    System.out.printf("|%-12d|%-20s|%-10s|%-12s|%-15s|%n",patientId, patientName, patientAge, patientgender,patientcontno);
                    System.out.println("----------------------------------------------------------------------------");
                }
            }
        }
    }

    private static void viewPatients(Connection con) throws SQLException {
        String readQuery = "SELECT * FROM patients";

        try (PreparedStatement readStmt = con.prepareStatement(readQuery);
             ResultSet rs = readStmt.executeQuery()) {
        	System.out.println("+------------+--------------------+----------+------------+---------------+");          
            System.out.println("| Patient ID |        Name        |    Age   |   Gender   |    Phone No   |");
            System.out.println("+------------+--------------------+----------+------------+---------------+"); 
            while (rs.next()) {
                int patientId = rs.getInt("id");
                String patientName = rs.getString("name");
                int patientAge = rs.getInt("age");
                String patientgender = rs.getString("gender");
                String patientcontno = rs.getString("phoneno");
                
                System.out.printf("|%-12d|%-20s|%-10s|%-12s|%-15s|%n",patientId, patientName, patientAge, patientgender,patientcontno);
                System.out.println("----------------------------------------------------------------------------");
            }
            
        }
    }

  
     
    private static void viewDoctors(Connection con) throws SQLException {
        String readQuery = "SELECT * FROM doctors";

        try (PreparedStatement readStmt = con.prepareStatement(readQuery);
             ResultSet rs = readStmt.executeQuery()) {
            System.out.println("+-----------+------------------+----------------+");
            System.out.println("| Doctor ID | Name             | Specialization |");
            System.out.println("+-----------+------------------+----------------+");
            while (rs.next()) {
                int doctorId = rs.getInt("id");
                String doctorName = rs.getString("name");
                String specialization = rs.getString("specialization");
                System.out.printf("|%-11d|%-18s|%-16s|%n|", doctorId, doctorName, specialization);
                System.out.println("------------------------------------------------");
            }
            
        }
    }

    private static void manageAppointments(Connection con, Scanner sc) throws SQLException {
        int choice;
        do {
            System.out.println("1. Add Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Back to Main Menu");
            System.out.println();
            System.out.println("Choose an Option:");
            choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addAppointment(con, sc);
                    break;
                case 2:
                    viewAppointments(con);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid choice. Please try again!!");
            }
        } while (choice != 3);
    }

    private static void addAppointment(Connection con, Scanner sc) throws SQLException {
        System.out.println("Enter Patient ID:");
        int patientId = sc.nextInt();
        System.out.println("Enter Doctor ID:");
        int doctorId = sc.nextInt();
        sc.nextLine();
        System.out.println("Enter Appointment Date (YYYY-MM-DD):");
        String appointmentDate = sc.nextLine();

        String createQuery = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
        try (PreparedStatement createStmt = con.prepareStatement(createQuery)) {
            createStmt.setInt(1, patientId);
            createStmt.setInt(2, doctorId);
            createStmt.setString(3, appointmentDate);
            int rowsAffected = createStmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Appointment added successfully!!");
            } else {
                System.out.println("Failed to add the appointment!!");
            }
        }
    }

    private static void viewAppointments(Connection con) throws SQLException {
        String readQuery = "SELECT * FROM appointments";

        try (PreparedStatement readStmt = con.prepareStatement(readQuery);
             ResultSet rs = readStmt.executeQuery()) {
            System.out.println("+----------------+------------------+------------------+-------------------+");
            System.out.println("| Appointment ID | Patient ID       | Doctor ID        | Date              |");
            System.out.println("+----------------+------------------+------------------+-------------------+");
      
            while (rs.next()) {
                int appointmentId = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");
                String appointmentDate = rs.getString("appointment_date");
                System.out.printf("|%-17d|%-18d|%-18d|%-19s|%n", appointmentId, patientId, doctorId, appointmentDate);
                System.out.println("+----------------+------------------+------------------+-------------------+");
            }
            
        }
    }
}
