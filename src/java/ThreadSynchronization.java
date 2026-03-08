package src.java;

class Counter{
    int value;
     Counter(){
         value = 0;
     }

     public synchronized void increment(){
         value++;
     }

     public synchronized int getValue(){
         return value;
     }
}

class Task implements Runnable {
    Counter counter;
    Task(Counter counter){
        this.counter = counter;
    }

    @Override
    public void run(){
        for(int i=0;i<10000;i++){
            counter.increment();
        }
    }
}
public class ThreadSynchronization {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();
        Thread t1 = new Thread(new Task(counter));
        Thread t2 = new Thread(new Task(counter));
        Thread t3 = new Thread(new Task(counter));

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Final count: " + counter.getValue());
    }
}
