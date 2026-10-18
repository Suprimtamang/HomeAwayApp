package dataStructures;

import dataStructures.exceptions.NoSuchElementException;
/**
 * Iterator of values
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic element
 */
class ValuesIterator<E> implements Iterator<E> {

    private final Iterator<Map.Entry<?,E>> iterator;


    public ValuesIterator(Iterator<Map.Entry<?,E>> it) {
        this.iterator = it;
    }

    /**
     * Returns true if next would return an element
     * rather than throwing an exception.
     *
     * @return true iff the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    @Override
    public E next() {
        return iterator.next().value();
    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    @Override
    public void rewind() {
        iterator.rewind();
    }
}
