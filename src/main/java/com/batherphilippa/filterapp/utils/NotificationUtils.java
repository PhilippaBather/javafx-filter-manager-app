package com.batherphilippa.filterapp.utils;

import javafx.scene.control.Alert;

public class NotificationUtils {

    public static void showAlertDialog(String msg, Alert.AlertType alertType) {
            Alert alert = new Alert(alertType);
            alert.setContentText(msg);
            alert.show();
    }
}
