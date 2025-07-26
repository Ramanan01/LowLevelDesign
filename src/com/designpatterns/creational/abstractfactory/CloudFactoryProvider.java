package src.com.designpatterns.creational.abstractfactory;

public class CloudFactoryProvider {
    public static CloudResourceFactory getFactory(String provider){
        return switch(provider.toLowerCase()){
            case "aws" ->  new AWSFactory();
            case "azure" -> new AzureFactory();
            default -> throw new IllegalArgumentException("Illegal value for cloud servoce provider");
        };
    }
}
