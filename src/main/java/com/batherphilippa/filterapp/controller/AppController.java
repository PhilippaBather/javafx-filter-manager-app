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
            File file = FileUtils.getFileFromChooser(radBtnOneFile);
            File newFile = FileUtils.createFile(file);
            launchImageController(newFile);
        } else {
            System.out.println("Multiples are to be chosen.");
            List<File> files = FileUtils.getMultipleFilesFromChooser(radBtnMultipleFiles);
            for (File file:
                 files) {
                File newFile = FileUtils.createFile(file);
                launchImageController(newFile);
            }
        }
    }

    private FXMLLoader launchImageController(File file) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH + "image_progress_pane.fxml"));
        loader.setController(new ImageController(file));
        openImageTab(loader, file);
        return loader;
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
