package com.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

public class AdminPage extends Application {
    Login login = new Login();  
    Label welcomeLabel = new Label("Welcome " + login.userAccount);

    TableView<Jobs> tableView = new TableView<>();
    public static ObservableList<Jobs> jobData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        JobsDataLoad jobsData = new JobsDataLoad();

        //all the components
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        Button approveJobsButton = new Button("Approve Jobs");
        Button viewAllJobsButton = new Button("View All Jobs");
        Button viewJobsByCompanyButton = new Button("View jobs by Department");
        Button viewJobsByJobDescriptionButton = new Button("View jobs by Job description");
        Button deleteJobButton = new Button("Delete Job");
        Button editJobButton = new Button("Edit Job");
        Button changePasswordButton = new Button("Change Password");
        Button logoutButton = new Button("Logout");

        // search section
        HBox searchBox = new HBox(10, searchField, searchButton);
        searchBox.setPadding(new Insets(10));
        searchBox.setVisible(false);

        //buttons section
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(10);
        buttonsGrid.setVgap(10);
        buttonsGrid.setPadding(new Insets(10));

        
        buttonsGrid.add(welcomeLabel, 0, 0, 2, 1);

        //add buttons to the grid, starting from row 1
        buttonsGrid.add(approveJobsButton, 0, 1);
        buttonsGrid.add(viewAllJobsButton, 1, 1);
        buttonsGrid.add(viewJobsByCompanyButton, 0, 2);
        buttonsGrid.add(viewJobsByJobDescriptionButton, 1, 2);
        buttonsGrid.add(deleteJobButton, 0, 3);
        buttonsGrid.add(editJobButton, 1, 3);
        buttonsGrid.add(changePasswordButton, 0, 4);
        buttonsGrid.add(logoutButton, 1, 4);

        // table columns generation
        TableColumn<Jobs, Integer> jobIdColumn = new TableColumn<>("Job ID");
        jobIdColumn.setCellValueFactory(new PropertyValueFactory<>("jobId"));

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

        tableView.getColumns().addAll(jobIdColumn, jobTitleColumn, jobDescriptionColumn, departmentColumn,
                locationColumn, closingDateColumn, postedDateColumn, statusColumn);

        //actions for buttons
        //this action will allow to approve pending job to admin
        approveJobsButton.setOnAction(e -> {
            approveJobForm(primaryStage);
        });

        //view all the jobs
        viewAllJobsButton.setOnAction(e -> {
            ObservableList<Jobs> loadedJobs = jobsData.loadDataFromDatabase("SELECT * FROM Jobs");
            if (loadedJobs != null) {
                jobData.setAll(loadedJobs);
                tableView.setItems(jobData);
            } else {
                System.out.println("No data found or error in database query.");
            }
        });

        //filter by department
        viewJobsByCompanyButton.setOnAction(e -> {
            searchBox.setVisible(true);
            searchField.setPromptText("Enter the department name");
        });

        //filter by job description
        viewJobsByJobDescriptionButton.setOnAction(e -> {
            searchBox.setVisible(true);
            searchField.setPromptText("Write the description");
        });

        //search by using filters
        searchButton.setOnAction(e -> {
            jobData.clear();
            String keyword = searchField.getText().trim();
            if (searchField.getPromptText().equals("Enter the department name")) {
                String query = keyword.isEmpty() ? "SELECT * FROM Jobs" : "SELECT * FROM Jobs WHERE department = '" + keyword + "'";
                jobData = jobsData.loadDataFromDatabase(query);
                tableView.setItems(jobData);
            } else if (searchField.getPromptText().equals("Write the description")) {
                String query = keyword.isEmpty() ? "SELECT * FROM Jobs" : "SELECT * FROM Jobs WHERE jobDescription = '" + keyword + "'";
                jobData = jobsData.loadDataFromDatabase(query);
                tableView.setItems(jobData);
            }
        });

        //action that allows to delete a record
        deleteJobButton.setOnAction(e -> {
            deleteJobForm(primaryStage);
        });

        //edit the record
        editJobButton.setOnAction(e -> {
            editJobForm(primaryStage);
        });

        //change the admin password
        changePasswordButton.setOnAction(e -> {
            UsersDataLoad usersData = new UsersDataLoad();
            changeAdminPasswordForm(primaryStage);
        });

        logoutButton.setOnAction(e -> {
            // Logic to log out the admin(close window)
            primaryStage.close(); 
            Login login = new Login();
            login.start(primaryStage);
        });

        //main Layout
        BorderPane root = new BorderPane();
        root.setTop(buttonsGrid);
        VBox centerBox = new VBox(10, searchBox, tableView); 
        centerBox.setPadding(new Insets(10));
        root.setCenter(centerBox);

