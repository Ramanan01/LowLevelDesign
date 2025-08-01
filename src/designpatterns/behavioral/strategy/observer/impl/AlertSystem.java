package src.designpatterns.behavioral.strategy.observer.impl;

import src.designpatterns.behavioral.strategy.observer.StockObserver;

public class AlertSystem implements StockObserver {
    @Override
    public void update(String stockSymbol, double newPrice) {
        if (newPrice > 1000) {
            System.out.println("AlertSystem: High price alert for " + stockSymbol + " at $" + newPrice);
        }
    }
}