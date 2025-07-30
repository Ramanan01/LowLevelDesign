package src.designpatterns.behavioral.strategy;

import src.designpatterns.behavioral.strategy.impl.CreditCardPayment;
import src.designpatterns.behavioral.strategy.impl.PayPalPayment;
import src.designpatterns.behavioral.strategy.impl.UpiPayment;

import java.util.*;

public class PaymentApp {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Map<String, PaymentStrategy> paymentMethods = new HashMap<>();
        List<PaymentStrategy> paymentStrategies = Arrays.asList(new UpiPayment(), new CreditCardPayment(), new PayPalPayment());


        for(PaymentStrategy paymentStrategy: paymentStrategies){
            paymentMethods.put(paymentStrategy.getType(), paymentStrategy);
        }

        PaymentContext paymentContext = new PaymentContext();

        while (true) {
            System.out.println("\nSelect payment method: credit / paypal / upi or 'exit' to quit");
            String method = scanner.nextLine().trim();

            if(method.equals("exit")) break;

            if(paymentMethods.get(method) == null){
                System.out.println("Invalid method. Try again.");
                continue;
            }


            System.out.print("Enter amount: ");
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                paymentContext.setPaymentStrategy(paymentMethods.get(method));
                paymentContext.pay(amount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount.");
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        System.out.println("Payment session ended.");
    }
}
