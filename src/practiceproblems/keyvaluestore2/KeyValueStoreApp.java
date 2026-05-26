package src.practiceproblems.keyvaluestore2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.random.*;
import java.util.*;

class Entry<K, V> {
    K key;
    V value;

    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public String toString() {
        return key + " " + value;
    }
}

class KeyValueStore<K, V> {
    Map<K, Entry<K, V>> keyMap;
    Map<K, Integer> indexMap;
    List<Entry<K, V>> entries;
    Random random;

    KeyValueStore() {
        keyMap = new HashMap<>();
        indexMap = new HashMap<>();
        entries = new ArrayList<>();
        random = new Random();
    }

    public void put(K key, V value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("Illegal key or value");
        }
        Entry<K, V> entry;
        if(keyMap.containsKey(key)) {
            entry = keyMap.get(key);
            entry.setValue(value);
        }
        else{
            entry = new Entry<>(key, value);
            keyMap.put(key, entry);
            entries.add(entry);
            indexMap.put(key, entries.size()-1);
        }
    }

    public V get(K key) {
        if(key==null || !keyMap.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        return keyMap.get(key).getValue();
    }

    public void delete(K key) {
        if(key==null || !keyMap.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        int ind = indexMap.get(key);
        keyMap.remove(key);
        indexMap.remove(key);

        int last = entries.size() -1;
        entries.set(ind, entries.get(last));
        indexMap.put((K) entries.get(last).getKey(), ind);
        entries.removeLast();
    }

    public Entry<K, V> getRandom() {
        int size = entries.size();
        int ind = random.nextInt(size);

        return entries.get(ind);
    }

}

public class KeyValueStoreApp {
    public static void main(String[] args) {
        KeyValueStore<String, Integer> keyValueStore = new KeyValueStore<>();

        keyValueStore.put("Ramanan", 1);
        keyValueStore.put("Pavan", 3);
        keyValueStore.put("Prahalad", 1);

        System.out.println("The value of key Pavan is " + keyValueStore.get("Pavan"));

        keyValueStore.put("Pavan", 4);

        System.out.println("The value of key Pavan is " + keyValueStore.get("Pavan"));

        keyValueStore.delete("Pavan");

        //System.out.println("The value of key Pavan is " + keyValueStore.get("Pavan"));

        System.out.println("A random entry is " + keyValueStore.getRandom().toString());
    }
}
