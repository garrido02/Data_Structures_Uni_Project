/**
 * Interface EntryUpdatable
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package dataStructures;

/**
 * Interface EntryUpdatable responsible to prototype set methods to handle an Entry Object, extending the Entry interface and Serializable
 */
public interface EntryUpdatable<K extends Comparable<K>, V> extends Entry<K,V>{
    /**
     * Updates the entry key
     * @param key - The new key
     */
    void setKey(K key);

    /**
     * Updates the entry value
     * @param value - The new value
     */
    void setValue(V value);
}

/**
 * End of interface EntryUpdatable
 */
