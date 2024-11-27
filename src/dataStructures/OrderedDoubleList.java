package dataStructures;
import Exceptions.*;

import java.io.Serial;


/**
 * Doubly linked list Implementation 
 * @author AED  Team
 * @version 1.0
 * @param <K,V> Generics - K extends Comparable
 * 
 */
public class OrderedDoubleList<K extends Comparable<K>, V> implements OrderedDictionary<K , V> {

    /**
	 * Serial Version UID of the Class
	 */
     @Serial
     private static final long serialVersionUID = 0L;
    
	/**
     *  Node at the head of the list.
     */
	protected DoubleListNode<Entry<K,V>> head;

    /**
     * Node at the tail of the list.
     */
	protected DoubleListNode<Entry<K,V>> tail;

    /**
     * Number of elements in the list.
     */
	protected int currentSize;
	
    /**
     * Constructor of an empty ordered double linked list.
     * head and tail are initialized as null.
     * currentSize is initialized as 0.
     */
	public OrderedDoubleList() {
		head=null;
		tail=null;
		currentSize=0;
	}

    /**
     * Inserts the Entry element before node after.
     * Precondition: after is not the head of the ordered double list.
     * @param element - Entry to be inserted
     * @param after - Node to be next to the new node  
     */
	protected void addBeforeNode(Entry<K,V> element, DoubleListNode<Entry<K,V>> after){
        DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<Entry<K,V>>(element,after.getPrevious(),after);
        if (after.getPrevious() != null ) {
            after.getPrevious().setNext(newNode);
        }

        after.setPrevious(newNode);

        if(after == head){
            head = newNode;
        }
        currentSize++;

	}
	
    /**
     * Inserts the Entry element at the first position in the list.
     * @param element - Entry to be inserted
     */
    protected void addFirst( Entry<K,V> element )
    {
        DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<>(element, null, head);
        if ( this.isEmpty() )
            tail = newNode;
        else
            head.setPrevious(newNode);
        head = newNode;
        currentSize++;
    }


    /**
     * Inserts the Entry element at the last position in the list.
     * @param element - Entry to be inserted
     */
    protected void addLast( Entry<K,V> element )
    {
        DoubleListNode<Entry<K,V>> newNode = new DoubleListNode<>(element,tail,null);
        if( this.isEmpty() )
            head = newNode;
        else
            tail.setNext(newNode);
        tail = newNode;
        currentSize++;
    }

	@Override
    public Entry<K, V> maxEntry() throws EmptyDictionaryException {
        if ( this.isEmpty())
            throw new EmptyDictionaryException();
        return tail.getElement();
	}

    @Override
	public Entry<K, V> minEntry() throws EmptyDictionaryException {
        if( this.isEmpty())
            throw new EmptyDictionaryException();
        return head.getElement();
	}

    /**
     * Returns the node with the Entry with key
     * in the list, if the list contains this entry.
     * Otherwise, returns null.
     * @param key - Key of type K to be searched
     * @return DoubleListNode<E> where the Entry with key was found, or the one with the key immediately after
     */
    protected DoubleListNode<Entry<K, V>> findNode(K key) {
        DoubleListNode<Entry<K, V>> currentNode = head;

        while (currentNode != null) {
            Entry<K, V> currentEntry = currentNode.getElement();

            if (currentEntry == null || currentEntry.getKey() == null) {
                currentNode = currentNode.getNext();
                continue; // Skip null entries safely
            }

            K currentKey = currentEntry.getKey();

            // Check for key match
            if (currentKey.equals(key)) {
                return currentNode;
            }

            // Compare keys to maintain ordered traversal
            int compare = currentKey.compareTo(key);
            if (compare > 0) {
                // If current key is greater, key is not in the list
                return null;
            }

            currentNode = currentNode.getNext();
        }

        // Key not found in the list
        return null;
    }

	
    @Override
	public V find(K key) {
		DoubleListNode<Entry<K,V>> node = findNode(key);

        if( node == null )
            return null;
        else
            return node.getElement().getValue();

	}


    public V insert(K key, V value) {
        DoubleListNode<Entry<K, V>> node = head;
        DoubleListNode<Entry<K, V>> previousNode = null;

        // Traverse to find the correct position
        while (node != null && node.getElement().getKey().compareTo(key) < 0) {
            previousNode = node;
            node = node.getNext();
        }

        // If key exists, update its value
        if (node != null && node.getElement().getKey().equals(key)) {
            V oldValue = node.getElement().getValue();
            node.getElement().setValue(value);
            return oldValue;
        }

        // Create a new entry
        Entry<K, V> newEntry = new EntryClass<>(key, value);
        DoubleListNode<Entry<K, V>> newNode = new DoubleListNode<>(newEntry);

        // Insert at the correct position
        if (previousNode == null) {
            // Insert at the head
            addFirst(newEntry);
        } else if (node == null) {
            // Insert at the tail
            addLast(newEntry);
        } else {
            // Insert before the current node
            addBeforeNode(newEntry, node);
        }

        return null; // New entry added
    }

	
	@Override
    public boolean isEmpty() {
	
		return currentSize==0;
	}


    @Override
	public Iterator<Entry<K, V>> iterator() {
		return new DoubleListIterator<>(head,tail);
	}

    /**
     * Removes the first node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeFirstNode( )
    {
        head = head.getNext();
        if ( head == null )
            tail = null;
        else
            head.setPrevious(null);
        currentSize--;
    }


    /**
     * Removes and returns the value at the first entry in the list.
     */
    protected V removeFirst( )  {
        if (this.isEmpty()) {
            return null;
        } else {
            V value = head.getElement().getValue();
            head = head.getNext();
            if (head != null) {
                head.setPrevious(null);
            } else {
                tail = null;
            }

            currentSize--;
            return value;

        }
    }


    /**
     * Removes the last node in the list.
     * Pre-condition: the list is not empty.
     */
    protected void removeLastNode( )
    {
        tail = tail.getPrevious();
        if( tail == null )
            head = null;
        else
            tail.setNext(null);
        currentSize--;
    }


    /**
     * Removes and returns the value at the last entry in the list.
     */
    protected V removeLast( )
    {
        if ( this.isEmpty() )
            return null;

        V value = tail.getElement().getValue();
        this.removeLastNode();
        return value;
    }

    /**
     * Removes the specified node from the list.
     * Pre-condition: the node is neither the head nor the tail of the list.
     *
     * @param node - middle node to be removed
     * @return
     */
    protected V removeMiddleNode(DoubleListNode<Entry<K,V>> node ) {
        DoubleListNode<Entry<K,V>> prevNode = node.getPrevious( );
        DoubleListNode<Entry<K,V>> nextNode = node.getNext( );
        prevNode.setNext( nextNode );
        nextNode.setPrevious( prevNode );
        currentSize--;
        return node.getElement().getValue();
    }

    @Override
    public V remove(K key)  {
		DoubleListNode<Entry<K,V>> node = findNode(key);
        if ( node == head )
            return this.removeFirst();
        else if ( node == tail )
            return this.removeLast();
        else
        {
            removeMiddleNode(node);
            return node.getElement().getValue();
        }
	}

    @Override
	public int size() {
		return currentSize;
	}
	
	
}
