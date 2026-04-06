package src.java.concurrency.programs;

import java.util.ArrayList;
import java.util.List;

class BookingTickets{
    private int availableTickets = 500;
    private final Object lock = new Object();

    public void bookTicket(){
        synchronized (lock) {
            System.out.println("Inside thread " + Thread.currentThread().getName());
            if(availableTickets > 0){
                availableTickets--;
            }
        }
    }

    public int getAvailableTickets(){
        synchronized (lock){
            return availableTickets;
        }
    }
}

public class SynchronizedBookingMain {
    public static void main(String args[]){
        BookingTickets bookingTickets = new BookingTickets();

        List<Thread> threads = new ArrayList<>();

        int threadCount = 5;
        int ticketsPerThread = 100;

        for(int i=0;i<threadCount;i++){
            Thread t = new Thread(() -> {
                for(int j=0;j<ticketsPerThread;j++){
                    bookingTickets.bookTicket();
                }
            }, "Thread No " + i);

            threads.add(t);
            t.start();
        }

        for(Thread t: threads){
            try{
                t.join();
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
                System.out.println("Thread was interrupted");
            }
        }

        System.out.println("Final Seats Remaining: " + bookingTickets.getAvailableTickets());
        System.out.println("Expected Seats Remaining: 0");

        if (bookingTickets.getAvailableTickets() == 0) {
            System.out.println("Result: Thread-safe! No seats over-booked.");
        } else {
            System.out.println("Result: Race condition! Seats were lost.");
        }
    }
}
