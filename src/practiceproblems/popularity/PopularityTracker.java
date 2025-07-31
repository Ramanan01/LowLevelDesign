package src.practiceproblems.popularity;


import java.util.*;

interface MostPopular {
    void increasePopularity(Integer contentId);
    Integer mostPopular();
    void decreasePopularity(Integer contentId);
}

class NoPopularContentException extends RuntimeException{
    NoPopularContentException(String message){
        super(message);
    }
}

class NegativePopularityException extends RuntimeException{
    NegativePopularityException(String message){
        super(message);
    }
}

public class PopularityTracker implements MostPopular {
    private final Map<Integer, Integer> contentPopularity;
    private final TreeMap<Integer, Set<Integer>> popularityToContent;
    Integer mostRecentMostPopular;

    PopularityTracker(){
        contentPopularity = new HashMap<>();
        popularityToContent = new TreeMap<>();
        mostRecentMostPopular = -1;
    }

    @Override
    public void increasePopularity(Integer contentId){
        int popularity = contentPopularity.getOrDefault(contentId, 0);
        int newPopularity = popularity + 1;

        popularityToContent.computeIfAbsent(newPopularity, k -> new HashSet<>()).add(contentId);
        contentPopularity.put(contentId, newPopularity);

        if(popularity > 0){
            popularityToContent.get(popularity).remove(contentId);
            if(popularityToContent.get(popularity).isEmpty()){
                popularityToContent.remove(popularity);
            }
        }

        if(popularityToContent.lastKey() == newPopularity){
            mostRecentMostPopular = contentId;
        }
    }

    @Override
    public Integer mostPopular() {
        if(contentPopularity.isEmpty()){
            throw new NoPopularContentException("No popular content found");
        }

        return popularityToContent.lastEntry().getValue().iterator().next();
    }

    @Override
    public void decreasePopularity(Integer contentId){
        int popularity = contentPopularity.getOrDefault(contentId, 0);
        if(popularity == 0){
            throw new NegativePopularityException("Popularity is going negative");
        }

        int newPopularity = popularity - 1;

        popularityToContent.get(popularity).remove(contentId);
        if(popularityToContent.get(popularity).isEmpty()){
            popularityToContent.remove(popularity);
        }

        if(newPopularity != 0){
            popularityToContent.computeIfAbsent(newPopularity, k -> new HashSet<>()).add(contentId);
            contentPopularity.put(contentId, newPopularity);
        }
        else {
            contentPopularity.remove(contentId); // clean up
        }

        if(!popularityToContent.isEmpty()){
            if(popularityToContent.lastEntry().getValue().contains(contentId)){
                mostRecentMostPopular = contentId;
            }
        }
        else{
            mostRecentMostPopular = -1;
        }

    }

    public Integer getMostRecentMostPopular(){
        if(mostRecentMostPopular == -1){
            throw new NoPopularContentException("No popular content yet");
        }

        return mostRecentMostPopular;
    }

    public static void main(String[] args) {
        MostPopular popularityTracker = new PopularityTracker();

        popularityTracker.increasePopularity(7);
        popularityTracker.increasePopularity(7);
        popularityTracker.increasePopularity(8);
        System.out.println(popularityTracker.mostPopular()); // 7
        popularityTracker.increasePopularity(8);
        popularityTracker.increasePopularity(8);
        System.out.println(popularityTracker.mostPopular()); // 8
        popularityTracker.decreasePopularity(8);
        popularityTracker.decreasePopularity(8);
        System.out.println(popularityTracker.mostPopular()); // 7
        popularityTracker.decreasePopularity(7);
        popularityTracker.decreasePopularity(7);
        popularityTracker.decreasePopularity(8);
        try {
            System.out.println(popularityTracker.mostPopular());
        } catch (NoPopularContentException e) {
            System.out.println(-1);  // match expected output
        }
    }
}
