package com.gemini.service;

import com.gemini.listener.TradeListener;
import com.gemini.model.Entities.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MatchingEngineTest {

    final Order[] buy = new Order[1];
    final Order[] sell = new Order[1];
    MatchingEngine engine = MatchingEngine.getInstance();
    private TradeListener mockListener;

    @BeforeEach
    void setUp() {
        mockListener = new TradeListener() {
            @Override
            public String id() {
                return "test";
            }

            @Override
            public void onMatch(Order buyOrders, Order sellOrders) {
                buy[0] = buyOrders;
                sell[0] = sellOrders;
            }
        };
        engine.register(mockListener);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void placeOrder_when_simple() {
        engine.processOrder("12345 BUY BTCUSD 5 10000");
        engine.processOrder("12345 SELL BTCUSD 5 10000");

        OrderBook btcusd = engine.oms().get("BTCUSD");

        assertEquals(0, btcusd.buyOrders.size());
        assertEquals(0, btcusd.sellOrders.size());
        assertEquals("12345", buy[0].parentOrderId());
        assertEquals("12345", sell[0].parentOrderId());
    }

    @Test
    void placeOrder_when_partial_fill() {
        engine.processOrder("12345 BUY BTCUSD 5 10000");
        engine.processOrder("12345 SELL BTCUSD 6 10000");

        OrderBook btcusd = engine.oms().get("BTCUSD");

        assertEquals(0, btcusd.buyOrders.size());
        assertEquals(1, btcusd.sellOrders.size());
        assertEquals("12345", buy[0].parentOrderId());
        assertEquals("12345", sell[0].parentOrderId());
    }

    @Test
    void placeOrder_when_partial_fill_get_matched() {
        engine.processOrder("12345 BUY BTCUSD 5 10000");
        engine.processOrder("12345 SELL BTCUSD 6 10000");

        OrderBook btcusd = engine.oms().get("BTCUSD");

        assertEquals(0, btcusd.buyOrders.size());
        assertEquals(1, btcusd.sellOrders.size());
        assertEquals("12345", buy[0].parentOrderId());
        assertEquals("12345", sell[0].parentOrderId());

        engine.processOrder("12345 BUY BTCUSD 1 10000");

        assertEquals(0, btcusd.buyOrders.size());
        assertEquals(0, btcusd.sellOrders.size());
    }

    @Test
    void placeOrder_when_match_by_arrival_time() {

        engine.processOrder("12347 SELL BTCUSD 5 10000");
        engine.processOrder("12348 SELL BTCUSD 5 10000");
        engine.processOrder("12345 BUY BTCUSD 5 10000");

        OrderBook btcusd = engine.oms().get("BTCUSD");

        assertEquals(0, btcusd.buyOrders.size());
        assertEquals(2, btcusd.sellOrders.size());
        assertEquals("12345", buy[0].parentOrderId());
        assertEquals("12347", sell[0].parentOrderId());
    }

}
