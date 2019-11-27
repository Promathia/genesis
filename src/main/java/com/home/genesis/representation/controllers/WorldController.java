package com.home.genesis.representation.controllers;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class WorldController {

    private TableView table = new TableView();

    public void initializeWorldMatrix(Stage stage) {
        Scene scene = new Scene(new Group());
        stage.setTitle("Life Simulator");
        stage.setWidth(1200);
        stage.setHeight(700);
        final Label label = new Label("World");
        label.setFont(new Font("Arial", 20));
        table.setEditable(true);
        //Pane header = (Pane) table.lookup("TableHeaderRow");
        //header.setVisible(false);
        List<TableColumn> tableColumnList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TableColumn tableColumn = new TableColumn();
            tableColumn.setMinWidth(10);
            tableColumn.setCellValueFactory(new PropertyValueFactory<Integer, String>(String.valueOf(i)));
        }
        table.getColumns().addAll(tableColumnList);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setScene(scene);
        stage.show();
    }

}
