package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.filter.FilterUtils;
import com.batherphilippa.filterapp.utils.NotificationUtils;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.batherphilippa.filterapp.constants.MessageConstants.*;
import static com.batherphilippa.filterapp.filter.FilterType.*;

/**
 * FilterTask - el Task para aplicar los filtros de forma concurrente; extiende Task
 *
 * @author Philippa Bather
 */
public class FilterTask extends Task<BufferedImage> {

    private final File file;
    private final File tempFile;
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
            handleFilterApplication(bufferedImage);
            ImageIO.write(bufferedImage, "png", tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // elimina el archivo temporal si un proceso está cancelado
        if (isCancelled()) {
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

        String msg = UI_FILTER_APPLIED + file.getName();
        NotificationUtils.showAlertDialog(msg, Alert.AlertType.INFORMATION);
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        String msg = UI_FILTER_CANCELLED_FILE_INFO + file.getName();
        NotificationUtils.showAlertDialog(msg, Alert.AlertType.INFORMATION);
    }

    /**
     * Maneja la aplicación de los filtros.
     * @param bufferedImage - imagén para filtrar
     */
    private void handleFilterApplication(BufferedImage bufferedImage) {

        int index = 0;
        do {
            if (selectedFilters.get(index).equals(BLUR)) {
                blurImage(bufferedImage);
            } else {
                // maneja la aplicación de filtros que no son de circunvolución
                applyStandardFilters(bufferedImage, index);
            }
            index++;
        } while (index < selectedFilters.size());
    }

    /**
     * Aplica los filtros estánderes que no son de circunvolución (convolution filters):
     * escala de grises (grey scale), invesión de color (color inversion), y aumento de
     * brillo (increased brightness).
     * @param bufferedImage - imagén para filtrar
     * @param index - refiere al filtro seleccionado
     */
    private void applyStandardFilters(BufferedImage bufferedImage, int index) {
        double progress;
        double totalSize = bufferedImage.getHeight() * bufferedImage.getWidth();
        double totalRead = 0d;
        String filterType = selectedFilters.get(index);
        try {
            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                Thread.sleep(20);
                for (int j = 0; j < bufferedImage.getWidth(); j++) {

                    switch (filterType) {
                        case GREY_SCALE -> FilterUtils.setGreyScale(bufferedImage, i, j);
                        case COLOR_INVERSION -> FilterUtils.setColourInversion(bufferedImage, i, j);
                        case INCREASED_BRIGHTNESS -> FilterUtils.setIncreasedBrightness(bufferedImage, i, j);
                        default -> System.out.println(FILTER_NOT_RECOGNISED);
                    }

                    // actualiza la barra de progreso y el label
                    progress = totalRead / totalSize;
                    updateProgress(progress, 1);

                    String msg = String.format("%s: ", filterType);
                    updateMessage(msg + Math.round(100 * progress) + "%");

                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            updateMessage( filterType + UI_FILTER_CANCELLED); // actualiza el mensaje y notifica al usauario
            return; // para salir del método
        }
        updateMessage( filterType + UI_FILTER_COMPLETED); // actualiza el mensaje y notifica al usauario

    }

    /**
     * Maneja la tarea 'difuminado de imagén' (blur filter).
     * @param bufferedImage - imagén para filtrar
     */
    private void blurImage(BufferedImage bufferedImage) {
        try {
            // indica que el difuminado está en proceso
            updateMessage(UI_FILTER_BLUR_APPLIED);
            for (int y = 0; y < bufferedImage.getHeight() - 2; y++) {
                Thread.sleep(20);
                for (int x = 0; x < bufferedImage.getWidth() - 2; x++) {
                    FilterUtils.setBlur(bufferedImage, x, y);
                }
            }
        } catch (InterruptedException ie) {
            updateMessage( UI_FILTER_BLUR_CANCELLED); // actualiza el mensaje y notifica al usauario
            return; // para salir del método
        }
        updateMessage(BLUR + UI_FILTER_COMPLETED);
    }
}

