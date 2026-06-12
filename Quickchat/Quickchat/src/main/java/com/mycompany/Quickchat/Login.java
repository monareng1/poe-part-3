package com.mycompany.Quickchat;

import java.util.Scanner; // Imported to take user input in the console

public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Parameterized Constructor
    public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // Default Constructor
    public Login() {
        this.firstName = "";
        this.lastName = "";
        this.username = "";
        this.password = "";
        this.cellPhoneNumber = "";
    }

    public boolean checkUserName() {
        if (username == null) {
            return false;
        }
        boolean hasUnderscore = username.contains("_");
        boolean correctLength = username.length() <= 5;
        return hasUnderscore && correctLength;
    }

    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber() {
        if (cellPhoneNumber == null) {
            return false;
        }
        // Matches standard international formats like +27123456789
        String regex = "^\\+[0-9]{10,12}$";
        return cellPhoneNumber.matches(regex);
    }

    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "Registration successful! Username successfully captured. Password successfully captured. Cell phone number successfully added.";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername == null || enteredPassword == null) {
            return false;
        }
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    // --- Getters and Setters ---
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getCellPhoneNumber() { return cellPhoneNumber; }
    public void setCellPhoneNumber(String cellPhoneNumber) { this.cellPhoneNumber = cellPhoneNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    // =========================================================================
    // ADDED MAIN METHOD: This acts as the engine to execute and run the code
    // =========================================================================
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login user = new Login(); // Creating user instance via default constructor
        
        System.out.println("--- QUICKCHAT REGISTRATION ---");
        
        System.out.print("Enter First Name: ");
        user.setFirstName(input.nextLine());
        
        System.out.print("Enter Last Name: ");
        user.setLastName(input.nextLine());
        
        System.out.print("Enter Username (max 5 chars, must contain '_'): ");
        user.setUsername(input.nextLine());
        
        System.out.print("Enter Password (min 8 chars, 1 Capital, 1 Number, 1 Special): ");
        user.setPassword(input.nextLine());
        
        System.out.print("Enter Cell Number (e.g., +27812345678): ");
        user.setCellPhoneNumber(input.nextLine());
        
        System.out.println("\n--- Registration Status ---");
        String registrationResult = user.registerUser();
        System.out.println(registrationResult);
        
        // Only proceed to Login if registration actually succeeded
        if (registrationResult.startsWith("Registration successful!")) {
            System.out.println("\n--- QUICKCHAT LOGIN ---");
            System.out.print("Enter Username to log in: ");
            String loginUser = input.nextLine();
            
            System.out.print("Enter Password to log in: ");
            String loginPass = input.nextLine();
            
            System.out.println("\n--- Login Status ---");
            System.out.println(user.returnLoginStatus(loginUser, loginPass));
        } else {
            System.out.println("\nProcess terminated due to validation failure. Fix inputs and rerun.");
        }
        
        input.close();
    }
}



