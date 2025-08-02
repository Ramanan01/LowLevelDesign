package src.designpatterns.behavioral.chainofresponsibility.impl;

import src.designpatterns.behavioral.chainofresponsibility.SupportHandler;

public class FrontLineSupport extends SupportHandler {
    @Override
    public void handleRequest(String issueType){
        if(issueType.equalsIgnoreCase("basic")){
            System.out.println("Frontline Support resolved the issue.");
        }
        else{
            System.out.println("Frontline Support escalates to Supervisor.");
            if(nextHandler != null){
                nextHandler.handleRequest(issueType);
            }
        }
    }
}
