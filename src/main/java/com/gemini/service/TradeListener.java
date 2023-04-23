package com.gemini.service;

import com.gemini.Entities.Order;

import java.util.List;

public interface TradeListener {
    String id();

    void onMatch(Order buyOrders, Order sellOrders);
}
