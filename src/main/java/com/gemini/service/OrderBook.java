package com.gemini.service;

import com.gemini.Entities.OrderComparator;
import com.gemini.Entities.Side;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static com.gemini.Entities.Order;

public class OrderBook {

  String instrumentId;

  Comparator<Order> comparator = new OrderComparator().reversed();
  Comparator<Order> sellComparator = new OrderComparator();

  PriorityQueue<Order> buyOrders = new PriorityQueue<>(comparator);
  PriorityQueue<Order> sellOrders = new PriorityQueue<>(sellComparator);

  public OrderBook(String instrumentId) {
    this.instrumentId = instrumentId;
  }

  public void add(Order order) {
    if (order.side() == Side.BUY) {
      buyOrders.add(order);
    } else {
      sellOrders.add(order);
    }
  }

  public List<Order> match() {
    List<Order> result = Collections.emptyList();
    if (buyOrders.isEmpty() || sellOrders.isEmpty()) return result;

    Order highestBid = buyOrders.peek();
    Order lowestOffer = sellOrders.peek();
    if (highestBid.price() >= lowestOffer.price()) {
      Order b = buyOrders.poll();
      Order s = sellOrders.poll();
      if (b.quantity() > s.quantity()) {
        buyOrders.add(b.clone(b.quantity() - s.quantity()));
      } else if (b.quantity() < s.quantity()) {
        sellOrders.add(s.clone(s.quantity() - b.quantity()));
      } else {
        buyOrders.add(b);
        sellOrders.add(s);
      }
      int minQty = Math.min(b.quantity(), s.quantity());
      result = List.of(b.clone(minQty), s.clone(minQty));
    }
    return result;
  }

  private void remove(String orderId) {}
}
