package src.practiceproblems.syntax;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

public class SyntaxCheck {
    public static void main(String args[]) {
        ExecutorService executors = Executors.newFixedThreadPool(5);
        executors.submit(() -> {
            System.out.println("Inside thread " + Thread.currentThread().getName());
        });

        LongAdder adder = new LongAdder();
        adder.increment();
        adder.increment();
        adder.sum();
    }
}
