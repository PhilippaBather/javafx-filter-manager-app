package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.utils.FileUtils;
import com.batherphilippa.filterapp.utils.NotificationUtils;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;

import static com.batherphilippa.filterapp.constants.FileConstants.*;
import static com.batherphilippa.filterapp.constants.MessageConstants.*;

public class FileWriterTask extends Task<File> {

    private final File file;
    private final File tempFile;
    private final List<String> selectedFilters;

    public FileWriterTask(File file, File tempFile, List<String> selectedFilters) {
        this.file = file;
        this.tempFile = tempFile;
        this.selectedFilters = selectedFilters;
    }

    @Override
    protected File call() throws Exception {
        boolean hasFile = checkFileExists();

        if (hasFile) {
            writeToLog();
        } else {
            File logFile = FileUtils.returnNewFile(IMAGE_FILE_PATH, LOG_FILE_NAME, LOG_FILE_TYPE_TXT);
            boolean isCreated = logFile.createNewFile();
            if (isCreated) {
                writeToLog();
            } else {
                NotificationUtils.showAlertDialog(NOTIFICATION_INFO_LOG_NOT_UPDATED, Alert.AlertType.ERROR);
            }
        }

        return null;
    }

    private boolean checkFileExists() {
        Path path = Paths.get(IMAGE_FILE_PATH + LOG_FILE_NAME + LOG_FILE_TYPE_TXT);
        return Files.exists(path);
    }

    private void writeToLog() {
        String fileDetails = getFileDetails();
        try {
            Thread.sleep(10);
            Files.write(
                    Paths.get(IMAGE_FILE_PATH + LOG_FILE_NAME + LOG_FILE_TYPE_TXT),
                    fileDetails.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileDetails() {
        String timestamp = LocalDateTime.now().toString();

        String origFileDetails = String.format("\nArchivo original: %s; path: %s", file.getName(), file.getPath());
        String filteredFileDetails = String.format("\nVersión modificada: %s; path: %s", tempFile.getName(), tempFile.getPath());

        StringBuilder filtersApplied = new StringBuilder().append(LOG_TITLE_FILTERS_APPLIED);
        for (String filter:
             selectedFilters) {
            filtersApplied.append(filter).append("\n\t");
        }

        return new StringBuilder(timestamp)
                .append(origFileDetails)
                .append(filteredFileDetails)
                .append(filtersApplied)
                .append("\n").toString();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.println(CONSOLE_MSG_LOG_UPDATED);
    }

    @Override
    protected void failed() {
        super.failed();
        System.out.println(CONSOLE_MSG_LOG_UNABLE_WRITE_FILE);
    }
}
