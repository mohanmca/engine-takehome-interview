package com.gemini;

import java.util.Comparator;

public interface Entities {
    enum Side {
        SELL, BUY
    }

    record Order(String orderId, String parentOrderId, Side side, String instrument, int quantity, float price,
                 long orderedTime,
                 long arrivedTime) {
        public Order clone(int withQty) {
            return new Order(orderId + "_1", parentOrderId, side, instrument, withQty, price, System.currentTimeMillis(), System.currentTimeMillis());
        }
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
