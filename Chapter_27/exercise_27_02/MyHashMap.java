package exercise_27_02;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static int DEFAULT_INITIAL_CAPACITY = 4;

    private static int MAXIMUM_CAPACITY = 1 << 30; 

    private int capacity;

    private static float DEFAULT_MAX_LOAD_FACTOR = 0.5f; 

    private float loadFactorThreshold; 

    private int size = 0; 

    ArrayList<MyMap.Entry<K,V>> table;
    
    public MyHashMap() {  
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_MAX_LOAD_FACTOR);    
    }

    public MyHashMap(int initialCapacity) { 
        this(initialCapacity, DEFAULT_MAX_LOAD_FACTOR);    
    }

    public MyHashMap(int initialCapacity, float loadFactorThreshold) { 
        if(initialCapacity > MAXIMUM_CAPACITY)
            this.capacity = MAXIMUM_CAPACITY;
        else
            this.capacity = trimToPowerOf2(initialCapacity);

        this.loadFactorThreshold = loadFactorThreshold;    
        table = new ArrayList<>();
        
        for(int i = 0; i < capacity; i++) {
            table.add(null);
        }
    }

    @Override
    public void clear() {
        size = 0;
        removeEntries();
    }

    @Override
    public boolean containsKey(K key) {    
        if(get(key) != null)
            return true;
        else
            return false;
    }

    @Override
    public boolean containsValue(V value) {
        for(MyMap.Entry<K, V> entry : table) {
            if(entry != null && entry.getValue().equals(value)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<MyMap.Entry<K,V>> entrySet() {
        Set<MyMap.Entry<K, V>> set = new HashSet<>();

        for(MyMap.Entry<K, V> entry : table) {
            if(entry != null) {
                set.add(entry); 
            }
        }

        return set;
    }

    @Override
    public V get(K key) {
        int index = hash(key.hashCode());
        int i = index;
        int j = 1;
        while(table.get(index) != null) {
            if(table.get(index).getKey().equals(key)) {
                return table.get(index).getValue();
            }
            index = (i + j * j) % capacity;
            j++;
        }

        return null;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }  

    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();

        for(MyMap.Entry<K, V> entry : table) {
            if(entry != null) {
                set.add(entry.getKey()); 
            }
        }

        return set;
    }

    @Override
    public V put(K key, V value) {
        int index = hash(key.hashCode());
        int i = index;
        int j = 1;
        if(get(key) != null) {
            while(table.get(index) != null) {
                if(table.get(index).getKey().equals(key)) {
                    V oldValue = table.get(index).getValue();
                    table.get(index).value = value; 
                    return oldValue;
                }
                index = (i + j * j) % capacity;
                j++;
            }
        }

        if(size >= capacity * loadFactorThreshold) {
            if(capacity == MAXIMUM_CAPACITY)
                throw new RuntimeException("Exceeding maximum capacity");

            rehash();
        }

        MyMap.Entry<K, V> entry = new MyMap.Entry(key, value);
        
        while(table.get(index) != null) {
            index = (i + j * j) % capacity;
            j++;
        }
        table.add(index, entry);
        
        size++;

        return value;  
    } 

    @Override
    public void remove(K key) {
        int index = hash(key.hashCode());
        int i = index;
        int j = 1;
        
        while(table.get(index) != null) {
            if(table.get(index).getKey().equals(key)) {
                table.remove(index);
                size--;
                return;
            }
            index = (i + j * j) % capacity;
            j++;
        }
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<V> values() {
        Set<V> set = new HashSet<>();

        for(MyMap.Entry<K, V> entry : table) {
            if(entry != null) {
                set.add(entry.getValue()); 
            }
        }

        return set;
    }

    private int hash(int hashCode) {
        return supplementalHash(hashCode) & (capacity - 1);
    }

    private static int supplementalHash(int h) {
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

    private int trimToPowerOf2(int initialCapacity) {
        int capacity = 1;
        while(capacity < initialCapacity) {
            capacity <<= 1;
        }

        return capacity;
    }

    private void removeEntries() {
        table.clear();
    }

    private void rehash() {
        Set<MyMap.Entry<K, V>> set = entrySet();
        capacity <<= 1;
        table = new ArrayList<MyMap.Entry<K, V>>();
        size = 0;
        
        for(int i = 0; i < capacity; i++) {
            table.add(null);
        }
        
        for(MyMap.Entry<K, V> entry: set) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");

        for(MyMap.Entry<K, V> entry : table) {
            if(entry != null && table.size() > 0) 
                builder.append(entry);
        }

        builder.append("]");
        return builder.toString();
    }
}