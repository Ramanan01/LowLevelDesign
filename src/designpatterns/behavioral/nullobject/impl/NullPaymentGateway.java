package src.designpatterns.behavioral.nullobject.impl;

import src.designpatterns.behavioral.nullobject.PaymentGateway;

public class NullPaymentGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount) {
        System.out.println("Payment gateway not available in this region. Skipping payment.");
    }
}
