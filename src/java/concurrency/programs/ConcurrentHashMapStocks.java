package src.java.concurrency.programs;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapStocks {
    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        Map<String, Double> stockPrices = new ConcurrentHashMap<>();
        stockPrices.put("APPL", 500.0);
        stockPrices.put("AMZN", 250.0);

        for(int i=0;i<100;i++){
            executor.submit(() -> {
                stockPrices.compute("APPL", (k, v)  -> v + Math.random());
                stockPrices.compute("AMZN", (k, v)  -> v + Math.random());
            });
        }

        for(int i=0;i<100;i++){
            executor.submit(() -> {
                System.out.println("Current APPL price " + stockPrices.get("APPL"));
                System.out.println("Current AMZN price " + stockPrices.get("AMZN"));
            });
        }

        executor.shutdown();

        try{
            if(!executor.awaitTermination(1, TimeUnit.MINUTES)){
                executor.shutdownNow();
            }
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }

    }
}
