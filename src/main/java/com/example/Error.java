package com.example;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Error {
    public void showErrorWindow(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);

        //add custom buttons
        ButtonType retryButton = new ButtonType("Retry");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(retryButton, cancelButton);

        //show the error window and handle the user's response
        alert.showAndWait().ifPresent(response -> {
            if (response == retryButton) {
                System.out.println("User clicked Retry");
            } else if (response == cancelButton) {
                System.out.println("User clicked Cancel");
            }
        });
    }
}
