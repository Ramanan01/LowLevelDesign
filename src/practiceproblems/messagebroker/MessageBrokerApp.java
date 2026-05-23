package src.practiceproblems.messagebroker;

import java.util.Set;
import java.util.concurrent.*;

class Message {
    private final String topic;
    private final String payload;
    private final long creationTime;
    private final long expiryDelay;

    public Message(String topic, String payload, long creationTime, long expiryDelay) {
        this.topic = topic;
        this.payload = payload;
        this.creationTime = creationTime;
        this.expiryDelay = expiryDelay;
    }

    public String getTopic() {
        return topic;
    }

    public String getPayload() {
        return payload;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getExpiryDelay() {
        return expiryDelay;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > creationTime + expiryDelay;
    }
}

interface Subscriber {
    public void onMessage(String message);
}

class PrintSubscriber implements Subscriber{

    @Override
    public void onMessage(String message) {
        System.out.println("Message is " + message);
    }
}

class Topic {
    private final String topicName;
    private Set<Subscriber> subscribers;
    private final BlockingDeque<Message> messages;

    public Topic(String topicName) {
        this.topicName = topicName;
        this.subscribers = new CopyOnWriteArraySet<>();
        this.messages = new LinkedBlockingDeque<>();
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public String getTopicName() {
        return topicName;
    }

    public Set<Subscriber> getSubscribers() {
        return subscribers;
    }

    public BlockingDeque<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) throws InterruptedException {
        messages.put(message);
    }
}

class MessageBroker {
    private final ConcurrentMap<String, Topic> topics;
    private final ExecutorService consumerPool;
    private final ExecutorService dispatcherPool;

    public MessageBroker(int consumerThreads) {
        this.topics = new ConcurrentHashMap<>();
        consumerPool = Executors.newFixedThreadPool(consumerThreads);
        this.dispatcherPool = Executors.newCachedThreadPool();
    }

    public void addTopic(Topic topic) {
        if(topics.containsKey(topic.getTopicName())) {
            return;
        }
        topics.put(topic.getTopicName(), topic);
        setupDispatcher(topic);
    }

    public void sendMessage(Message message, String topicName) throws InterruptedException {
        Topic topic = topics.get(topicName);

        if(topic == null) {
            return;
        }

        topic.addMessage(message);
    }

    public void addSubscriber ( String topicName, Subscriber subscriber) {
        Topic topic = topics.get(topicName);

        if(topic == null) {
            return;
        }

        topic.addSubscriber(subscriber);
    }

    private void setupDispatcher(Topic topic) {
        dispatcherPool.submit(() -> {
            while(true) {
                Message message = topic.getMessages().take();
                if(message.isExpired()) {
                    continue;
                }

                for(Subscriber subscriber: topic.getSubscribers()) {
                    consumerPool.submit(() -> subscriber.onMessage(message.getPayload()));
                }
            }
        });
    }

    public void shutDown() {
        consumerPool.shutdown();
        dispatcherPool.shutdown();

        try {
            if(!consumerPool.awaitTermination(5, TimeUnit.SECONDS)){
                consumerPool.shutdownNow();
            }
            if(!dispatcherPool.awaitTermination(5, TimeUnit.SECONDS)) {
                dispatcherPool.shutdownNow();
            }
        }
        catch(InterruptedException e) {
            consumerPool.shutdownNow();
            dispatcherPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}


public class MessageBrokerApp {
}
