package com.batherphilippa.filterapp.controller;

import com.batherphilippa.filterapp.constants.MessageConstants;
import com.batherphilippa.filterapp.task.FileWriterTask;
import com.batherphilippa.filterapp.task.FilterTask;
import com.batherphilippa.filterapp.utils.FileUtils;
import com.batherphilippa.filterapp.utils.NotificationUtils;
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
import static com.batherphilippa.filterapp.constants.MessageConstants.UI_FILTER_APPLIED;
import static com.batherphilippa.filterapp.constants.MessageConstants.UI_FILTER_CANCELLED_FILE_INFO;

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
    private Image workingImage; // TODO - working image
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


    public ImageController(File sourceFile, List<String> selectedFilters) {
        this.sourceFile = sourceFile;
        this.selectedFilters = selectedFilters;
        this.tab = new Tab();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        renderSourceImageInTabPane();
        disableAllBtns();

        setSourceBImage();
        List<String> selectedFiltersCopy = new ArrayList<>(selectedFilters);
        filterTask = new FilterTask(sourceBImg, selectedFiltersCopy);

        // actualiza el mensaje del porcentaje de progreso
        filterTask.messageProperty().addListener(((observableValue, msg, newMsg) -> lbFilterStatus.setText(newMsg)));

        // actualiza el estado de la barra de progreso
        filterTask.progressProperty().addListener((observableValue, number, t1) -> pbFilter.setProgress(t1.doubleValue()));

        setSucceededActions();
        setCancelledActions();

        new Thread(filterTask).start();
    }

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

    private void setSourceBImage() {
        try {
            sourceBImg = ImageIO.read(sourceFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void setSucceededActions() {
        filterTask.setOnSucceeded(event -> {
            outputBImg = filterTask.getValue();
            sourceBImg = outputBImg;

            // Snapshots the BufferedImage; changes to BufferedImage not reflected in the Image snapshot
            workingImage = SwingFXUtils.toFXImage(outputBImg, null); // TODO - second param: writeable image
            imgVwOutput.setImage(workingImage);

            pbFilter.setVisible(false);

            btCancel.setVisible(false);
            btnSave.setDisable(false);
            btnUndo.setDisable(false);
        });
    }

    private void setCancelledActions() {
        filterTask.setOnCancelled(event -> {
            btCancel.setText(MessageConstants.UI_BTN_PROCESS_TERMINATED);
            btCancel.setDisable(true);
            sourceBImg.flush();
            outputBImg.flush();
            disableAllBtns();
        });
        String msg = UI_FILTER_CANCELLED_FILE_INFO + sourceFile.getName();
        NotificationUtils.showAlertDialog(msg, Alert.AlertType.INFORMATION);
    }

    private void disableAllBtns() {
        btnApply.setDisable(true);
        btnRedo.setDisable(true);
        btnSave.setDisable(true);
        btnUndo.setDisable(true);
    }

    @FXML
    void applyFilter(ActionEvent event) {
        // TODO
        System.out.println("Apply filter btn clicked");
    }

    /**
     * Cancela la aplicación de filtros para una imagen cuando el usurio hace clic en el bóton Cancel.
     *
     * @param event click event
     */
    @FXML
    private void cancelApplyFilter(ActionEvent event) {
        filterTask.cancel();
    }

    @FXML
    private void redoFilter(ActionEvent event) {
        // TODO
        System.out.println("Redo filter btn clicked");
    }

    @FXML
    private void saveFilteredFile(ActionEvent event) {
        long ts = Timestamp.from(Instant.now()).getTime();
        String newName = FileUtils.setFileNameAndPath(sourceFile, IMAGE_FILE_NAME_SUFFIX_TEMP + ts);
        File outputFile = new File(newName);

        try {
            ImageIO.write(outputBImg, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        writeTaskToLog(outputFile);

        sourceBImg.flush();
        outputBImg.flush();
    }

    @FXML
    private void undoFilter(ActionEvent event) {
        // TODO
        System.out.println("Undo filter btn clicked");
    }

    private void writeTaskToLog(File outputFile) {
        FileWriterTask fileWriterTask = new FileWriterTask(sourceFile, outputFile, selectedFilters);
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
