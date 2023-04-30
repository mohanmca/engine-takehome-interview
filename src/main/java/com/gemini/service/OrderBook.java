package com.gemini.service;

import com.gemini.model.Entities.OrderComparator;
import com.gemini.model.Entities.Side;

import java.util.*;

import static com.gemini.model.Entities.Order;

public class OrderBook {

    private final String instrumentId;

    Comparator<Order> buyComparator = new OrderComparator().reversed();
    Comparator<Order> sellComparator = new OrderComparator();

    PriorityQueue<Order> buyOrders = new PriorityQueue<>(buyComparator);
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

    public boolean hasMatch() {
        if (buyOrders.isEmpty() || sellOrders.isEmpty()) return false;
        Order highestBid = buyOrders.peek();
        Order lowestOffer = sellOrders.peek();
        return highestBid.price() >= lowestOffer.price();
    }

    public List<Order> match() {
        List<Order> result = Collections.emptyList();
        if (hasMatch()) {
            Order buyOrder = buyOrders.poll();
            Order sellOrder = sellOrders.poll();

            Optional<Order> optUnfilled = fillOrder(buyOrder, sellOrder);
            optUnfilled.ifPresent(this::handlePartialFill);

            int minQty = Math.min(buyOrder.quantity(), sellOrder.quantity());
            result = new ArrayList<Order>(List.of(buyOrder.clone(minQty), sellOrder.clone(minQty)));

        }
        return result;
    }

    private void handlePartialFill(Order unfilled) {
        add(unfilled);
    }

    private Optional<Order> fillOrder(Order buyOrder, Order sellOrder) {
        Order unfilledOrder = null;
        if (buyOrder.quantity() > sellOrder.quantity()) {
            unfilledOrder = buyOrder.clone(buyOrder.quantity() - sellOrder.quantity());
        } else if (buyOrder.quantity() < sellOrder.quantity()) {
            unfilledOrder = sellOrder.clone(sellOrder.quantity() - buyOrder.quantity());
        } else {
            // Exact match do nothing
        }
        return Optional.ofNullable(unfilledOrder);
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
