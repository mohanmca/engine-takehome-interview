package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.*;

public class MatchingEngine {

  private static MatchingEngine engine = null;
  private final Map<String, OrderBook> orderBookMap = new TreeMap<>();
  private final Map<String, TradeListener> listeners = new TreeMap<>();

  private MatchingEngine() {
    this(new TradeListenerPrinter());
  }

  private MatchingEngine(TradeListener listener) {
    initializeMatchingListener(listener);
  }

  public static MatchingEngine getInstance() {
    if (engine == null) {
      engine = new MatchingEngine();
    }
    return engine;
  }

  private void initializeMatchingListener(TradeListener listener) {
    listeners.put(listener.id(), listener);
  }

  public void processOrder(String s) {
    Optional<Order> optOrder = OrderParser.parse(s);
    if (optOrder.isEmpty()) throw new IllegalArgumentException("Invalid trade : " + s);

    Order order = optOrder.get();
    orderBookMap.putIfAbsent(order.instrument(), new OrderBook(order.instrument()));
    orderBookMap.get(order.instrument()).add(order);
    List<Order> matches = orderBookMap.get(order.instrument()).match();
    while (!matches.isEmpty()) {
      listeners.values().stream().forEach(l -> l.onMatch(matches.get(0), matches.get(1)));
      matches.clear();
      matches.addAll(orderBookMap.get(order.instrument()).match());
    }
  }

  public void register(TradeListener subscriber) {
    listeners.put(subscriber.id(), subscriber);
  }

  public void printSnapshot() {
    List<Order> orders = getSnapshot();
    String report = OrderReportFormatter.INSTANCE.format(orders);
    System.err.println(report);
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
}
