package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.Constants.PATH;

public class AppController implements Initializable {

    @FXML
    private RadioButton radBtnOneFile;

    @FXML
    private ToggleGroup fileSelection;

    @FXML
    private RadioButton radBtnMultipleFiles;

    @FXML
    private TabPane tpFilterTabManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("App controller initialised...");
    }

    @FXML
    public void handleFileSelection(ActionEvent event) {
        if (radBtnOneFile.isSelected()) {
            // un archivo elegido
            File file = FileUtils.getFileFromChooser(radBtnOneFile);
            launchImageController(file);
        } else {
            // multiples archivos elegidos
            List<File> files = FileUtils.getMultipleFilesFromChooser(radBtnMultipleFiles);
            for (File file:
                 files) {
                launchImageController(file);
            }
        }
    }

    private void launchImageController(File file) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "image_progress_pane.fxml"));
        loader.setController(new ImageController(file));
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
