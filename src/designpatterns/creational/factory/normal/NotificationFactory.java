package src.designpatterns.creational.factory.normal;
import src.designpatterns.creational.factory.Notification;
import src.designpatterns.creational.factory.impl.EmailNotification;
import src.designpatterns.creational.factory.impl.PushNotification;
import src.designpatterns.creational.factory.impl.SMSNotification;

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
