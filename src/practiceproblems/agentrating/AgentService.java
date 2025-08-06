package src.practiceproblems.agentrating;

import java.time.YearMonth;
import java.util.*;

public class AgentService {
    Map<Integer, Agent> agentMap;

    AgentService(){
        agentMap = new HashMap<>();
    }

    public void addAgent(int id, String name){
        if(agentMap.get(id) != null){
            System.out.println("Agent already exists in the map");
        }

        try{
            agentMap.put(id, new Agent(id, name));
        }
        catch(IllegalArgumentException e){
            System.err.println("Failed to add agent: " + e.getMessage());
        }
    }

    public void addRating(int id, double rating, int year, int month){
        if(agentMap.get(id) == null){
            throw new AgentNotFoundException("Agent not found for id " + id);
        }

        try{
            agentMap.get(id).addRating(rating, YearMonth.of(year, month));
        }
        catch(IllegalArgumentException e){
            System.err.println("Invalid rating value");
        }
    }

    public List<Agent> getAgentsByAverageRating(){
        List<Agent> agentList = new ArrayList<>(agentMap.values());
        agentList.sort((a, b) -> Double.compare(b.getAverageRating(), a.getAverageRating()));
        return agentList;
    }

    public List<Agent> getAgentsByAverageMonthlyRating(int year, int month){
        List<Agent> agentList = new ArrayList<>(agentMap.values());
        agentList.sort((a, b) -> Double.compare(b.getAverageRatingsByMonth(YearMonth.of(year, month)), a.getAverageRatingsByMonth(YearMonth.of(year, month))));
        return agentList;
    }

    public List<Agent> getAgentsByCustomComparison(){
        List<Agent> agentList = new ArrayList<>(agentMap.values());
        agentList.sort(Comparator.comparingDouble(Agent::getAverageRating).reversed().thenComparing(Agent::getRatingCount).thenComparing(Agent::getId));
        return agentList;
    }
}
