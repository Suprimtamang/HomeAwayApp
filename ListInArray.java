package dataStructures;
import dataStructures.exceptions.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * List in Array
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 *
 */
@SuppressWarnings("ALL")
public class ListInArray<E> implements List<E>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int FACTOR = 2;
    /**
     * Array of generic elements E.
     */
    private E[] elems;

    /**
     * Number of elements in array.
     */
    private int counter;


    /**
     * Construtor with capacity.
     * @param dimension - initial capacity of array.
     */
    @SuppressWarnings("unchecked")
    public ListInArray(int dimension) {
        elems = (E[]) new Object[dimension];
        counter = 0;
    }
    /**
     * Returns true iff the list contains no elements.
     *
     * @return true if list is empty
     */
    public boolean isEmpty() {
        return counter==0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return number of elements in the list
     */
    public int size() {
        return counter;
    }

    /**
     * Returns an iterator of the elements in the list (in proper sequence).
     *
     * @return Iterator of the elements in the list
     */
    public Iterator<E> iterator() {
        return new ArrayIterator<>(elems,counter);
    }

    /**
     * Returns the first element of the list.
     *
     * @return first element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getFirst() {
        //TODO: Left as an exercise.
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return elems[0];
    }

    /**
     * Returns the last element of the list.
     *
     * @return last element in the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E getLast() {
        //TODO: Left as an exercise.
       if(isEmpty()){
              throw new NoSuchElementException();
       }
         return elems[counter-1];
    }

    /**
     * Returns the element at the specified position in the list.
     * Range of valid positions: 0, ..., size()-1.
     * If the specified position is 0, get corresponds to getFirst.
     * If the specified position is size()-1, get corresponds to getLast.
     *
     * @param position - position of element to be returned
     * @return element at position
     * @throws InvalidPositionException if position is not valid in the list
     */
    public E get(int position) {
        //TODO: Left as an exercise.
      if(position<0 || position>=counter){
          throw new InvalidPositionException();

      }
        return elems[position];
    }

    /**
     * Returns the position of the first occurrence of the specified element
     * in the list, if the list contains the element.
     * Otherwise, returns -1.
     *
     * @param element - element to be searched in list
     * @return position of the first occurrence of the element in the list (or -1)
     */
    public int indexOf(E element) {
        //TODO: Left as an exercise.
        if(isEmpty()){
            return -1;
        }
          for(int i=0;i<counter;i++){
              if(elems[i].equals(element)){
                  return i;
              }
          }
          return -1;
    }

    /**
     * Inserts the specified element at the first position in the list.
     *
     * @param element to be inserted
     */
    public void addFirst(E element) {
        //TODO: Left as an exercise.
        add(0,element);

    }

    /**
     * Inserts the specified element at the last position in the list.
     *
     * @param element to be inserted
     */
    public void addLast(E element) {
        //TODO: Left as an exercise.
        if(counter==elems.length){
            //noinspection unchecked
            E[] newElems=(E[]) new Object[elems.length*FACTOR];
            for(int i=0;i<elems.length;i++){
                newElems[i]=elems[i];
            }
            elems=newElems;
        }
        elems[counter++]=element;
    }

    /**
     * Inserts the specified element at the specified position in the list.
     * Range of valid positions: 0, ..., size().
     * If the specified position is 0, add corresponds to addFirst.
     * If the specified position is size(), add corresponds to addLast.
     *
     * @param position - position where to insert element
     * @param element  - element to be inserted
     * @throws InvalidPositionException - if position is not valid in the list
     */
    public void add(int position, E element) {
        //TODO: Left as an exercise.
        if(position<0 || position>counter){
            throw new InvalidPositionException();
        }
        if(counter==elems.length){
            @SuppressWarnings("unchecked")
            E[] newElems=(E[]) new Object[elems.length*FACTOR];
            for(int i=0;i<elems.length;i++){
                newElems[i]=elems[i];
            }
            elems=newElems;
        }
        for(int i=counter;i>position;i--){
            elems[i]=elems[i-1];
        }
        elems[position]=element;
        counter++;
    }

    /**
     * Removes and returns the element at the first position in the list.
     *
     * @return element removed from the first position of the list
     * @throws NoSuchElementException - if size() == 0
     */
    public E removeFirst() {
        //TODO: Left as an exercise.
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        E temp = elems[0];
        for (int i = 0; i < counter - 1; i++) {
            elems[i] = elems[i + 1];
        }
        elems[counter - 1] = null;
        counter--;
        return temp;
    }

        /**
         * Removes and returns the element at the last position in the list.
         *
         * @return element removed from the last position of the list
         * @throws NoSuchElementException - if size() == 0
         */
        public E removeLast () {
            //TODO: Left as an exercise.
            if (isEmpty()) {
                throw new NoSuchElementException();
            }
            E temp = elems[counter - 1];
            elems[counter - 1] = null;
            counter--;
            return temp;
        }

        /**
         * Removes and returns the element at the specified position in the list.
         * Range of valid positions: 0, ..., size()-1.
         * If the specified position is 0, remove corresponds to removeFirst.
         * If the specified position is size()-1, remove corresponds to removeLast.
         *
         * @param position - position of element to be removed
         * @return element removed at position
         * @throws InvalidPositionException - if position is not valid in the list
         */
        public E remove ( int position){
            //TODO: Left as an exercise.
           for (int i=position;i<counter-1;i++){
               elems[i]=elems[i+1];
           }
           E temp=elems[counter-1];
           elems[counter-1]=null;
           counter--;
           return temp;
        }
    }
