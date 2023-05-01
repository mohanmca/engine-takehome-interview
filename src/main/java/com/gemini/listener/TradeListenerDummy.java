package com.gemini.listener;

import com.gemini.model.Entities;

public class TradeListenerDummy implements TradeListener {
    @Override
    public String id() {
        return null;
    }

    @Override
    public void onMatch(Entities.Order buyOrders, Entities.Order sellOrders) {
    }
}
