package com.gemini.service;

import com.gemini.Entities;

import java.util.List;

public enum OrderReportFormatter {
  INSTANCE;

  public String format(List<Entities.Order> orders) {
    StringBuffer sb = new StringBuffer();
    for (Entities.Order o : orders) {
      sb.append(toString(o));
    }
    return sb.toString();
  }

  private String toString(Entities.Order buyOrder) {
    StringBuffer sb = new StringBuffer();
    sb.append(buyOrder.parentOrderId()).append(" ");
    sb.append(buyOrder.side().name()).append(" ");
    sb.append(buyOrder.instrument()).append(" ");
    sb.append(buyOrder.quantity()).append(" ");
    sb.append(buyOrder.price()).append(System.lineSeparator());
    return sb.toString();
  }
}