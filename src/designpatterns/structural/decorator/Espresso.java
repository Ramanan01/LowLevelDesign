package src.designpatterns.structural.decorator;

public class Espresso implements Beverage{
    public String getDescription(){
        return "Espresso";
    }

    public int getCost(){
        return 100;
    }
}
