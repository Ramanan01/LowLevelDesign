package src.com.designpatterns.creational.factory.impl;

import src.com.designpatterns.creational.factory.Notification;

public class EmailNotification implements Notification {
    private final String smtpServer;

    public EmailNotification() {
        this.smtpServer = "smtp.company.com";
        authenticate();
    }

    private void authenticate() {
        System.out.println("Authenticating Email Service with SMTP server " + smtpServer);
    }

    public void notifyUser(String message){
        System.out.println("Sent notification to Email. Message: " + message);
    }
}
