package src.designpatterns.behavioral.strategy.impl;

import src.designpatterns.behavioral.strategy.PaymentStrategy;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("Paid ₹" + amount + " using PayPal.");
    }

    @Override
    public String getType() {
        return "paypal";
    }
}
