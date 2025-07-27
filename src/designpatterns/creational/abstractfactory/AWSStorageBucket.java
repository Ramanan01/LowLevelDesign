package src.designpatterns.creational.abstractfactory;

public class AWSStorageBucket implements StorageBucket{
    public void upload(String file){
        System.out.println("Uploaded file to AWS: " + file);
    }
}
