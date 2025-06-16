package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UsersDataLoad {

    // database connection details
    private static final String CONNECTION_URL = "jdbc:sqlserver://sql.coccork.ie:8443;databaseName=JobRegisterDB;user=artur.romanov@morrisonsislandcampus.com;password=VDLH8804a@;encrypt=true;trustServerCertificate=true";

    // boolean value that will validate the user input
    // public static boolean isValid = false;

    // method to load employee data from the database
    public ObservableList<Employees> loadDataFromDatabase(String query) {
        ObservableList<Employees> employeeData = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL)) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");

                try (PreparedStatement statement = connection.prepareStatement(query);
                        ResultSet resultSet = statement.executeQuery()) {

                    //populate the ObservableList with data from the database
                    while (resultSet.next()) {
                        int employeeID = resultSet.getInt("employeesId");
                        String firstName = resultSet.getString("first_name");
                        String lastName = resultSet.getString("last_name");
                        String email = resultSet.getString("email");
                        String jobPosition = resultSet.getString("user_type");
                        String hireDate = resultSet.getString("hireDate");
                        double salary = resultSet.getDouble("salary");
                        int departmentNum = resultSet.getInt("departmentNum");

                        employeeData.add(new Employees(employeeID, firstName, lastName, email, jobPosition, hireDate,
                                salary, departmentNum));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to the database or fetching data: " + e.getMessage());
        }

        return employeeData;
    }

    public boolean executeQueryWithSingleQuotes(String query) {
        boolean isValid = false;

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            //check if the query returns any rows
            if (resultSet.next()) {
                //aaaaassuming the query is a COUNT query
                //count value from the first column
                int count = resultSet.getInt(1);
                isValid = count > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }

        return isValid;
    }

    public static String executeQueryAndReturnValue(String query) {
        String result = null;

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            //check if the query returned any rows
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (SQLException e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }



    public String getEmployeesId(String query) {
        String employeesId = null;

        try (Connection connection = DriverManager.getConnection(CONNECTION_URL);
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery()) {

            //check if the result set has data
            if (resultSet.next()) {
                //retrieve the employeesId from the result set
                employeesId = resultSet.getString("employeesId");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return employeesId;
    }


    
}