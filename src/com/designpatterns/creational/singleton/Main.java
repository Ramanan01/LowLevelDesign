package src.com.designpatterns.creational.singleton;

public class Main {
    public static void main(String args[]) {
        Logger logger1 = Logger.getInstance();
        logger1.log("Logger 1 created");

        Logger logger2 = Logger.getInstance();
        logger2.log("Logger 2 created");

        System.out.println("Same instance? " + (logger1 == logger2));
    }
}
