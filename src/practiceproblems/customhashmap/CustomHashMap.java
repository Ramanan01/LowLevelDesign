package src.practiceproblems.customhashmap;


class Entry<K, V> {
    K key;
    V value;
    Entry<K, V> next;
    public Entry(K key, V value){
        this.key = key;
        this.value = value;
    }
}

public class CustomHashMap<K, V> {
    private Entry<K, V>[] buckets;
    private int capacity;
    private int size;
    private final double loadFactor = 0.75;

    @SuppressWarnings("unchecked")
    public CustomHashMap(){
        capacity = 16;
        buckets = new Entry[capacity];
        size = 0;
    }

    private int getBucketIndex(K key){
        return Math.abs(key.hashCode() % capacity);
    }

    public void put(K key, V value){
        int index = getBucketIndex(key);
        Entry<K, V> head = buckets[index];

        Entry<K, V> current = head;
        while(current != null){
            if(current.key.equals(key)){
                current. value = value;
                return;
            }
            current = current.next;
        }

        Entry<K, V> newNode = new Entry<>(key, value);
        newNode.next = head;
        buckets[index] = newNode;
        size++;

        if((1.0 * size) / capacity >= loadFactor){
            resize();
        }
    }

    public V get(K key){
        int index = getBucketIndex(key);
        Entry<K, V> node = buckets[index];
        while(node != null){
            if(node.key.equals(key)){
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public V remove(K key){
        int index = getBucketIndex(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> prev = null;

        while(current!=null){
            if(current.key.equals(key)){
                if(prev!=null){
                    prev.next = current.next;
                }
                else{
                    buckets[index] = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    // For Debugging
    public void display() {
        System.out.println("Current Size: " + size + ", Capacity: " + capacity);
        for (int i = 0; i < capacity; i++) {
            System.out.print("Bucket " + i + ": ");
            Entry<K, V> current = buckets[i];
            while (current != null) {
                System.out.print("[" + current.key + "=" + current.value + "] -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        System.out.println("Resizing... Old Capacity: " + capacity + ", New Capacity: " + (capacity * 2));
        Entry<K, V>[] oldBuckets = buckets;

        capacity = capacity*2;
        buckets = new Entry[capacity];
        size = 0;

        for(Entry<K, V> node : oldBuckets){
            while(node != null){
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public static void main(String[] args){
        CustomHashMap<String, Integer> map = new CustomHashMap<>();

        for (int i = 1; i <= 20; i++) {
            map.put("Key" + i, i * 10);
        }

        map.display();

        System.out.println("\nGet Key15: " + map.get("Key15"));  // Should print 150
        System.out.println("Removing Key10: " + map.remove("Key10")); // Should print 100
        System.out.println("Get Key10 After Removal: " + map.get("Key10")); // Should print null
    }

}