        //scene and Stage
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void approveJobForm(Stage primaryStage) {
        AdminPage admin = new AdminPage();

        Label titleLabel = new Label("Approve Job");

        TextField jobIdField = new TextField();
        jobIdField.setPromptText("Enter Job ID");
        jobIdField.setMaxWidth(200);

        Button backButton = new Button("Back");
        Button approveButton = new Button("Approve");

        backButton.setOnAction(e -> {
            admin.start(primaryStage);
        });

        approveButton.setOnAction(event -> {
            Error error = new Error();
            UsersDataLoad usersData = new UsersDataLoad();

            String jobId = jobIdField.getText();
            boolean isValid = true;
            //try and catch exception that will check if jobId field is an integer to proceed next validation
            try {
                Integer.parseInt(jobId);
            } catch (Exception e) {
                isValid = false;
                error.showErrorWindow("Your JobID required to be in Integer format!\n");
            }

            //check if the jobId exist and job is pending (not approved)
            if (!usersData.executeQueryWithSingleQuotes("SELECT *FROM Jobs WHERE jobId = " + Integer.parseInt(jobId) + " AND status = 'pending'")) {
                isValid = false;
                error.showErrorWindow("JobId You have entered does not exist!\n");
            }

            //if all the infomation entered is valid --> execute the query 
            if (isValid) {
                usersData.executeQueryWithSingleQuotes("UPDATE Jobs SET status = 'Approved' WHERE jobId = '" + Integer.parseInt(jobId) + "'");
                admin.start(primaryStage);
            }
        });

        //create layout and set up the scene
        VBox root = new VBox(20, titleLabel, jobIdField, backButton, approveButton);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Admin Job Approval Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void editJobForm(Stage primaryStage) {
        AdminPage admin = new AdminPage();

        Error error = new Error();
        UsersDataLoad usersData = new UsersDataLoad();

        //components
        Label titleLabel = new Label("Edit Job");

        Label jobIdLabel = new Label("Enter Job ID:");
        TextField jobIdField = new TextField();
        jobIdField.setPromptText("Job ID");

        Label attributeLabel = new Label("Select Attribute to Edit:");
        ComboBox<String> attributeComboBox = new ComboBox<>();
        attributeComboBox.getItems().addAll("jobTitle", "jobDescription", "department", "location");
        attributeComboBox.setPromptText("Select Attribute");

        Label newValueLabel = new Label("Enter New Value:");
        TextField newValueField = new TextField();
        newValueField.setPromptText("New Value");

        Button backButton = new Button("Back");
        Button editButton = new Button("Edit");
        Label messageLabel = new Label();

        //back to admin page
        backButton.setOnAction(e -> {
            admin.start(primaryStage);
        });

        //edit job record
        editButton.setOnAction(e -> {
            String jobId = jobIdField.getText().trim();
            String selectedAttribute = attributeComboBox.getValue();
            String newValue = newValueField.getText().trim();

            if (jobId.isEmpty() || selectedAttribute == null || newValue.isEmpty()) {
                error.showErrorWindow("Please enter Job ID, select an attribute, and enter a new value!");
            }

            if (!jobId.matches("\\d+")) {
                error.showErrorWindow("Job ID must be a valid integer!");
            }

            int jobIdInt = Integer.parseInt(jobId);

            if (!usersData.executeQueryWithSingleQuotes("SELECT * FROM Jobs WHERE jobId = " + jobIdInt)) {
                error.showErrorWindow("The Job ID entered does not exist!");
            }

            String updateQuery = "UPDATE Jobs SET " + selectedAttribute + " = '" + newValue + "' WHERE jobId = " + jobIdInt;
            usersData.executeQueryWithSingleQuotes(updateQuery);

            jobIdField.clear();
            attributeComboBox.getSelectionModel().clearSelection();
            newValueField.clear();

            admin.start(primaryStage);
        });

        //layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, jobIdLabel, jobIdField, attributeLabel, attributeComboBox,
                newValueLabel, newValueField, backButton, editButton, messageLabel);

        //scene and Stage
        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setTitle("Edit Job");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void deleteJobForm(Stage primaryStage) {
        Error error = new Error();
        UsersDataLoad usersData = new UsersDataLoad();

        //allcomponents
        Label titleLabel = new Label("Delete Job");

        Label jobIdLabel = new Label("Enter Job ID:");
        TextField jobIdField = new TextField();
        jobIdField.setPromptText("Job ID");

        Button backButton = new Button("Back");
        Button deleteButton = new Button("Delete");

        backButton.setOnAction(e -> {
            EmployerPage employer = new EmployerPage();
            employer.start(primaryStage);
        });

        deleteButton.setOnAction(e -> {
            String jobId = jobIdField.getText();
            boolean isQueryValid = false;

            //check if jobId field is not empty
            if (jobId.isEmpty()) {
                error.showErrorWindow("Please enter a Job ID.");
                isQueryValid = false;
            }
            //if jobId field doesnt contains an integer value
            if (!jobId.matches(".*\\d.*")) {
                error.showErrorWindow("Your Id contains only digits! Integer value!\n");
                isQueryValid = false;
            }
            //if record does not exists
            if (!usersData.executeQueryWithSingleQuotes("select *FROM Jobs WHERE jobId = " + Integer.parseInt(jobId))) {
                error.showErrorWindow("Job ID that you have entered, does not exist!\n");
                isQueryValid = false;
            } 
            //query execution
            else {
                usersData.executeQueryWithSingleQuotes("DELETE FROM Jobs WHERE jobId = " + Integer.parseInt(jobId));
                jobIdField.clear();
                AdminPage admin = new AdminPage();
                admin.start(primaryStage);
                isQueryValid = true;
            }
        });

        //ayout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, jobIdLabel, jobIdField, deleteButton);

        //scene and Stage
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setTitle("Delete Job");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void changeAdminPasswordForm(Stage primaryStage) {
        AdminPage admin = new AdminPage();
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

        backButton.setOnAction(e ->{
            admin.start(primaryStage);
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
                admin.start(primaryStage);
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
    public static void main(String[] args) {
        launch(args);
    }
}