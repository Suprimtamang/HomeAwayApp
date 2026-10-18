package dataStructures;
/**
 * Binary Tree
 * @author AED  Team
 * @version 1.0
 * @param <E> Generic Element
 */
abstract class BTree<E> extends Tree<E> {

    /**
     * Returns the height of the tree.
     */
    public int getHeight() {
        if(isEmpty())
            return 0;
        return ((BTNode<E>)root).getHeight();
    }

    /**
     * Return the further left node of the tree
     * @return
     */
    BTNode<E> furtherLeftElement() {
        //TODO: Left as an exercise.
        if (root == null)
            return null;

        BTNode<E> node = (BTNode<E>) root;

        // keep going left until no more
        while (node.getLeftChild() != null)
            node = (BTNode<E>) node.getLeftChild();

        return node;
    }

    /**
     * Return the further right node of the tree
     * @return
     */
    BTNode<E> furtherRightElement() {
        //TODO: Left as an exercise.
        if (root == null)
            return null;

        BTNode<E> node = (BTNode<E>) root;

        // keep going right until no more
        while (node.getRightChild() != null)
            node = (BTNode<E>) node.getRightChild();

        return node;
    }


   //new methods: Left as an exercise.
}
