package src.designpatterns.behavioral.nullobject.impl;

import src.designpatterns.behavioral.nullobject.PaymentGateway;

public class StripeGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing Stripe payment of $" + amount);
    }
}
