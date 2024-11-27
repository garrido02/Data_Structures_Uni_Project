package dataStructures;
import Exceptions.*;

import java.io.Serial;

/**
 * Separate Chaining Hash table implementation
 * @author AED  Team
 * @version 1.0
 * @param <K> Generic Key, must extend comparable
 * @param <V> Generic Value 
 */
@SuppressWarnings("unchecked")
public class SepChainHashTable<K extends Comparable<K>, V>
    extends HashTable<K,V>
{ 
	/**
	 * Serial Version UID of the Class.
	 */
    @Serial
    private static final long serialVersionUID = 0L;

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
        return Math.abs(key.hashCode()) % table.length;
    }

    @Override
    public V find( K key )
    {
        return table[ this.hash(key) ].find(key);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V insert( K key, V value ){
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
    public V remove( K key ) {
            V element = table[ this.hash(key) ].find(key);
            table[ this.hash(key) ].remove(key);
            currentSize--;
            return element;
    }

    @SuppressWarnings("unchecked")
    private void rehash() {
        int newSize = HashTable.nextPrime((int) (1.1 * maxSize));
        Dictionary<K,V>[] newTable = (Dictionary<K,V>[]) new Dictionary[newSize];
        for (int i = 0; i < newSize; i++) {
            newTable[i] = new OrderedDoubleList<K,V>();
        }
        Iterator<Entry<K,V>> elemsIt = iterator();
        while (elemsIt.hasNext()) {
            Entry<K,V> currEntry = elemsIt.next();
            int hashCode = Math.abs(currEntry.getKey().hashCode() % newSize);
            newTable[hashCode].insert(currEntry.getKey(), currEntry.getValue());
        }
        this.table = newTable;
        maxSize = newSize; // Replace old table with the new one
    }



    @Override
    public Iterator<Entry<K,V>> iterator()  {
        Iterator<Entry<K,V>> ite = new OpenHashTableIterator<>(this, currentSize);
        return ite;
    } 
}
































