package com.batherphilippa.filterapp.domain;

import static com.batherphilippa.filterapp.constants.Constants.IMAGE_FILE_PATH;

public class PathDataSingleton {

    private static final PathDataSingleton pathDataInstance = new PathDataSingleton();
    private String path;

    public PathDataSingleton() {
        this.path = IMAGE_FILE_PATH;  // path por defecto
    }

    public static PathDataSingleton getInstance() {
        return pathDataInstance;
    }

    public String getPath() {
        return path;
    }

    public String setPath(String path) {
        return this.path = path;
    }

}
