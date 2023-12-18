package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.utils.FileUtils;
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

import static com.batherphilippa.filterapp.constants.Constants.*;
import static com.batherphilippa.filterapp.filter.FilterType.*;

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

    private ObservableList<String> filterOptions = FXCollections.observableArrayList(GREY_SCALE,
            COLOR_INVERSION, INCREASED_BRIGHTNESS, BLUR);
    private List<File> files;

    private Tab tab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lvFilterSelection.setItems(filterOptions);
        lvFilterSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tpFilterTabManager.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
    }

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
            // multiples archivos elegidos
            List<File> tempFiles = FileUtils.getMultipleFilesFromChooser(radBtnMultipleFiles);
            if (tempFiles != null) {
                // para modificar la lista de archivos de forma concurrente
                files = new CopyOnWriteArrayList<>(tempFiles);
            }
        }
    }

    // menu items
    @FXML
    void closeApp(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void goToSelectPathView(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML_FILE_PATH + "path_selection.fxml"));
        loader.setController(new PathSelectionController());
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void openLogHistoryFile(ActionEvent event) {
        System.out.println("Menu item open log history clicked.");
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

    @FXML
    private void applyFilters() {
        List<String> selectedItems = lvFilterSelection.getSelectionModel().getSelectedItems();

        if(selectedItems.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Elige filtros para continuar.");
            alert.show();
            return;
        }

        // CopyOnWriteArrayList es Thread Safe
        // permite eliminar elementos de la list usando un Iterator de forma concurrente
        Iterator<File> fileIterator = files.listIterator();
        if (files != null) {
            while(fileIterator.hasNext()) {
                File file = fileIterator.next();
                try {
                    launchImageController(file, selectedItems);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                files.remove(file);
            }
        }

        // de-seleccionar el toggle button elegido
        fileSelection.getSelectedToggle().setSelected(false);
    }

    private void launchImageController(File file, List<String> selectedFilters) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(FXML_FILE_PATH + "progress_pane.fxml"));
        ImageController imageController = new ImageController(file, selectedFilters);
        loader.setController(imageController);
        openImageTab(loader, file, imageController);
    }

    private void openImageTab(FXMLLoader loader, File file, ImageController imageController) {
        String tabName = file.getName();
        try {
            tab = new Tab(tabName, loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        imageController.setTab(tab); // pasa el tab a la instancia de FilterTask
        tpFilterTabManager.getTabs().add(tab);
    }

}
