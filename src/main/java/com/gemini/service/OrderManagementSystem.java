package com.gemini.service;

import com.gemini.model.Entities.Order;

import java.util.*;

public class OrderManagementSystem {
    private static final OrderManagementSystem manager = new OrderManagementSystem();
    private final Map<String, OrderBook> orderBookMap = new TreeMap<>();

    private OrderManagementSystem() {

    }

    public static OrderManagementSystem getInstance() {
        return manager;
    }

    public void addToOrderBook(String orderInstrument, Order order) {
        orderBookMap.putIfAbsent(orderInstrument, new OrderBook(orderInstrument));
        orderBookMap.get(orderInstrument).add(order);
    }

    public Optional<Order> processOrder(String orderStr) {
        Optional<Order> optOrder = OrderParser.parse(orderStr);
        if (optOrder.isEmpty())
            throw new IllegalArgumentException(String.format("Invalid trade : [%s]", orderStr));

        Order order = optOrder.get();
        String orderInstrument = order.instrument();
        addToOrderBook(orderInstrument, order);

        return optOrder;
    }

    public List<Order> getSnapshot() {
        List<Order> orders = new ArrayList<>();
        for (Map.Entry<String, OrderBook> kv : orderBookMap.entrySet()) {
            orders.addAll(kv.getValue().listSellOrders());
        }

        for (Map.Entry<String, OrderBook> kv : orderBookMap.entrySet()) {
            orders.addAll(kv.getValue().listBuyOrders());
        }

        orders.sort(Comparator.comparing(Order::side).thenComparing(Order::arrivedTime));

        return orders;
    }

    public List<List<Order>> matchOrder(String instrument) {
        List<List<Order>> result = new ArrayList<>();
        matchOrder(instrument, result);
        return result;
    }

    private List<List<Order>> matchOrder(String instrument, List<List<Order>> accumulator) {
        OrderBook orderBook = orderBookMap.get(instrument);
        List<Order> matchedOrders = orderBook.match();
        if (matchedOrders.isEmpty()) return accumulator;
        accumulator.add(matchedOrders);
        return matchOrder(instrument, accumulator);
    }

    public OrderBook get(String instrument) {
        return orderBookMap.get(instrument);
    }

    public void clear() {
        orderBookMap.clear();
    }
}
