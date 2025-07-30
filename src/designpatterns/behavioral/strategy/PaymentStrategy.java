package src.designpatterns.behavioral.strategy;

public interface PaymentStrategy {
    void pay(double amount);
    String getType();
}
