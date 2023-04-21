package com.gemini.service;

import com.gemini.Entities.OrderComparator;
import com.gemini.Entities.Side;

import java.util.PriorityQueue;

import static com.gemini.Entities.Order;

public class OrderBook {

    String instrumentId;

    OrderComparator comparator = new OrderComparator();
    OrderComparator sellComparator = (OrderComparator) new OrderComparator().reversed();

    PriorityQueue<Order> buyOrders = new PriorityQueue<>(comparator);
    PriorityQueue<Order> sellOrders = new PriorityQueue<>(sellComparator);

    public OrderBook(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    private void add(Order order) {
        if (order.side() == Side.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    private void remove(String orderId) {

    }


}
