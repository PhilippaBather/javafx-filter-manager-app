package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.Constants.PATH;

public class AppController implements Initializable {

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

    private ObservableList<String> filterOptions = FXCollections.observableArrayList("Escala de Grises",
            "Inversión de Color", "Aumento de Brillo", "Difuminado de la Imagen");

    private List<File> files = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("App controller initialised...");
        lvFilterSelection.setItems(filterOptions);
        lvFilterSelection.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    public void handleFileSelection(ActionEvent event) throws IOException {
        if (radBtnOneFile.isSelected()) {
            // un archivo elegido
            File file = FileUtils.getFileFromChooser(radBtnOneFile);
            if (file != null) {
                files.add(file);
            }
        } else {
            // multiples archivos elegidos
            files = FileUtils.getMultipleFilesFromChooser(radBtnMultipleFiles);
        }
    }

    @FXML
    private void applyFilters() {
        List<String> selectedItems = lvFilterSelection.getSelectionModel().getSelectedItems();

        if (files != null) {
            for (File file :
                    files) {
                launchImageController(file, selectedItems);
            }
        }
    }

    private void launchImageController(File file, List<String> selectedFilters) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "image_progress_pane.fxml"));
        loader.setController(new ImageController(file, selectedFilters));
        openImageTab(loader, file);
    }

    private void openImageTab(FXMLLoader loader, File file) {
        String tabName = file.getName();

        try {
            tpFilterTabManager.getTabs().add(new Tab(tabName, loader.load()));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
