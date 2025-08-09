package src.practiceproblems.agentrating;

import java.time.YearMonth;
import java.util.*;

public class Agent {
    private int id;
    private String name;
    private final Map<YearMonth, List<Double>> monthlyRatings;
    private Double totalRatingSum;
    private int totalRatingCount;

    public Agent(int id, String name){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name is invalid for new agent");
        }

        this.name = name;
        this.id = id;
        this.totalRatingSum = 0.0;
        this.totalRatingCount = 0;
        this.monthlyRatings = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRatingCount() {
        return totalRatingCount;
    }

    public void addRating(double rating, YearMonth month){
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        monthlyRatings.putIfAbsent(month, new ArrayList<>());
        monthlyRatings.get(month).add(rating);

        // Update total stats
        totalRatingSum += rating;
        totalRatingCount++;
    }

    public double getAverageRating(){
        return totalRatingCount == 0 ? 0.0 : totalRatingSum / (double) totalRatingCount;
    }

    public double getAverageRatingsByMonth(YearMonth month){
        if(monthlyRatings.get(month) == null || monthlyRatings.get(month).isEmpty()){
            return 0.0;
        }
        return monthlyRatings.get(month).stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    @Override
    public String toString() {
        return String.format("Agent{id=%d, name='%s', avgRating=%.2f, ratings=%d}",
                id, name, getAverageRating(), totalRatingCount);
    }

    public String toMonthlyString(YearMonth month) {
        double avg = getAverageRatingsByMonth(month);
        int count = monthlyRatings.getOrDefault(month, new ArrayList<>()).size();
        return String.format("Agent{id=%d, name='%s', avgRating=%.2f, ratings=%d}",
                id, name, avg, count);
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if (!(o instanceof Agent)) return false;
        Agent agent = (Agent) o;
        return id == agent.id && Objects.equals(name, agent.name);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id, name);
    }
}
