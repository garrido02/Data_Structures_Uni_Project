package dataStructures;
import Exceptions.*;

/**
 * Separate Chaining Hash table implementation
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key, must extend comparable
 * @param <V> Generic Value 
 */

public class SepChainHashTable<K extends Comparable<K>, V>
    extends HashTable<K,V>
{ 
	/**
	 * Serial Version UID of the Class.
	 */
    static final long serialVersionUID = 0L;

	/**
	 * The array of dictionaries.
	 */
    protected Dictionary<K,V>[] table;


    /**
     * Constructor of an empty separate chaining hash table,
     * with the specified initial capacity.
     * Each position of the array is initialized to a new ordered list
     * maxSize is initialized to the capacity.
     * @param capacity defines the table capacity.
     */
    @SuppressWarnings("unchecked")
    public SepChainHashTable( int capacity )
    {
        int arraySize = HashTable.nextPrime((int) (1.1 * capacity));
        // Compiler gives a warning.
        table = (Dictionary<K,V>[]) new Dictionary[arraySize];
        for ( int i = 0; i < arraySize; i++ )
            table[i] = new OrderedDoubleList<>();
        maxSize = capacity;
        currentSize = 0;
    }                                      


    public SepChainHashTable()
    {
        this(DEFAULT_CAPACITY);
    }                                                                

    /**
     * Returns the hash value of the specified key.
     * @param key to be encoded
     * @return hash value of the specified key
     */
    protected int hash( K key )
    {
        return Math.abs( key.hashCode() ) % table.length;
    }

    @Override
    public V find( K key )
    {
        return table[ this.hash(key) ].find(key);
    }

    @Override
    public V insert( K key, V value ) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        if ( this.isFull() ){
            this.rehash();
        }

        V element = table[ this.hash(key) ].find(key);
        if ( element == null ){
            currentSize++;
        }
        table[ this.hash(key) ].insert(key, value);
        return element;
    }

    @Override
    public V remove( K key )
    {
        V element = table[ this.hash(key) ].find(key);
        table[ this.hash(key) ].remove(key);
        currentSize--;
        return element;
    }

    @SuppressWarnings("unchecked")
    private void rehash() throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        // Calculate new capacity: typically, we double the size and find the next prime.
        int newCapacity = HashTable.nextPrime((table.length * 2));

        // Create a new table with the updated capacity
        Dictionary<K, V>[] newTable = (Dictionary<K, V>[]) new Dictionary[newCapacity];

        // Initialize each position in the new table to a new ordered list
        for (int i = 0; i < newCapacity; i++) {
            newTable[i] = new OrderedDoubleList<>();
        }

        // Reinsert elements from the old table to the new table
        Iterator<Entry<K,V>> ite = this.iterator();
        while(ite.hasNext()){
            Entry<K,V> entry = ite.next();

            // Compute new hash for each entry
            K key = entry.getKey();
            V value = entry.getValue();
            int newIndex = Math.abs(key.hashCode()) % newCapacity;

            // Insert entry in the new table
            newTable[newIndex].insert(key, value);
        }

        // Update the table reference and maxSize to the new table and capacity
        this.table = newTable;
        this.maxSize = newCapacity;
    }

    @Override
    public Iterator<Entry<K,V>> iterator( ) throws EmptyTreeException {
        Iterator<Entry<K,V>> ite = new OpenHashTableIterator<>(this, currentSize);
        return ite;
    } 
}
































