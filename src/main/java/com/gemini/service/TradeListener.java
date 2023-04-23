package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.List;

public interface TradeListener {
    public void onMatch(List<Order> buyOrders, List<Order> sellOrders);
}
