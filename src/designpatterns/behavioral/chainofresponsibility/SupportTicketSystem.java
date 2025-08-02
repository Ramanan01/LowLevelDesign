package src.designpatterns.behavioral.chainofresponsibility;

import src.designpatterns.behavioral.chainofresponsibility.impl.FrontLineSupport;
import src.designpatterns.behavioral.chainofresponsibility.impl.ManagerSupport;
import src.designpatterns.behavioral.chainofresponsibility.impl.SupervisorSupport;

public class SupportTicketSystem {
    public static void main(String[] args) {
        // Create handlers
        SupportHandler frontline = new FrontLineSupport();
        SupportHandler supervisor = new SupervisorSupport();
        SupportHandler manager = new ManagerSupport();

        // Setup Chain: FrontLine → Supervisor → Manager
        frontline.setNextHandler(supervisor);
        supervisor.setNextHandler(manager);

        // Test Cases
        System.out.println("Test 1: Basic Issue");
        frontline.handleRequest("basic");

        System.out.println("\nTest 2: Intermediate Issue");
        frontline.handleRequest("intermediate");

        System.out.println("\nTest 3: Advanced Issue");
        frontline.handleRequest("advanced");

        System.out.println("\nTest 4: Unknown Issue");
        frontline.handleRequest("unknown");
    }
}