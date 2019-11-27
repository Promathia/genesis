package com.home.genesis.representation.controllers;

import com.home.genesis.App;
import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("/primary");
    }
}
