package src.practiceproblems.hitcounterapp;

import java.util.*;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

class HitCounter {
    Map<String, LongAdder> hitCounter;

    public HitCounter() {
        hitCounter = new ConcurrentHashMap<>();
    }

    public void record(String url) {
        if(url==null || url.isEmpty()) {
            throw new IllegalArgumentException("Invalid URL");
        }

        LongAdder adder = hitCounter.computeIfAbsent(url, x -> new LongAdder());

        adder.increment();
    }

    public long get(String url) {
        if(url==null || url.isEmpty()) {
            throw new IllegalArgumentException("Invalid URL");
        }

        LongAdder adder = hitCounter.get(url);

        if(adder == null) {
            return 0L;
        }

        return adder.sum();
    }
}

public class HitCounterApplication {

    public static void main(String args[]) {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter initial page hits");
        List<String> urls = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        HitCounter hitCounter = new HitCounter();

        while(true) {
            String input = sc.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String url = parts[0];

            if(Objects.equals(url, "exit")) {
                break;
            }

            urls.add(url);
            counts.add(Integer.parseInt(parts[1]));
        }

        for(int i=0;i<urls.size();i++) {
            for(int j=0;j<counts.get(i);j++) {
                int finalI = i;
                executorService.submit(() -> {
                    hitCounter.record(urls.get(finalI));
                });
            }
        }

        executorService.shutdownNow();

        try{
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        System.out.println("Enter command");
        while(true) {
            String input = sc.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

            switch(command) {
                case "record":
                    hitCounter.record(parts[1]);
                    break;
                case "get":
                    System.out.println("Hit count is " + hitCounter.get(parts[1]));
                    break;
                case "exit":
                    return;
                default:
                    sc.close();
                    System.out.println("Enter valid command");
            }
        }
    }
}
