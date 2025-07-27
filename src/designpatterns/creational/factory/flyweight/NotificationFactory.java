package src.designpatterns.creational.factory.flyweight;

import src.designpatterns.creational.factory.Notification;
import src.designpatterns.creational.factory.impl.EmailNotification;
import src.designpatterns.creational.factory.impl.PushNotification;
import src.designpatterns.creational.factory.impl.SMSNotification;

import java.util.HashMap;
import java.util.Map;

public class NotificationFactory {
    Map<String, Notification> cache = new HashMap<>();
    public Notification getNotification(String type){
        return cache.computeIfAbsent(type.toLowerCase(), t -> switch(t) {
            case "email" -> new EmailNotification();
            case "push" -> new PushNotification();
            case "sms" -> new SMSNotification();
            default -> throw new IllegalArgumentException("Unkown Notification Type " + type);
        });
    }
}
