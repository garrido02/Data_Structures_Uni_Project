package dataStructures;

/**
 * Separate Chaining Hash table implementation
 * @author AED  Team
 * @author of changes Francisco Correia 67264 & Sérgio Garrido 67202
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

    /**
     * Rehash method responsible for increasing the hash table size and reintroducing all the existing elements
     */
    @SuppressWarnings("unchecked")
    private void rehash() {
        int newSize = HashTable.nextPrime((2 * maxSize));
        Dictionary<K,V>[] newTable = (Dictionary<K,V>[]) new Dictionary[newSize];
        for (int i = 0; i < newSize; i++) {
            newTable[i] = new OrderedDoubleList<>();
        }
        Iterator<Entry<K,V>> ite = iterator();
        while (ite.hasNext()) {
            Entry<K,V> entry = ite.next();
            int hashCode = Math.abs(entry.getKey().hashCode() % newSize);
            newTable[hashCode].insert(entry.getKey(), entry.getValue());
        }
        this.table = newTable;
        maxSize = newSize;
    }

    @Override
    public Iterator<Entry<K,V>> iterator()  {
        Iterator<Entry<K,V>> ite = new OpenHashTableIterator<>(this);
        return ite;
    } 
}

/**
 * End of Class SepChainHashTable
 */
































