package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.*;

public class MatchingEngine {

  private static MatchingEngine engine = null;
  private final Map<String, OrderBook> orderBookMap = new TreeMap<>();
  private final Map<String, TradeListener> listeners = new TreeMap<>();

  public void processOrder(String orderStr) {
    Optional<Order> optOrder = OrderParser.parse(orderStr);
    if (optOrder.isEmpty())
      throw new IllegalArgumentException(String.format("Invalid trade : [%s]", orderStr));

    Order order = optOrder.get();
    String orderInstrument = order.instrument();
    addToOrderBook(orderInstrument, order);
    matchOrder(orderInstrument);
  }

  public void matchOrder(String instrument) {
    OrderBook orderBook = orderBookMap.get(instrument);
    List<Order> matchedOrders = orderBook.match();
    if (matchedOrders.isEmpty()) {
      return;
    }
    listeners.values().forEach(l -> l.onMatch(matchedOrders.get(0), matchedOrders.get(1)));
    matchOrder(instrument);
  }

  private void addToOrderBook(String orderInstrument, Order order) {
    orderBookMap.putIfAbsent(orderInstrument, new OrderBook(orderInstrument));
    orderBookMap.get(orderInstrument).add(order);
  }

  public void register(TradeListener subscriber) {
    listeners.put(subscriber.id(), subscriber);
  }

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

  public Map<String, OrderBook> getOrderBook() {
    return orderBookMap;
  }
}
