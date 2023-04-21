package com.gemini;

import java.util.Comparator;

public interface Entities {
    enum Side {
        BUY, SELL
    }

    record Order(String orderId, Side side, String instrument, int quantity, float price, long orderedTime,
                 long arrivedTime) {
    }

    class OrderComparator implements Comparator<Order> {
        @Override
        public int compare(Order order, Order anotherOrder) {
            return Comparator
                    .comparing(Order::instrument)
                    .thenComparing(Order::side)
                    .thenComparingDouble(Order::price)
                    .thenComparingLong(Order::arrivedTime)
                    .compare(order, anotherOrder);
        }
    }

}
