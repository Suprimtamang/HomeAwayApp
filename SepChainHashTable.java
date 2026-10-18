package dataStructures;
/**
 * SepChain Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class SepChainHashTable<K,V> extends HashTable<K,V> {

    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.75f;
    static final float MAX_LOAD_FACTOR =0.9f;

    // The array of Map with singly linked list.
    private Map<K,V>[] table;

    public SepChainHashTable(){
        this (DEFAULT_CAPACITY);
    }
    
    @SuppressWarnings("unchecked")
    public SepChainHashTable( int capacity ){
        super(capacity);
        table = (Map<K,V>[]) new Map[capacity];
        // Initialize each bucket with an empty MapSinglyList
        for (int i = 0; i < capacity; i++) {
            table[i] = new MapSinglyList<>();
        }
        maxSize = (int) (capacity * MAX_LOAD_FACTOR);
    }

    // Returns the hash value of the specified key.
    protected int hash( K key ){
        return Math.abs( key.hashCode() ) % table.length;
    }
    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    public V get(K key) {
        int index = hash(key);
        return table[index].get(key);
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * replaces its value by the specified value and returns the old value;
     * otherwise, inserts the entry (key, value) and returns null.
     *
     * @param key   with which the specified value is to be associated
     * @param value to be associated with the specified key
     * @return previous value associated with key,
     * or null if the dictionary does not have an entry with that key
     */
    public V put(K key, V value) {
        if (isFull())
            rehash();

        int index = hash(key);
        V oldValue = table[index].put(key, value);

        // If it was a new entry (oldValue is null), increment currentSize
        if (oldValue == null) {
            currentSize++;
        }
        return oldValue;
    }


    @SuppressWarnings("unchecked")
    private void rehash() {
        // Save the old table
        Map<K,V>[] oldTable = table;

        // Create a new table with double the capacity
        int newCapacity = table.length * 2;
        table = (Map<K,V>[]) new Map[newCapacity];

        // Initialize each bucket in the new table
        for (int i = 0; i < newCapacity; i++) {
            table[i] = new MapSinglyList<>();
        }
        // Reset currentSize and update maxSize
        currentSize = 0;
        maxSize = (int) (newCapacity * MAX_LOAD_FACTOR);

        // Rehash all entries from the old table
        for (Map<K,V> bucket : oldTable) {
            Iterator<Entry<K,V>> it = bucket.iterator();
            while (it.hasNext()) {
                Entry<K,V> entry = it.next();
                put(entry.key(), entry.value());
            }
        }
    }

    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * removes it from the dictionary and returns its value;
     * otherwise, returns null.
     *
     * @param key whose entry is to be removed from the map
     * @return previous value associated with key,
     * or null if the dictionary does not an entry with that key
     */
    public V remove(K key) {
        int index = hash(key);
        V oldValue = table[index].remove(key);
        // If the key was found and removed, decrement currentSize
        if (oldValue != null) { currentSize--;}
        return oldValue;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SepChainHashTableIterator<>(table);
    }


}
