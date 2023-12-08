package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.utils.FileUtils;
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

import static com.batherphilippa.filterapp.constants.Constants.*;

public class FileWriterTask extends Task<Integer> {

    private File file;
    private File tempFile;
    private List<String> selectedFilters;
    private File logFile;

    public FileWriterTask(File file, File tempFile, List<String> selectedFilters) {
        this.file = file;
        this.tempFile = tempFile;
        this.selectedFilters = selectedFilters;
    }

    @Override
    protected Integer call() throws Exception {
        boolean hasFile = checkFileExists();

        if (hasFile) {
            writeToLog();
        } else {
            logFile = FileUtils.returnNewFile(FILE_PATH, FILE_NAME_LOG, FILE_TYPE_TXT);
            boolean isCreated = logFile.createNewFile();
            if (isCreated) {
                writeToLog();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No fue posible a actualizar el historial.");
                alert.show();
            }
        }

        return null;
    }

    private boolean checkFileExists() {
        Path path = Paths.get(FILE_PATH + "log.txt");
        return Files.exists(path);
    }

    private void writeToLog() throws IOException, InterruptedException {
        String fileDetails = getFileDetails();
        try {
            Thread.sleep(10);
            Files.write(
                    Paths.get(FILE_PATH + FILE_NAME_LOG + FILE_TYPE_TXT),
                    fileDetails.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileDetails() {
        String timestamp = LocalDateTime.now().toString();

        String origFileDetails = String.format("\nArchivo original: %s; path: %s", file.getName(), file.getPath());
        String filteredFileDetails = String.format("\nVersión modificada: %s; path: %s", file.getName(), file.getPath());

        StringBuilder filtersApplied = new StringBuilder().append("\nFiltros aplicados:\n\t");
        for (String filter:
             selectedFilters) {
            filtersApplied.append(filter).append("\n\t");
        }

        return new StringBuilder(timestamp)
                .append(origFileDetails)
                .append(filteredFileDetails)
                .append(filteredFileDetails)
                .append(filtersApplied)
                .append("\n").toString();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        System.out.println("Log file updated successfully.");
    }

    @Override
    protected void failed() {
        super.failed();
        System.out.println("Unable to write to log file");
    }
}
