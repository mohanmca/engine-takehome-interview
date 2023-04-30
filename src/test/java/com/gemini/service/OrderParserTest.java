package com.gemini.service;

import com.gemini.model.Entities;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.gemini.model.Entities.Order;
import static org.junit.jupiter.api.Assertions.*;

class OrderParserTest {

    @Test
    public void testOrderParser() {
        Optional<Order> parsedResult = OrderParser.parse("12345 BUY BTCUSD 5 10000");
        if (parsedResult.isEmpty()) {
            fail("Order should have been parsed for given input!");
            return;
        }
        boolean successful = parsedResult.filter(o -> o.side() == Entities.Side.BUY && o.orderId().equals("12345")).isPresent();
        assertTrue(successful, "Order parsed successfully");
    }

}