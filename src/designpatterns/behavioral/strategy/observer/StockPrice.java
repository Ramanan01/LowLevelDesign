package src.designpatterns.behavioral.strategy.observer;

import src.designpatterns.behavioral.strategy.observer.impl.AlertSystem;
import src.designpatterns.behavioral.strategy.observer.impl.MobileApplication;
import src.designpatterns.behavioral.strategy.observer.impl.WebDashboard;

public class StockPrice {
    public static void main(String[] args) {
        Stock appleStock = new Stock("AAPL", 950.0);

        StockObserver user1App = new MobileApplication("Alice");
        StockObserver dashboard = new WebDashboard();
        StockObserver alertSystem = new AlertSystem();

        appleStock.addObserver(user1App);
        appleStock.addObserver(dashboard);
        appleStock.addObserver(alertSystem);

        appleStock.setPrice(980.0);
        appleStock.setPrice(1025.0);
        appleStock.setPrice(1025.0); // No change → No notification

        // Remove one observer
        appleStock.removeObserver(dashboard);

        appleStock.setPrice(900.0);
    }
}

