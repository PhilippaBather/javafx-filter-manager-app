package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.task.FilterTask;
import com.batherphilippa.filterapp.utils.FileUtils;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.Constants.FILE_NAME_SUFFIX_TEMP;

public class ImageController implements Initializable {

    private File file;
    private FilterTask filterTask; // TODO - refactor

    @FXML
    private ProgressBar pbFilter;

    @FXML
    private Label lbFilterStatus;

    @FXML
    private Button btCancel;

    public ImageController(File file) {
        this.file = file;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("In Image Controller...");
        String newName = FileUtils.setFileNameAndPath(file, FILE_NAME_SUFFIX_TEMP);
        File tempFile = new File(newName);
        filterTask = new FilterTask(file, tempFile);
        filterTask.stateProperty().addListener(((observableValue, state, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Filtro aplicado.");
                alert.show();
            }
        }));

        // actualiza el mensaje del porcentaje de progreso
        filterTask.messageProperty().addListener(((observableValue, msg, newMsg) -> {
            lbFilterStatus.setText(newMsg);
        }));
        // actualiza el estado de la barra de progreso
        filterTask.progressProperty().addListener((observableValue, number, t1) -> pbFilter.setProgress(t1.doubleValue()));
        new Thread(filterTask).start();
    }

    @FXML
    void cancelApplyFilter(ActionEvent event) {
        System.out.println("Cancel pressed");
    }
}
