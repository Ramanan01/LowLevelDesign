package src.designpatterns.structural.decorator.toppings;

import src.designpatterns.structural.decorator.Beverage;
import src.designpatterns.structural.decorator.BeverageDecorator;

public class CaramelDecorator extends BeverageDecorator {
    public CaramelDecorator(Beverage beverage){
        super(beverage);
    }

    @Override
    public String getDescription(){
        return beverage.getDescription() + ", Caramel";
    }

    @Override
    public int getCost(){
        return beverage.getCost() + 20;
    }
}
