package src.designpatterns.behavioral.observer;

public interface StockSubject {
    void addObserver(StockObserver stockObserver);

    void removeObserver(StockObserver stockObserver);

    void notifyObservers();
}
