package src.designpatterns.creational.abstractfactory;

public class AWSFactory implements CloudResourceFactory {
    public VMInstance createVMInstance(){
        return new AWSVMInstance();
    }

    public StorageBucket createStorageBucket(){
        return new AWSStorageBucket();
    }
}
