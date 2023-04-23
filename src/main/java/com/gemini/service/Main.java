package com.gemini.service;

public class Main {
  public static void main(String[] args) throws Exception {
    String input = new String(System.in.readAllBytes());
    String[] lines = input.split(System.lineSeparator());
    MatchingEngine engine = MatchingEngine.getInstance();
    for (String line : lines) {
      engine.placeOrder(line);
    }

  }
}
