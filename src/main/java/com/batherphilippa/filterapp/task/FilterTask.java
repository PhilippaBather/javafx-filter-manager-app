package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.filter.FilterUtils;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.batherphilippa.filterapp.filter.FilterType.*;

public class FilterTask extends Task<BufferedImage> {

    private final File file;
    private File tempFile;
    private final List<String> selectedFilters;

    public FilterTask(File file, File tempFile, List<String> selectedFilters) {
        this.file = file;
        this.tempFile = tempFile;
        this.selectedFilters = selectedFilters;
    }

    @Override
    protected BufferedImage call() {
        BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(file);
            applyFilter(bufferedImage);
            ImageIO.write(bufferedImage, "png", tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // elimina el archivo temporal si un proceso está cancelado
        if (isCancelled()) {
            System.out.println("Task cancelled; deleting temporary file.");
            try {
                Files.deleteIfExists(Paths.get(tempFile.toURI()));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();

        FileWriterTask fileWriterTask = new FileWriterTask(file, tempFile, selectedFilters);
        new Thread(fileWriterTask).start();

        String msg = "Filtro aplicado a una copia de";
        showInformationAlert(msg);
    }

    @Override
    protected void cancelled() {
        super.cancelled();

        String msg = "Filtro cancelado para el archivo";
        showInformationAlert(msg);
    }

    private void showInformationAlert(String msg) {
        String msgStr = msg.concat(" ").concat(file.getName());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msgStr);
        alert.show();
    }

    private BufferedImage applyFilter(BufferedImage bufferedImage) {

        int index = 0;
        do {
            switch (selectedFilters.get(index)) {
                case GREY_SCALE -> greyScaleImage(bufferedImage);
                case COLOR_INVERSION -> invertImageColor(bufferedImage);
                case INCREASED_BRIGHTNESS -> increaseImageBrightness(bufferedImage);
                case BLUR -> blurImage(bufferedImage);
                default -> System.out.println("Filter not recognised");
            }
            index++;
        } while (index < selectedFilters.size());

        return bufferedImage;
    }

    // métodos y búcles for diferentes para manejar y especificar el mensaje para cada proceso
    private void greyScaleImage(BufferedImage bufferedImage) {
        double progress;
        double totalSize = bufferedImage.getHeight() * bufferedImage.getWidth();
        double totalRead = 0d;
        try {
            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                Thread.sleep(5);
                for (int j = 0; j < bufferedImage.getWidth(); j++) {

                    FilterUtils.setGreyScale(bufferedImage, i, j);

                    progress = totalRead / totalSize;
                    updateProgress(progress, 1);

                    String msg = String.format("%s: ", GREY_SCALE);
                    updateMessage(msg + Math.round(100 * progress) + "%");

                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage(GREY_SCALE + ": 100%");
    }

    private void invertImageColor(BufferedImage bufferedImage) {

        double progress;
        double totalSize = bufferedImage.getHeight() * bufferedImage.getWidth();
        double totalRead = 0d;
        try {
            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                Thread.sleep(5);
                for (int j = 0; j < bufferedImage.getWidth(); j++) {
                    FilterUtils.setColourInversion(bufferedImage, i, j);

                    progress = totalRead / totalSize;
                    updateProgress(progress, 1);

                    String msg = String.format("%s: ", COLOR_INVERSION);
                    updateMessage(msg + Math.round(100 * progress) + "%");

                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage(COLOR_INVERSION + ": 100%");
    }

    private void increaseImageBrightness(BufferedImage bufferedImage) {
        double progress;
        double totalSize = bufferedImage.getHeight() * bufferedImage.getWidth();
        double totalRead = 0d;
        try {
            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                Thread.sleep(5);
                for (int j = 0; j < bufferedImage.getWidth(); j++) {
                    FilterUtils.setIncreasedBrightness(bufferedImage, i, j);

                    progress = totalRead / totalSize;
                    updateProgress(progress, 1);

                    String msg = String.format("%s: ", INCREASED_BRIGHTNESS);
                    updateMessage(msg + Math.round(100 * progress) + "%");
                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage(INCREASED_BRIGHTNESS + " Completado");
    }

    private void blurImage(BufferedImage bufferedImage) {
        try {
            // indica que el difuminado está en proceso
            updateMessage("Processing...\tdifuminado de la imagen");
            for (int y = 0; y < bufferedImage.getHeight() - 2; y++) {
                Thread.sleep(5);
                for (int x = 0; x < bufferedImage.getWidth() - 2; x++) {
                    FilterUtils.setBlur(bufferedImage, x, y);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage(BLUR + " Completado");
    }
}

