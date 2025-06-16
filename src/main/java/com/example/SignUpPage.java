package com.example;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SignUpPage extends Application {

    @Override
    public void start(Stage primaryStage) {

        //all the elements in window
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        Label userTypeLabel = new Label("Select user_type:");
        ComboBox<String> userTypeComboBox = new ComboBox<>();
        userTypeComboBox.getItems().addAll("student", "admin", "employer");
        userTypeComboBox.setValue("student");

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

        //action that will validate all the data that user entered and allocate to the student page
        signinButton.setOnAction(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            String password  = passwordField.getText();
            String userType  = userTypeComboBox.getValue();
            
            //error message that will have all the content-messages to show whats wrong 
            String errorMessage = ""; 

            //validators
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
                System.out.println(email_databaseCheck(email) + " " + isValidCollegeEmail(email));
                errorMessage += "Your email isn't valid!\n";
            }
            if (passwordCheck(passwordField, passwordRepeatField)) {
                isValidPassword = true;
            }
            else{
                isValidPassword = false;
                errorMessage += "Your Password should match and contain at least 8 characters";
            }

            //query that will build a new record in Users table, if all data is valid
            String query = "INSERT INTO Users (first_name, last_name, email, user_type, hireDate, salary, departmentNum, password) " +
           "VALUES ('" + firstName + "', '" + lastName + "', '" + email + "', '"+ userType +"', NULL, NULL, NULL, '" + password + "')";
        
            if (isValidEmail && isValidPassword && isValidFName && isValidSName) {
                UsersDataLoad.executeQueryAndReturnValue(query);
                
                StudentPage student = new StudentPage();
                EmployerPage employer = new EmployerPage();
                AdminPage admin = new AdminPage();
                Login login = new Login();

                //assigning new welcome-label for signed up user
                login.userAccount = email;

                //student
                if (userType.equals("student")) {
                    student.start(primaryStage);
                }
                //employer
                else if (userType.equals("employer")) {
                    employer.start(primaryStage);
                }
                // admin
                else {
                    admin.start(primaryStage);
                }
            }
            //error message to show whats wrong 
            else{
                Error error = new Error();
                error.showErrorWindow(errorMessage);
            }
        });

        //layout
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
        grid.add(passwordLabel, 0, 3);
        grid.add(passwordField, 1, 3);
        grid.add(passwordRepeatLabel, 0, 4);
        grid.add(passwordRepeatField, 1, 4);
        grid.add(userTypeLabel, 0, 5);
        grid.add(userTypeComboBox, 1, 5);
        grid.add(signinButton, 1, 6);
        grid.add(backButton, 0, 6);

        // Scene and Stage
        Scene scene = new Scene(grid, 400, 280);
        primaryStage.setTitle("Sign Up Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //method that will validate the email
    //the last part required to be @morrisonsislandcampus.com
    private boolean isValidCollegeEmail(String email) {
        // Example validation: Check if the email ends with ".com"
        return email != null && email.endsWith(".com");
    }

    //method that will validate if email doesnt exist already, otherwise n
    private boolean email_databaseCheck(String email) {
        return UsersDataLoad.executeQueryAndReturnValue("SELECT user_type FROM Users WHERE email =" + email) == null;
    }

    //method that will validate the password
    private boolean passwordCheck(PasswordField passwordField, PasswordField passwordRepeatField) {
        String password = passwordField.getText();
        String password2 = passwordRepeatField.getText(); 
        //validation, if passwords match and if the password length is greater than 7
        if (password2.equals(password) && password.length() > 7) {
            return true; 
        } else {
            return false; 
        }
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}