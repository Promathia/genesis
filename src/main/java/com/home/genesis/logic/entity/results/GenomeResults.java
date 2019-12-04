package com.home.genesis.logic.entity.results;

import java.util.List;

public class GenomeResults {

    private List<Integer> payload;

    public GenomeResults(final List<Integer> payload) {
        this.payload = payload;
    }

    public List<Integer> getPayload() {
        return payload;
    }

    public void setPayload(List<Integer> payload) {
        this.payload = payload;
    }
}
