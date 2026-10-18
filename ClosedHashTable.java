package dataStructures;
/**
 * Closed Hash Table
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class ClosedHashTable<K,V> extends HashTable<K,V> {

    //Load factors
    static final float IDEAL_LOAD_FACTOR =0.5f;
    static final float MAX_LOAD_FACTOR =0.8f;
    static final int NOT_FOUND=-1;

    // removed cell
    static final Entry<?,?> REMOVED_CELL = new Entry<>(null,null);

    // The array of entries.
    private Entry<K,V>[] table;

    /**
     * Constructors
     */

    public ClosedHashTable( ){
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public ClosedHashTable( int capacity ){
        super(capacity);
        int arraySize = HashTable.nextPrime((int) (capacity / IDEAL_LOAD_FACTOR));
        // Compiler gives a warning.
        table = (Entry<K,V>[]) new Entry[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = null;
        maxSize = (int)(arraySize * MAX_LOAD_FACTOR);
    }

    //Methods for handling collisions.
    // Returns the hash value of the specified key.
    int hash( K key, int i ){
        return Math.abs( key.hashCode() + i) % table.length;
    }
    /**
     * Linear Proving
     * @param key to search
     * @return the index of the table, where is the entry with the specified key, or null
     */
    int searchLinearProving(K key) {
        //TODO: Left as an exercise.
            for (int i = 0; i < table.length; i++) {
                int index = hash(key, i);
                Entry<K, V> entry = table[index];
                if (entry == null) {
                    return NOT_FOUND; // Key not found
                }
                if (entry != REMOVED_CELL && entry.key().equals(key)) {
                    return index; // Key found
                }
            }
        return NOT_FOUND; 
    }

    
    /**
     * If there is an entry in the dictionary whose key is the specified key,
     * returns its value; otherwise, returns null.
     *
     * @param key whose associated value is to be returned
     * @return value of entry in the dictionary whose key is the specified key,
     * or null if the dictionary does not have an entry with that key
     */
    @Override
    public V get(K key) {
        //TODO: Left as an exercise.
        
        int index = searchLinearProving(key);
       if(index == NOT_FOUND){
           return null;
       } else {
           return table[index].value();
       }
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
    @Override
    public V put(K key, V value) {
        if (isFull())
            rehash();
        //TODO: Left as an exercise.

        int firstRemoved = -1;
        for (int i = 0; i < table.length; i++) {
            int idx = hash(key, i);
            Entry<K,V> e = table[idx];
            if (e == null) {
                // insert here (prefer previously found removed)
                int insertIdx = (firstRemoved != -1) ? firstRemoved : idx;
                table[insertIdx] = new Entry<>(key, value);
                currentSize++;
                return null;
            } else if (e == REMOVED_CELL) {
                if (firstRemoved == -1) firstRemoved = idx;
            } else if (e.key().equals(key)) {
                V old = e.value();
                table[idx] = new Entry<>(key, value);
                return old;
            }}
        // table full (shouldn't happen because we rehashed earlier), try insert at firstRemoved
        if (firstRemoved != -1) {
            table[firstRemoved] = new Entry<>(key, value);
            currentSize++;
            return null;
        }
        return null;
    }

     private void rehash(){
 //TODO: Left as an exercise.
         Entry<K,V>[] old = table;
         int newSize = HashTable.nextPrime(old.length * 2);
         if (newSize == 0) newSize = old.length * 2 + 1;
         table = (Entry<K,V>[]) new Entry[newSize];
         for (int i = 0; i < table.length; i++) table[i] = null;
         currentSize = 0;
         maxSize = (int)(table.length * MAX_LOAD_FACTOR);
         for (Entry<K,V> e : old) {
             if (e != null && e != REMOVED_CELL) {
                 // insert into new table (linear probing without triggering rehash)
                 for (int i = 0; i < table.length; i++) {
                     int idx = Math.abs(e.key().hashCode() + i) % table.length;
                     if (table[idx] == null) {
                         table[idx] = new Entry<>(e.key(), e.value());
                         currentSize++;
                         break;
                     }
                 }
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
    @Override
    public V remove(K key) {
        //TODO: Left as an exercise.

        int idx = searchLinearProving(key);
        if (idx == NOT_FOUND) return null;
        V old = table[idx].value();
        table[idx] = (Entry<K,V>) REMOVED_CELL;
        currentSize--;
        return old;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */


@Override
public Iterator<Entry<K, V>> iterator() {
    return new Iterator<>() {
        private int index = -1;

        public boolean hasNext() {
            for (int i = index + 1; i < table.length; i++) {
                Entry<K,V> e = table[i];
                if (e != null && e != REMOVED_CELL) return true;
            }
            return false;
        }

        public Entry<K, V> next() {
            for (int i = index + 1; i < table.length; i++) {
                Entry<K,V> e = table[i];
                if (e != null && e != REMOVED_CELL) {
                    index = i;
                    return e;
                }
            }
            throw new dataStructures.exceptions.NoSuchElementException();
        }

        public void rewind() {
            index = -1;
        }
    };
}

}
