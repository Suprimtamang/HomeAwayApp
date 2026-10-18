package dataStructures;

import java.io.Serializable;

/**
     * Double List Node Implementation
     * @author AED  Team
     * @version 1.0
     * @param <E> Generic Element
     * 
     */

class DoublyListNode<E> implements Serializable {

    	/**
         * Element stored in the node.
         */
        private E element;

        /**
         * (Pointer to) the previous node.
         */
        private DoublyListNode<E> previous;

        /**
         * (Pointer to) the next node.
         */
        private DoublyListNode<E> next;

        /**
         * 
         * @param theElement - The element to be contained in the node
         * @param thePrevious - the previous node
         * @param theNext - the next node
         */
        public DoublyListNode(E theElement, DoublyListNode<E> thePrevious,
                              DoublyListNode<E> theNext ) {
            //TODO: Left as an exercise.

                element = theElement;
                previous = thePrevious;
                next = theNext;

        }

        /**
         * 
         * @param theElement to be contained in the node
         */
        public DoublyListNode(E theElement ) {
            //TODO: Left as an exercise.
                this (theElement, null, null);

        }

        /**
         * 
         * @return the element contained in the node
         */
        public E getElement( ) {
            return element;
        }

        /**
         * 
         * @return the previous node
         */
        public DoublyListNode<E> getPrevious( ) {
            return previous;
        }

        /**
         * 
         * @return the next node
         */
        public DoublyListNode<E> getNext( ) {
            return next;
        }

        /**
         * 
         * @param newElement - New element to replace the current element
         */
        public void setElement( E newElement ) {
            //TODO: Left as an exercise.
                this.element = newElement;

        }

        /**
         * 
         * @param newPrevious - node to replace the current previous node
         */
        public void setPrevious( DoublyListNode<E> newPrevious ) {
            //TODO: Left as an exercise.
                this.previous = newPrevious;

        }

        /**
         * 
         * @param newNext - node to replace the next node
         */
        public void setNext( DoublyListNode<E> newNext ) {
            //TODO: Left as an exercise.
                this.next = newNext;

        }
    }