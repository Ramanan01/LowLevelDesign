package src.designpatterns.structural.adapter;

public class Main {
    public static void main(String[] args) {
        AppLogger logger = new ExternalLoggerAdapter(new ExternalLogger());
        OrderService service = new OrderService(logger);
        service.placeOrder();
    }
}
