/**
 * Class OpenHashTableIterator
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package dataStructures;
import Exceptions.NoSuchElementException;


/**
 * Class OpenHashTableIterator responsible to implement the methods of the Iterator interface to a HashTable
 */
public class OpenHashTableIterator<K extends Comparable<K>,V> implements Iterator<Entry<K,V>>{

    private static final long serialVersionUID = 0L;

    /**
     * Instance Variables
     */
    private Dictionary<K,V>[] table;
    private int size;
    private int current;
    private int returned;
    private Iterator<Entry<K,V>> currIt;


    /**
     * Constructor
     * @param hashTable - The hash table to be iterated
     */
    public OpenHashTableIterator(SepChainHashTable<K,V> hashTable){
        this.table = hashTable.table;
        this.size = table.length;
        this.current = 0;
        this.returned = 0;
        this.currIt = table[0].iterator();
        rewind();
    }

    /**
     * Finds the next element of the hash table to be iterated, skipping empty buckets
     */
    private void findNext() {
        while (current < table.length && table[current].isEmpty()){
            current++;
        }
        if (current < table.length)
            currIt = table[current++].iterator();
    }


    @Override
    public boolean hasNext() {
        if (currIt != null && currIt.hasNext()) {
            return true;
        }
        returned++;
        findNext();
        return ((current < table.length && returned < size) || (currIt != null && currIt.hasNext()));
    }

    @Override
    public Entry<K, V> next() throws NoSuchElementException {
        if (size == 0)
            throw new NoSuchElementException();
        return currIt.next();
    }

    @Override
    public void rewind() {
        current = 0;
        returned = 0;
        findNext();
    }
}

/**
 * End of class OpenHashTableIterator
 */
