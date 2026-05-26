package src.practiceproblems.ratelimiter;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Request {
    private final long requestTime;
    private final String user;

    public Request(long requestTime, String user) {
        this.requestTime = requestTime;
        this.user = user;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public String getUser() {
        return user;
    }
}

interface RateLimitingPolicy {
    boolean allow(Request request, Queue<Request> requests);
}

class SlidingWindowRateLimiter implements RateLimitingPolicy {

    private final long windowTimeMillis;
    private final long threshold;
    private final Lock lock;

    public SlidingWindowRateLimiter(long windowTimeMillis, long threshold) {
        this.windowTimeMillis = windowTimeMillis;
        this.threshold = threshold;
        lock = new ReentrantLock();
    }

    @Override
    public boolean allow(Request request, Queue<Request> requests) {
        lock.lock();
        try {
            while (!requests.isEmpty() && requests.peek().getRequestTime() <= request.getRequestTime() - windowTimeMillis) {
                requests.poll();
            }

            if (requests.size() >= threshold) {
                return false;
            } else {
                requests.offer(request);
                return true;
            }
        }
        finally {
            lock.unlock();
        }
    }

    public long getWindowTimeMillis() {
        return windowTimeMillis;
    }

    public long getThreshold() {
        return threshold;
    }
}

enum CircuitBreakerState {
    OPEN, CLOSED, HALF_OPEN
}

class CircuitBreakerConfig {
    private final long windowTimeMillis;
    private final long cooldownMillis;
    private final long threshold;
    private final long halfOpenThreshold;

    public CircuitBreakerConfig(long windowTimeMillis, long cooldownMillis, long threshold, long halfOpenLimit) {
        this.windowTimeMillis = windowTimeMillis;
        this.cooldownMillis = cooldownMillis;
        this.threshold = threshold;
        this.halfOpenThreshold = halfOpenLimit;
    }

    public long getWindowTimeMillis() {
        return windowTimeMillis;
    }

    public long getCooldownMillis() {
        return cooldownMillis;
    }

    public long getThreshold() {
        return threshold;
    }

    public long getHalfOpenThreshold() {
        return halfOpenThreshold;
    }
}

class CircuitBreakerRateLimiter implements RateLimitingPolicy {
    CircuitBreakerConfig config;
    CircuitBreakerState state;
    private final Lock lock;
    private long openTime;
    private long halfOpenTime;

    public CircuitBreakerRateLimiter(CircuitBreakerConfig config) {
        this.state = CircuitBreakerState.CLOSED;
        this.config = config;
        this.lock = new ReentrantLock();
    }


    @Override
    public boolean allow(Request request, Queue<Request> requests) {
        lock.lock();

        try{
            cleanup(request.getRequestTime(), requests);

            switch(state) {
                case CLOSED :
                    if(requests.size() >= config.getThreshold()) {
                        state = CircuitBreakerState.OPEN;
                        openTime = request.getRequestTime();
                        return false;
                    }
                    else{
                        requests.offer(request);
                        return true;
                    }
                case OPEN:
                    if(request.getRequestTime() > openTime + config.getCooldownMillis()) {
                        requests.offer(request);
                        state = CircuitBreakerState.HALF_OPEN;
                        halfOpenTime = request.getRequestTime();
                        return true;
                    }
                    else{
                        return false;
                    }
                case HALF_OPEN:
                    if(requests.size() >= config.getThreshold()) {
                        state = CircuitBreakerState.OPEN;
                        openTime = request.getRequestTime();
                        return false;
                    }
                    else{
                        if(request.getRequestTime() > halfOpenTime + config.getHalfOpenThreshold()) {
                            state = CircuitBreakerState.OPEN;
                        }
                        requests.offer(request);
                        return true;
                    }
            }
        }
        finally {
            lock.unlock();
        }
        return false;
    }

    private void cleanup(long currRequest, Queue<Request> requests) {
        while (!requests.isEmpty() && requests.peek().getRequestTime() <= currRequest - config.getWindowTimeMillis()) {
            requests.poll();
        }
    }
}

class RateLimiter {
    RateLimitingPolicy rateLimitingPolicy;
    Map<String, Queue<Request>> userRequests;

    public RateLimiter(RateLimitingPolicy rateLimitingPolicy) {
        this.rateLimitingPolicy = rateLimitingPolicy;
        userRequests = new HashMap<>();
    }

    boolean allow(Request request) {
        if(request == null || request.getUser()==null || request.getUser().isEmpty()) {
            return false;
        }

        userRequests.computeIfAbsent(request.getUser(), x-> new LinkedList<>());

        return rateLimitingPolicy.allow(request, userRequests.get(request.getUser()));
    }
}

public class RateLimiterApp {
}
