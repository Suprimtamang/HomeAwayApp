package dataStructures;
/**
 * AVL Tree Sorted Map
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value
 */
public class AVLSortedMap <K extends Comparable<K>,V> extends AdvancedBSTree<K,V>{

    // track number of entries when superclass doesn't provide it
    protected int size = 0;
    /**
     * 
     * @param key
     * @param value
     * @return
     */
    public V insert(K key, V value) {

        //TODO: Left as an exercise.
        // If exists a entry with this Key, update the node with new element
        // and return the old value of the entry
        // otherwise, insert the newNode, "rebalance" from the insertion position
        // and return value

        Entry<K,V> entry = new Entry<>(key, value);
        //empty tree
        if (root == null) {
            AVLNode<Entry<K,V>> newNode = new AVLNode<>(entry, null, null, null);
            this.root = newNode;
            this.size++;
            return value;
        }
        AVLNode<Entry<K,V>> node = (AVLNode<Entry<K,V>>) root;
        AVLNode<Entry<K,V>> parent = null;
        int cmp = 0;
        while (node != null) {
            parent = node;
            K nodeKey = node.getElement().key();
            cmp = key.compareTo(nodeKey);
            if (cmp == 0) {
                V oldValue = node.getElement().value();
                node.setElement(entry);
                return oldValue;
            } else if (cmp < 0) {
                node = (AVLNode<Entry<K,V>>) node.getLeftChild();
            } else {
                node = (AVLNode<Entry<K,V>>) node.getRightChild();
            }
        }
            AVLNode<Entry<K,V>> newNode = new AVLNode<>(entry,  null, null, parent);
            if (cmp < 0) {
                parent.setLeftChild(newNode);
            } else {
                parent.setRightChild(newNode);
            }
            this.size++;
            // Rebalance from parent
            rebalance(parent);
            return value;
    }

    /**
     *
     * @param key whose entry is to be removed from the map
     * @return
     */
    public V remove(K key) {
        //TODO: Left as an exercise.
        // If does not exist a entry with this Key, return null
        // otherwise, remove the node where is the element with this key,
        // "rebalance" from the removal position and return value
        // 1. Search the node
        AVLNode<Entry<K,V>> node = (AVLNode<Entry<K,V>>) root;

        while (node != null && key.compareTo(node.getElement().key()) != 0) {
            if (key.compareTo(node.getElement().key()) < 0)
                node = (AVLNode<Entry<K,V>>) node.getLeftChild();
            else
                node = (AVLNode<Entry<K,V>>) node.getRightChild();
        }

        // 2. Key not found
        if (node == null)
            return null;

        V oldValue = node.getElement().value();
        this.size--;

        AVLNode<Entry<K,V>> parent;

        // ===========================================
        // CASE A → Node has TWO children
        // ===========================================
        if (node.getLeftChild() != null && node.getRightChild() != null) {

            // Find successor (smallest in right subtree)
            AVLNode<Entry<K,V>> succ =
                    (AVLNode<Entry<K,V>>) node.getRightChild();

            while (succ.getLeftChild() != null)
                succ = (AVLNode<Entry<K,V>>) succ.getLeftChild();

            // Copy successor element into current node
            node.setElement(succ.getElement());

            // We now delete successor instead
            node = succ;
        }

        // Now node has at most 1 child
        AVLNode<Entry<K,V>> replacement =
                (node.getLeftChild() != null)
                        ? (AVLNode<Entry<K,V>>) node.getLeftChild()
                        : (AVLNode<Entry<K,V>>) node.getRightChild();

        parent = (AVLNode<Entry<K,V>>) node.getParent();

        // ===========================================
        // CASE B → Node has ONE child
        // ===========================================
        if (replacement != null) {

            replacement.setParent(parent);

            if (parent == null) {
                root = replacement;
            }
            else if (node == parent.getLeftChild()) {
                parent.setLeftChild(replacement);
            }
            else {
                parent.setRightChild(replacement);
            }
        }

        // ===========================================
        // CASE C → Node is LEAF
        // ===========================================
        else if (parent == null) { // node is root
            root = null;
        }
        else {
            if (node == parent.getLeftChild())
                parent.setLeftChild(null);
            else
                parent.setRightChild(null);
        }

        // 3. Rebalance from parent upward
        rebalance(parent);

        return oldValue;
    }
//HELPERS METHODS FOR INSERT AND REMOVE AND REBALANCE
private int balanceFactor(AVLNode<Entry<K,V>> node) {
    if (node == null) return 0;

    int leftH = (node.getLeftChild() == null) ? -1 :
            ((AVLNode<Entry<K,V>>) node.getLeftChild()).getHeight();

    int rightH = (node.getRightChild() == null) ? -1 :
            ((AVLNode<Entry<K,V>>) node.getRightChild()).getHeight();

    return leftH - rightH;
}

