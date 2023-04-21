package com.gemini.service;

class MatchingEngineTest {

    MatchingEngine engine = MatchingEngine.getInstance();

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    void placeOrder() {
        engine.placeOrder("12345 BUY BTCUSD 5 10000");
        //assertEquals(engine.getOrderBook().size(), 1);
    }
}