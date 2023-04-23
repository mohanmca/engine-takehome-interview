package com.gemini.service;

import com.gemini.Entities;

public class TradeListenerDummy implements TradeListener {
  @Override
  public String id() {
    return null;
  }

  @Override
  public void onMatch(Entities.Order buyOrders, Entities.Order sellOrders) {}
}
