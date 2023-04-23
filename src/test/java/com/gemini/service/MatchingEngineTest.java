package com.gemini.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

class MatchingEngineTest {

  MatchingEngine engine = MatchingEngine.getInstance();

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  void placeOrder() {
    engine.processOrder("12345 BUY BTCUSD 5 10000");
    // assertEquals(engine.getOrderBook().size(), 1);
  }
}
