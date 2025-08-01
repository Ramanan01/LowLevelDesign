package src.designpatterns.behavioral.strategy.observer.impl;

import src.designpatterns.behavioral.strategy.observer.StockObserver;

public class WebDashboard implements StockObserver {
    @Override
    public void update(String stockSymbol, double newPrice) {
        System.out.println("WebDashboard: " + stockSymbol + " updated to $" + newPrice);
    }
}
