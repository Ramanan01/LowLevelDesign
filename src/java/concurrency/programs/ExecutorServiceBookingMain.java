package src.java.concurrency.programs;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

class Booking implements Runnable {

    int taskId;

    public Booking(int taskId){
        this.taskId = taskId;
    }

    @Override
    public void run(){
        System.out.println(String.format("Executing task %d in thread %s", taskId, Thread.currentThread().getName()));
        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        System.out.println(String.format("Finished task %d in thread %s", taskId, Thread.currentThread().getName()));
    }
}

public class ExecutorServiceBookingMain {
    public static void main(String args[]) {
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for(int i=0;i<100;i++){
            executor.submit(new Booking(i));
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
