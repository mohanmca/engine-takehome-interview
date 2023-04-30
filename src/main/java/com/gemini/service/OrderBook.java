package com.gemini.service;

import com.gemini.Entities.OrderComparator;
import com.gemini.Entities.Side;

import java.util.*;

import static com.gemini.Entities.Order;

public class OrderBook {

  private final String instrumentId;

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
      }
      int minQty = Math.min(b.quantity(), s.quantity());
      result = new ArrayList<>(List.of(b.clone(minQty), s.clone(minQty)));

    }
    return result;
  }

  public List<Order> listSellOrders() {
    List<Order> orders = new ArrayList<>(sellOrders);
    orders.sort(Comparator.comparingLong(Order::arrivedTime));
    return orders;
  }

  public List<Order> listBuyOrders() {
    List<Order> orders = new ArrayList<>(buyOrders);
    orders.sort(Comparator.comparingLong(Order::arrivedTime));
    return orders;
  }

}
