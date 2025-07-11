package dataStructures;
import Exceptions.*;

/**
 * BinarySearchTree implementation
 * @author AED team
 * @author of changes Francisco Correia 67264 & Sérgio Garrido 67202
 * @version 1.0
 * @param <K> Generic type Key, must extend comparable
 * @param <V> Generic type Value 
 */
public class BinarySearchTree<K extends Comparable<K>, V> 
    implements OrderedDictionary<K,V>
{
    static final long serialVersionUID = 0L;

    /**
     * The root of the tree.                                            
     * 
     */
    BSTNode<Entry<K,V>> root;

    /**
     * Number of entries in the tree.                                  
     * 
     */
    protected int currentSize;                   

    /**
     * Tree Constructor - creates an empty tree.
     */
    public BinarySearchTree( ) {
        root = null;
        currentSize = 0;
    }

    @Override
    public boolean isEmpty( )                               
    {    
        return root == null;
    }

    @Override
    public int size( )                                      
    {    
        return currentSize;
    }

    @Override
    public V find( K key ) {
        BSTNode<Entry<K,V>> node = this.findNode(key);
        if ( node == null || node.getElement().getKey().compareTo(key) != 0 )
            return null;                                    
        else                                                     
            return node.getElement().getValue();
    }

    /*
    **
     * Returns the node whose key is the specified key;
     * or null if no such node exists.        
     *                         
     * @param node where the search starts 
     * @param key to be found
     * @return the found node, when the search is successful
     *
     */
    BSTNode<Entry<K,V>> findNode( BSTNode<Entry<K,V>> node, K key ) {
        if ( node == null )
            return null;
        else
        {
            int compResult = key.compareTo( node.getElement().getKey() );
            if ( compResult == 0 )
                return node;                                         
            else if ( compResult < 0 )
                return this.findNode(node.getLeft(), key);
            else                                                     
                return this.findNode(node.getRight(), key); 
        }                 
    }



    @Override
    public Entry<K,V> minEntry( ) throws EmptyDictionaryException {
        if ( this.isEmpty() )                              
            throw new EmptyDictionaryException();           

        return this.minNode(root).getElement();
    }


    /**
     * Returns the node with the smallest key 
     * in the tree rooted at the specified node.
     * Requires: node != null.
     * @param node - node that roots the tree
     * @return node with the smallest key in the tree
     */
    BSTNode<Entry<K,V>> minNode( BSTNode<Entry<K,V>> node ) {
        while ( node.getLeft() != null )
        {
            node = node.getLeft();
        }
        return node;

    }                               


    @Override
    public Entry<K,V> maxEntry( ) throws EmptyDictionaryException {
        if ( this.isEmpty() )                              
            throw new EmptyDictionaryException();           

        return this.maxNode(root).getElement();
    }


    /**
     * Returns the node with the largest key 
     * in the tree rooted at the specified node.
     * Requires: node != null.
     * @param node that roots the tree
     * @return node with the largest key in the tree
     */
    BSTNode<Entry<K,V>> maxNode( BSTNode<Entry<K,V>> node ) {
        if ( node.getRight() == null )                            
            return node;                                             
        else                                                     
            return this.maxNode( node.getRight() );                       
    }                               


    /**
     * Returns the node whose key is the specified key;
     * or the parent of the node where the key should exist if no such node exists.
     * @param key to be searched
     * @return see above
     
     */
    BSTNode<Entry<K,V>> findNode( K key) {
        BSTNode<Entry<K,V>> node = root;

        while ( node != null )
        {
            int compResult = key.compareTo( node.getElement().getKey() );
            if ( compResult == 0 )
                return node;
            else if ( compResult < 0 ) {

                node = node.getLeft();
            }
            else {

                node = node.getRight();
            }
        }
        return node;
    }                               

    @Override
    public V insert( K key, V value ) {
        BSTNode<Entry<K,V>> node = this.findNode(key);

        if ( node == null || node.getElement().getKey().compareTo(key) != 0 ) {
            BSTNode<Entry<K,V>> newLeaf = new BSTNode<>(new EntryClass<>(key, value));
            this.linkSubtree(newLeaf, node);
            currentSize++;
            return null;   
        } else {
            V oldValue = node.getElement().getValue();
            node.setElement(new EntryClass<>(key, value));
            return oldValue;
        }
    }

    /**
     * Links a new subtree, rooted at the specified node, to the tree.
     *
     * @param node - root of the subtree
     * @param parent - parent node for the new subtree
     */
    void linkSubtree( BSTNode<Entry<K,V>> node, BSTNode<Entry<K,V>> parent ) {
        if ( parent == null ) {
            root = node;
        } else {
            node.setParent(parent);
            if (parent.getElement().getKey().compareTo(node.getElement().getKey()) >= 0) {
                parent.setLeft(node);
            } else {
                parent.setRight(node);
            }
        }
    }

    @Override
    public V remove( K key ) {
        BSTNode<Entry<K,V>> node = this.findNode(key);
        if ( node == null || node.getElement().getKey().compareTo(key) != 0 )
            return null;
        else
        {
            V oldValue = node.getElement().getValue();
            if ( node.getLeft() == null ) {
                this.linkSubtree(node.getRight(), node.getParent());
            } else if ( node.getRight() == null ) {
                this.linkSubtree(node.getLeft(), node.getParent());
             } else {
                BSTNode<Entry<K,V>> minNode = this.minNode(node.getRight());
                node.setElement( minNode.getElement() );
                this.linkSubtree(minNode.getRight(), minNode.getParent());
            }
            currentSize--;
            return oldValue;
        }                                 
    }                                

    /**
     * Returns an iterator of the entries in the dictionary 
     * which preserves the key order relation.
     * @return  key-order iterator of the entries in the dictionary
     */
    public Iterator<Entry<K,V>> iterator( ){
        if (root == null){
            return null;
        }
        return new BSTKeyOrderIterator<>(root);
    }
}

/**
 * End of class BinarySearchTree
 */

