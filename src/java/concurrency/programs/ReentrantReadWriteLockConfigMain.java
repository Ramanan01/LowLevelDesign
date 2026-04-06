package src.java.concurrency.programs;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ConfigHandler {
    private String config = "Intial config";
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public void updateConfig(String newConfig) {
        lock.writeLock().lock();

        try{
            this.config = newConfig;
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    public String getConfig(){
        lock.readLock().lock();

        try{
            return config;
        }
        finally{
            lock.readLock().unlock();
        }
    }
}

public class ReentrantReadWriteLockConfigMain {
    public static void main(String args[]) {
        ConfigHandler handler = new ConfigHandler();

        List<Thread> threads = new ArrayList<>();

        Runnable readTask = () -> {
            for(int i=0;i<10;i++){
                try{
                    Thread.sleep(250);
                }
                catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                System.out.println(String.format("Reading config from thread %s Current value: %s", Thread.currentThread().getName(), handler.getConfig()));
            }
        };

        Runnable writeTask = () -> {
            for(int i=0;i<3;i++){
                try{
                    Thread.sleep(250);
                }
                catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                }
                System.out.println(String.format("Write config from thread %s", Thread.currentThread().getName()));
                handler.updateConfig(String.format("%s - %d", Thread.currentThread().getName(), i+1));
            }
        };

        for(int i=0;i<5;i++){
            Thread t = new Thread(readTask, "Read thread " + i);
            threads.add(t);
            t.start();
        }

        for(int i=0;i<2;i++){
            Thread t = new Thread(writeTask, "Write thread " + i);
            threads.add(t);
            t.start();
        }


        for(Thread t: threads){
            try{
                t.join();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }

    }
}
