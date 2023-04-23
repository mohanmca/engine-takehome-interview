package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MatchingEngine {

  private static MatchingEngine engine = null;
  private final Map<String, OrderBook> orderBookMap = new HashMap<>();
  private final Map<String, TradeListener> listeners;

  private MatchingEngine() {
    this(new TradeListenerPrinter());
  }

  private MatchingEngine(TradeListener subscriber) {
    listeners = new HashMap<>();
    listeners.put(subscriber.id(), subscriber);
  }

  public static MatchingEngine getInstance() {
    if (engine == null) {
      engine = new MatchingEngine();
    }
    return engine;
  }

  public void placeOrder(String s) {
    Optional<Order> optOrder = OrderParser.parse(s);
    if (optOrder.isPresent()) {
      Order order = optOrder.get();
      orderBookMap.putIfAbsent(order.instrument(), new OrderBook(order.instrument()));
      orderBookMap.get(order.instrument()).add(order);
      List<Order> matches = orderBookMap.get(order.instrument()).match();
      while (!matches.isEmpty()) {
        listeners.values().stream().forEach(l -> l.onMatch(matches.get(0), matches.get(1)));
        matches.clear();
        matches.addAll(orderBookMap.get(order.instrument()).match());
      }
    } else {
      throw new IllegalArgumentException("Invalid trade : " + s);
    }
  }

  public void register(TradeListener subscriber) {
    listeners.put(subscriber.id(), subscriber);
  }
}
