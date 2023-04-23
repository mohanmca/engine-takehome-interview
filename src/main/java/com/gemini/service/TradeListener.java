package com.gemini.service;

import com.gemini.Entities.Order;

public interface TradeListener {
  String id();

  void onMatch(Order buyOrders, Order sellOrders);
}
