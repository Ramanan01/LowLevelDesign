package src.practiceproblems.agentrating;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addRating(int id, double rating){
        if(agentMap.get(id) == null){
            throw new AgentNotFoundException("Agent not found for id " + id);
        }

        try{
            agentMap.get(id).addRating(rating);
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
}
