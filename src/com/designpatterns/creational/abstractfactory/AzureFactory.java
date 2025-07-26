package src.com.designpatterns.creational.abstractfactory;

public class AzureFactory implements CloudResourceFactory {
    public VMInstance createVMInstance(){
        return new AzureVMInstance();
    }

    public StorageBucket createStorageBucket(){
        return new AzureStorageBucket();
    }
}
