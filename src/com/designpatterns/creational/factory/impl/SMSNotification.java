package src.com.designpatterns.creational.factory.impl;

import src.com.designpatterns.creational.factory.Notification;

public class SMSNotification implements Notification{
    public void notifyUser(String message){
        System.out.println("Sent notification to SMS. Message: " + message);
    }
}
