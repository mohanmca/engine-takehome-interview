package com.gemini.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderManagementSystemTest {

    private final static OrderManagementSystem oms = OrderManagementSystem.getInstance();

    @BeforeAll()
    static void setup() {
        oms.clear();
    }

    @AfterEach
    void tearDown() {
        oms.clear();
    }

    @Test
    void placeOrder_when_simple() {
        oms.processOrder("12345 BUY BTCUSD 5 10000");
        oms.processOrder("12345 SELL BTCUSD 5 10000");

        OrderBook btcusd = oms.get("BTCUSD");

        assertEquals(1, btcusd.buyOrders.size());
        assertEquals(1, btcusd.sellOrders.size());

    }

    @Test
    void placeOrder_when_partial_fill_get_matched() {
        oms.processOrder("12345 BUY BTCUSD 5 10000");
        oms.processOrder("12345 SELL BTCUSD 6 10000");

        OrderBook btcusd = oms.get("BTCUSD");

        assertEquals(1, btcusd.buyOrders.size());
        assertEquals(1, btcusd.sellOrders.size());

        oms.processOrder("12345 BUY BTCUSD 1 10000");

        assertEquals(2, btcusd.buyOrders.size());
        assertEquals(1, btcusd.sellOrders.size());
    }

    @Test
    void placeOrder_when_match_by_arrival_time() {

        oms.processOrder("12347 SELL BTCUSD 5 10000");
        oms.processOrder("12348 SELL BTCUSD 5 10000");
        oms.processOrder("12345 BUY BTCUSD 5 10000");

        OrderBook btcusd = oms.get("BTCUSD");

        assertEquals(1, btcusd.buyOrders.size());
        assertEquals(2, btcusd.sellOrders.size());

    }


}