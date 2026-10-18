package dataStructures;

 import dataStructures.exceptions.NoSuchElementException;

 /**
  * Implementation of Two Way Iterator for DLList
  * @author AED  Team
  * @version 1.0
  * @param <E> Generic Element
  *
  */
 public class TwoWayDoublyIterator<E> extends DoublyIterator<E>
         implements TwoWayIterator<E> {

     /**
      * Node with the first element in the iteration.
      */
     private final DoublyListNode<E> lastNode;
     /**
      * Node with the previous element in the iteration.
      */
     private DoublyListNode<E> prevToReturn;

     /**
      * DoublyLLIterator constructor
      *
      * @param first - Node with the first element of the iteration
      * @param last  - Node with the last element of the iteration
      */
     public TwoWayDoublyIterator(DoublyListNode<E> first, DoublyListNode<E> last) {
         super(first);
         lastNode = last;
         prevToReturn = null; // Start with no previous element available
     }

     /**
      * Returns true if previous would return an element
      * rather than throwing an exception.
      * @return true iff the iteration has more elements in the reverse direction
      */
     public boolean hasPrevious() {
         return prevToReturn != null;
     }

     /**
      * Returns the next element in the iteration.
      * @return the next element in the iteration
      * @throws NoSuchElementException - if call is made without verifying pre-condition
      */
     @Override
     public E next() {
         if (!hasNext()) {
             throw new NoSuchElementException();
         }
         E element = super.next();      // advances nextToReturn
         // sync prevToReturn with the node we just returned
         prevToReturn = (nextToReturn == null) ? lastNode : nextToReturn.getPrevious();
         //prevToReturn = nextToReturn;  // Update for backward traversal
         //nextToReturn = nextToReturn.getNext();
         return element;
     }

     /**
      * Returns the previous element in the iteration.
      * @return previous element in the iteration
      * @throws NoSuchElementException - if call is made without verifying pre-condition
      */
     public E previous() {
         if (!hasPrevious()) {
             throw new NoSuchElementException();
         }
         E element = prevToReturn.getElement();
         nextToReturn = prevToReturn;  // Update for forward traversal
         prevToReturn = prevToReturn.getPrevious();
         return element;
     }



     /**
      * Restarts the iteration.
      * After rewind, if the iteration is not empty, next will return the first element.
      */
     public void rewind() {
         super.rewind();
         prevToReturn = null; // No previous element available at start
     }
 }