package src.designpatterns.creational.abstractfactory;

public class Main {
    public static void main(String[] args) {
        CloudResourceFactory factory = CloudFactoryProvider.getFactory("aws");

        VMInstance vmInstance = factory.createVMInstance();
        StorageBucket storageBucket = factory.createStorageBucket();

        vmInstance.start();
        storageBucket.upload("project.zip");

        // Switching to Azure
        factory = CloudFactoryProvider.getFactory("azure");

        vmInstance = factory.createVMInstance ();
        storageBucket = factory.createStorageBucket();

        vmInstance.start();
        storageBucket.upload("image.png");
    }
}
