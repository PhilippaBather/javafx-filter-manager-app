package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.filter.FilterUtils;
import javafx.concurrent.Task;

import java.awt.image.BufferedImage;
import java.util.List;

import static com.batherphilippa.filterapp.constants.MessageConstants.*;
import static com.batherphilippa.filterapp.filter.FilterType.*;

/**
 * FilterTask - el Task para aplicar los filtros de forma concurrente; extiende Task
 *
 * @author Philippa Bather
 */
public class FilterTask extends Task<BufferedImage> {

    private BufferedImage srcImage;
    private List<String> selectedFilters;

    public FilterTask(BufferedImage srcImg, List<String> selectedFilters) {
        this.srcImage = srcImg;
        this.selectedFilters = selectedFilters;
    }

    @Override
    protected BufferedImage call() {
        handleFilterApplication();
        return srcImage;
    }

    /**
     * Maneja la aplicación de los filtros.
     */
    public void handleFilterApplication() {

        int index = 0;
        do {
            if (selectedFilters.get(index).equals(BLUR)) {
                blurImage(srcImage);
            } else {
                // maneja la aplicación de filtros que no son de circunvolución
                applyStandardFilters(srcImage, index);
            }
            index++;
        } while (index < selectedFilters.size());
    }

    /**
     * Aplica los filtros estánderes que no son de circunvolución (convolution filters):
     * escala de grises (grey scale), invesión de color (color inversion), y aumento de
     * brillo (increased brightness).
     *
     * @param bufferedImage - imagén para filtrar
     * @param index         - refiere al filtro seleccionado
     */
    private void applyStandardFilters(BufferedImage bufferedImage, int index) {
        double progress;
        double totalSize = bufferedImage.getHeight() * bufferedImage.getWidth();
        double totalRead = 0d;
        String filterType = selectedFilters.get(index);
        try {
            for (int i = 0; i < bufferedImage.getHeight(); i++) {
                Thread.sleep(15);
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

                    if (isCancelled()) {
                        updateMessage(filterType + UI_FILTER_CANCELLED); // actualiza el mensaje y notifica al usauario
                        return;  // para prevenir la aplicación de otros filtros listados para esta imagén
                    }
                }
            }
        } catch (InterruptedException ie) {
            updateMessage(filterType + UI_FILTER_CANCELLED); // actualiza el mensaje y notifica al usauario
            return; // para salir del método
        }
        updateMessage(filterType + UI_FILTER_COMPLETED); // actualiza el mensaje y notifica al usauario

    }

    /**
     * Maneja la tarea 'difuminado de imagén' (blur filter).
     *
     * @param bufferedImage - imagén para filtrar
     */
    private void blurImage(BufferedImage bufferedImage) {
        try {
            // indica que el difuminado está en proceso
            updateMessage(UI_FILTER_BLUR_APPLIED);
            for (int y = 0; y < bufferedImage.getHeight() - 2; y++) {
                Thread.sleep(15);
                for (int x = 0; x < bufferedImage.getWidth() - 2; x++) {
                    FilterUtils.setBlur(bufferedImage, x, y);
                }
            }
        } catch (InterruptedException ie) {
            updateMessage(UI_FILTER_BLUR_CANCELLED); // actualiza el mensaje y notifica al usauario
            return; // para salir del método
        }
        updateMessage(BLUR + UI_FILTER_COMPLETED);
    }
}

