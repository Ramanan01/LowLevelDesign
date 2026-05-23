package src.practiceproblems.jobscheduler;


import java.util.concurrent.*;

class Job {
    private final String id;
    private final Runnable task;
    private final long startTimeMillis;
    private final long delayMillis;

    public Job(String id, Runnable task, long startTimeMillis, long delayMilis) {
        this.id = id;
        this.task = task;
        this.startTimeMillis = startTimeMillis;
        this.delayMillis = delayMilis;
    }

    public String getId() {
        return id;
    }

    public Runnable getTask() {
        return task;
    }

    public long getDelayMilis() {
        return delayMillis;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }
}

class JobScheduler {
    ExecutorService executor;
    ScheduledExecutorService scheduler;

    public JobScheduler(int executorThreads, int schedulerThreads) {
        this.executor = Executors.newFixedThreadPool(executorThreads);
        this.scheduler = Executors.newScheduledThreadPool(schedulerThreads);
    }

    public void submit(Job job) {

        long initialDelay = Math.max(job.getStartTimeMillis() - System.currentTimeMillis(), 0);


        if(job.getDelayMilis() != 0) {
            scheduler.scheduleWithFixedDelay(() -> {
                Future<?> future = executor.submit(() -> job.getTask().run());
                try {
                    future.get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }, initialDelay, job.getDelayMilis(), TimeUnit.MILLISECONDS);
        }
        else{
            scheduler.schedule(() -> {
                executor.submit(() -> {
                    job.getTask().run();
                });
            }, initialDelay, TimeUnit.MILLISECONDS);
        }
    }

    public void shutDown() {
        executor.shutdown();
        scheduler.shutdown();

        try {
            if(!scheduler.awaitTermination(5, TimeUnit.SECONDS)){
                scheduler.shutdownNow();
            }
            if(!executor.awaitTermination(5, TimeUnit.SECONDS)){
                executor.shutdownNow();
            }
        }
        catch(InterruptedException e) {
            scheduler.shutdownNow();
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }


}
public class JobSchedulerApp {
}
