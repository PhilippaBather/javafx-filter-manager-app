package com.batherphilippa.filterapp.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.batherphilippa.filterapp.constants.Constants.*;

// TODO - comments in Spanish

public class FilterUtils {

    public static void setGreyScale(BufferedImage bufferedImage, int i, int j) {

        Color color = new Color(bufferedImage.getRGB(j, i));

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        int grey = (red + green + blue) / 3;

        Color newColor = new Color(grey, grey, grey);
        bufferedImage.setRGB(j, i, newColor.getRGB());
    }

    public static void setColourInversion(BufferedImage bufferedImage, int i, int j) {
        Color color = new Color(bufferedImage.getRGB(j, i));

        int invertedRed = MAX_RGB_VALUE - color.getRed();
        int invertedGreen = MAX_RGB_VALUE - color.getGreen();
        int invertedBlue = MAX_RGB_VALUE - color.getBlue();

        Color newColor = new Color(invertedRed, invertedGreen, invertedBlue);
        bufferedImage.setRGB(j, i, newColor.getRGB());
    }

    public static void setIncreasedBrightness(BufferedImage bufferedImage, int i, int j) {
        Color color = new Color(bufferedImage.getRGB(j, i));

        int brightenedRed = truncateColourValue(color.getRed() + FILTER_BRIGHTNESS);
        int brightendGreen = truncateColourValue(color.getGreen() + FILTER_BRIGHTNESS);
        int brightendBlue = truncateColourValue(color.getBlue() + FILTER_BRIGHTNESS);

        Color newColor = new Color(brightenedRed, brightendGreen, brightendBlue);
        bufferedImage.setRGB(j, i, newColor.getRGB());
    }

    private static int truncateColourValue(int value) {

        if (value < MIN_RGB_VALUE) {
            value = MIN_RGB_VALUE;
        } else if (value > MAX_RGB_VALUE) {
            value = MAX_RGB_VALUE;
        }

        return value;
    }

    public static void setBlur(BufferedImage img, int x, int y) {
        int r = 0, g = 0, b = 0;  // channels: red (r), green (g), and blue (b)

        int rgb = 2 * (img.getRGB(x + 1, y + 1));  // note: 0xFF leaves only the least significant byte
        r += ((rgb >> 16) & 0xFF); // bitwise shift operation to get the right most byte, third rightmost byte (red)
        g += (rgb >> 8) & 0xFF; // bitwise shift operation to get the right most byte, second rightmost byte (green)
        b += (rgb & 0xFF); // bitwise shift operation to get the right most byte, first rightmost byte (green)

        rgb += (img.getRGB(x + 1, y));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += (img.getRGB(x + 1, y + 2));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += (img.getRGB(x, y + 1));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += (img.getRGB(x + 2, y + 1));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (img.getRGB(x, y));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (img.getRGB(x, y + 2));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (img.getRGB(x + 2, y));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (img.getRGB(x + 2, y + 2));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        r /= 16;
        g /= 16;
        b /= 16;

        Color newColor = new Color(r, g, b);
        img.setRGB(x, y, newColor.getRGB());

    }
}