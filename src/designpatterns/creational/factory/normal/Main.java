package src.designpatterns.creational.factory.normal;

import src.designpatterns.creational.factory.Notification;

public class Main {
    public static void main(String[] args){
        Notification emailNotification = NotificationFactory.createNotification("email");
        emailNotification.notifyUser("Email notification sent");

        Notification pushNotification = NotificationFactory.createNotification("push");
        pushNotification.notifyUser("Push notification sent");

        Notification smsNotification = NotificationFactory.createNotification("SMS");
        pushNotification.notifyUser("SMS notification sent");
    }
}
