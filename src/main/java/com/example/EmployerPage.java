package com.example;

import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployerPage extends Application {

    //separate tables for Employees and Jobs
    TableView<Employees> employeeTable = new TableView<>();
    TableView<Jobs> jobTable = new TableView<>();

    UsersDataLoad usersData = new UsersDataLoad();
    Login login = new Login();
    Error error = new Error();

    //observable lists to hold data
    public static ObservableList<Employees> employeeData = FXCollections.observableArrayList();
    public static ObservableList<Jobs> jobData = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        Login login = new Login();
        
        Label welcomeLabel = new Label("Welcome " + login.userAccount);
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;"); 

        //all components
        Button addJobButton = new Button("Add  New Job");
        Button viewJobsButton = new Button("View My Jobs");
        Button editJobButton = new Button("Edit Jobs");
        Button showEmployeesButton = new Button("Show all Employees");
        Button deleteRecordButton = new Button("Delete Job");
        Button changeMyPasswordButton = new Button("Change my password");
        Button logoutButton = new Button("Logout");

        //buttons section GridPane layout
        GridPane buttonsGrid = new GridPane();
        buttonsGrid.setHgap(10); // Horizontal gap between buttons
        buttonsGrid.setVgap(10); // Vertical gap between buttons
        buttonsGrid.setPadding(new Insets(10));

        buttonsGrid.add(welcomeLabel, 0, 0, 3, 1);

        buttonsGrid.add(deleteRecordButton, 0, 1);
        buttonsGrid.add(addJobButton, 1, 1);
        buttonsGrid.add(showEmployeesButton, 0, 2);
        buttonsGrid.add(viewJobsButton, 1, 2);
        buttonsGrid.add(editJobButton, 0, 3);
        buttonsGrid.add(changeMyPasswordButton, 1, 3);
        buttonsGrid.add(logoutButton, 2, 1);

        //configure Employee Table Columns
        configureEmployeeTable();

        //configure Job Table Columns
        configureJobTable();

        //action that will call method to delete employee record
        deleteRecordButton.setOnAction(e -> {
            deleteJobForm(primaryStage);
        });
        //action that will call method to edit employee record
        editJobButton.setOnAction(e -> {
            editJobForm(primaryStage);
        });
        //action to show all employees at the table
        showEmployeesButton.setOnAction(e -> {
            UsersDataLoad usersData = new UsersDataLoad();
            employeeData = usersData.loadDataFromDatabase("SELECT * FROM Users");
            employeeTable.setItems(employeeData);
        });

        //action that will call method to change employer password record record
        changeMyPasswordButton.setOnAction(e -> {
            changeEmployerPasswordForm(primaryStage);
            
        });

        //action that will call method to add a new record
        addJobButton.setOnAction(e -> {
            addJobForm(primaryStage);
        });

        //view all jobs that is posted by employer who logged in into account
        viewJobsButton.setOnAction(e -> {
            JobsDataLoad jobsData = new JobsDataLoad();
            jobData = jobsData.loadDataFromDatabase(
                    "DECLARE @employerId INT; " +
               "SELECT @employerId = employeesId FROM Users WHERE email = '" + login.userAccount + "'; " +
               "SELECT *FROM Jobs WHERE employerId = @employerId;");
            jobTable.setItems(jobData);
        });

        logoutButton.setOnAction(e -> {
            //logic to log out the empl(close window)
            primaryStage.close(); 
            login.start(primaryStage);
        });
        //layout
        BorderPane root = new BorderPane();
        root.setTop(buttonsGrid); // Place the button grid at the top

        //container for the tables
        VBox tablesBox = new VBox(10, employeeTable, jobTable);
        tablesBox.setPadding(new Insets(10));

        //search section and tables together
        VBox centerBox = new VBox(10, tablesBox);
        centerBox.setPadding(new Insets(10));
        root.setCenter(centerBox);

        // Scene and stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Employer Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Configure Employee Table Columns
    private void configureEmployeeTable() {
        TableColumn<Employees, Integer> employeeIdColumn = new TableColumn<>("Employee ID");
        employeeIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeID"));

        TableColumn<Employees, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Employees, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Employees, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Employees, String> jobPositionColumn = new TableColumn<>("Job Position");
        jobPositionColumn.setCellValueFactory(new PropertyValueFactory<>("jobPosition"));

        TableColumn<Employees, String> hireDateColumn = new TableColumn<>("Hire Date");
        hireDateColumn.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        TableColumn<Employees, Double> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        TableColumn<Employees, Integer> departmentNumColumn = new TableColumn<>("Department Number");
        departmentNumColumn.setCellValueFactory(new PropertyValueFactory<>("departmentNum"));

        employeeTable.getColumns().addAll(employeeIdColumn, firstNameColumn, lastNameColumn, emailColumn,
                jobPositionColumn, hireDateColumn, salaryColumn, departmentNumColumn);
    }

    //Job Table Columns
    private void configureJobTable() {
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

        jobTable.getColumns().addAll(jobIdColumn, employerIdColumn, jobTitleColumn, jobDescriptionColumn,
                departmentColumn, locationColumn, closingDateColumn, postedDateColumn);
    }

    public void addJobForm(Stage primaryStage) {

        EmployerPage employer = new EmployerPage();

        //gridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        //all textfields
        TextField jobTitleField = new TextField();
        TextField jobDescriptionField = new TextField();
        TextField departmentField = new TextField();
        TextField locationField = new TextField();
        DatePicker closingDatePicker = new DatePicker();
        closingDatePicker.getEditor().setDisable(true);

        //gridpane
        grid.add(new Label("Job Title:"), 0, 2);
        grid.add(jobTitleField, 1, 2);
        grid.add(new Label("Job Description:"), 0, 3);
        grid.add(jobDescriptionField, 1, 3);
        grid.add(new Label("Department:"), 0, 4);
        grid.add(departmentField, 1, 4);
        grid.add(new Label("Location:"), 0, 5);
        grid.add(locationField, 1, 5);
        grid.add(new Label("Closing Date:"), 0, 6);
        grid.add(closingDatePicker, 1, 6);

        Button backButton = new Button("Back");
        Button submitButton = new Button("Submit");
        grid.add(submitButton, 1, 9);
        grid.add(backButton, 0, 9);

        //back to the employer page
        backButton.setOnAction(e -> {
            employer.start(primaryStage);
        });

        //submit button, will validate all the data and setup to the table records
        submitButton.setOnAction(e -> {
            String jobTitle = jobTitleField.getText();
            String jobDescription = jobDescriptionField.getText();
            String department = departmentField.getText();
            String location = locationField.getText();
            LocalDate closingDate = closingDatePicker.getValue();
            if (validateJobFields(jobTitle, jobDescription, department, location, closingDate).equals("")) {
                //string that will hold the id of employer(who is posting a new job)
                int employerID = Integer.parseInt(usersData.getEmployeesId("SELECT employeesId FROM Users WHERE email = '" + login.userAccount + "';"));
                usersData.executeQueryWithSingleQuotes(
                        "INSERT INTO Jobs (employerId, jobTitle, jobDescription, department, location, closingDate, postedDate, status) "
                                +
                                "VALUES (" + employerID + ", '" + jobTitle + "', '" + jobDescription + "', '"
                                + department + "', '" +
                                location + "', '" + closingDate + "', '" + LocalDate.now() + "', 'pending')");
                employer.start(primaryStage);

                //removing all data entered by user from textfields
                jobTitleField.clear();
                jobDescriptionField.clear();
                departmentField.clear();
                locationField.clear();
                closingDatePicker.setValue(null);
            } 
            //error message, if some data is wrong
            else {
                Error error = new Error();
                error.showErrorWindow(validateJobFields(jobTitle, jobDescription, department, location, closingDate));
            }
        });

        //scene and set it on the stage
        Scene scene = new Scene(grid, 400, 400);
        primaryStage.setTitle("Job Submission Form");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public String validateJobFields(String jobTitle, String jobDescription, String department, String location,
            LocalDate closingDate) {
        //validate job title
        String error = "";
        if (jobTitle == null || jobTitle.trim().isEmpty()) {
            error += "Job title cannot be empty!\n";
        }
        if (jobTitle.length() < 5 || jobTitle.length() > 100) {
            error += "Job title must be between 5 and 100 characters!\n";
        }

        // Validate job description
        if (jobDescription == null || jobDescription.trim().isEmpty()) {
            error += "Job description cannot be empty!\n";
        }
        if (jobDescription.length() < 10) {
            error += "Job description must be at least 10 characters long!\n";
        }

        //validate department
        if (department == null || department.trim().isEmpty()) {
            error += "Department cannot be empty!\n";
        }

        // Validate location
        if (location == null || location.trim().isEmpty()) {
            error += "Location cannot be empty!\n";
        }

        // Validate closing date
        if (closingDate == null) {
            error += "Closing date cannot be empty!\n";
        } else if (closingDate.isBefore(LocalDate.now())) {
            error += "Closing date must be in the future!\n";
        }

        return error;
    }

    public void deleteJobForm(Stage primaryStage) {
        // UI components
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
        // Set action for the delete button
        deleteButton.setOnAction(e -> {
            String jobId = jobIdField.getText();
            boolean isQueryValid = false;
            if (jobId.isEmpty()) {
                error.showErrorWindow("Please enter a Job ID.");
                isQueryValid = false;
            }
            if (!jobId.matches(".*\\d.*")) {
                error.showErrorWindow("Your Id contains only digits! Integer value!\n");
                isQueryValid = false;
            }
            if (!usersData.executeQueryWithSingleQuotes("select *FROM Jobs WHERE jobId = " + Integer.parseInt(jobId)+ 
            " AND employerId = (SELECT employeesId FROM Users WHERE email = '" + login.userAccount + "') and closingDate > GETDATE()") ) {
                error.showErrorWindow("Job ID that you have entered, does not exist!\n");
                isQueryValid = false;
            } else {

                usersData.executeQueryWithSingleQuotes("DELETE FROM Jobs WHERE jobId = " + Integer.parseInt(jobId)+ 
                " AND employerId = (SELECT employeesId FROM Users WHERE email = '" + login.userAccount + "')");
                jobIdField.clear(); // Clear the input field
                EmployerPage employer = new EmployerPage();
                employer.start(primaryStage);
                isQueryValid = true;
            }
        });

        //layout
        VBox layout = new VBox(10); // 10px spacing between components
        layout.setPadding(new Insets(20)); // 20px padding around the layout
        layout.setAlignment(Pos.CENTER); // Center everything in the window
        layout.getChildren().addAll(titleLabel, jobIdLabel, jobIdField, backButton, deleteButton);

        //scene and Stage
        Scene scene = new Scene(layout, 300, 200); // Window size: 300x200
        primaryStage.setTitle("Delete Job");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void editJobForm(Stage primaryStage) {

        EmployerPage employer = new EmployerPage();

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

        backButton.setOnAction(e -> {
            employer.start(primaryStage);
        });

        //set action for the edit button
        editButton.setOnAction(e -> {
            String jobId = jobIdField.getText().trim();
            String selectedAttribute = attributeComboBox.getValue();
            String newValue = newValueField.getText().trim();

            //validator for user-inputs
            boolean isValid = true;

            //input fields validation
            if (jobId.isEmpty() || selectedAttribute == null || newValue.isEmpty()) {
                error.showErrorWindow("Please enter Job ID, select an attribute, and enter a new value!");
                isValid = false;
            }

            //check if JobID is an integer
            if (!jobId.matches("\\d+")) {
                error.showErrorWindow("Job ID must be a valid integer!");
                isValid = false;
            }

            //convert to integer
            int jobIdInt = Integer.parseInt(jobId);

            //check if job exists
            if (!usersData.executeQueryWithSingleQuotes("SELECT * FROM Jobs WHERE jobId = " + jobIdInt + 
               " AND employerId = (SELECT employeesId FROM Users WHERE email = '" + login.userAccount + "')")) {
                error.showErrorWindow("The Job ID entered does not exist!");
                isValid = false;
            }

            if (isValid) {
                //query that will update the record in Jobs table
                String updateQuery = "UPDATE Jobs SET " + selectedAttribute + " = '" + newValue + "' WHERE jobId = "
                + jobIdInt+ 
                " AND employerId = (SELECT employeesId FROM Users WHERE email = '" + login.userAccount + "')";
                usersData.executeQueryWithSingleQuotes(updateQuery);

                //clear input fields after successful update
                jobIdField.clear();
                attributeComboBox.getSelectionModel().clearSelection();
                newValueField.clear();

                //edirect to Employer Page
                employer.start(primaryStage);
            }
        });

        // layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(titleLabel, jobIdLabel, jobIdField, attributeLabel, attributeComboBox,
                newValueLabel, newValueField, backButton, editButton, messageLabel);

        // Scene and Stage
        Scene scene = new Scene(layout, 400, 350);
        primaryStage.setTitle("Edit Job");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void changeEmployerPasswordForm(Stage primaryStage) {
        Error error = new Error();
        EmployerPage employer = new EmployerPage();
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
            employer.start(primaryStage);
        });

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
                employer.start(primaryStage);
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