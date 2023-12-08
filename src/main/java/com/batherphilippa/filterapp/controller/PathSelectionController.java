package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.domain.PathDataSingleton;
import com.batherphilippa.filterapp.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.Constants.IMAGE_FILE_PATH;

public class PathSelectionController implements Initializable {
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;
    @FXML
    private RadioButton radBtnDefaultPath;
    @FXML
    private RadioButton radBtnChoosePath;
    @FXML
    private ToggleGroup tgGrpPathSelection;

    PathDataSingleton pathDataSingleton;

    private String initialPathValue;
    private String newPathValue;
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pathDataSingleton = PathDataSingleton.getInstance();
        initialPathValue = pathDataSingleton.getPath();
        newPathValue = null;
    }


    @FXML
    void handleCancelPathSelection(ActionEvent event) {
        // cast: porque Window es un súper class de Stage y no hay acesso al Stage
        stage = (Stage) btnCancel.getScene().getWindow();
        pathDataSingleton.setPath(initialPathValue);
        System.out.println("On cancel: path saved: " + pathDataSingleton.getPath());
        stage.close();
    }

    @FXML
    void handlePathSelection(ActionEvent event) {
        if (radBtnDefaultPath.isSelected()) {
            newPathValue = IMAGE_FILE_PATH;
        } else if (radBtnChoosePath.isSelected()) {
            File directory = FileUtils.getDirectoryFromChooser(radBtnChoosePath);
            if (directory != null) {
                // añade la barra para para guardar archivos dentro la carpeta
                newPathValue = directory.getAbsolutePath() + "/";
            }
        }
    }

    @FXML
    void handleSubmit(ActionEvent event) {
        // cast: porque Window es un súper class de Stage y no hay acesso al Stage
        stage = (Stage) btnApply.getScene().getWindow();
        if (newPathValue == null) {
            // guard clause: null pointer exception; mantener el path pre-seleccionado
            pathDataSingleton.setPath(initialPathValue);
        } else  {
            // establece el nueve path
            pathDataSingleton.setPath(newPathValue);
        }
        stage.close();
    }
}
