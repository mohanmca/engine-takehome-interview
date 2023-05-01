package com.gemini.listener;

import com.gemini.model.Entities.Order;

public interface TradeListener {
    String id();

    void onMatch(Order buyOrders, Order sellOrders);
}
