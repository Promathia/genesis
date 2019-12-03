package com.home.genesis.general.process;

import com.home.genesis.logic.entity.ActionResultBundle;
import com.home.genesis.representation.controllers.WorldViewController;

import java.util.concurrent.BlockingQueue;

public class UpdateUI implements Runnable {

    private BlockingQueue<ActionResultBundle> resultsQueue;
    private WorldViewController worldViewController;

    public UpdateUI(BlockingQueue<ActionResultBundle> resultsQueue, WorldViewController worldViewController) {
        this.resultsQueue = resultsQueue;
        this.worldViewController = worldViewController;
    }

    @Override
    public void run() {
        while (true) {
            try {
                worldViewController.handleActionResults(resultsQueue.take());
                Thread.sleep(17); // 1000 ms / 60 frame rate per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
