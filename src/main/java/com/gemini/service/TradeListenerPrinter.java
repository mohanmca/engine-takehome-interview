package com.gemini.service;

import com.gemini.Entities;

import java.util.List;

public class TradeListenerPrinter implements TradeListener {
    @Override
    public void onMatch(List<Entities.Order> buyOrders, List<Entities.Order> sellOrders) {
        //print
    }
}
