package dataStructures;
/**
 * Map with a singly linked list with head and size
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
class MapSinglyList<K,V> implements Map<K, V> {


    private SinglyListNode<Entry<K,V>> head;

    private int size;

    public MapSinglyList() {
        head = null;
        size = 0;
    }

    /**
     * Returns true iff the dictionary contains no entries.
     *
     * @return true if dictionary is empty
     */

    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @return number of elements in the dictionary
     */
    @Override
    public int size() {
        return size;
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
        SinglyListNode<Entry<K,V>> current = head;
        while (current != null) {
            Entry<K,V> entry = current.getElement();
            if (entry.key().equals(key)) {
                return entry.value();
            }
            current = current.getNext();
        }
        return null;
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
        SinglyListNode<Entry<K,V>> current = head;
        // Search for existing key
        while (current != null) {
            Entry<K,V> entry = current.getElement();
            if (entry.key().equals(key)) {
                // Key found, replace value and return old value
                V oldValue = entry.value();
                current.setElement(new Entry<>(key, value));
                return oldValue;
            }
            current = current.getNext();
        }
        // Key not found, insert new entry at the head
        head = new SinglyListNode<>(new Entry<>(key, value), head);
        size++;
        return null;
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
        if (head == null) {
            return null;
        }

        // Check if head contains the key
        Entry<K,V> entry = head.getElement();
        if (entry.key().equals(key)) {
            V oldValue = entry.value();
            head = head.getNext();
            size--;
            return oldValue;
        }

        // Search for the key in the rest of the list
        SinglyListNode<Entry<K,V>> current = head;
        while (current.getNext() != null) {
            Entry<K,V> nextEntry = current.getNext().getElement();
            if (nextEntry.key().equals(key)) {
                V oldValue = nextEntry.value();
                current.setNext(current.getNext().getNext());
                size--;
                return oldValue;
            }
            current = current.getNext();
        }
        return null;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    public Iterator<Entry<K, V>> iterator() {
        return new SinglyIterator<>(head);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */

    @SuppressWarnings("unchecked")
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */

    @SuppressWarnings("unchecked")
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }

}
