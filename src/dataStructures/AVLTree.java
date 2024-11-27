package dataStructures;

import java.io.Serial;

/**
 * AVL tree implementation
 * 
 * @author AED team
 * @version 1.0
 *
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public class AVLTree<K extends Comparable<K>, V> 
    extends AdvancedBSTree<K,V> implements OrderedDictionary<K,V> {

    @Serial
    private static final long serialVersionUID = 0L;


    protected AVLTree(AVLNode<Entry<K, V>> node) {
        root = node;
    }

    public AVLTree() {
        this(null);
    }

    /**
     * Rebalance method called by insert and remove.  Traverses the path from
     * zPos to the root. For each node encountered, we recompute its height
     * and perform a trinode restructuring if it's unbalanced.
     * the rebalance is completed with O(log n) running time
     */
    void rebalance(AVLNode<Entry<K, V>> zPos) {
        if (zPos.isInternal())
            zPos.setHeight();
        // Improve if possible...1
        while (zPos!=root) {  // traverse up the tree towards the root
            zPos = (AVLNode<Entry<K, V>>) zPos.getParent();
            zPos.setHeight();
            if (!zPos.isBalanced()) {
                // perform a trinode restructuring at zPos's tallest grandchild
                //If yPos (zPos.tallerChild()) denote the child of zPos with greater height.
                //Finally, let xPos be the child of yPos with greater height
                AVLNode<Entry<K, V>> xPos = zPos.tallerChild().tallerChild();
                if( xPos != null ){
                    zPos = (AVLNode<Entry<K, V>>) restructure(xPos); // tri-node restructure (from parent class)
                    ((AVLNode<Entry<K, V>>) zPos.getLeft()).setHeight();  // recompute heights
                    ((AVLNode<Entry<K, V>>) zPos.getRight()).setHeight();
                    zPos.setHeight();
                }
            }
        }
    }

    //TODO ( feito )
    @Override
    public V insert(K key, V value) {

        V valueToReturn = null;
        AVLNode<Entry<K, V>> newNode = (AVLNode<Entry<K, V>>) findNode(key); // node where the new entry is being inserted (if find(key)==null)
        // insert the new Entry (if find(key)==null)
        AVLNode<Entry<K, V>> current = (AVLNode<Entry<K, V>>) this.root;
        if (newNode == null) {
            newNode = new AVLNode<>(new EntryClass<>(key, value));
            if(current == null){
                linkSubtree(newNode,null);          // define o novo no como root
            }
            boolean found = false;
            while (!found && current != null) {
                if (newNode.compareTo(current) < 0) {
                    if (current.left == null) {
                        current.setLeft(newNode);
                        newNode.setParent(current);
                        found = true;
                    } else {
                        current = (AVLNode<Entry<K, V>>) current.left;
                    }
                } else {
                    if (current.right == null) {
                        current.setRight(newNode);
                        newNode.setParent(current);
                        found = true;
                    } else {
                        current = (AVLNode<Entry<K, V>>) current.right;
                    }
                }
            }
            valueToReturn = newNode.element.getValue();
            currentSize++;
            rebalance(newNode);

        }
        return valueToReturn;
    }

    @Override
    public V remove(K key) {
        AVLNode<Entry<K, V>> node = (AVLNode<Entry<K, V>>) findNode(key);
        if (node == null) {
            return null;
        }
        V valueToReturn = node.element.getValue();


        // Case 1: Node is a leaf
        if (node.left == null && node.right == null) {
            replaceNode(node, null);
        }
        // Case 2: Node has only a right child
        else if (node.left == null) {
            replaceNode(node, (AVLNode<Entry<K, V>>) node.right);
        }
        // Case 3: Node has only a left child
        else if (node.right == null) {
            replaceNode(node, (AVLNode<Entry<K, V>>) node.left);
        }
        // Case 4: Node has two children
        else {
            AVLNode<Entry<K, V>> successor = (AVLNode<Entry<K, V>>) minNode(node.right);
            node.element = successor.element; // Replace node's value with successor's value
            replaceNode(successor, (AVLNode<Entry<K, V>>) successor.right); // Remove the successor
        }

        currentSize--; // Update the size of the tree
        if(node.getParent() != null){
            rebalance((AVLNode<Entry<K, V>>) node.getParent()); // Rebalance from the parent of the removed/replaced node
        }
        return valueToReturn;
    }

    /**
     * Replace a node with a new child in the tree.
     */
    private void replaceNode(AVLNode<Entry<K, V>> node, AVLNode<Entry<K, V>> child) {
        if (node.getParent() == null) {
            root = child; // If node is the root, update the root
        } else if (node == node.getParent().left) {
            node.getParent().setLeft(child); // Update parent's left child
        } else {
            node.getParent().setRight(child); // Update parent's right child
        }

        if (child != null) {
            child.setParent(node.getParent()); // Update child's parent
        }
    }

}

