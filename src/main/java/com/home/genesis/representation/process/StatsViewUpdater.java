package com.home.genesis.representation.process;

import com.home.genesis.Constants;
import com.home.genesis.logic.entity.results.ActionResultBundle;
import com.home.genesis.logic.entity.results.GenomeResults;
import com.home.genesis.representation.Styles;
import com.home.genesis.representation.entity.GenomeTile;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.List;
import java.util.ListIterator;

public class StatsViewUpdater implements Runnable {

    private ActionResultBundle actionResultBundle;
    private GenomeTile[][] genomeTiles;
    private Pane mainPane;

    public StatsViewUpdater(final GenomeTile[][] genomeTiles, final Pane mainPane, final ActionResultBundle actionResultBundle) {
        this.actionResultBundle = actionResultBundle;
        this.genomeTiles = genomeTiles;
        this.mainPane = mainPane;
    }

    @Override
    public void run() {
        updateGenomeTableView();
        Integer botBestActionCounter = actionResultBundle.getBotBestActionCounter();
        Integer generationCounter = actionResultBundle.getGenerationCounter();
        if (botBestActionCounter != null && generationCounter != null) {
            updateTextCounterView(Styles.BOT_BEST_ACTION_COUNTER.getStyleName(), botBestActionCounter);
            updateTextCounterView(Styles.GENERATION_COUNTER.getStyleName(), generationCounter);
            updateGenerationActionGraphics(Styles.GENERATION_ACTION_CHART.getStyleName(), botBestActionCounter, generationCounter);
        }
    }

    private void updateGenerationActionGraphics(final String id, final Integer botBestActionCounter, final Integer generationCounter) {
        final LineChart<Number, Number> lineChart = (LineChart<Number, Number>) mainPane.lookup(String.format("#%s", id));
        if (lineChart == null) {
            System.out.printf("Line Chart was not found id=%s\n", id);
            return;
        }
        ObservableList<XYChart.Series<Number, Number>> data = lineChart.getData();
        if (data == null || data.size() == 0) {
            System.out.printf("No data series was added to the line chart id=%s\n", id);
            return;
        }
        ObservableList<XYChart.Data<Number, Number>> series = data.get(0).getData();
        if (series.size() > Constants.STATS_CHART_LENGTH) {
            series.remove(0);
        }
        data.get(0).getData().add(new XYChart.Data(generationCounter,botBestActionCounter));
    }

    private void updateTextCounterView(final String id, final Integer value) {
        Text text = (Text) mainPane.lookup(String.format("#%s", id));
        if (text == null) {
            System.out.printf("Text was not found id=%s\n", id);
            return;
        }
        if (value > Integer.parseInt(text.getText())) {
            text.setText(String.valueOf(value));
        }
    }

    private void updateGenomeTableView() {
        final List<GenomeResults> genomeResults = actionResultBundle.getGenomeResults();
        if (genomeResults.size() > 0) {
            ListIterator<Integer> integerListIterator = genomeResults.get(0).getPayload().listIterator();
            for (int x = 0; x < Constants.BOT_DNA_DIMENSION; x++) {
                for (int y = 0; y < Constants.BOT_DNA_DIMENSION; y++) {
                    GenomeTile genomeTile = genomeTiles[x][y];
                    Integer gen = integerListIterator.next();
                    genomeTile.getText().setText(String.valueOf(gen));
                }
            }
        }
    }

}
