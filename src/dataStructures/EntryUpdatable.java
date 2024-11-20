package dataStructures;

public interface EntryUpdatable<K extends Comparable<K>,V> extends Entry<K,V>{
    void setKey(K key);
    void setValue(V value);
}
