package com.batherphilippa.filterapp.utils;

import javafx.scene.control.RadioButton;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import static com.batherphilippa.filterapp.constants.Constants.FILE_PATH;
import static com.batherphilippa.filterapp.constants.Constants.FILE_TYPE_PNG;

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

    public static String setFileNameAndPath(File file, String suffix) {
        String name = file.getName();
        String filename = name.substring(0, name.lastIndexOf("."));
        return new StringBuilder()
                    .append(FILE_PATH)
                    .append(filename)
                    .append(suffix)
                    .append(FILE_TYPE_PNG).toString();
    }
}
