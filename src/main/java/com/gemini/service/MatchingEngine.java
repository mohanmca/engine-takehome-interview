package com.gemini.service;

import com.gemini.listener.TradeListener;
import com.gemini.listener.TradeListenerPrinter;
import com.gemini.model.Entities;
import com.gemini.model.Entities.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class MatchingEngine {

    private static MatchingEngine engine = null;
    private final Map<String, TradeListener> listeners = new TreeMap<>();
    private final OrderManagementSystem oms;

    private MatchingEngine() {
        this(new TradeListenerPrinter(), OrderManagementSystem.getInstance());
    }

    private MatchingEngine(TradeListener listener, OrderManagementSystem orderBookManager) {
        this.oms = orderBookManager;
        initializeMatchingListener(listener);
    }

    public static MatchingEngine getInstance() {
        if (engine == null) {
            engine = new MatchingEngine();
        }
        return engine;
    }

    public void processOrder(String orderStr) {
        try {
            Optional<Order> optOrder = oms.processOrder(orderStr);
            optOrder.ifPresent(order -> processMatches(order.instrument()));
        } catch (Exception e) {
            System.err.printf("Error processing an order, and ignoring %s: \n", orderStr);
        }
    }

    private void processMatches(String orderInstrument) {
        List<List<Order>> result = oms.matchOrder(orderInstrument);
        for (List<Order> matches : result) {
            listeners.values().forEach(l -> l.onMatch(matches.get(0), matches.get(1)));
        }
    }

    public void register(TradeListener subscriber) {
        listeners.put(subscriber.id(), subscriber);
    }

    public void printSnapshot() {
        List<Entities.Order> orders = oms.getSnapshot();
        String report = OrderReportFormatter.INSTANCE.format(orders);
        System.err.printf("\n%s\n", report);
    }

    public OrderManagementSystem oms() {
        return oms;
    }

    private void initializeMatchingListener(TradeListener listener) {
        listeners.put(listener.id(), listener);
    }


}
