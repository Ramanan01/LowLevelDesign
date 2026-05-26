package src.practiceproblems.latchtesting;

import org.junit.jupiter.api.Test;

public class Counter {
    int value = 0;
    synchronized void increment() {
        value++;
    }

    public int getValue() {
        return value;
    }

    public static void main(String args[]) {
        Counter counter = new Counter();
    }

}
