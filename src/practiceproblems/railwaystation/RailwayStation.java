package src.practiceproblems.railwaystation;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/*
2. The Integer Deadlock Risk
In Java, Integer objects between -128 and 127 are "interned" (cached).

If you do Integer platformCount = 1;, Java doesn't create a new object; it points to a pre-existing object in a pool.

The Danger: If another developer in a completely different part of the codebase (or a library you are using) also calls synchronized(someIntegerThatEquals1), you are both locking on the exact same object in memory.

The Result: Your Platform constructor could be waiting for a lock held by a logging library or a UI component, leading to a Deadlock where the whole system freezes.

The Fix: Always synchronize on a private final Object lock = new Object();. This object is unique to your class instance and cannot be "guessed" or shared by accident.
 */

class TimeInterval {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;

    public TimeInterval(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}

class Train {
    private final TimeInterval trainTime;
    private final String id;
    private final String name;

    public Train(String name, TimeInterval trainTime) {
        this.name = name;
        this.trainTime = trainTime;
        this.id = UUID.randomUUID().toString();
    }

    public TimeInterval getTrainTime() {
        return trainTime;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TimeInterval getInterval() {
        return trainTime;
    }
}

class Platform {
    private static final Object lock = new Object();
    private static final AtomicInteger platformCount = new AtomicInteger(0);
    int platformNo;
    private final TreeSet<Train> trains;

    public Platform() {

        this.platformNo = platformCount.incrementAndGet();

        trains = new TreeSet<>(Comparator.comparing((Train t) -> t.getInterval().getStartTime()).thenComparing(Train::getId));
    }

    public boolean allotIfAvailable(Train train) {
        synchronized (trains) {
            Train floor = trains.floor(train);
            if(floor != null && floor.getInterval().getEndTime().isAfter(train.getInterval().getStartTime())){
                return false;
            }

            Train ceil = trains.ceiling(train);
            if(ceil != null && ceil.getInterval().getStartTime().isBefore(train.getInterval().getEndTime())){
                return false;
            }

            trains.add(train);

            return true;
        }
    }

    public Train getTrainAtTime(LocalDateTime time) {
        TimeInterval dummyInterval = new TimeInterval(time, time);
        Train dummy = new Train("dummy", dummyInterval);
        Train potential = trains.floor(dummy);
        if(potential != null && potential.getInterval().getEndTime().isAfter(time)){
            return potential;
        }
        return null;
    }
}

public class RailwayStation {
    private final List<Platform> platforms = new ArrayList<>();
    private final ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final String stationName;

    public RailwayStation(String stationName, int initialPlatforms) {
        this.stationName = stationName;
        for (int i = 0; i < initialPlatforms; i++) {
            addPlatform();
        }
    }

    public void addPlatform() {
        rwLock.writeLock().lock();
        try{
            platforms.add(new Platform());
        }
        finally{
            rwLock.writeLock().unlock();
        }
    }

    public int scheduleTrain(String trainName, LocalDateTime start, LocalDateTime end) {
        rwLock.writeLock().lock();
        try {
            TimeInterval interval = new TimeInterval(start, end);
            Train newTrain = new Train(trainName, interval);

            return scheduleTrain(newTrain);
        }
        finally {
            rwLock.writeLock().unlock();
        }
    }

    private int scheduleTrain(Train train) {
        for (Platform platform : platforms) {
            if (platform.allotIfAvailable(train)) {
                return platform.platformNo;
            }
        }
        return -1;
    }

    public int getPlatformCount() {
        rwLock.readLock().lock();

        try{
            return platforms.size();
        }
        finally{
            rwLock.readLock().unlock();
        }
    }
}