    private void updateHeight(AVLNode<Entry<K,V>> node) {
        if (node != null)
            node.updateHeight();
    }

    private AVLNode<Entry<K,V>> rotateRight(AVLNode<Entry<K,V>> y) {

        AVLNode<Entry<K,V>> x =
                (AVLNode<Entry<K,V>>) y.getLeftChild();
        AVLNode<Entry<K,V>> T2 =
                (AVLNode<Entry<K,V>>) x.getRightChild();

        // rotation
        x.setRightChild(y);
        y.setLeftChild(T2);

        // fix parents
        x.setParent(y.getParent());
        if (y.getParent() == null) {
            root = x;
        }
        else {
            AVLNode<Entry<K,V>> p =
                    (AVLNode<Entry<K,V>>) y.getParent();

            if (p.getLeftChild() == y)
                p.setLeftChild(x);
            else
                p.setRightChild(x);
        }

        y.setParent(x);

        // update heights
        y.updateHeight();
        x.updateHeight();

        return x;
    }


    private AVLNode<Entry<K,V>> rotateLeft(AVLNode<Entry<K,V>> y) {

        AVLNode<Entry<K,V>> x =
                (AVLNode<Entry<K,V>>) y.getRightChild();
        AVLNode<Entry<K,V>> T2 =
                (AVLNode<Entry<K,V>>) x.getLeftChild();

        // rotation
        x.setLeftChild(y);
        y.setRightChild(T2);

        // fix parents
        x.setParent(y.getParent());
        if (y.getParent() == null) {
            root = x;
        }
        else {
            AVLNode<Entry<K,V>> p =
                    (AVLNode<Entry<K,V>>) y.getParent();

            if (p.getLeftChild() == y)
                p.setLeftChild(x);
            else
                p.setRightChild(x);
        }

        y.setParent(x);

        // update heights
        y.updateHeight();
        x.updateHeight();

        return x;
    }


    private void rebalance(AVLNode<Entry<K,V>> node) {

        while (node != null) {

            node.updateHeight();
            int balance = balanceFactor(node);

            // Case 1: Left heavy (LL)
            if (balance > 1 && balanceFactor((AVLNode<Entry<K,V>>)node.getLeftChild()) >= 0) {
                node = rotateRight(node);
            }

            // Case 2: Left-Right (LR)
            else if (balance > 1) {
                rotateLeft((AVLNode<Entry<K,V>>) node.getLeftChild());
                node = rotateRight(node);
            }

            // Case 3: Right heavy (RR)
            else if (balance < -1 && balanceFactor((AVLNode<Entry<K,V>>)node.getRightChild()) <= 0) {
                node = rotateLeft(node);
            }

            // Case 4: Right-Left (RL)
            else if (balance < -1) {
                rotateRight((AVLNode<Entry<K,V>>) node.getRightChild());
                node = rotateLeft(node);
            }

            node = (AVLNode<Entry<K,V>>) node.getParent();
        }
    }
}
