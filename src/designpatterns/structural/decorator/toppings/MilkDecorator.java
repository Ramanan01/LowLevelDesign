package src.designpatterns.structural.decorator.toppings;

import src.designpatterns.structural.decorator.Beverage;
import src.designpatterns.structural.decorator.BeverageDecorator;

public class MilkDecorator extends BeverageDecorator {
    public MilkDecorator(Beverage beverage){
        super(beverage);
    }

    @Override
    public String getDescription(){
        return beverage.getDescription() + ", Milk";
    }

    @Override
    public int getCost() {
        return beverage.getCost() + 40;
    }
}
