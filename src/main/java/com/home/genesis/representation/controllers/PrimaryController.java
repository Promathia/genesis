package com.home.genesis.representation.controllers;

import java.io.IOException;

import com.home.genesis.App;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class PrimaryController {

    private TableView table = new TableView();

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("/secondary");
        /*List<>
        table.getColumns().setAll()*/
    }
}
