package src.designpatterns.behavioral.observer.impl;

import src.designpatterns.behavioral.observer.StockObserver;

public class MobileApplication implements StockObserver {
    private final String user;

    public MobileApplication(String user) {
        this.user = user;
    }

    @Override
    public void update(String stockSymbol, double newPrice) {
        System.out.println("MobileApp (" + user + ") received update: " + stockSymbol + " is now $" + newPrice);
    }
}