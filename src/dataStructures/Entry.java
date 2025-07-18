package dataStructures;

import java.io.Serializable;

/**
 * Entry Abstract Data Type 
 * Includes description of general methods to be implemented by an entry.
 * @author AED  Team
 * @author of changes Francisco Correia 67264 & Sérgio Garrido 67202
 * @version 1.0
 * @param <K> Generic Key
 * @param <V> Generic Value  
 */

public interface Entry<K extends Comparable<K>,V> extends Serializable, Comparable<Entry<K, V>> {
	/**
	 * Returns the key in the entry.
	 * @return key in the entry
	 */
    K getKey( );

	/**
	 * Returns the value in the entry.
	 * @return value in the entry
	 */
    V getValue( );


}

/**
 * End of Entry
 */
