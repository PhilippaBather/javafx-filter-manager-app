package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.filter.FilterUtils;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.batherphilippa.filterapp.filter.FilterType.*;

public class FilterTask extends Task<Integer> {

    private File file;
    private File tempFile;
    private List<String> selectedFilters;
    private FileWriterTask fileWriterTask;

    public FilterTask(File file, File tempFile, List<String> selectedFilters) {
        this.file = file;
        this.tempFile = tempFile;
        this.selectedFilters = selectedFilters;
    }

    @Override
    protected Integer call() {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(file);
            applyFilter(bufferedImage);
            ImageIO.write(bufferedImage, "png", tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        // TODO - buffered file as a field so as to access info re changed file
        fileWriterTask = new FileWriterTask(file, tempFile, selectedFilters);
        new Thread(fileWriterTask).start();
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
        updateMessage(INCREASED_BRIGHTNESS + ": 100%");
    }

    private void blurImage(BufferedImage bufferedImage) {
        double progress;
        double totalSize = bufferedImage.getHeight() - 2 * bufferedImage.getWidth() - 2;
        double totalRead = 0d;
        try {
            for (int y = 0; y < bufferedImage.getHeight() - 2; y++) {
                Thread.sleep(5);
                for (int x = 0; x < bufferedImage.getWidth() - 2; x++) {
                    FilterUtils.setBlur(bufferedImage, x, y);

                    // TODO - resolve problem with the progress bar
                    progress = totalRead / totalSize;
                    updateProgress(progress, 1);

                    String msg = String.format("%s: ", BLUR);
                    updateMessage(Math.round(100 * progress) + "%");
                    totalRead = (y + 1) * (x + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage(BLUR + ": 100%");
    }

}

