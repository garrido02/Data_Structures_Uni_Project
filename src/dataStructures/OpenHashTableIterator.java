package dataStructures;
import Exceptions.*;

public class OpenHashTableIterator<K extends Comparable<K>,V> implements Iterator<Entry<K,V>> {
    private int size;
    private int current;
    private int returned;
    protected Dictionary<K, V>[] table;
    private Iterator<Entry<K,V>> iterator;

    static final long serialVersionUID = 0L;

    public OpenHashTableIterator(SepChainHashTable<K, V> hashTable, int size) throws EmptyTreeException {
        this.table = hashTable.table;
        this.size = size;
        this.current = 0;
        this.returned = 0;
        findNext();
    }

    private void findNext() throws EmptyTreeException {
        while (table[current].isEmpty() && current < size) {
            current++;
        }
        iterator = table[current].iterator();
    }


    @Override
    public boolean hasNext() {
        return returned < size;
    }

    @Override
    public Entry<K,V> next() throws NoSuchElementException, EmptyTreeException {
        if (size == 0){
            throw new NoSuchElementException();
        }
        if(iterator.hasNext()){
            return iterator.next();
        }
        returned++;
        findNext();
        return iterator.next();
    }

    @Override
    public void rewind() {
        current = 0;
        returned = 0;
    }
}
