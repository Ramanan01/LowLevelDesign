package src.practiceproblems.latchtesting;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class CounterTest {

    @Test
    void CounterTest() throws InterruptedException {
        Counter counter = new Counter();
        ExecutorService executors = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);
        for(int i=0;i<100;i++) {
            executors.submit(() -> {
                counter.increment();
                latch.countDown();
            });
        }

        latch.await();
        assertEquals(100, counter.getValue());
    }
}
