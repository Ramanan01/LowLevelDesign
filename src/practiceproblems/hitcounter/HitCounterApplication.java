package src.practiceproblems.hitcounter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class InvalidPageUrlException extends Exception {
    public InvalidPageUrlException(String message){
        super(message);
    }
}

class UnknownStrategyException extends Exception {
    public UnknownStrategyException(String message) {
        super(message);
    }
}

interface HitCounter {
    void recordHit(String pageUrl) throws InvalidPageUrlException;
    long getHits(String pageUrl) throws InvalidPageUrlException;
    Map<String, Long> getAllHits();
}


class LongAddedHitCounter implements  HitCounter {

    private final ConcurrentHashMap<String, LongAdder> hitCounts = new ConcurrentHashMap<>();

    @Override
    public void recordHit(String pageUrl) throws InvalidPageUrlException{
        if(pageUrl == null || pageUrl.isEmpty()){
            throw new InvalidPageUrlException("Url is invalid");
        }

        hitCounts.computeIfAbsent(pageUrl, k -> new LongAdder()).increment();
    }

    @Override
    public long getHits(String pageUrl) throws InvalidPageUrlException{
        if(pageUrl == null || pageUrl.isEmpty()){
            throw new InvalidPageUrlException("Url is invalid");
        }

        LongAdder adder = hitCounts.get(pageUrl);

        if(adder == null){
            return 0L;
        }

        return adder.sum();
    }

    @Override
    public Map<String, Long> getAllHits() {
        return hitCounts.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().sum()));
    }
}

class AtomicLongHitCounter implements HitCounter {

    private final ConcurrentHashMap<String, AtomicLong> hitCounts = new ConcurrentHashMap<>();

    @Override
    public void recordHit(String pageUrl) throws InvalidPageUrlException {
        if(pageUrl == null || pageUrl.isEmpty()){
            throw new InvalidPageUrlException("Url is invalid");
        }

        hitCounts.computeIfAbsent(pageUrl, k-> new AtomicLong(0)).incrementAndGet();
    }

    @Override
    public long getHits(String pageUrl) throws InvalidPageUrlException {
        if(pageUrl == null || pageUrl.isEmpty()){
            throw new InvalidPageUrlException("Url is invalid");
        }

        AtomicLong counter = hitCounts.get(pageUrl);
        if(counter == null){
            return 0L;
        }
        return counter.get();
    }

    @Override
    public Map<String, Long> getAllHits() {
        return hitCounts.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e-> e.getValue().get()));
    }
}

class SynchronizedCounter implements HitCounter {

    private final Map<String, Long> hitCounts = new HashMap<>();

    @Override
    public synchronized void recordHit(String pageUrl) throws InvalidPageUrlException {
        if(pageUrl == null || pageUrl.isEmpty()){
            throw new InvalidPageUrlException("Url is invalid");
        }

        long currHits = hitCounts.getOrDefault(pageUrl, 0L);
        hitCounts.put(pageUrl, currHits + 1);

    }

    @Override
    public synchronized long getHits(String pageUrl) throws InvalidPageUrlException {
        if(pageUrl == null || pageUrl.isEmpty()){
            throw new InvalidPageUrlException("Url is invalid");
        }

        return hitCounts.getOrDefault(pageUrl, 0L);
    }

    @Override
    public synchronized Map<String, Long> getAllHits() {
        return new HashMap<>(hitCounts);
    }
}

enum CounterStrategy {
    LONGADDER,
    ATOMICLONG,
    SYNCHRONIZED;

    public static CounterStrategy fromString(String strategy) throws UnknownStrategyException {
        try{
            return CounterStrategy.valueOf(strategy.trim().toUpperCase());
        }
        catch(Exception e){
            throw new UnknownStrategyException("Unknown strategy");
        }
    }
}

class CounterFactory{
    public static HitCounter getStrategy(String strategy) throws UnknownStrategyException {
        return switch(CounterStrategy.fromString(strategy)) {
            case LONGADDER -> new LongAddedHitCounter();
            case ATOMICLONG -> new AtomicLongHitCounter();
            case SYNCHRONIZED -> new SynchronizedCounter();
        };
    }
}


public class HitCounterApplication {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Website Hit Counter Simulator ===");
        System.out.println("Available Strategies: STRIPED, ATOMIC, SYNCHRONIZED");
        System.out.print("Enter strategy to use: ");

        String userInput = scanner.nextLine();

        try {
            // 1. Initialize the chosen strategy
            HitCounter counter = CounterFactory.getStrategy(userInput);

            // 2. Setup Simulation
            int threadCount = 10;
            int hitsPerThread = 1000;
            String testUrl = "www.google.com";
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            System.out.println("\nExecuting 10,000 hits across 10 threads...");

            // 3. Run Hits Simultaneously
            for (int i = 0; i < threadCount; i++) {
                executor.submit(() -> {
                    try {
                        for (int j = 0; j < hitsPerThread; j++) {
                            counter.recordHit(testUrl);
                        }
                    } catch (InvalidPageUrlException e) {
                        System.err.println(e.getMessage());
                    }
                });
            }

            // 4. Shutdown and cleanup
            executor.shutdown();
            if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("------------------------------------");
                System.out.println("Strategy Used: " + userInput.toUpperCase());
                System.out.println("Total Hits Recorded: " + counter.getHits(testUrl));
                System.out.println("------------------------------------");
            }

        } catch (UnknownStrategyException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close(); // Important to close resources
        }


    }
}
