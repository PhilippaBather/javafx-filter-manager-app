package com.batherphilippa.filterapp.task;

import com.batherphilippa.filterapp.filter.FilterUtils;
import javafx.concurrent.Task;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class FilterTask extends Task<Integer> {

    private File file;
    private File tempFile;

    public FilterTask(File file, File tempFile) {
        this.file = file;
        this.tempFile = tempFile;
    }

    @Override
    protected Integer call() throws Exception {
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

    private BufferedImage applyFilter(BufferedImage bufferedImage) throws InterruptedException {
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
        return bufferedImage;
    }
}

