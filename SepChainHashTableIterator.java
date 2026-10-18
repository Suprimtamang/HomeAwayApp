package dataStructures;
/**
 * SepChain Hash Table Iterator
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
import dataStructures.exceptions.NoSuchElementException;

class SepChainHashTableIterator<K,V> implements Iterator<Map.Entry<K,V>> {

    private final Map<K,V>[] table;
    private int currentBucket;
    private Iterator<Map.Entry<K,V>> bucketIterator;

    public SepChainHashTableIterator(Map<K,V>[] table) {
        this.table = table;
        rewind();
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        if (bucketIterator != null && bucketIterator.hasNext()) {
            return true;
        }

        // Move to next non-empty bucket
        while (currentBucket < table.length) {
            bucketIterator = table[currentBucket].iterator();
            currentBucket++;
            if (bucketIterator.hasNext()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public Map.Entry<K,V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return bucketIterator.next();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        currentBucket = 0;
        bucketIterator = null;
    }
}

