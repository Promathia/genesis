package com.home.genesis.general.services;

import com.home.genesis.logic.entity.SingleBot;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StatisticsService {

    public static final String FILE_PATH = "/results.csv";

    public StatisticsService() {
        this.clearFileAndWriteHeader();
    }

    public void writeDataToFile(List<SingleBot> bots, int generationNumber) {
        Optional<Integer> first = bots.stream().map(SingleBot::getActionsCounter).sorted().findFirst();
        Integer maxActionsTaken = first.orElse(null);
        if (maxActionsTaken != null && maxActionsTaken > 0) {
            try(FileWriter csvWriter = new FileWriter(FILE_PATH, true)) {
                String str = String.format("%s,%s\n", generationNumber, maxActionsTaken);
                csvWriter.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFileAndWriteHeader() {
        try(FileWriter csvWriter = new FileWriter(FILE_PATH, false)) {
            String str = String.format("Start Time,%s\n", new Date().toString());
            csvWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
