package src.practiceproblems.messagequeue2;

import java.util.*;
import java.util.concurrent.*;

class Message {
    private final String payload;
    private final long publishTime;
    private final String topic;


    public Message(String payload, String topic) {
        this.payload = payload;
        this.publishTime = System.currentTimeMillis();
        this.topic = topic;
    }

    public String getPayload() {
        return payload;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public String getTopic() {
        return topic;
    }
}

interface TopicObserver {
    void onMessage(Message message);
}

class PrintObserver implements  TopicObserver {

    @Override
    public void onMessage(Message message) {
        System.out.println("Received new message " + message.getPayload());
    }
}

class Topic {
    BlockingDeque<Message> messages;
    String topicName;
    Set<TopicObserver> subscribers;

    public Topic(String topicName) {
        this.topicName = topicName;
        this.messages = new LinkedBlockingDeque<>();
        subscribers = new CopyOnWriteArraySet<>();
    }

    public void addMessage(Message message) throws InterruptedException {
        if(message==null) {
            return;
        }

        messages.put(message);
    }

    public BlockingDeque<Message> getMessages() {
        return messages;
    }

    public void addSubscriber(TopicObserver observer) {
        subscribers.add(observer);
    }

    public Set<TopicObserver> getSubscribers() {
        return subscribers;
    }
}

class MessageBroker {
    Map<String, Topic> topicMap;
    Map<String, Message> messageMap;
    ExecutorService dispatcherPool;
    ExecutorService consumerPool;

    public MessageBroker(int consumers) {
        consumerPool = Executors.newFixedThreadPool(consumers);
        dispatcherPool = Executors.newCachedThreadPool();
        topicMap = new ConcurrentHashMap<>();
        messageMap = new ConcurrentHashMap<>();
    }

    public void addTopic(String topicName) {
        if(topicMap.containsKey(topicName)) {
            throw new IllegalArgumentException("Topic already exists");
        }

        Topic topic = new Topic(topicName);
        topicMap.put(topicName, topic);

        setUpDispatcher(topic);
    }

    public void subscribeToTopic(TopicObserver observer, String topicName) {
        if(!topicMap.containsKey(topicName)) {
            throw new IllegalArgumentException("Topic does not exist");
        }

        Topic topic = topicMap.get(topicName);
        topic.addSubscriber(observer);
    }

    public void publish(Message message, String topicName) throws InterruptedException {
        if(message == null) {
            return;
        }

        if(topicName == null || topicName.isEmpty()) {
            throw new IllegalArgumentException("Invalid topic name");
        }

        if(!topicMap.containsKey(topicName)) {
            throw new IllegalArgumentException("Topic does not exist");
        }

        Topic topic = topicMap.get(topicName);
        topic.addMessage(message);
    }

    private void setUpDispatcher(Topic topic) {
        dispatcherPool.submit(() -> {
           while(true) {
               Message message = topic.getMessages().take();
               for(TopicObserver observer: topic.getSubscribers()) {
                   consumerPool.submit(() -> {
                      try{
                          observer.onMessage(message);
                      }
                      catch(Exception e) {
                          System.out.println("Exiting observer");
                      }
                   });
               }
           }
        });
    }

    public void shutdown() {
        consumerPool.shutdown();
        dispatcherPool.shutdown();

        try{
            if(!consumerPool.awaitTermination(5, TimeUnit.SECONDS)){
                consumerPool.shutdownNow();
            }

            if(!dispatcherPool.awaitTermination(5, TimeUnit.SECONDS)) {
                dispatcherPool.shutdown();
            }
        }
        catch(InterruptedException e) {
            consumerPool.shutdownNow();
            dispatcherPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}


public class MessageQueueApp {
}
