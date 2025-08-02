package src.designpatterns.behavioral.nullobject;

public class ECommercePlatform {
    public static void main(String[] args) {
        PaymentGateway gateway1 = PaymentGatewayFactory.getPaymentGateway("USA", "Stripe");
        gateway1.processPayment(100.0);

        PaymentGateway gateway2 = PaymentGatewayFactory.getPaymentGateway("INDIA", "Stripe");
        gateway2.processPayment(200.0);

        PaymentGateway gateway3 = PaymentGatewayFactory.getPaymentGateway("INDIA", "PayPal");
        gateway3.processPayment(150.0);

        PaymentGateway gateway4 = PaymentGatewayFactory.getPaymentGateway("USA", "UnknownGateway");
        gateway4.processPayment(50.0);
    }
}
