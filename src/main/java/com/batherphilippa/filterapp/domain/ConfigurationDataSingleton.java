package com.batherphilippa.filterapp.domain;

import static com.batherphilippa.filterapp.constants.FileConstants.IMAGE_FILE_PATH;

/**
 * ConfigurationDataSingleton - singleton para manejar y guardar la selección de path del usuario para que
 * se pueda compartir los datos entre Stages diferentes y el máximo número de archivos que un usuario se puede procesar
 *
 * @author Philippa Bather
 */
public class ConfigurationDataSingleton {

    private static final ConfigurationDataSingleton configurationDataInstance = new ConfigurationDataSingleton();
    private String path;
    private boolean isPathDefault;
    private boolean isMaxImageFiles;
    private int maxImageFiles;

    public ConfigurationDataSingleton() {
        this.isPathDefault = true;
        this.path = IMAGE_FILE_PATH;  // path por defecto
        this.isMaxImageFiles = false;  // no hay límite de archivos procesados por defecto
        this.maxImageFiles = -1; // por defecto
    }

    public static ConfigurationDataSingleton getInstance() {
        return configurationDataInstance;
    }

    public String getPath() {
        return path;
    }

    public boolean isPathDefault() {
        return isPathDefault;
    }

    public void setPathDefault(boolean pathDefault) {
        isPathDefault = pathDefault;
    }

    public String setPath(String path) {
        return this.path = path;
    }

    public boolean isMaxImageFiles() {
        return isMaxImageFiles;
    }

    public void setMaxImageFiles(boolean maxImageFiles) {
        isMaxImageFiles = maxImageFiles;
    }

    public int getMaxImageFiles() {
        return maxImageFiles;
    }

    public void setMaxImageFiles(int maxImageFiles) {
        this.maxImageFiles = maxImageFiles;
    }
}
