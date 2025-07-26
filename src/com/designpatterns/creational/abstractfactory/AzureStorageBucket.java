package src.com.designpatterns.creational.abstractfactory;

public class AzureStorageBucket implements StorageBucket{
    public void upload(String file){
        System.out.println("Uploaded file to Azure: " + file);
    }
}
