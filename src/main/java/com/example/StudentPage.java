package com.example;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StudentPage extends Application {
    TableView<Jobs> tableView = new TableView<>();
    public static ObservableList<Jobs> jobData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        Login login = new Login();
        
        Label welcomeLabel = new Label("Welcome " + login.userAccount);
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"); // Optional: Add styling
        
        //all the elements
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button viewAllJobsButton = new Button("View All Jobs");
        Button viewByDepartmentButton = new Button("View by Department");
        Button viewByPartialSearchButton = new Button("View by Partial Search");
        Button changePasswordButton = new Button("Change Password");
        Button logoutButton = new Button("Logout");
        Button unsubscribeButton = new Button("Unsubscribe from register");

        //search section with hbox layout
        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));
        searchBox.setVisible(false); // Initially hidden

        //buttons section gridPane layout
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(10);
        buttonsGrid.setVgap(10);
        buttonsGrid.setPadding(new Insets(10));

        buttonsGrid.add(welcomeLabel, 0, 0, 2, 1); 

        buttonsGrid.add(viewAllJobsButton, 0, 1);
        buttonsGrid.add(viewByDepartmentButton, 1, 1);
        buttonsGrid.add(viewByPartialSearchButton, 0, 2);
        buttonsGrid.add(changePasswordButton, 1, 2);
        buttonsGrid.add(logoutButton, 0, 3);
        buttonsGrid.add(unsubscribeButton, 1, 3);

        
        // Table columns generation
        TableColumn<Jobs, Integer> jobIdColumn = new TableColumn<>("Job ID");
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));

        TableColumn<Jobs, Integer> employerIdColumn = new TableColumn<>("Employer ID");
        employerIdColumn.setCellValueFactory(new PropertyValueFactory<>("employerId"));

        TableColumn<Jobs, String> jobTitleColumn = new TableColumn<>("Job Title");
        jobTitleColumn.setCellValueFactory(new PropertyValueFactory<>("jobTitle"));

        TableColumn<Jobs, String> jobDescriptionColumn = new TableColumn<>("Job Description");
        jobDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("jobDescription"));

        TableColumn<Jobs, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        TableColumn<Jobs, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Jobs, String> closingDateColumn = new TableColumn<>("Closing Date");
        closingDateColumn.setCellValueFactory(new PropertyValueFactory<>("closingDate"));

        TableColumn<Jobs, String> postedDateColumn = new TableColumn<>("Posted Date");
        postedDateColumn.setCellValueFactory(new PropertyValueFactory<>("postedDate"));

        TableColumn<Jobs, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tableView.getColumns().addAll(jobIdColumn, employerIdColumn, jobTitleColumn, jobDescriptionColumn, departmentColumn,
                locationColumn, closingDateColumn, postedDateColumn, statusColumn);

        JobsDataLoad jobsData = new JobsDataLoad();

        //actions for each buttons
        viewAllJobsButton.setOnAction(e -> {
            jobData.clear();
            jobData = jobsData.loadDataFromDatabase("SELECT * FROM Jobs WHERE status = 'approved'");
            tableView.setItems(jobData); 
        });

        viewByDepartmentButton.setOnAction(e -> {
            searchBox.setVisible(true); 
            searchField.setPromptText("Enter the department name"); 
        });

        viewByPartialSearchButton.setOnAction(e ->{
            searchBox.setVisible(true);
            searchField.setPromptText("Enter the any information that can be helpful to find job");
        });

        changePasswordButton.setOnAction(e -> {
            UsersDataLoad usersData = new UsersDataLoad();
            changeStudentPasswordForm(primaryStage);
            
        });

        searchButton.setOnAction(e -> {
            jobData.clear();
        
            //content from the search field
            String keyword = searchField.getText();
            
            //check the prompt text to determine the type of search
            if (searchField.getPromptText().equals("Enter the department name")) {
                // Search by department
                String query;
                if (keyword.isEmpty()) {
                    //if no keyword is entered, fetch all approved jobs
                    query = "SELECT * FROM Jobs WHERE status = 'Approved' AND closingDate > GETDATE()";
                } else {
                    //if a keyword is entered, filter by department
                    query = "SELECT * FROM Jobs WHERE department = '" + keyword + "' AND status = 'Approved' AND closingDate > GETDATE()";
                }
                jobData = jobsData.loadDataFromDatabase(query);
                //bind the data to the TableView
                tableView.setItems(jobData); 
            } else if (searchField.getPromptText().equals("Enter the any information that can be helpful to find job")) {
                //search by partial match (department, jobDescription, location, or jobTitle)
                String query;
                if (keyword.isEmpty()) {
                    //if no keyword is entered, fetch all approved jobs
                    query = "SELECT * FROM Jobs WHERE status = 'Approved' AND closingDate > GETDATE()";
                } else {
                    //if a keyword is entered, filter by partial match
                    query = "SELECT * FROM Jobs WHERE (department LIKE '%" + keyword + "%' OR jobDescription LIKE '%" + keyword + "%' OR location LIKE '%" + keyword + "%' OR jobTitle LIKE '%" + keyword + "%') AND status = 'approved' AND closingDate > GETDATE()";
                }
                jobData = jobsData.loadDataFromDatabase(query);
                tableView.setItems(jobData); 
            } 
        });

        logoutButton.setOnAction(e -> {
            // Logic to log out the student(close window)
            primaryStage.close(); 
            login.start(primaryStage);
        });

        //aaccount removal
        unsubscribeButton.setOnAction(e -> {
            unsubscribeConfirmation(primaryStage);
            //login.start(primaryStage);
        });

        BorderPane root = new BorderPane();
        //button grid at the top
        root.setTop(buttonsGrid);
        //the search section and table together
        VBox centerBox = new VBox(10, searchBox, tableView);
        centerBox.setPadding(new Insets(10));
        //the search and table section in the center
        root.setCenter(centerBox); 

        // Scene and Stage
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Student Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeStudentPasswordForm(Stage primaryStage) {
        StudentPage student = new StudentPage();
        Error error = new Error();
        Login login = new Login();
        UsersDataLoad usersData = new UsersDataLoad();
        // all components
        Label titleLabel = new Label("Change Password");
        titleLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Label currentPasswordLabel = new Label("Enter Current Password:");
        TextField currentPasswordField = new TextField();
        currentPasswordField.setPromptText("Current Password");

        Label newPasswordLabel = new Label("Enter New Password:");
        TextField newPasswordField = new TextField();
        newPasswordField.setPromptText("New Password");

        Label confirmPasswordLabel = new Label("Confirm New Password:");
        TextField confirmPasswordField = new TextField();
        confirmPasswordField.setPromptText("Confirm New Password");

        Button backButton = new Button("Back");
        Button changePasswordButton = new Button("Change Password");
        Label messageLabel = new Label();

        backButton.setOnAction(e -> {
            student.start(primaryStage);
        });
        // Set action for the change password button
        changePasswordButton.setOnAction(e -> {
            String currentPassword = currentPasswordField.getText();
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            boolean isPasswordValid = true;

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() ) {
                error.showErrorWindow("Please fill in all fields.");
                isPasswordValid = false;
            } else if (!newPassword.equals(confirmPassword)) {
                error.showErrorWindow("New passwords do not match.");
                isPasswordValid = false;
            } else if (!usersData.executeQueryWithSingleQuotes("SELECT * FROM Users WHERE email = '" + login.userAccount + "' AND password = '" + currentPassword + "';")) {
                error.showErrorWindow("Your data is wrong!");

                isPasswordValid = false;
            }

            if (isPasswordValid = true) {
                currentPasswordField.clear();
                newPasswordField.clear();
                confirmPasswordField.clear();
                usersData.executeQueryWithSingleQuotes("UPDATE Users SET password = '" + newPassword + "' WHERE email = '" + login.userAccount + "'");
                student.start(primaryStage);
            }
        });

        //layout
        VBox layout = new VBox(10); //10px spacing between components
        layout.setPadding(new Insets(20)); //20px padding around the layout
        layout.setAlignment(Pos.CENTER); //Center everything in the window
        layout.getChildren().addAll(titleLabel, currentPasswordLabel, currentPasswordField, newPasswordLabel,
                newPasswordField, confirmPasswordLabel, confirmPasswordField, changePasswordButton, messageLabel);

        //scene and stage
        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setTitle("Change Password");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void unsubscribeConfirmation(Stage primaryStage) {
        StudentPage student = new StudentPage();
        Login login = new Login();
        UsersDataLoad usersData = new UsersDataLoad();

        //label for confirmation message
        Label confirmationLabel = new Label("Are you sure you want to unsubscribe?");

        //yes button
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            usersData.executeQueryWithSingleQuotes("delete from Users where employeesId = (SELECT employeesId FROM Users WHERE email = '" + login.userAccount + "')");
            login.start(primaryStage);
        });

        // No button
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            student.start(primaryStage);
        });

        // Layout
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(confirmationLabel, yesButton, noButton);

        // Scene and Stage 300x200
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Unsubscribe Confirmation");
        primaryStage.setScene(scene);
        primaryStage.show();
    } 

    public static void main(String[] args) {
        launch(args);
    }
}