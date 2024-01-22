package com.batherphilippa.filterapp.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.batherphilippa.filterapp.constants.FileConstants.FXML_FILE_PATH;

/**
 * SplashScreenController - maneja el splash screen
 */
public class SplashScreenController implements Initializable {

    @FXML
    private AnchorPane paneSplashScreen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        splashScreenHandler();
    }

    /**
     * Crea un hilo para establecer un tiempo de espera para la pantalla de presentación; invoca loadAppScreen()
     */
    public void splashScreenHandler() {
        new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            loadAppScreen();
        }).start();
    }

    /**
     * Carga la pantalla principal de la aplicación.
     */
    private void loadAppScreen(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML_FILE_PATH + "main.fxml"));
                    loader.setController(new AppController());
                    Scene scene = new Scene(loader.load());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    paneSplashScreen.getScene().getWindow().hide();
                } catch (IOException ie) {
                    ie.printStackTrace();
                }
            }
        });
    }
}
