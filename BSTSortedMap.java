package dataStructures;

import dataStructures.exceptions.EmptyMapException;
/**
 * Binary Search Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class BSTSortedMap<K extends Comparable<K>,V> extends BTree<Map.Entry<K,V>> implements SortedMap<K,V>{

    /**
     * Constructor
     */
    public BSTSortedMap(){
        super();
    }
    /**
     * Returns the entry with the smallest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> minEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherLeftElement().getElement();
    }

    /**
     * Returns the entry with the largest key in the dictionary.
     *
     * @return
     * @throws EmptyMapException
     */
    @Override
    public Entry<K, V> maxEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherRightElement().getElement();
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
        Node<Entry<K,V>> node=getNode((BTNode<Entry<K,V>>)root,key);
        if (node!=null)
            return node.getElement().value();
        return null;
    }

    private BTNode<Entry<K,V>> getNode(BTNode<Entry<K,V>> node, K key) {
        //TODO: Left as an exercise.

        while (node != null) {
            K nodeKey = node.getElement().key();
            int cmp = key.compareTo(nodeKey);

            if (cmp == 0)
                return node;

            else if (cmp < 0)
                node = (BTNode<Entry<K,V>>) node.getLeftChild();

            else
                node = (BTNode<Entry<K,V>>) node.getRightChild();
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
    @Override
    public V put(K key, V value) {
        //TODO: Left as an exercise.

        Entry<K,V> entry = new Entry<>(key, value);

        // Empty tree
        if (root == null) {
            root = new BTNode<>(entry);
            return null;
        }

        BTNode<Entry<K,V>> node = (BTNode<Entry<K,V>>) root;
        BTNode<Entry<K,V>> parent = null;
        int cmp = 0;

        while (node != null) {
            parent = node;
            cmp = key.compareTo(node.getElement().key());

            if (cmp == 0) {
                V oldValue = node.getElement().value();
                node.setElement(entry);
                return oldValue;
            }
            else if (cmp < 0)
                node = (BTNode<Entry<K,V>>) node.getLeftChild();
            else
                node = (BTNode<Entry<K,V>>) node.getRightChild();
        }

        // Insert new node
        BTNode<Entry<K,V>> newNode = new BTNode<>(entry, parent);

        if (cmp < 0)
            parent.setLeftChild(newNode);
        else
            parent.setRightChild(newNode);

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
    @Override
    public V remove(K key) {
        //TODO: Left as an exercise.

        BTNode<Entry<K,V>> node =
                getNode((BTNode<Entry<K,V>>) root, key);

        if (node == null)
            return null;

        V oldValue = node.getElement().value();

        // CASE 3: Two children
        if (node.getLeftChild() != null && node.getRightChild() != null) {

            // Find successor
            BTNode<Entry<K,V>> succ =
                    (BTNode<Entry<K,V>>) node.getRightChild();

            while (succ.getLeftChild() != null)
                succ = (BTNode<Entry<K,V>>) succ.getLeftChild();

            node.setElement(succ.getElement());

            // Remove successor instead
            node = succ;
        }

        // Node now has <=1 child
        BTNode<Entry<K,V>> replacement =
                (node.getLeftChild() != null)
                        ? (BTNode<Entry<K,V>>) node.getLeftChild()
                        : (BTNode<Entry<K,V>>) node.getRightChild();

        BTNode<Entry<K,V>> parent =
                (BTNode<Entry<K,V>>) node.getParent();

        // Case: one child
        if (replacement != null) {

            replacement.setParent(parent);

            if (parent == null)
                root = replacement;

            else if (parent.getLeftChild() == node)
                parent.setLeftChild(replacement);

            else
                parent.setRightChild(replacement);
        }

        // Case: leaf
        else if (parent == null) {
            root = null;
        }
        else {
            if (parent.getLeftChild() == node)
                parent.setLeftChild(null);
            else
                parent.setRightChild(null);
        }

        return oldValue;
    }

    /**
     * Returns an iterator of the entries in the dictionary.
     *
     * @return iterator of the entries in the dictionary
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new InOrderIterator<>((BTNode<Entry<K,V>>) root);
    }

    /**
     * Returns an iterator of the values in the dictionary.
     *
     * @return iterator of the values in the dictionary
     */
    @Override
@SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<V> values() {
        return new ValuesIterator(iterator());
    }

    /**
     * Returns an iterator of the keys in the dictionary.
     *
     * @return iterator of the keys in the dictionary
     */
    @Override
@SuppressWarnings({"unchecked","rawtypes"})
    public Iterator<K> keys() {
        return new KeysIterator(iterator());
    }
}
