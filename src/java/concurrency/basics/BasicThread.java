package src.java.concurrency.basics;

class LoggerThread extends Thread {
    public void run() {
        System.out.println(String.format("Inside thread %s", Thread.currentThread().getName()));
    }
}

public class BasicThread {
    public static void main(String[] args){
        LoggerThread loggerThread = new LoggerThread();
        loggerThread.start();
    }
}
