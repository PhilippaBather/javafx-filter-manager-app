package com.batherphilippa.filterapp.constants;

public class Constants {

    public static final String FXML_FILE_PATH = "/com/batherphilippa/filterapp/";

    public static final String IMAGE_FILE_NAME_SUFFIX_TEMP = "_filtered";
    public static final String IMAGE_FILE_PATH = System.getProperty("user.home").concat("/Pictures/FilteredImages/");
    public static final String IMAGE_FILE_TYPE_PNG = ".png";
    public static final String LOG_FILE_NAME = "log";
    public static final String LOG_FILE_TYPE_TXT = ".txt";

    public static final int MIN_RGB_VALUE = 0;
    public static final int MAX_RGB_VALUE = 255;
    public static final int FILTER_BRIGHTNESS = 75;
}
