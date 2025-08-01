package src.designpatterns.behavioral.strategy.observer;

import java.util.HashSet;
import java.util.Set;

public class Stock implements StockSubject{
    private final Set<StockObserver> stockObservers;
    private final String symbol;
    double price;

    Stock(String symbol, double openPrice){
        stockObservers = new HashSet<>();
        this.symbol = symbol;
        this.price = openPrice;
    }

    @Override
    public void addObserver(StockObserver stockObserver){
        stockObservers.add(stockObserver);
    }

    @Override
    public void removeObserver(StockObserver stockObserver){
        stockObservers.remove(stockObserver);
    }

    @Override
    public void notifyObservers(){
        stockObservers.forEach(o->o.update(symbol, price));
    }

    public void setPrice(double price){
        this.price = price;
        notifyObservers();
    }

}
