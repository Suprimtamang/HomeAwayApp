package dataStructures;

import dataStructures.exceptions.NoSuchElementException;

/**
 * Iterator Abstract Data HomeType with Filter
 * Includes description of general methods for one way iterator.
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
public class FilterIterator<E> implements Iterator<E> {

    /**
     *  Iterator of elements to filter.
     */
    private final Iterator<E> iterator;

    /**
     *  Filter.
     */
    private final Predicate<E> filter;

    /**
     * Node with the next element in the iteration.
     */
    private E nextToReturn;

    /**
     *
     * @param list to be iterated
     * @param filter
     */
    public FilterIterator(Iterator<E> list, Predicate<E> filter) {
        //TODO Resolved
        this.iterator=list;
        this.filter=filter;
        advanceNext();
    }

    private void advanceNext() {
        nextToReturn =  null;
        while (iterator.hasNext()) {
            E candidate = iterator.next();
            if (filter.check(candidate)) {
                nextToReturn = candidate;
                break;
            }
        }
    }

    /**
     * Returns true if next would return an element
     *
     * @return true iff the iteration has more elements
     */
    public boolean hasNext() {
        //TODO Resolved
        return nextToReturn != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException - if call is made without verifying pre-condition
     */
    public E next() {
        //TODO Resolved
        if(!hasNext()){
            throw new NoSuchElementException();
        }
        E out = nextToReturn;
        advanceNext();      // move buffer forward
        return out;

    }

    /**
     * Restarts the iteration.
     * After rewind, if the iteration is not empty, next will return the first element.
     */
    public void rewind() {
        //TODO Resolved
        iterator.rewind();
        advanceNext();      // rebuild buffer from start

    }
}
