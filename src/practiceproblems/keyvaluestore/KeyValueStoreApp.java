package src.practiceproblems.keyvaluestore;


import java.util.*;

class Entry<K, V> {
    K key;
    V value;

    Entry(K k, V v) {
        this.key = k;
        this. value = v;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }

        if(o==null) {
            return false;
        }

        if(!(o instanceof Entry)) {
            return false;
        }

        Entry<?, ?> e = (Entry<?, ?>) o;

        if(Objects.equals(e.getKey(), this.getKey()) && Objects.equals(e.getValue(), this.getValue())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}

class InvalidInputException extends Exception {
    public InvalidInputException() {
        super("Invalid key or value");
    }
}

class NoElementsInMapException extends Exception {
    public NoElementsInMapException() {
        super("No elements in map exception");
    }
}

class KeyValueStore<K, V> {
    Map<K, V> keyMap;
    Map<K, Integer> indexMap;
    List<K> keys;
    Random random;

    public KeyValueStore() {
        keyMap = new HashMap<>();
        indexMap = new HashMap<>();
        keys = new ArrayList<>();
        random = new Random();
    }

    public V get(K key) throws InvalidInputException {
        if(key == null){
            throw new InvalidInputException();
        }
        return keyMap.get(key);
    }

    public void put (K key, V value) throws InvalidInputException {
        if(key==null || value == null){
            throw new InvalidInputException();
        }
        if(!keyMap.containsKey(key)) {
            keys.add(key);
            indexMap.put(key, keys.size()-1);
        }
        keyMap.put(key, value);
    }

    public void remove(K key) throws InvalidInputException {
        if(key==null) {
            throw new InvalidInputException();
        }
        if(!keyMap.containsKey(key)) {
            return;
        }

        int i = indexMap.get(key);
        int last = keys.size() - 1;

        keys.set(i, keys.get(last));
        indexMap.put(keys.get(i), i);

        keys.removeLast();
        indexMap.remove(key);
        keyMap.remove(key);
    }

    Entry<K, V> getRandom() throws NoElementsInMapException {
        int n = keys.size();
        if(n==0){
            throw new NoElementsInMapException();
        }
        int i = random.nextInt(n);
        K key = keys.get(i);
        V value = keyMap.get(key);
        return new Entry<>(key, value);
    }
}



public class KeyValueStoreApp {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);

        KeyValueStore<String, Integer> keyValueStore = new KeyValueStore<>();
        String key; Integer value;

        while(true) {
            System.out.print("\nEnter command: ");
            String command = sc.next().toUpperCase();
            try{
                switch (command) {
                    case "GET":
                        key = sc.next();
                        value = keyValueStore.get(key);
                        System.out.println("Value is " + value);
                        break;
                    case "PUT":
                        key = sc.next();
                        value = sc.nextInt();
                        keyValueStore.put(key, value);
                        break;
                    case "REMOVE":
                        key = sc.next();
                        keyValueStore.remove(key);
                        break;
                    case "RANDOM":
                        Entry<String, Integer> entry = keyValueStore.getRandom();
                        System.out.println(String.format("Key is %s and Value is %d", entry.getKey(), entry.getValue()));
                        break;
                    case "EXIT":
                        System.out.println(
                                "Exiting..."
                        );
                        sc.close();
                        return;
                    case "DEFAULT":
                        System.out.println(
                                "Invalid command"
                        );
                }
            }
            catch(Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
