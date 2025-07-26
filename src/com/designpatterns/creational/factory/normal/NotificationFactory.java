package src.com.designpatterns.creational.factory.normal;
import src.com.designpatterns.creational.factory.*;
import src.com.designpatterns.creational.factory.impl.EmailNotification;
import src.com.designpatterns.creational.factory.impl.PushNotification;
import src.com.designpatterns.creational.factory.impl.SMSNotification;

public class NotificationFactory {
    public static Notification createNotification(String type){
        if(type==null || type.isEmpty()) return null;

        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "push" -> new PushNotification();
            case "sms" -> new SMSNotification();
            default -> throw new IllegalArgumentException("Unkown Notification Type " + type);
        };
    }
}
