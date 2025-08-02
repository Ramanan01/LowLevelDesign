package src.designpatterns.behavioral.nullobject;

import src.designpatterns.behavioral.nullobject.impl.NullPaymentGateway;
import src.designpatterns.behavioral.nullobject.impl.PayPalGateway;
import src.designpatterns.behavioral.nullobject.impl.StripeGateway;

public class PaymentGatewayFactory {
    public static PaymentGateway getPaymentGateway(String region, String gatewayType){
        if(gatewayType.equalsIgnoreCase("Stripe") && region.equalsIgnoreCase("India")){
            return new NullPaymentGateway();
        }
        else if (gatewayType.equalsIgnoreCase("Stripe")) {
            return new StripeGateway();
        }
        else if (gatewayType.equalsIgnoreCase("PayPal")) {
            return new PayPalGateway();
        }
        else{
            return new NullPaymentGateway();
        }
    }
}
