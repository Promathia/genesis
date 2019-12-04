package com.home.genesis.general.process;

import com.home.genesis.logic.entity.results.ActionResultBundle;
import com.home.genesis.representation.controllers.WorldViewController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class UpdateUI implements Runnable {

    private BlockingQueue<ActionResultBundle> resultsQueue;
    private WorldViewController worldViewController;
    private Semaphore semaphore;

    public UpdateUI(BlockingQueue<ActionResultBundle> resultsQueue,
                    Semaphore semaphore,
                    WorldViewController worldViewController) {
        this.resultsQueue = resultsQueue;
        this.worldViewController = worldViewController;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        while (true) {
            try {
                worldViewController.handleActionResults(resultsQueue.take());
                Thread.sleep(1);
                semaphore.acquire();
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
