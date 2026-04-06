package src.java.concurrency.programs;

import java.util.ArrayList;
import java.util.List;

class BankAccount {
    private volatile int balance = 0;

    public synchronized void deposit(int amount){
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }
}

public class SynchronizedDepositMain {
    public static void main(String args[]) {
        BankAccount bankAccount = new BankAccount();

        int noOfThreads = 10;
        int depositAmount = 10;
        int depositTimes = 100;

        List<Thread> threads = new ArrayList<>();

        Runnable depositTask = () -> {
            for(int i=0;i<depositTimes;i++){
                bankAccount.deposit(depositAmount);
                }
        };

        for(int i=0;i<noOfThreads;i++){
            Thread t = new Thread(depositTask, "Thread " + i);
            threads.add(t);
            t.start();
        }

        int expectedBalance = noOfThreads * depositAmount * depositTimes;
        for(Thread t: threads){
            try{
                t.join();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted");
            }
        }

        System.out.println("Expected balance: " + expectedBalance);
        System.out.println("Current balance: " + bankAccount.getBalance());

    }
}
