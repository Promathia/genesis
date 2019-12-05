package com.home.genesis.representation.service;

import com.home.genesis.Constants;
import com.home.genesis.representation.StyleConstants;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.GenomeTile;
import com.home.genesis.representation.entity.WorldTile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

public class WorldViewService {

    private int currentActionPauseValue;
    private Semaphore semaphore;

    public WorldViewService(final int currentActionPauseValue, final Semaphore semaphore) {
        this.currentActionPauseValue = currentActionPauseValue;
        this.semaphore = semaphore;
    }

    public Pane initializeView(final WorldTile[][] tilesArray, final GenomeTile[][] genomeTiles) {
        final Pane rootPane = new Pane();
        rootPane.setPrefSize(Constants.SIZE_X, Constants.SIZE_Y);
        rootPane.getStyleClass().add(Styles.ROOT_PANE.getStyleName());
        rootPane.getChildren().addAll(
                initializeWorldPane(tilesArray),
                initializeControlButtonPane(),
                initializeStatisticsPane(genomeTiles));
        return rootPane;
    }

    public int getCurrentActionPauseValue() {
        return currentActionPauseValue;
    }

    private Pane initializeWorldPane(final WorldTile[][] tilesArray) {
        final Pane worldPane = new Pane();
        worldPane.getStyleClass().add(Styles.WORLD_PANE.getStyleName());
        worldPane.getChildren().addAll(Arrays.stream(tilesArray)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList()));
        worldPane.setLayoutX(StyleConstants.PADDING_MARGIN);
        worldPane.setLayoutY(StyleConstants.PADDING_MARGIN);
        return worldPane;
    }

    private Pane initializeStatisticsPane(final GenomeTile[][] genomeTiles) {
        final Pane statsPane = new Pane();
        statsPane.setLayoutY(StyleConstants.PADDING_MARGIN);
        statsPane.setLayoutX(StyleConstants.PADDING_MARGIN * 2 + (Constants.TILE_SIZE * Constants.CELL_NUMBER_X));
        statsPane.getChildren().addAll(getStatisticsView(genomeTiles));
        return statsPane;
    }

    private List<Node> getStatisticsView(final GenomeTile[][] genomeTiles) {
        final Pane genomePane = new Pane();
        genomePane.setLayoutY(StyleConstants.PADDING_MARGIN * 2);
        genomePane.getChildren().addAll(
                (Arrays.stream(genomeTiles)
                        .flatMap(Arrays::stream)
                        .collect(Collectors.toList())));
        int generationCounterPositionY = StyleConstants.PADDING_MARGIN * 4 + Constants.BOT_DNA_DIMENSION * Constants.STATS_TILE_SIZE;
        return Arrays.asList(
                getText("Current Best Genome", 0, StyleConstants.PADDING_MARGIN, Styles.GENOME_TABLE_LABEL, null),
                genomePane,
                getText("Generation Counter", 0, generationCounterPositionY, Styles.GENOME_TABLE_LABEL, null),
                getText("0",
                        //(Constants.STATS_TILE_SIZE * 3 + Constants.STATS_TILE_SIZE / 2),
                        0,
                        generationCounterPositionY + StyleConstants.PADDING_MARGIN * 4,
                        Styles.GENERATION_COUNTER,
                        Styles.GENERATION_COUNTER.getStyleName()),
                getText("Bot Best Action Counter",
                        0,
                        generationCounterPositionY + StyleConstants.PADDING_MARGIN * 6.5,
                        Styles.GENOME_TABLE_LABEL, null),
                getText("0",
                        //(Constants.STATS_TILE_SIZE * 3 + Constants.STATS_TILE_SIZE / 2),
                        0,
                        generationCounterPositionY + StyleConstants.PADDING_MARGIN * 10.5,
                        Styles.BOT_BEST_ACTION_COUNTER,
                        Styles.BOT_BEST_ACTION_COUNTER.getStyleName()),
                getText("Bots Progression", 0, generationCounterPositionY + StyleConstants.PADDING_MARGIN * 13, Styles.GENOME_TABLE_LABEL, null),
                getRealTimeStatChart()
        );
    }

    private LineChart<Number, Number> getRealTimeStatChart() {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generation");
        xAxis.setBorder(Border.EMPTY);
        xAxis.setAnimated(false);
        xAxis.setForceZeroInRange(false);
        yAxis.setLabel("Max Actions");
        yAxis.setAnimated(false);
        yAxis.setBorder(Border.EMPTY);
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setLegendVisible(false);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Bots Progression");
        lineChart.getData().add(series);
        lineChart.getStyleClass().add(Styles.GENERATION_ACTION_CHART.getStyleName());
        lineChart.setId(Styles.GENERATION_ACTION_CHART.getStyleName());
        lineChart.setTranslateY(380);
        lineChart.setTranslateX(-8);
        lineChart.setPrefSize(60, 100);
        return lineChart;
    }

    private Text getText(final String text, final double positionX, final double positionY, final Styles style, final String id) {
        final Text textNode = new Text(text);
        if (positionX > 0) {
            textNode.setLayoutX(positionX);
        }
        if (positionY > 0) {
            textNode.setLayoutY(positionY);
        }
        if (id != null && !id.isEmpty()) {
            textNode.setId(id);
        }
        textNode.getStyleClass().add(style.getStyleName());
        return textNode;
    }

    private Pane initializeControlButtonPane() {
        final Pane buttonPane = new Pane();
        buttonPane.getStyleClass().add(Styles.BUTTON_PANE.getStyleName());
        buttonPane.setLayoutX(StyleConstants.PADDING_MARGIN);
        buttonPane.setLayoutY(StyleConstants.PADDING_MARGIN * 2 + (Constants.TILE_SIZE * Constants.CELL_NUMBER_Y));
        Text speedCounter = new Text(); //TODO replace with method
        speedCounter.setText(String.valueOf(currentActionPauseValue));
        speedCounter.setTranslateX(22);
        speedCounter.setTranslateY(19);
        speedCounter.getStyleClass().add(Styles.SPEED_COUNTER_TEXT.getStyleName());
        buttonPane.getChildren().addAll(
                createSpeedButton(Styles.BUTTON_LEFT, speedCounter, -20, 0, 0),
                speedCounter,
                createSpeedButton(Styles.BUTTON_RIGHT, speedCounter, 20, 60, 0),
                createPlayPauseButton(Styles.PLAY_BUTTON, Styles.PAUSE_BUTTON, 90, 0));
        return buttonPane;
    }

    private Button createPlayPauseButton(final Styles playButtonStyle,
                                         final Styles pauseButtonStyle,
                                         final int translateX,
                                         final int translateY) {
        Button button = new Button();
        button.getStyleClass().add(pauseButtonStyle.getStyleName());
        button.setTranslateX(translateX);
        button.setTranslateY(translateY);
        button.setOnAction(new EventHandler<>() {

            private int clickCounter = 0;

            @Override
            public void handle(ActionEvent event) {
                if (clickCounter % 2 == 0) {
                    try {
                        semaphore.acquire(2);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    button.getStyleClass().remove(pauseButtonStyle.getStyleName());
                    button.getStyleClass().add(playButtonStyle.getStyleName());
                } else if (clickCounter % 2 == 1) {
                    semaphore.release(2);
                    button.getStyleClass().remove(playButtonStyle.getStyleName());
                    button.getStyleClass().add(pauseButtonStyle.getStyleName());
                }
                clickCounter++;
            }
        });
        return button;
    }

    private Button createSpeedButton(final Styles style,
                                     final Text counter,
                                     final int speedIncrement,
                                     final int translateX,
                                     final int translateY) {
        Button button = new Button();
        button.getStyleClass().add(style.getStyleName());
        button.setTranslateX(translateX);
        button.setTranslateY(translateY);
        button.setOnAction(e -> {
            if (currentActionPauseValue > 20 || speedIncrement > 0) {
                currentActionPauseValue = currentActionPauseValue + speedIncrement;
                counter.setText(String.valueOf(currentActionPauseValue));
            }
        });
        return button;
    }
}
