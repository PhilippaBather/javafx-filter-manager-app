package com.batherphilippa.filterapp.utils;

/**
 * InputUtils - para validar la entrada del usuario
 */
public class InputUtils {

    /**
     * Valida el número introducido por el usuario para el límite de archivos procesados.
     * @param num - el límite
     * @return el límite introducido si está válido o -1 si no
     */
    public static int validateIntegerInput(String num){
        int input;
        try {
            input = Integer.parseInt(num);
        } catch(IllegalArgumentException iae) {
            return -1;
        }

        return input;
    }
}
