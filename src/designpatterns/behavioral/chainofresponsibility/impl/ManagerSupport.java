package src.designpatterns.behavioral.chainofresponsibility.impl;

import src.designpatterns.behavioral.chainofresponsibility.SupportHandler;

public class ManagerSupport extends SupportHandler {
    @Override
    public void handleRequest(String issueType) {
        if (issueType.equalsIgnoreCase("advanced")) {
            System.out.println("Manager resolved the issue.");
        } else {
            System.out.println("Issue could not be resolved. Needs further escalation.");
        }
    }
}