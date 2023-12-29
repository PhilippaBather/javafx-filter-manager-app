package com.batherphilippa.filterapp.utils;

import com.batherphilippa.filterapp.domain.PathDataSingleton;
import javafx.scene.control.RadioButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

import static com.batherphilippa.filterapp.constants.FileConstants.IMAGE_FILE_TYPE_PNG;

/**
 * FileUtils - utiles para manejar archivos
 *
 * @author Philippa Bather
 */
public class FileUtils {

    public static File getFileFromChooser(RadioButton btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenDialog(stage);
    }

    public static File getDirectoryFromChooser(RadioButton btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(stage);
    }

    public static List<File> getMultipleFilesFromChooser(RadioButton btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        return fileChooser.showOpenMultipleDialog(stage);
    }

    public static String setFileNameAndPath(File file, String suffix) {
        PathDataSingleton pathData = PathDataSingleton.getInstance();
        String name = file.getName();
        String filename = name.substring(0, name.lastIndexOf("."));
        return new StringBuilder()
                    .append(pathData.getPath())
                    .append(filename)
                    .append(suffix)
                    .append(IMAGE_FILE_TYPE_PNG).toString();
    }

    public static File returnNewFile(String filePath, String filename, String fileType) {
        String newFilenameWithPath = new StringBuilder()
                .append(filePath)
                .append(filename)
                .append(fileType).toString();

        return new File(newFilenameWithPath);
    }
}
