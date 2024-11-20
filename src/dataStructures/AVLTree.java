package dataStructures;

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
    extends AdvancedBSTree<K,V> implements OrderedDictionary<K,V>
{                                                                   

    protected AVLTree(AVLNode<Entry<K,V>> node) {
        root = node;
    }

    public AVLTree() {
        this(null);
    }

    /**
     * Rebalance method called by insert and remove. Traverses the path from
     * zPos to the root. For each node encountered, we recompute its height
     * and perform a trinode restructuring if it's unbalanced.
     * the rebalance is completed with O(log n) running time
     */
    void rebalance(AVLNode<Entry<K,V>> zPos) {
        if(zPos.isInternal())
            zPos.setHeight();
        // Improve if possible...
        while (zPos!=null) {  // traverse up the tree towards the root
            zPos = (AVLNode<Entry<K, V>>) zPos.getParent();
            zPos.setHeight();
            if (!zPos.isBalanced()) {
                // perform a trinode restructuring at zPos's tallest grandchild
                //If yPos (zPos.tallerChild()) denote the child of zPos with greater height.
                //Finally, let xPos be the child of yPos with greater height
                AVLNode<Entry<K,V>> xPos = zPos.tallerChild().tallerChild();

                zPos = (AVLNode<Entry<K, V>>) restructure(xPos); // tri-node restructure (from parent class)
                ((AVLNode<Entry<K, V>>) zPos.getLeft()).setHeight();  // recompute heights
                ((AVLNode<Entry<K, V>>) zPos.getRight()).setHeight();
                zPos.setHeight();
            }
        }
    }


    @Override
    public V insert( K key, V value )
    {
        V valueToReturn = null;
        AVLNode<Entry<K,V>> newNode = (AVLNode<Entry<K,V>>) findNode(key);
        if (newNode == null){
            newNode = new AVLNode<>(new EntryClass<>(key, value));
            boolean found = false;
            AVLNode<Entry<K,V>> current = (AVLNode<Entry<K,V>>) root;
            while (!found){
                if (newNode.compareTo(current) < 0){
                    if (current.left == null){
                        current.setLeft(newNode);
                        newNode.setParent(current);
                        found = true;
                    } else {
                        current = (AVLNode<Entry<K, V>>) current.left;
                    }
                } else {
                    if (current.right == null){
                        current.setRight(newNode);
                        newNode.setParent(current);
                        found = true;
                    } else {
                        current = (AVLNode<Entry<K, V>>) current.right;
                    }
                }
            }
        }
        valueToReturn = newNode.element.getValue();
        rebalance(newNode);
        return valueToReturn;
    }

   @Override
    public V remove( K key )
     {
         V valueToReturn = null;
         AVLNode<Entry<K,V>> node = (AVLNode<Entry<K, V>>) findNode(key).parent;
         if (node != null){
             valueToReturn = findNode(key).element.getValue();
             remove(key);
             rebalance(node);
         }
         return valueToReturn;
    }
}
