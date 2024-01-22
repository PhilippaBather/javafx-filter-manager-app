package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.constants.MessageConstants;
import com.batherphilippa.filterapp.domain.ConfigurationDataSingleton;
import com.batherphilippa.filterapp.utils.FileUtils;
import com.batherphilippa.filterapp.utils.NotificationUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.batherphilippa.filterapp.constants.FileConstants.*;
import static com.batherphilippa.filterapp.constants.MessageConstants.UI_NOTIFICATION_INFO_MAX_FILES;
import static com.batherphilippa.filterapp.filter.FilterType.*;

/**
 * AppController - controla las funciones principales de la aplicación; implementa Initializable
 *
 * @author Philippa Bather
 */
public class AppController implements Initializable {

    @FXML
    private Menu menuSettings;

    @FXML
    private MenuItem menuItemSelectPath;

    @FXML
    private MenuItem menuItemLogHistory;

    @FXML
    private MenuItem menuItemCloseApp;

    @FXML
    private Button applyFilters;

    @FXML
    private RadioButton radBtnOneFile;

    @FXML
    private ToggleGroup fileSelection;

    @FXML
    private RadioButton radBtnMultipleFiles;

    @FXML
    private TabPane tpFilterTabManager;

    @FXML
    private ListView<String> lvFilterSelection;

    private final ObservableList<String> filterOptions = FXCollections.observableArrayList(GREY_SCALE,
            COLOR_INVERSION, INCREASED_BRIGHTNESS, BLUR);
    private List<File> files;
    ConfigurationDataSingleton configurationDataSingleton;

    public AppController() {
        configurationDataSingleton = ConfigurationDataSingleton.getInstance();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvFilterSelection.setItems(filterOptions);
        lvFilterSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tpFilterTabManager.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

    /**
     * Maneja la selección de archivos: uno o múltiples
     *
     * @param event onClick
     */
    @FXML
    public void handleFileSelection(ActionEvent event) {
        if (radBtnOneFile.isSelected()) {
            // un archivo elegido
            File file = FileUtils.getFileFromChooser(radBtnOneFile);
            if (file != null) {
                // para modificar la lista de archivos de forma concurrente
                files = new CopyOnWriteArrayList<>();
                files.add(file);
            }
        } else {
            // múltiples archivos elegidos
            List<File> files = FileUtils.getMultipleFilesFromChooser(radBtnMultipleFiles);

            // comprobar si hay límite de archivos establecidos
            int maxFiles = configurationDataSingleton.getMaxImageFiles();
            if (configurationDataSingleton.isMaxImageFiles()) {
                NotificationUtils.showAlertDialog(maxFiles + UI_NOTIFICATION_INFO_MAX_FILES, Alert.AlertType.INFORMATION);
                return;
            }

            if (files != null) {
                // para modificar la lista de archivos de forma concurrente
                this.files = new CopyOnWriteArrayList<>(files);
            }
        }
    }

    /**
     * Cierra la aplicación.
     *
     * @param event onClick
     */
    @FXML
    void closeApp(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Dirige al usuario a la pantalla para selecionar el path para guardar las imagenes
     *
     * @param event on menu item click
     */
    @FXML
    void goToSelectPathView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML_FILE_PATH + "configuration_selection.fxml"));
        loader.setController(new ConfigurationController());
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Abre la historial de los filtros.
     *
     * @param event on menu item click
     */
    @FXML
    void openLogHistoryFile(ActionEvent event) {
        Desktop desktop = Desktop.getDesktop();
        File logFile = new File(IMAGE_FILE_PATH + LOG_FILE_NAME + LOG_FILE_TYPE_TXT);
        if (logFile.exists()) {
            try {
                desktop.open(logFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Maneja la aplicación de filtros.
     */
    @FXML
    private void applyFilters() {
        List<String> selectedItems = lvFilterSelection.getSelectionModel().getSelectedItems();

        if (selectedItems.size() == 0) {
            NotificationUtils.showAlertDialog(MessageConstants.UI_NOTIFICATION_INFO_CHOOSE_FILTERS, Alert.AlertType.INFORMATION);
            return;
        }

        // CopyOnWriteArrayList es Thread Safe
        // permite la eliminación de elementos de la list usando un Iterator de forma concurrente
        if (files != null && files.size() > 0) {
            Iterator<File> fileIterator = files.listIterator();
            while (fileIterator.hasNext()) {
                File file = fileIterator.next();
                try {
                    launchImageController(file, selectedItems);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                files.remove(file);
            }
            // de-seleccionar el toggle button elegido
            fileSelection.getSelectedToggle().setSelected(false);
        } else {
            NotificationUtils.showAlertDialog(MessageConstants.UI_NOTIFICATION_INFO_CHOOSE_FILES, Alert.AlertType.INFORMATION);
        }
        lvFilterSelection.getSelectionModel().clearSelection();
    }

    /**
     * Lanza el ImageController
     *
     * @param file            imagen para procesar
     * @param selectedFilters filtros para aplicar
     */
    private void launchImageController(File file, List<String> selectedFilters) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_PATH + "progress_pane.fxml"));
        ImageController imageController = new ImageController(file, selectedFilters);
        loader.setController(imageController);
        openImageTab(loader, file, imageController);
    }

    /**
     * Abre el 'tab' que corresponde a la imagen.
     *
     * @param loader          para progress_pane.fxml
     * @param file            archivo correspondiente al tab
     * @param imageController controller para establecer el tab
     */
    private void openImageTab(FXMLLoader loader, File file, ImageController imageController) {
        String tabName = file.getName();
        Tab tab;
        try {
            tab = new Tab(tabName, loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageController.setTab(tab); // pasa el tab a la instancia de FilterTask
        tpFilterTabManager.getTabs().add(tab);
    }

}
