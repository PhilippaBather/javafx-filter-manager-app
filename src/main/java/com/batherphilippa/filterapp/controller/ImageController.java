package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.constants.MessageConstants;
import com.batherphilippa.filterapp.task.FilterTask;
import com.batherphilippa.filterapp.utils.FileUtils;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.FileConstants.IMAGE_FILE_NAME_SUFFIX_TEMP;

/**
 * ImageController - maneja la aplicación de FilterTasks y la presentación del
 * panel de control indicando el estado de los filtros para cada imagen.
 *
 * @author Philippa Bather
 */
public class ImageController implements Initializable {

    private final File file;
    private FilterTask filterTask;
    private final List<String> selectedFilters;
    private Tab tab;

    @FXML
    private Button btnApply;
    @FXML
    private Button btCancel;
    @FXML
    private Button btnRedo;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUndo;
    @FXML
    private ImageView imgVwSource;
    @FXML
    private ImageView imgVwOutput;
    @FXML
    private Label lbFilterStatus;
    @FXML
    private ListView<String> listVwFilters;
    @FXML
    private ProgressBar pbFilter;



    public ImageController(File file, List<String> selectedFilters) {
        this.file = file;
        this.selectedFilters = selectedFilters;
        this.tab = new Tab();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        long ts = Timestamp.from(Instant.now()).getTime(); // para identificación más precisa
        String newName = FileUtils.setFileNameAndPath(file, IMAGE_FILE_NAME_SUFFIX_TEMP + ts);

        File tempFile = new File(newName);
        List<String> selectedFiltersCopy = new ArrayList<>(selectedFilters);
        filterTask = new FilterTask(file, tempFile, selectedFiltersCopy);

        filterTask.stateProperty().addListener(((observableValue, state, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // oculta la barra de progreso y el botón de 'Cancel' correspondiente después de la operación ha terminado
                pbFilter.setVisible(false);
                btCancel.setVisible(false);
            }
            if (newState == Worker.State.CANCELLED) {
                // indica al usuario explícitamente que la aplicación de filtrada está cancelada
                btCancel.setText(MessageConstants.UI_BTN_PROCESS_TERMINATED);
                btCancel.setDisable(true);
            }
        }));

        // actualiza el mensaje del porcentaje de progreso
        filterTask.messageProperty().addListener(((observableValue, msg, newMsg) -> lbFilterStatus.setText(newMsg)));

        // actualiza el estado de la barra de progreso
        filterTask.progressProperty().addListener((observableValue, number, t1) -> pbFilter.setProgress(t1.doubleValue()));

        new Thread(filterTask).start();
    }

    @FXML
    void applyFilter(ActionEvent event) {
        // TODO
        System.out.println("Apply filter btn clicked");
    }

    /**
     * Cancela la aplicación de filtros para una imagen cuando el usurio hace clic en el bóton Cancel.
     * @param event click event
     */
    @FXML
    private void cancelApplyFilter(ActionEvent event) {
        filterTask.cancel();
    }

    @FXML
    void redoFilter(ActionEvent event) {
        // TODO
        System.out.println("Redo filter btn clicked");
    }

    @FXML
    void saveFilteredFile(ActionEvent event) {
        // TODO
        System.out.println("Save filtered file btn clicked");
    }

    @FXML
    void undoFilter(ActionEvent event) {
        // TODO
        System.out.println("Undo filter btn clicked");
    }

    /**
     * Asigna el objeto Tab y establece el EventHandler setOnClosed.
     * El método setOnClosed permite que el Task está cancelado cuando el tab esté cerrado.
     *
     * @param tab - contiene la información sobre el archivo y los filtros aplicados que corresponde con this.filterTask.
     */
    public void setTab(Tab tab) {
        this.tab = tab;
        tab.setOnClosed(e -> filterTask.cancel());
    }
}
