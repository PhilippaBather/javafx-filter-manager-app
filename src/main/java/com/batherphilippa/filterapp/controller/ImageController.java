package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.task.FilterTask;
import com.batherphilippa.filterapp.utils.FileUtils;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.Constants.FILE_NAME_SUFFIX_TEMP;

public class ImageController implements Initializable {

    private File file;
    private List<String> selectedFilters;
    private FilterTask filterTask;

    @FXML
    private ProgressBar pbFilter;

    @FXML
    private Label lbFilterStatus;

    @FXML
    private Button btCancel;
    private Tab tab;


    public ImageController(File file, List<String> selectedFilters) {
        this.file = file;
        this.selectedFilters = selectedFilters;
        this.tab = new Tab();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("In Image Controller...");
        String newName = FileUtils.setFileNameAndPath(file, FILE_NAME_SUFFIX_TEMP);

        File tempFile = new File(newName);
        filterTask = new FilterTask(file, tempFile, selectedFilters);

        filterTask.stateProperty().addListener(((observableValue, state, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Filtro aplicado a una copia de %s.", file.getName()));
                alert.show();
            }
            if (newState == Worker.State.CANCELLED) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Filtro cancelado para el archivo %s.", file.getName()));
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
    private void cancelApplyFilter(ActionEvent event) {
        filterTask.cancel();
    }

    /**
     * Asigna el objeto Tab y establece el EventHandler setOnClosed.
     * El método setOnClosed permite que el Task está cancelado cuando el tab esté cerrado.
     *
     * @param tab - contiene la información sobre el archivo y los filtros aplicados que corresponde con this.filterTask.
     */
    public void setTab(Tab tab) {
        this.tab = tab;
        tab.setOnClosed(e -> filterTask.cancel());
    }


}
