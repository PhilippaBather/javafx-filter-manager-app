package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.domain.ConfigurationDataSingleton;
import com.batherphilippa.filterapp.utils.FileUtils;
import com.batherphilippa.filterapp.utils.InputUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.FileConstants.IMAGE_FILE_PATH;
import static com.batherphilippa.filterapp.constants.MessageConstants.UI_NOTIFICATION_ERROR_INVALID_INT_INPUT;
import static com.batherphilippa.filterapp.constants.MessageConstants.UI_NOTIFICATION_INFO_SELECTED_PATH;

/**
 * ConfigurationController - maneja la selección del path donde las imagenes están guardas.
 *
 * @author Philippa Bather
 */
public class ConfigurationController implements Initializable {
    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lbSelectedPath;
    @FXML
    private RadioButton radBtnDefaultPath;
    @FXML
    private RadioButton radBtnChoosePath;
    @FXML
    private ToggleGroup tgGrpPathSelection;
    @FXML
    private RadioButton radBtnDefaultMaxImg;
    @FXML
    private RadioButton radBtnChooseMaxImg;
    @FXML
    private ToggleGroup tgGrpMaxImgFilesSelection;
    @FXML
    private Text txtError;
    @FXML
    private TextField txtfldMaxImg;

    private String initialPathValue;
    private String newPathValue;
    private boolean isMaxFilesSet;
    private Stage stage;

    ConfigurationDataSingleton configurationDataSingleton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurationDataSingleton = ConfigurationDataSingleton.getInstance();
        initialPathValue = configurationDataSingleton.getPath();

        if (!configurationDataSingleton.isPathDefault()) {
            // pinta el path no por defecto/cambiado en la pantalla
            radBtnChoosePath.setSelected(true);
            lbSelectedPath.setText(UI_NOTIFICATION_INFO_SELECTED_PATH + initialPathValue);
        }

        isMaxFilesSet = configurationDataSingleton.isMaxImageFiles();
        if (isMaxFilesSet) {
            // pinta el límite elegido por el usuario en la pantalla
            radBtnChooseMaxImg.setSelected(true);
            txtfldMaxImg.setText(String.valueOf(configurationDataSingleton.getMaxImageFiles()));
        }

        newPathValue = null;
    }


    /**
     * Maneja la cancelación de la selección de un path por el usuario.
     * @param event on click
     */
    @FXML
    void handleCancelPathSelection(ActionEvent event) {
        // cast: porque Window es un súper class de Stage y no hay acesso al Stage
        stage = (Stage) btnCancel.getScene().getWindow();
        configurationDataSingleton.setPath(initialPathValue);
        stage.close();
    }

    /**
     * Maneja la selección de un path por el usuario.
     * @param event on click
     */
    @FXML
    void handlePathSelection(ActionEvent event) {
        if (radBtnDefaultPath.isSelected()) {
            newPathValue = IMAGE_FILE_PATH;
            lbSelectedPath.setText("");
        } else if (radBtnChoosePath.isSelected()) {
            File directory = FileUtils.getDirectoryFromChooser(radBtnChoosePath);
            if (directory != null) {
                // añade la barra para para guardar archivos dentro la carpeta
                newPathValue = directory.getAbsolutePath() + "/";
                lbSelectedPath.setText(newPathValue);
            }
        }
    }

    /**
     * Maneja el número máximo de imagenes para procesar.
     * @param event - click event
     */
    @FXML
    void handleMaxImgSelection(ActionEvent event) {
        if(radBtnDefaultMaxImg.isSelected()) {
            // por defecto: text field de max imagenes está vacío y deshabilitado
            txtfldMaxImg.setText("");
            txtfldMaxImg.setDisable(true);
            isMaxFilesSet = false;
        } else if(radBtnChooseMaxImg.isSelected()) {
            txtfldMaxImg.setDisable(false);
            isMaxFilesSet = true;
        }
    }

    /**
     * Maneja la confirmación de la selección de un path por el usuario.
     * @param event click event
     */
    @FXML
    void handleSubmit(ActionEvent event) {
        // cast: porque Window es un súper class de Stage y no hay acesso al Stage
        stage = (Stage) btnApply.getScene().getWindow();
        if (newPathValue == null) {
            // guard clause: null pointer exception; mantener el path pre-seleccionado
            configurationDataSingleton.setPath(initialPathValue);
            configurationDataSingleton.setPathDefault(true);
        } else  {
            // establece el nuevo path
            configurationDataSingleton.setPath(newPathValue);
            configurationDataSingleton.setPathDefault(false);
        }

        if(isMaxFilesSet) {
            int maxFiles = InputUtils.validateIntegerInput(txtfldMaxImg.getText());
            if (maxFiles > 0) {
                // establece el límite de archivos
                configurationDataSingleton.setMaxImageFiles(maxFiles);
                configurationDataSingleton.setMaxImageFiles(true);
            } else {
                // notifica al usuario si la entrada es inválida
                txtError.setText(UI_NOTIFICATION_ERROR_INVALID_INT_INPUT);
                return;
            }
        } else {
            configurationDataSingleton.setMaxImageFiles(false);
        }

        stage.close();
    }
}
