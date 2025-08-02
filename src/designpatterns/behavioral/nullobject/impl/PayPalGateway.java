package src.designpatterns.behavioral.nullobject.impl;

import src.designpatterns.behavioral.nullobject.PaymentGateway;

public class PayPalGateway implements PaymentGateway {
    @Override
    public void processPayment(double amount){
        System.out.println("Processing PayPal payment of $" + amount);
    }
}
