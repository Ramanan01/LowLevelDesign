package src.designpatterns.creational.factory.impl;

import src.designpatterns.creational.factory.Notification;

public class PushNotification implements Notification {
    private final String apiToken;

    public PushNotification() {
        this.apiToken = "XYZ123TOKEN";
        System.out.println("Push API Token initialized");
    }

    public void notifyUser(String message) {
        System.out.println("Sending Push Notification with JSON payload: " + message);
    }
}