package src.com.designpatterns.creational.abstractfactory;

public class AWSVMInstance implements VMInstance{
    public void start(){
        System.out.println("Started AWS VM instance");
    }
}
