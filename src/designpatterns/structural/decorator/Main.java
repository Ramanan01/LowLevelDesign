package src.designpatterns.structural.decorator;

import src.designpatterns.structural.decorator.toppings.CaramelDecorator;
import src.designpatterns.structural.decorator.toppings.MilkDecorator;

public class Main {
    public static void main(String[] args){
        Beverage beverage = new CaramelDecorator(new MilkDecorator(new Espresso()));
        System.out.println("My beverage is " + beverage.getDescription());
        System.out.println("The cost is " + beverage.getCost());
    }
}
