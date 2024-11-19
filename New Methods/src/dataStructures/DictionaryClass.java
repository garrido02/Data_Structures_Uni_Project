/**
 * Class Date
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package dataStructures;


/**
 * Class Dictionary responsible to implement the methods of the Dictionary interface
 */
public class DictionaryClass<K extends Comparable<K>,V> implements Dictionary<K,V> {
    /**
     * Constant variables
     */
    private static int ZERO = 0;

    /**
     * Instance Variables
     */
    private int currentSize;
    private List<Entry<K,V>> list;

    static final long serialVersionUID = 0L;

    /**
     * Constructor
     */
    public DictionaryClass(){
        currentSize = ZERO;
        list = new DoubleList<>();
    }

    @Override
    public boolean isEmpty() {
        return currentSize == ZERO;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V find(K key) {
        Entry<K,V> entry = createDummyEntry(key);
        int idx = list.find(entry);
        if (idx != -1){
            return list.get(idx).getValue();
        } else {
            return null;
        }
    }

    @Override
    public V insert(K key, V value) {
        Entry<K,V> entry = createEntry(key, value);
        list.addLast(entry);
        currentSize++;
        return value;
    }

    @Override
    public V insertAtPos(int position, K key, V value) {
        Entry<K,V> entry = createEntry(key, value);
        list.add(position, entry);
        currentSize++;
        return value;
    }


    @Override
    public V remove(K key) {
        Entry<K,V> entry = createDummyEntry(key);
        int idx = list.find(entry);
        V value = find(key);
        list.remove(idx);
        currentSize--;
        return value;
    }

    /**
     * Creates a dummy Entry Object with a given key for comparison purposes
     * @param key - The key to
     * @return An Object of Type Entry to be used for comparison purposes
     */
    private Entry<K,V> createDummyEntry(K key){
        return new EntryClass<>(key, null);
    }

     /**
     * Creates an Entry Object with a given key and value for
     * @param key - The key
     * @param value - The value
     * @return An Object of Type Entry
     */
    private Entry<K,V> createEntry(K key, V value){
        return new EntryClass<>(key, value);
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return list.iterator();
    }
}


/**
 * End of Class Dictionary
 */
