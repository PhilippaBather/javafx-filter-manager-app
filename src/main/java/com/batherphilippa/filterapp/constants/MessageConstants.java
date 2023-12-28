package com.batherphilippa.filterapp.constants;

/**
 * MessageConstants - constantes para los mensajes y notificaciónes de la interfaz.
 *
 * @author Philippa Bather
 */
public class MessageConstants {

    public static String BTN_PROCESS_TERMINATED = "Process Terminated";
    public static String CONSOLE_MSG_APP_INITIALISATION = "Initialising the application...";
    public static String CONSOLE_MSG_APP_TERMINATING = "Terminating the application...";
    public static String CONSOLE_MSG_LOG_UNABLE_WRITE_FILE = "Unable to write to log file";
    public static String CONSOLE_MSG_LOG_UPDATED = "Log file updated successfully.";
    public static String FILTER_NOT_RECOGNISED = "Filter not recognised";

    // UI strings
    public static String UI_FILTER_APPLIED = "Filtro aplicado a una copia de ";
    public static String UI_FILTER_BLUR_APPLIED = "Procesando...\tdifuminado de la imagen";
    public static String UI_FILTER_BLUR_CANCELLED = "Difuminado de Imagén Cancelado.";
    public static String UI_FILTER_CANCELLED = " Cancelado.";
    public static String UI_FILTER_CANCELLED_FILE_INFO = "Filtro cancelado para el archivo ";
    public static String UI_FILTER_COMPLETED = " Completado";
    public static String UI_NOTIFICATION_INFO_CHOOSE_FILES = "Elige un archivo/archivos para continuar";
    public static String UI_NOTIFICATION_INFO_CHOOSE_FILTERS = "Elige filtros para continuar.";
    public static String UI_NOTIFICATION_INFO_LOG_NOT_UPDATED = "No fue posible actualizar el historial.";

    // Log strings
    public static String USER_LOG_INFO_ORIGINAL_FILE = "\nArchivo original: %s; path: %s";
    public static String USER_LOG_INFO_FILTERED_FILE = "\nVersión modificada: %s; path: %s";
    public static String USER_LOG_TITLE_FILTERS_APPLIED = "\nFiltros aplicados:\n\t";
}
