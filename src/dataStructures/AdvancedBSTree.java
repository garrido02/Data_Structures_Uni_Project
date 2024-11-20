package dataStructures;

/**
 * Advanced BSTree Data Type implementation
 * @author AED team
 * @version 1.0
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public abstract class AdvancedBSTree<K extends Comparable<K>, V> extends BinarySearchTree<K,V>
{
    /**
     * Performs a single left rotation rooted at A node.
     * Node B was a  right  child  of A before the  rotation,
     * then A becomes the left child of B after the rotation.
     * @param A - root of the rotation
     * @pre: A has a right child
     */
    protected void rotateLeft( BSTNode<Entry<K,V>> A)
    {
        BSTNode<Entry<K,V>> B = A.right;
        A.setRight(B.left);
        if (B.left != null){
            B.left.setParent(A);
        }
        B.setParent(A.parent);
        if (A.parent == null){
            root = B;
        } else if (A == A.parent.left){
            A.parent.setLeft(B);
        } else {
            A.parent.setRight(B);
        }
        B.setLeft(A);
        A.setParent(B);
    }


    /**
     * Performs a single right rotation rooted at C node.
     * Node B was a  left  child  of C before the  rotation,
     * then C becomes the right child of B after the rotation.
     * @param C - root of the rotation
     * @pre: C has a left child
     */
    protected void rotateRight( BSTNode<Entry<K,V>> C)
    {
        BSTNode<Entry<K,V>> B = C.left;
        C.setLeft(B.right);

        if (B.right != null){
            B.right.setParent(C);
        }
        B.setParent(C.parent);
        if (C.parent == null){
            root = B;
        } else if (C == C.parent.left){
            C.parent.setLeft(B);
        } else {
            C.parent.setRight(B);
        }
        B.setRight(C);
        C.setParent(B);
    }

    /**
     * Performs a tri-node restructuring (a single or double rotation rooted at X node).
     * Assumes the nodes are in one of following configurations:
     *
     * @param X - root of the rotation
     * <pre>
     *          z=c       z=c        z=a         z=a
     *         /  \      /  \       /  \        /  \
     *       y=b  t4   y=a  t4    t1  y=c     t1  y=b
     *      /  \      /  \           /  \         /  \
     *    x=a  t3    t1 x=b        x=b  t4       t2 x=c
     *   /  \          /  \       /  \             /  \
     *  t1  t2        t2  t3     t2  t3           t3  t4
     * </pre>
     * @return the new root of the restructured subtree
     */
    protected BSTNode<Entry<K,V>> restructure(BSTNode<Entry<K,V>> X) {
        // the modification of a tree T caused by a trinode restructuring operation
        // can be implemented through case analysis either as a single rotation or as a double rotation.
        // The double rotation arises when position x has the middle of the three relevant keys
        // and is first rotated above its parent Y, and then above what was originally its grandparent Z.
        // In any of the cases, the trinode restructuring is completed with O(1)running time
        BSTNode<Entry<K,V>> Y = X.parent;
        BSTNode<Entry<K,V>> Z = Y.parent;

        if (Z.left.equals(Y)){
            if (Y.left.equals(X)){
                rotateRight(Z);
                return Y;
            } else {
                rotateLeft(Y);
                rotateRight(Z);
                return X;
            }
        } else {
            if (Y.right.equals(X)){
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

