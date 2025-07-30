package src.practiceproblems.agentrating;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    public static final AgentService agentService = new AgentService();

    public static void main(String[] args){

        while(true){
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine().trim());

            switch (choice){
                case 1: handleAddAgent();
                        break;
                case 2: handleAddRating();
                        break;
                case 3: handleGetRatings();
                        break;
                case 4: System.out.println("Exiting. Goodbye!");
                        return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("========== Agent Rating System ==========");
        System.out.println("1. Add Agent");
        System.out.println("2. Add Rating");
        System.out.println("3. List Agents by Average Rating");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void handleAddAgent(){
        try{
            System.out.print("Enter agent id: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter agent name: ");
            String name = scanner.nextLine().trim();
            agentService.addAgent(id, name);
        }
        catch (NumberFormatException e){
            System.err.println("Could not parse integer id");
        }
    }

    private static void handleAddRating(){
        try{
            System.out.print("Enter agent id: ");
            int id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter agent rating: ");
            double rating  = Double.parseDouble(scanner.nextLine().trim());
            agentService.addRating(id, rating);
        }
        catch(NumberFormatException e){
            System.err.println("Could not parse input value");
        }
        catch(AgentNotFoundException e){
            System.err.println(e.getMessage());
        }
    }

    public static void handleGetRatings(){
        List<Agent> agents = agentService.getAgentsByAverageRating();
        if(agents.isEmpty()){
            System.out.println("No agent rating available");
            return;
        }

        System.out.println("\nAgents sorted by average rating:");
        for(Agent agent : agents){
            System.out.println(agent);
        }
    }
}
