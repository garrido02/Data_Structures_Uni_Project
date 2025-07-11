/**
 * Class Entry
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package dataStructures;


/**
 * Class Entry responsible to implement the methods of the Entry interface
 */
public class EntryClass<K extends Comparable<K>,V> implements Entry<K,V> {
    /**
     * Instance variables
     */
    private K key;
    private V value;

    /**
     * Constructor
     * @param key - The key
     * @param value - The value
     */
    public EntryClass(K key, V value){
        this.key = key;
        this.value = value;
    }


    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public int compareTo(Entry<K, V> obj) {
        return this.key.compareTo(obj.getKey());
    }
}

/**
 * End of Class Entry
 */
