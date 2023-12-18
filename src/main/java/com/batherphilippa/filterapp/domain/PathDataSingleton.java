package com.batherphilippa.filterapp.domain;

import static com.batherphilippa.filterapp.constants.FileConstants.IMAGE_FILE_PATH;

/**
 * PathDataSingleton - singleton para manejar y guardar la selección de path del usuario para que
 * se pueda compartir los datos entre Stages diferentes.
 *
 * @author Philippa Bather
 */
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
