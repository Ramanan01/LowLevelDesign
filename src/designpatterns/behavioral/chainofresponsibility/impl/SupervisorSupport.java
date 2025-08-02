package src.designpatterns.behavioral.chainofresponsibility.impl;

import src.designpatterns.behavioral.chainofresponsibility.SupportHandler;

public class SupervisorSupport extends SupportHandler {
    @Override
    public void handleRequest(String issueType) {
        if (issueType.equalsIgnoreCase("intermediate")) {
            System.out.println("Supervisor resolved the issue.");
        } else {
            System.out.println("Supervisor escalates to Manager.");
            if (nextHandler != null) nextHandler.handleRequest(issueType);
        }
    }
}
