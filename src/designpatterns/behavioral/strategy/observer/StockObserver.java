package src.designpatterns.behavioral.strategy.observer;

public interface StockObserver {
    void update(String symbol, double newPrice);
}
