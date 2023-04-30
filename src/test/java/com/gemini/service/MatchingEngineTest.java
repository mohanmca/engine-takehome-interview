package com.gemini.service;

import com.gemini.Entities;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchingEngineTest {

  MatchingEngine engine = MatchingEngine.getInstance();
  final Entities.Order[] buy = new Entities.Order[1];
  final Entities.Order[] sell = new Entities.Order[1];
  private TradeListener listener;

  @BeforeEach
  void setUp() {
    listener = new TradeListener() {
      @Override
      public String id() {
        return "test";
      }

      @Override
      public void onMatch(Entities.Order buyOrders, Entities.Order sellOrders) {
        buy[0] = buyOrders;
        sell[0] = sellOrders;
      }
    };
    engine.register(listener);
  }

  @AfterEach
  void tearDown() {}

  @Test
  void placeOrder_when_simple() {
    engine.processOrder("12345 BUY BTCUSD 5 10000");
    engine.processOrder("12345 SELL BTCUSD 5 10000");

    OrderBook btcusd = engine.getOrderBook().get("BTCUSD");

    assertEquals(0, btcusd.buyOrders.size());
    assertEquals(0, btcusd.sellOrders.size());
    assertEquals("12345", buy[0].parentOrderId());
    assertEquals("12345", sell[0].parentOrderId());
  }

  @Test
  void placeOrder_when_partial() {
    engine.processOrder("12345 BUY BTCUSD 5 10000");
    engine.processOrder("12345 SELL BTCUSD 6 10000");

    OrderBook btcusd = engine.getOrderBook().get("BTCUSD");

    assertEquals(0, btcusd.buyOrders.size());
    assertEquals(1, btcusd.sellOrders.size());
    assertEquals("12345", buy[0].parentOrderId());
    assertEquals("12345", sell[0].parentOrderId());
  }
}
