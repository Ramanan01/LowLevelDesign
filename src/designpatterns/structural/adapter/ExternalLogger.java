package src.designpatterns.structural.adapter;

public class ExternalLogger {
    public void info(String msg) {
        System.out.println("External INFO: " + msg);
    }

    public void error(String msg) {
        System.out.println("External ERROR: " + msg);
    }
}
