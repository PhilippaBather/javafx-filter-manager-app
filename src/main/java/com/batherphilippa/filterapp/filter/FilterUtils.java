package com.batherphilippa.filterapp.filter;

import java.awt.*;
import java.awt.image.BufferedImage;

import static com.batherphilippa.filterapp.constants.FilterConstants.*;

/**
 * FilterUtils - utiles para manejar y aplicar los filtros en las imagenes.
 *
 * @author Philippa Bather
 */
public class FilterUtils {

    /**
     * Aplica el filtro de escala de grises
     * @param bufferedImage - imagén para filtrar
     * @param i - iteración vertical
     * @param j - iteración horizontal
     */
    public static void setGreyScale(BufferedImage bufferedImage, int i, int j) {

        Color color = new Color(bufferedImage.getRGB(j, i));

        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();

        int grey = (red + green + blue) / 3;

        Color newColor = new Color(grey, grey, grey);
        bufferedImage.setRGB(j, i, newColor.getRGB());
    }

    /**
     * Aplica el filtro de inversión de color
     * @param bufferedImage - imagén para filtrar
     * @param i - iteración vertical
     * @param j - iteración horizontal
     */
    public static void setColourInversion(BufferedImage bufferedImage, int i, int j) {
        Color color = new Color(bufferedImage.getRGB(j, i));

        int invertedRed = MAX_RGB_VALUE - color.getRed();
        int invertedGreen = MAX_RGB_VALUE - color.getGreen();
        int invertedBlue = MAX_RGB_VALUE - color.getBlue();

        Color newColor = new Color(invertedRed, invertedGreen, invertedBlue);
        bufferedImage.setRGB(j, i, newColor.getRGB());
    }

    /**
     * Aplica el filtro de aumento de brillo
     * @param bufferedImage - imagén para filtrar
     * @param i - iteración vertical
     * @param j - iteración horizontal
     */
    public static void setIncreasedBrightness(BufferedImage bufferedImage, int i, int j) {
        Color color = new Color(bufferedImage.getRGB(j, i));

        int brightenedRed = truncateColourValue(color.getRed() + FILTER_BRIGHTNESS);
        int brightendGreen = truncateColourValue(color.getGreen() + FILTER_BRIGHTNESS);
        int brightendBlue = truncateColourValue(color.getBlue() + FILTER_BRIGHTNESS);

        Color newColor = new Color(brightenedRed, brightendGreen, brightendBlue);
        bufferedImage.setRGB(j, i, newColor.getRGB());
    }

    /**
     * Trunca el valor de pixel para que esté en el rango de pixels.
     * @param value de pixel para comprobra
     * @return valor de pixel
     */
    private static int truncateColourValue(int value) {

        if (value < MIN_RGB_VALUE) {
            value = MIN_RGB_VALUE;
        } else if (value > MAX_RGB_VALUE) {
            value = MAX_RGB_VALUE;
        }

        return value;
    }

    /**
     * Aplica el filtro d diminado de la imagén
     * @param bufferedImage - imagén para filtrar
     * @param x - iteración vertical
     * @param y - iteración horizontal
     */
    public static void setBlur(BufferedImage bufferedImage, int x, int y) {
        int r = 0, g = 0, b = 0;  // canales: rojo (r), verde (g) y azul (b)

        int rgb = 2 * (bufferedImage.getRGB(x + 1, y + 1));
        //0xFF deja la última byte menos importante
        r += ((rgb >> 16) & 0xFF); // operación de desplazamiento bit a bit: coge el byte más a la derecha - el tercero más a la derecha (rojo)
        g += (rgb >> 8) & 0xFF; // operación de desplazamiento bit a bit: coge el byte más a la derecha - el segundo más a la derecha (verde)
        b += (rgb & 0xFF); // operación de desplazamiento bit a bit: coge el byte más a la derecha - el primero más a la derecha (azul)

        rgb += (bufferedImage.getRGB(x + 1, y));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += (bufferedImage.getRGB(x + 1, y + 2));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += (bufferedImage.getRGB(x, y + 1));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += (bufferedImage.getRGB(x + 2, y + 1));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (bufferedImage.getRGB(x, y));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (bufferedImage.getRGB(x, y + 2));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (bufferedImage.getRGB(x + 2, y));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        rgb += -1 * (bufferedImage.getRGB(x + 2, y + 2));
        r += ((rgb >> 16) & 0xFF);
        g += (rgb >> 8) & 0xFF;
        b += (rgb & 0xFF);

        r /= 16;
        g /= 16;
        b /= 16;

        Color newColor = new Color(r, g, b);
        bufferedImage.setRGB(x, y, newColor.getRGB());

    }
}