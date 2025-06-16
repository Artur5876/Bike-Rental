package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class JobsDataLoad {

    // database connection details
    private static final String CONNECTION_URL = "jdbc:sqlserver://sql.coccork.ie:8443;databaseName=JobRegisterDB;user=artur.romanov@morrisonsislandcampus.com;password=VDLH8804a@;encrypt=true;trustServerCertificate=true";

    // boolean value that will validate the user input
    // public static boolean isValid = false;

    // method to load employee data from the database
    public ObservableList<Jobs> loadDataFromDatabase(String query) {
        ObservableList<Jobs> jobData = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");

               
                try (PreparedStatement statement = connection.prepareStatement(query);
                        ResultSet resultSet = statement.executeQuery()) {

                    // Populate the ObservableList with data from the database
                    while (resultSet.next()) {
                        int jobId = resultSet.getInt("jobId");
                        int employerId = resultSet.getInt("employerId");
                        String jobTitle = resultSet.getString("jobTitle");
                        String jobDescription = resultSet.getString("jobDescription");
                        String department = resultSet.getString("department");
                        String location = resultSet.getString("location");
                        String closingDate = resultSet.getString("closingDate");
                        String postedDate = resultSet.getString("postedDate");
                        String status = resultSet.getString("status");


                        jobData.add(new Jobs(jobId, employerId, jobTitle, jobDescription, department,
                            location, closingDate, postedDate, status));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database or fetching data: " + e.getMessage());
        }

        return jobData;
    }

    public boolean executeQueryWithSingleQuotes(String query) {
        boolean isValid = false;

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Check if the query returns any rows
            if (resultSet.next()) {
                // Assuming the query is a COUNT query
                int count = resultSet.getInt(1); // Get the count value from the first column
                isValid = count > 0; // Set isValid to true only if count > 0
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
        }

        return isValid;
    }

    public static String executeQueryAndReturnValue(String query) {
        String result = null;

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            // Check if the query returned any rows
            if (resultSet.next()) {
                // Retrieve the value of the first column in the first row
                result = resultSet.getString(1); // Use getString(1) to get the first column
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace(); // Print full stack trace for debugging
        }

        return result;
    }

}