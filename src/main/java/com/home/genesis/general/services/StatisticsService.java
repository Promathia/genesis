package com.home.genesis.general.services;

import com.home.genesis.logic.entity.SingleBot;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class StatisticsService {

    private static final String FILE_PATH = "results.csv";

    public StatisticsService() {
        this.clearFileAndWriteHeader();
    }

    public void writeDataToFile(final List<SingleBot> bots, final int generationNumber) {
        final Optional<Integer> first = bots.stream().map(SingleBot::getActionsCounter).sorted().findFirst();
        final Integer maxActionsTaken = first.orElse(null);
        if (maxActionsTaken != null && maxActionsTaken > 0) {
            try(FileWriter csvWriter = getFileWriter(true)) {
                final String str = String.format("%s,%s\n", generationNumber, maxActionsTaken);
                csvWriter.write(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearFileAndWriteHeader() {
        try(FileWriter csvWriter = getFileWriter(false)) {
            final String str = String.format("Start Time,%s\n", new Date().toString());
            csvWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileWriter getFileWriter(final boolean isInAppendMode) throws IOException {
        final URL resource = getClass().getClassLoader().getResource(FILE_PATH);
        if (resource == null) {
            throw new IOException("File not found for stats");
        }
        return new FileWriter(resource.getFile(), isInAppendMode);
    }
}
