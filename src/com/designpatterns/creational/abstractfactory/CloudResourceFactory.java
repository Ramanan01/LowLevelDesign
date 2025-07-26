package src.com.designpatterns.creational.abstractfactory;

public interface CloudResourceFactory {

    VMInstance createVMInstance();

    StorageBucket createStorageBucket();
}
