package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.constants.MessageConstants;
import com.batherphilippa.filterapp.task.FileWriterTask;
import com.batherphilippa.filterapp.task.FilterTask;
import com.batherphilippa.filterapp.utils.FileUtils;
import com.batherphilippa.filterapp.utils.NotificationUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.FileConstants.IMAGE_FILE_NAME_SUFFIX_TEMP;
import static com.batherphilippa.filterapp.constants.MessageConstants.*;
import static com.batherphilippa.filterapp.filter.FilterType.*;
import static com.batherphilippa.filterapp.filter.FilterType.BLUR;

/**
 * ImageController - maneja la aplicación de FilterTasks y la presentación del
 * panel de control indicando el estado de los filtros para cada imagen.
 *
 * @author Philippa Bather
 */
public class ImageController implements Initializable {

    private BufferedImage outputBImg;
    private BufferedImage sourceBImg;
    private final File sourceFile;
    private FilterTask filterTask;
    private Image workingImage;
    private List<String> selectedFilters;
    private Tab tab;
    private boolean isUndone;

    @FXML
    private Button btnApply;
    @FXML
    private Button btnCancel;
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
    private final ObservableList<String> tbFilterOptions = FXCollections.observableArrayList(GREY_SCALE,
            COLOR_INVERSION, INCREASED_BRIGHTNESS, BLUR);
    private final List<String> appliedFilters;
    private String removedFilter;


    public ImageController(File sourceFile, List<String> selectedFilters) {
        this.sourceFile = sourceFile;
        this.selectedFilters = selectedFilters;
        this.appliedFilters = new ArrayList<>();
        this.appliedFilters.addAll(selectedFilters);
        this.tab = new Tab();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isUndone = false;
        listVwFilters.setItems(tbFilterOptions);
        listVwFilters.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        renderSourceImageInTabPane();
        setSourceBImage();
        applyFilters(sourceBImg);
    }

