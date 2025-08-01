package src.designpatterns.behavioral.observer;

public interface StockObserver {
    void update(String symbol, double newPrice);
}
