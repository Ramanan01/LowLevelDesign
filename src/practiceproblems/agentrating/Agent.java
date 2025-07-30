package src.practiceproblems.agentrating;
public class Agent {
    private int id;
    private String name;
    private double totalRating;
    private int ratingCount;

    Agent(int id, String name){
        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name is invalid for new agent");
        }

        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTotalRating() {
        return totalRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void addRating(double rating){
        if (rating < 1.0 || rating > 5.0) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        totalRating += rating;
        ratingCount += 1;
    }

    public double getAverageRating(){
        return ratingCount == 0 ? 0.0 : totalRating / (double) ratingCount;

    }

    @Override
    public String toString() {
        return String.format("Agent{id=%d, name='%s', avgRating=%.2f, ratings=%d}",
                id, name, getAverageRating(), ratingCount);
    }
}
