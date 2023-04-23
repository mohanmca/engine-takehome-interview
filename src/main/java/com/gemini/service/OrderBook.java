package com.gemini.service;

import com.gemini.Entities.OrderComparator;
import com.gemini.Entities.Side;

import java.util.Collections;
import java.util.List;
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

    public List<Order> add(Order order) {
        if (order.side() == Side.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public List<List<Order>> match() {
        List<List<Order>> result = Collections.emptyList();
        if (buyOrders.peek().price() >= sellOrders.peek().price()) {
            Order b = buyOrders.poll();
            Order s = sellOrders.poll();
            if (b.quantity() > s.quantity()) {
                buyOrders.add(b.clone(b.quantity() - s.quantity()));
            } else if (b.quantity() < s.quantity()) {
                sellOrders.add(s.clone(s.quantity() - b.quantity()));
            }
            int minQty = Math.min(b.quantity(), s.quantity());
            result = List.of(List.of(b.clone(minQty)), List.of(s.clone(minQty)));
        }
        return result;
    }

    private void remove(String orderId) {

    }


}
