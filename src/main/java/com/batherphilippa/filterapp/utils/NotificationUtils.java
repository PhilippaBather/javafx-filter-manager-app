package com.batherphilippa.filterapp.utils;

import javafx.scene.control.Alert;

/**
 * NotificationUtils - maneja la presentación de alerts.
 *
 * @author Philppa Bather
 */
public class NotificationUtils {

    public static void showAlertDialog(String msg, Alert.AlertType alertType) {
            Alert alert = new Alert(alertType);
            alert.setContentText(msg);
            alert.show();
    }
}