    /**
     * Pinta el imagén original en el tab.
     */
    private void renderSourceImageInTabPane() {
        InputStream stream;
        try {
            stream = new FileInputStream(sourceFile.getAbsoluteFile());
            Image image = new Image(stream);
            imgVwSource.setImage(image);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    /**
     * Establece el buffered image como el imagén original
     */
    private void setSourceBImage() {
        try {
            sourceBImg = ImageIO.read(sourceFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Aplica los filtros al buffered image.
     * @param img - imagén pasado (una copía del original o el 'output' image en el caso de que más filtros están aplicados.
     */
    private void applyFilters(BufferedImage img) {
        disableAllBtns();
        btnCancel.setDisable(false);
        btnCancel.setText(UI_BTN_CANCEL);
        List<String> selectedFiltersCopy = new ArrayList<>(selectedFilters);
        filterTask = new FilterTask(img, selectedFiltersCopy);

        // actualiza el mensaje del porcentaje de progreso
        filterTask.messageProperty().addListener(((observableValue, msg, newMsg) -> lbFilterStatus.setText(newMsg)));
        // actualiza el estado de la barra de progreso
        filterTask.progressProperty().addListener((observableValue, number, t1) -> pbFilter.setProgress(t1.doubleValue()));

        setSucceededActions();
        setCancelledActions();

        new Thread(filterTask).start();
    }

    /**
     * Establece las medidas cuando el task ha terminado.
     */
    private void setSucceededActions() {
        filterTask.setOnSucceeded(event -> {
            outputBImg = filterTask.getValue();
            sourceBImg = outputBImg;

            // Snapshots the BufferedImage; changes to BufferedImage not reflected in the Image snapshot
            workingImage = SwingFXUtils.toFXImage(outputBImg, null);
            imgVwOutput.setImage(workingImage);

            btnApply.setDisable(false);
            btnSave.setDisable(false);
            btnUndo.setDisable(false);
            btnCancel.setVisible(false);

            filterTask.messageProperty().addListener(((observableValue, msg, newMsg) -> lbFilterStatus.setText(newMsg)));
            // actualiza el estado de la barra de progreso
            filterTask.progressProperty().addListener((observableValue, number, t1) -> pbFilter.setProgress(t1.doubleValue()));
        });
    }

    /**
     * Establece las medidas cuando el task ha sido cancelado.
     */
    private void setCancelledActions() {
        filterTask.setOnCancelled(event -> {
            btnCancel.setText(MessageConstants.UI_BTN_PROCESS_TERMINATED);
            btnCancel.setDisable(true);
            sourceBImg.flush();
            if (outputBImg != null) {
                outputBImg.flush();
            }
            disableAllBtns();
            String msg = UI_FILTER_CANCELLED_FILE_INFO + sourceFile.getName();
            NotificationUtils.showAlertDialog(msg, Alert.AlertType.INFORMATION);
        });
    }

    /**
     * Inhabilitar todos los botones (salvo 'Cancelar')
     */
    private void disableAllBtns() {
        btnApply.setDisable(true);
        btnRedo.setDisable(true);
        btnSave.setDisable(true);
        btnUndo.setDisable(true);
    }

    /**
     * Maneja el botón de aplicar filtros
     * @param event
     */
    @FXML
    private void applyFilterHandler(ActionEvent event) {
        selectedFilters = listVwFilters.getSelectionModel().getSelectedItems();
        appliedFilters.addAll(selectedFilters);

        if (selectedFilters.size() == 0) {
            NotificationUtils.showAlertDialog(MessageConstants.UI_NOTIFICATION_INFO_CHOOSE_FILTERS, Alert.AlertType.INFORMATION);
            return;
        }

        if (!isUndone) {
            applyFilters(outputBImg);
        } else {
            applyFilters(sourceBImg);
        }
    }

    /**
     * Maneja el botón de cancelar filtros
     * @param event
     */
    @FXML
    private void cancelFilterHandler(ActionEvent event) {
        filterTask.cancel();
    }

    /**
     * Maneja el botón de guardar el imagén.
     * @param event
     */
    @FXML
    private void saveFileHandler(ActionEvent event) {
        long ts = Timestamp.from(Instant.now()).getTime();
        String newName = FileUtils.setFileNameAndPath(sourceFile, IMAGE_FILE_NAME_SUFFIX_TEMP + ts);
        File outputFile = new File(newName);

        try {
            ImageIO.write(outputBImg, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        btnSave.setText(UI_NOTIFICATION_INFO_IMAGE_SAVED);
        btnSave.setDisable(true);
        writeTaskToLog(outputFile);

        sourceBImg.flush();
        outputBImg.flush();
    }

    /**
     * Maneja el botón de deshacer los filtros.
     * @param event
     */
    @FXML
    private void undoFilterHandler(ActionEvent event) {
        InputStream stream;
        try {
            stream = new FileInputStream(sourceFile.getAbsoluteFile());
            sourceBImg = ImageIO.read(sourceFile);
            Image image = new Image(stream);
            imgVwOutput.setImage(image);
            btnSave.setDisable(false);
            btnRedo.setDisable(false);
            btnUndo.setDisable(true);
            isUndone = true;
            manageAppliedFilterList();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * Maneja los filtros aplicados en una lista.
     */
    private void manageAppliedFilterList() {
        removedFilter = appliedFilters.get(0);
        appliedFilters.clear();
    }

    /**
     * Maneja el botón de rehacer un filtro.
     * @param event
     */
    @FXML
    private void redoFilterHandler(ActionEvent event) {
        imgVwOutput.setImage(workingImage);
        sourceBImg = outputBImg;
        btnSave.setDisable(false);
        btnUndo.setDisable(false);
        btnRedo.setDisable(true);
        isUndone = false;
        appliedFilters.add(removedFilter);
    }

    /**
     * Escribe el task al historial.
     * @param outputFile
     */
    private void writeTaskToLog(File outputFile) {
        FileWriterTask fileWriterTask = new FileWriterTask(sourceFile, outputFile, appliedFilters);
        new Thread(fileWriterTask).start();

        String msg = UI_FILTER_APPLIED + sourceFile.getName();
        NotificationUtils.showAlertDialog(msg, Alert.AlertType.INFORMATION);
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
