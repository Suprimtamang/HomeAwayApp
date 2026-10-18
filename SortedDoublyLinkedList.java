package dataStructures;

import dataStructures.exceptions.*;

/**
 * Sorted Doubly linked list Implementation
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 * 
 */
public class SortedDoublyLinkedList<E> implements SortedList<E> {
    /**
     *  Node at the head of the list.
     */
    private DoublyListNode<E> head;
    /**
     * Node at the tail of the list.
     */
    private DoublyListNode<E> tail;
    /**
     * Number of elements in the list.
     */
    private int currentSize;
    /**
     * Comparator of elements.
     */
    private final Comparator<E> comparator;
    /**
     * Constructor of an empty sorted double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
    public SortedDoublyLinkedList(Comparator<E> comparator) {
        //TODO Resolved
        this.comparator = comparator;
        head = null;
        tail = null;
        currentSize = 0;

    }

    /**
     * Returns true iff the list contains no elements.
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return currentSize==0;
    }

    /**
     * Returns the number of elements in the list.
     * @return number of elements in the list
     */

    public int size() {
        return currentSize;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new DoublyIterator<>(head);
    }

    /**
     * Returns the first element of the list.
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMin( ) {
        //TODO Resolved
        if(isEmpty()){
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    /**
     * Returns the last element of the list.
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getMax( ) {
        //TODO Resolved
       if(isEmpty()){
              throw new NoSuchElementException();
       }
       return tail.getElement();
    }
    /**
     * Returns the first occurrence of the element equals to the given element in the list.
     * @return element in the list or null
     */
    public E get(E element) {
        //TODO Resolved
       if(isEmpty()){
           return null;
       }
         DoublyListNode<E> current=head;
            while(current!=null){
                if(comparator.compare(current.getElement(), element)==0){
                    return current.getElement();
                }
                current=current.getNext();
            }
            return null;
    }

    /**
     * Returns true iff the element exists in the list.
     *
     * @param element to be found
     * @return true iff the element exists in the list.
     */
    public boolean contains(E element) {
        //TODO Resolved
          DoublyListNode<E> current=head;
             while(current!=null){
                 if(comparator.compare(current.getElement(), element)==0){
                     return true;
                 }
                 current=current.getNext();
             }
                return false;


    }

    /**
     * Inserts the specified element at the list, according to the natural order.
     * If there is an equal element, the new element is inserted after it.
     * @param element to be inserted
     */
    public void add(E element) {
        //TODO Resolved
       //After checking that it is not to be
        //inserted at the head or at the tail, go to
        //the node that will be the next of the new node
        // correction done here with reference / help from deepseek
        DoublyListNode<E> newNode = new DoublyListNode<>(element);

        if (isEmpty()) {
            head = newNode;
            tail = newNode;
        }
        else if (comparator.compare(element, head.getElement()) < 0) {
            // Insert at beginning (smaller than head)
            newNode.setNext(head);
            head.setPrevious(newNode);
            head = newNode;
        }
        else {
            // Find insertion point - we want to insert AFTER nodes that are <= element
            DoublyListNode<E> current = head;
            while (current.getNext() != null &&
                    comparator.compare(element, current.getNext().getElement()) >= 0) {
                current = current.getNext();
            }
            // Insert after current
            newNode.setNext(current.getNext());
            newNode.setPrevious(current);
            if (current.getNext() != null) {
                current.getNext().setPrevious(newNode);
            } else {
                tail = newNode;  // Inserting at end
            }
            current.setNext(newNode);
        }
        currentSize++;

    }

    /**
     * Removes and returns the first occurrence of the element equals to the given element in the list.
     * @return element removed from the list or null if !belongs(element)
     */
    public E remove(E element) {
        //TODO Resolved
       //find the node to be removed
         if(isEmpty()){
                return null;
         }
         //update connections from previous and next nodes
        //decrease the number of elements
            DoublyListNode<E> current=head;
                while(current!=null){
                    if(comparator.compare(current.getElement(), element)==0){
                        if(current==head){
                            head=head.getNext();
                            if(head!=null){
                                head.setPrevious(null);
                            } else {
                                tail = null; // List is now empty
                            }
                        } else if(current==tail){
                            tail=tail.getPrevious();
                            if(tail!=null){
                                tail.setNext(null);
                            } else {
                                head = null; // List is now empty
                            }
                        } else {
                            current.getPrevious().setNext(current.getNext());
                            current.getNext().setPrevious(current.getPrevious());
                        }
                        currentSize--;
                        return current.getElement();
                    }
                    current=current.getNext();
                }
                return null;
    }
}