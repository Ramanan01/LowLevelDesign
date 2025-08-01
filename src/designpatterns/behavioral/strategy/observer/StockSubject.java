package src.designpatterns.behavioral.strategy.observer;

public interface StockSubject {
    void addObserver(StockObserver stockObserver);

    void removeObserver(StockObserver stockObserver);

    void notifyObservers();
}
