package src.com.designpatterns.creational.abstractfactory;

public class AzureVMInstance implements VMInstance{
    public void start(){
        System.out.println("Started Azure VM instance");
    }
}
