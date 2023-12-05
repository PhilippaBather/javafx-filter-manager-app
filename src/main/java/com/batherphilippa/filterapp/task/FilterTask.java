package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.filter.FilterUtils;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// TODO - comments in Spanish
// TODO - mechanism to handle multiple filter selection

public class FilterTask extends Task<Integer> {

    private File file;
    private File tempFile;

    public FilterTask(File file, File tempFile) {
        this.file = file;
        this.tempFile = tempFile;
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

    private BufferedImage applyFilter(BufferedImage bufferedImage) {
        // TODO - add param to accept array of filters to apply
        // TODO - do-while loop
        // TODO - array containing filters to contain Enum of filter type not Strings
        // TODO - handle image saving etc for multiple filter application

//        greyScaleImage(bufferedImage);
//        invertImageColor(bufferedImage);
//        increaseImageBrightness(bufferedImage);
        blurImage(bufferedImage);

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
                    updateMessage(Math.round(100 * progress) + "%");
                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage("100%");
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
                    updateMessage(Math.round(100 * progress) + "%");
                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage("100%");
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
                    updateMessage(Math.round(100 * progress) + "%");
                    totalRead = (i + 1) * (j + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage("100%");
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
                    updateMessage(Math.round(100 * progress) + "%");
                    totalRead = (y + 1) * (x + 1);
                }
            }
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        updateMessage("100%");
    }
}

