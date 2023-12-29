package com.batherphilippa.filterapp;

import com.batherphilippa.filterapp.controller.AppController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.batherphilippa.filterapp.constants.FileConstants.FXML_FILE_PATH;
import static com.batherphilippa.filterapp.constants.MessageConstants.CONSOLE_MSG_APP_INITIALISATION;
import static com.batherphilippa.filterapp.constants.MessageConstants.CONSOLE_MSG_APP_TERMINATING;

/**
 * App - la entrada a la aplicación
 *
 * @author Philippa Bather
 */
public class App extends Application {

    public static void main(String[] args) { launch(); }

    @Override
    public void init() throws Exception {
        System.out.println(CONSOLE_MSG_APP_INITIALISATION);
        super.init();
    }

    @Override
    public void stop() throws Exception {
        System.out.println(CONSOLE_MSG_APP_TERMINATING);
        super.stop();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource(FXML_FILE_PATH + "main.fxml"));
        loader.setController(new AppController());
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
}