package src.designpatterns.creational.factory.flyweight;

import src.designpatterns.creational.factory.Notification;


public class Main {
    public static void main(String[] args) {
        NotificationFactory factory = new NotificationFactory();

        Notification n1 = factory.getNotification("email");
        n1.notifyUser("Welcome to the platform!");

        Notification n2 = factory.getNotification("sms");
        n2.notifyUser("Your OTP is 123456");

        Notification n3 = factory.getNotification("push");
        n3.notifyUser("New feature released!");

        // Request same type again — should be reused from cache
        Notification n4 = factory.getNotification("email");
        n4.notifyUser("Weekly newsletter!");

        // Verify object reuse
        System.out.println("\nAre n1 and n4 the same instance? " + (n1 == n4)); // true
        System.out.println("Are n2 and n3 the same instance? " + (n2 == n3)); // false
    }
}