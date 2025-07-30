package src.designpatterns.behavioral.strategy.impl;

import src.designpatterns.behavioral.strategy.PaymentStrategy;

public class UpiPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using UPI.");
    }

    @Override
    public String getType() {
        return "upi";
    }
}