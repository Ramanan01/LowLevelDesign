package src.designpatterns.behavioral.strategy;

public class PaymentContext {
    private PaymentStrategy paymentStrategy;

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }

    public void pay(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("No payment strategy set.");
        }
        paymentStrategy.pay(amount);
    }
}
