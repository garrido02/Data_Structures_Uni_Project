package dataStructures;

import Exceptions.NoSuchElementException;

/**
 * @author Francisco Oliveira nº67711 fmr.oliveira@campus.fct.unl.pt
 * @author João Ribeiro nº 68155 joc.ribeiro@campus.fct.unl.pt
 */
public class OpenHashTableIterator<K extends Comparable<K>,V> implements Iterator<Entry<K,V>>{

    private static final long serialVersionUID = 0L;

    protected Dictionary<K,V>[] table;
    private int size;
    private int current;
    private int returned;
    private Iterator<Entry<K,V>> currIt;

    public OpenHashTableIterator(SepChainHashTable<K,V> hashTable, int currentSize){
        this.table = hashTable.table;
        this.size = table.length;
        this.current = 0;
        this.returned = 0;
        this.currIt = table[0].iterator(); // Initialize currIt to avoid NullPointerException
        rewind();
    }

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
        findNext(); // Tenta encontrar o próximo dicionário não vazio
        //Previne que ele ignore o último iterador
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
