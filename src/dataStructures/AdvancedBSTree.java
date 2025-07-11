package dataStructures;

/**
 * Advanced BSTree Data Type implementation
 * @author AED team
 * @author of changes Francisco Correia 67264 & Sérgio Garrido 67202
 * @version 1.0
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public abstract class AdvancedBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K,V> {
    private static final long serialVersionUID = 0L;


    /**
     * Performs a single left rotation rooted at a node.
     * Node b was a  right  child  of a before the  rotation,
     * then a becomes the left child of b after the rotation.
     *
     * @param a - root of the rotation
     * @pre: a has a right child
     */
    protected void rotateLeft(BSTNode<Entry<K, V>> a) {
        BSTNode<Entry<K, V>> b = a.right;
        a.setRight(b.left);

        if (b.left != null) {
            b.left.setParent(a);
        }

        b.setParent(a.getParent());

        if (a.getParent() == null) {
            root = b;
        } else if (a == a.getParent().left) {
            a.getParent().setLeft(b);
        } else {
            a.getParent().setRight(b);
        }

        b.setLeft(a);
        a.setParent(b);
    }

    /**
     * Performs a single right rotation rooted at c node.
     * Node b was a  left  child  of c before the  rotation,
     * then c becomes the right child of b after the rotation.
     *
     * @param c - root of the rotation
     * @pre: c has a left child
     */
    protected void rotateRight(BSTNode<Entry<K, V>> c) {

        BSTNode<Entry<K, V>> b = c.left;
        c.setLeft(b.right);

        if (b.right != null) {
            b.right.setParent(c);
        }

        b.setParent(c.getParent());

        if (c.getParent() == null) {
            root = b;
        } else if (c == c.getParent().left) {
            c.getParent().setLeft(b);
        } else {
            c.getParent().setRight(b);
        }

        b.setRight(c);
        c.setParent(b);

    }

    /**
     * Performs a tri-node restructuring (a single or double rotation rooted at X node).
     * Assumes the nodes are in one of following configurations:
     *
     * @param X - root of the rotation
     *          <pre>
     *                   z=c       z=c        z=a         z=a
     *                  /  \      /  \       /  \        /  \
     *                y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *               /  \      /  \           /  \         /  \
     *             x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *            /  \          /  \       /  \             /  \
     *           t1  t2        t2  t3     t2  t3           t3  t4
     *          </pre>
     * @return the new root of the restructured subtree
     */
    protected BSTNode<Entry<K, V>> restructure(BSTNode<Entry<K, V>> X) {
        // the modification of a tree T caused by a trinode restructuring operation
        // can be implemented through case analysis either as a single rotation or as a double rotation.
        // The double rotation arises when position x has the middle of the three relevant keys
        // and is first rotated above its parent Y, and then above what was originally its grandparent Z.
        // In any of the cases, the trinode restructuring is completed with O(1)running time

        BSTNode<Entry<K, V>> Y = X.parent;
        BSTNode<Entry<K, V>> Z = Y.parent;

        if (Z.left != null && Z.left.equals(Y)) {
            if (Y.left != null && Y.left.equals(X)) {
                rotateRight(Z);
                return Y;
            } else {
                rotateLeft(Y);
                rotateRight(Z);
                return X;
            }
        } else {
            if (Y.right != null && Y.right.equals(X)) {
                rotateLeft(Z);
                return Y;
            } else {
                rotateRight(Y);
                rotateLeft(Z);
                return X;
            }
        }
    }
}

/**
 * End of AdvancedBSTree
 */



