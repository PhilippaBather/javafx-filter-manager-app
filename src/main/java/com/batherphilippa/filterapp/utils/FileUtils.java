package com.batherphilippa.filterapp.utils;

import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUtils {

    public static File getFileFromChooser(RadioButton btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(stage);
    }

    public static List<File> getMultipleFilesFromChooser(RadioButton btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenMultipleDialog(stage);
    }

    public static File createFile(File file) {
        File newFile = new File(file.getName());
        try {
            newFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return newFile;
    }
}
