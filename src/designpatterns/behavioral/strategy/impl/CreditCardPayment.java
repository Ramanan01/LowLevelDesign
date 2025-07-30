package src.designpatterns.behavioral.strategy.impl;

import src.designpatterns.behavioral.strategy.PaymentStrategy;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card.");
    }

    @Override
    public String getType() {
        return "credit";
    }
}