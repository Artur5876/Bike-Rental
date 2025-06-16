package com.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StudentSignUpPage extends Application {

    @Override
    public void start(Stage primaryStage) {

        // UI components initialization
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        Label emailLabel = new Label("College Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Enter your password:");
        PasswordField passwordField  = new PasswordField();

        Label passwordRepeatLabel = new Label("Repeat your password:");
        PasswordField passwordRepeatField  = new PasswordField();

        Button signinButton = new Button("Sign Up");

        //back to login page button
        Button backButton = new Button("Back");
        
        backButton.setOnAction(e -> {
            Login login = new Login();
            login.start(primaryStage);
        });

        // Set action for the signup button
        signinButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password  = passwordField.getText();
            String errorMessage = ""; 
            boolean isValidFName = false;
            boolean isValidSName = false;
            boolean isValidEmail = false;
            boolean isValidPassword = false;
        
            if (firstName.length() == 0 || firstName.contains(" ")) {
                errorMessage+= "Your First Name cannot be empty or contain free space!\n";
                isValidFName=false;
            }
            else{
                isValidFName = true;
            }
            if (lastName.length() == 0 || lastName.contains(" ")) {
                errorMessage+= "Your Last Name cannot be empty or contain free space!\n";
                isValidSName=false;
            }
            else{
                isValidSName = true;
            }
            if (email_databaseCheck(email) && isValidCollegeEmail(email)) {
                isValidEmail = true;
            } else{
                isValidEmail = false;
                errorMessage += "Your email isn't valid!\n";
            }
            if (passwordCheck(passwordField, passwordRepeatField)) {
                isValidPassword = true;
            }
            else{
                isValidPassword = false;
                errorMessage += "Your Password should match and contain at least 8 characters";
            }
            System.out.println(isValidPassword);
            System.out.println(isValidEmail);
            String query = "INSERT INTO Users (first_name, last_name, email, user_type, hireDate, salary, departmentNum, password) " +
           "VALUES ('" + firstName + "', '" + lastName + "', '" + email + "', NULL, NULL, NULL, NULL, '" + password + "')";
        
            if (isValidEmail && isValidPassword && isValidFName && isValidSName) {
                UsersDataLoad.executeQueryAndReturnValue(query);
                StudentPage student = new StudentPage();
                student.start(primaryStage);
            }
            else{
                System.out.println("INVALIDDDDDDD");
                Error error = new Error();
                error.showErrorWindow(errorMessage);
            }
        // Validate the email (e.g., check if it's a valid college email)
        // if (isValidCollegeEmail(email)) {
        //     // Simulate successful signup (no SQL connection)
        //     messageLabel.setText("Signup successful for: " + firstName + " " + lastName);
        // } else {
        //     messageLabel.setText("Error: Invalid college email.");
        // }
        });

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(firstNameLabel, 0, 0);
        grid.add(firstNameField, 1, 0);
        grid.add(lastNameLabel, 0, 1);
        grid.add(lastNameField, 1, 1);
        grid.add(emailLabel, 0, 2);
        grid.add(emailField, 1, 2);
        grid.add(passwordLabel, 0, 3 );
        grid.add(passwordField,1,3);
        grid.add(passwordRepeatLabel, 0, 4);
        grid.add(passwordRepeatField, 1, 4);

        grid.add(signinButton, 1, 5);
        grid.add(backButton, 0, 5);

        //grid.add(signinButton, 1, 5);

        // Scene and Stage
        Scene scene = new Scene(grid, 400, 280);
        primaryStage.setTitle("Student Signup");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean isValidCollegeEmail(String email) {
        // Example validation: Check if the email ends with "@college.edu"
        return email != null && email.endsWith("@morrisonsislandcampus.com");
    }

    //method that will validate if email doesnt exist already, otherwise n
    private boolean email_databaseCheck(String email) {
       // System.out.println(UsersDataLoad.executeQueryAndReturnValue("SELECT user_type FROM Users WHERE email =" + email) + "54");
        return UsersDataLoad.executeQueryAndReturnValue("SELECT user_type FROM Users WHERE email =" + email) == null;
    }

    //method that will validate the password

    private boolean passwordCheck(PasswordField passwordField, PasswordField passwordRepeatField) {
        String password = passwordField.getText();
        String password2 = passwordRepeatField.getText(); 
        // Check if passwords match and if the password length is greater than 7
        if (password2.equals(password) && password.length() > 7) {
            return true; // Passwords match and length is valid
        } else {
            return false; // Either passwords don't match or length is invalid
        }
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}