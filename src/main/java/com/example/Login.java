package com.example;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
public class Login extends Application {

    public static String userAccount;
   @Override
    public void start(Stage primaryStage) {
        //all elements that will be in window
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("bestuser@morrisonsislandcampus.com");

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("password123");

        //Buttons
        Button signinButton = new Button("Login");
        Button signupButton = new Button("Sign Up");
        Button helpButton = new Button ("Help");

        //if user doesn't have an account --> option provided
        signupButton.setOnAction(e -> {
            SignUpPage signup = new SignUpPage();
            signup.start(primaryStage);
        });
        

        //action on button that will validate entered data by user and will provide window according what users data is that
        //either admin or employer or student
        signinButton.setOnAction(e -> {
            
            //we are assigning the emailfield text to the userAccount that will be used for account setup
            userAccount = emailField.getText();

            UsersDataLoad usersData = new UsersDataLoad();
            String queryStudentValidation = "SELECT COUNT(*) FROM Users WHERE email = '" + emailField.getText() + "'" + " AND password = '" + passwordField.getText() + "'" + "and user_type = 'student'" ;
            String queryEmployerValidation = "SELECT COUNT(*) FROM Users WHERE email = '" + emailField.getText() + "'" + " AND password = '" + passwordField.getText() + "'" + "and user_type = 'employer'" ;
            String queryAdminValidation = "SELECT COUNT(*) FROM Users WHERE email = '" + emailField.getText() + "'" + " AND password = '" + passwordField.getText() + "'" + "and user_type = 'admin'" ;        
            //option for student 
            if (usersData.executeQueryWithSingleQuotes(queryStudentValidation)) {
                StudentPage student = new StudentPage();
                student.start(primaryStage);
            }
            //option for employer
            else if (usersData.executeQueryWithSingleQuotes(queryEmployerValidation)) {
                EmployerPage employer = new EmployerPage();
                employer.start(primaryStage);
            }
            //option for admin
            else if (usersData.executeQueryWithSingleQuotes(queryAdminValidation)) {
                AdminPage admin = new AdminPage();
                admin.start(primaryStage);
            }
            //otherwise, if data is invalid or does not exist -->ERROR message
            else {
                Error error = new Error();
                error.showErrorWindow("Your Data is incorrect!!!");
                System.out.println(userAccount + passwordField.getText());
            }
        });


        helpButton.setOnAction(e -> {
            helpWindow();
        });
        //GridPane for layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER); // Center the grid
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20)); // Padding around grid

        grid.add(emailLabel, 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(helpButton, 0, 2);


        //HBox to align buttons horizontally
        HBox buttonBox = new HBox(10); 
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(signupButton, signinButton);
        grid.add(buttonBox, 1, 2);

        //BorderPane as root layout
        BorderPane root = new BorderPane();
        root.setCenter(grid); // Place the form in the center

        //Scene and Stage
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void helpWindow() {
        //help-window content
        Label helpLabel = new Label("LOGIN HELP\n\n"
                + "- Enter your email and password to log in.\n"
                + "- If you are an employer, you can post jobs.\n"
                + "- Students can search and apply for jobs.\n"
                + "- Admins can approve or reject jobs.\n"
                + "- If you forget your password, contact support.\n");

        //layout for label
        StackPane helpLayout = new StackPane(helpLabel);
        Scene helpScene = new Scene(helpLayout, 400, 200);

        // Help window setup
        Stage root = new Stage();
        root.setTitle("Help");
        root.setScene(helpScene);
        root.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}