package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MatchingEngine {

    private static MatchingEngine engine = null;
    private final OrderBook orderBook = new OrderBook();
    private final List<TradeListener> listeners;


    private MatchingEngine(TradeListener subscriber) {
        listeners = new ArrayList<>();
        listeners.add(subscriber);
    }

    public static MatchingEngine getInstance() {
        if (engine == null) {
            engine = new MatchingEngine();
        }
        return engine;
    }

    public void placeOrder(String s) {
        Optional<Order> order = OrderParser.parse(s);
        if (order.isPresent()) {
            order.ifPresent(orderBook::add);
        } else {
            throw new IllegalArgumentException("Invalid trade : " + s);
        }
        List<List<Order>> matches = orderBook.match();
        if(!matches.isEmpty()){
            listeners.stream().forEach( l -> l.onMatch( matches.get(0), matches.get(1) ) );
        }
    }


}
