package src.designpatterns.creational.abstractfactory;

public interface CloudResourceFactory {

    VMInstance createVMInstance();

    StorageBucket createStorageBucket();
}
