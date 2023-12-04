package com.batherphilippa.filterapp.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

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
}
