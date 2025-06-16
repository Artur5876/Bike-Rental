module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Required for JDBC APIs
    requires com.microsoft.sqlserver.jdbc;
    requires javafx.graphics;
    requires javafx.base; // Add this line for the SQL Server JDBC driver

    opens com.example to javafx.fxml;
    exports com.example;
}