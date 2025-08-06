package src.practiceproblems.agentrating;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final Scanner scanner = new Scanner(System.in);
    public static final AgentService agentService = new AgentService();



    public static void main(String[] args){
        clearFilesInDirectory();
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
                case 4: handleMonthlyGetRatings();
                        break;
                case 5: System.out.println("Exiting. Goodbye!");
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
        System.out.println("4. List Agents by Average Monthly Rating");
        System.out.println("5. Exit");
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
            System.out.print("Enter year: ");
            int year = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Enter month: ");
            int month = Integer.parseInt(scanner.nextLine().trim());
            agentService.addRating(id, rating, year, month);
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

    public static void handleMonthlyGetRatings(){
        System.out.print("Enter year: ");
        int year = Integer.parseInt(scanner.nextLine().trim());
        System.out.print("Enter month: ");
        int month = Integer.parseInt(scanner.nextLine().trim());

        List<Agent> agents = agentService.getAgentsByAverageMonthlyRating(year, month);

        if(agents.isEmpty()){
            System.out.println("No agent rating available");
            return;
        }

        System.out.printf("\nAgents sorted by average rating for %d - %d:\n", month, year);
        for(Agent agent : agents){
            System.out.println(agent.toMonthlyString(YearMonth.of(year, month)));
        }

        writeMonthlyReportsToCSV(agents, year, month);
    }

    private static void writeMonthlyReportsToCSV(List<Agent> agents, int year, int month){
        String fileName = String.format("files/monthly_report_%d_%d.csv", month, year);

        File file = new File(fileName);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write("Agent ID,Name,Average Rating\n");
            for(Agent agent : agents){
                writer.write(String.format("%d,%s,%f\n", agent.getId(), agent.getName(), agent.getAverageRatingsByMonth(YearMonth.of(year, month))));
            }
            System.out.println("Monthly report written to " + fileName);
        }
        catch (IOException e) {
            System.err.println("Failed to write CSV: " + e.getMessage());
        }
    }

    private static void clearFilesInDirectory(){
        File folder = new File("files");

        if(!folder.exists()){
            folder.mkdirs();
        }

        for (File file : folder.listFiles()){
            file.delete();
        }
    }
}
