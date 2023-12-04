package com.batherphilippa.filterapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleGroup;

import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    @FXML
    private RadioButton rbtnOneFile;

    @FXML
    private ToggleGroup fileSelection;

    @FXML
    private RadioButton rbtnMultipleFiles;

    @FXML
    private TabPane tpFilterTabManager;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("App controller initialised...");
    }

    @FXML
    void selectFile(ActionEvent event) {

        if (rbtnOneFile.isSelected()) {
            System.out.println("One file is to be chosen.");
        } else {
            System.out.println("Multiples are to be chosen.");
        }

    }


}
