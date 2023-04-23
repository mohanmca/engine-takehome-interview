package com.gemini.service;

import com.gemini.Entities.Order;

public class TradeListenerPrinter implements TradeListener {
  @Override
  public String id() {
    return "default";
  }

  @Override
  public void onMatch(Order buyOrders, Order sellOrders) {
    StringBuffer sb = tradeReport(buyOrders, sellOrders);
    System.out.println(sb);
    return;
  }

  private StringBuffer tradeReport(Order buyOrder, Order sellOrder) {
    StringBuffer sb = new StringBuffer();
    sb.append("TRADE ");
    sb.append(buyOrder.instrument()).append(" ");
    sb.append(buyOrder.parentOrderId()).append(" ");
    sb.append(sellOrder.parentOrderId()).append(" ");
    sb.append(sellOrder.quantity()).append(" ");
    sb.append(sellOrder.price()).append(" ");
    sb.append("\n");
    return sb;
  }
}
