package src.designpatterns.structural.adapter;

public class OrderService {
    private final AppLogger logger;

    public OrderService(AppLogger logger){
        this.logger = logger;
    }

    public void placeOrder(){
        logger.logInfo("Placing order");
        // ... do something
        logger.logInfo("Order placed");
    }
}
