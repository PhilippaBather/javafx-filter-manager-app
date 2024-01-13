package com.batherphilippa.filterapp.utils;

public class InputUtils {

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
