package src.java;

import java.util.*;

class DummyObject{
    public int a, b;
    DummyObject(int a, int b){
        this.a = a;
        this.b = b;
    }
}

public class DataStructures{
    // This is a sample test code to try out operations in data structures for java compared to STL data structures in C++

    public static void main(String[] args){

        //ArrayList in java is similar to C++ vector without dynamic adding and deleting

        ArrayList<Integer> arrayList = new ArrayList<>();

        //Use add instead of push_back
        arrayList.add(5);
        arrayList.add(10);

        //Use get instead of square brackets accessing
        System.out.println(arrayList.get(1));
        //Use set instead of square brackets assignment
        arrayList.set(0, 6);
        System.out.println(arrayList.getFirst());
        System.out.println(arrayList.size());

        //Iterate
        for(int i=0;i<arrayList.size();i++){
            System.out.println(arrayList.get(i));
        }

        for(Integer a: arrayList){
            System.out.println(a);
        }

        Iterator<Integer> itr = arrayList.iterator();
        while(itr.hasNext()){
            System.out.println(itr.next());
        }

        //Unordered map in C++ is equal to HashMap in java

        Map<String, Integer> map = new HashMap<>();
        map.put("apple", 1);
        map.put("banana", 2);
        map.put("orange", 3);

        System.out.println(map.get("apple"));
        if(map.containsKey("grapes")){
            System.out.println("Grapes is present");
        }
        else{
            System.out.println("Grapes is not present");
        }

        map.remove("orange");
        if(map.containsKey("orange")){
            System.out.println("Orange is present");
        }
        else{
            System.out.println("Orange is not present");
        }

        for(Map.Entry<String, Integer> entry: map.entrySet()){
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }


        //For C++ map which is ordered we use TreeMap

        Map<String, Integer> sortedMap = new TreeMap<>();
        sortedMap.put("apple", 1);
        sortedMap.put("banana", 2);
        sortedMap.put("orange", 3);

        for(Map.Entry<String, Integer> entry : sortedMap.entrySet()){
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }

        //This is for queue in C++. Queue is an interface. We can use multiple implementations
        Queue<Integer> q = new LinkedList<>();
        q.add(2);
        q.add(5);
        q.add(6);
        q.offer(7);

        System.out.println(q.size());

        q.poll();

        System.out.println(q.size());
        System.out.println(q.isEmpty());
        System.out.println(q.peek());

        q.poll();
        q.poll();
        q.poll();

        System.out.println(q.size());

        //Stack implementation is very similar

        Stack<Integer> st = new Stack<>();
        st.push(10);
        st.push(20);
        st.push(30);
        System.out.println(st.capacity());
        System.out.println(st.peek());
        st.pop();
        System.out.println(st.peek());


        //Priority_Queue

        //By default priority queue is min Heap in java unlike C++

        PriorityQueue<Integer> q1 = new PriorityQueue<>();
        PriorityQueue<Integer> q2 = new PriorityQueue<>(Collections.reverseOrder());

        q1.add(10);
        q2.add(10);

        q1.add(20);
        q2.add(20);

        q1.add(30);
        q2.add(30);

        q1.add(40);
        q2.add(40);

        System.out.println(q1.peek());
        System.out.println(q2.peek());
        q1.poll();
        q2.poll();
        System.out.println(q1.peek());
        System.out.println(q2.peek());

        //Sorting

        List<Integer> list = new ArrayList<>(Arrays.asList(1, 19, 20, 34, 12, 10));
        Collections.sort(list);
        System.out.println("Printing List");
        for(Integer a: list){
            System.out.println(a);
        }

        list = new ArrayList<>(Arrays.asList(1, 19, 20, 34, 12, 10));
        list.sort(null);
        System.out.println("Printing List");
        for(Integer a: list) {
            System.out.println(a);
        }

        list = new ArrayList<>(Arrays.asList(1, 19, 20, 34, 12, 10));
        list.sort(Collections.reverseOrder());
        System.out.println("Printing List");
        for(Integer a: list){
            System.out.println(a);
        }

        ArrayList<DummyObject> dummyObjects = new ArrayList<>(Arrays.asList(
           new DummyObject(1, 5),
           new DummyObject(8, 2),
           new DummyObject(15, 1)
        ));

        dummyObjects.sort((d1, d2) -> {
            if(d1.a > d2.a){
                return -1;
            }
            else if(d2.a > d1.a){
                return 1;
            }
            else{
                return 0;
            }
        });
        System.out.println("Printing List");
        for(DummyObject d: dummyObjects){
            System.out.println(d.a + " " + d.b);
        }


        dummyObjects = new ArrayList<>(Arrays.asList(
                new DummyObject(1, 5),
                new DummyObject(8, 2),
                new DummyObject(15, 25)
        ));

        dummyObjects.sort((d1, d2) -> {
            if(d1.b > d2.b){
                return -1;
            }
            else if(d2.b > d1.b){
                return 1;
            }
            else{
                return 0;
            }
        });
        System.out.println("Printing List");
        for(DummyObject d: dummyObjects){
            System.out.println(d.a + " " + d.b);
        }



    }
}
