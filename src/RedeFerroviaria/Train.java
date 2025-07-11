/**
 * Interface Train
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;
import dataStructures.Entry;
import dataStructures.Iterator;

import java.io.Serializable;


/**
 * Interface Train responsible to prototype methods to handle a Train Object, extending Serializable & Comparable
 */
public interface Train extends Serializable, Comparable<Train> {
    /**
     * Gets the number of the current train
     * @return The number of the current train
     */
    int getNr();

    /**
     * Returns an iterator of Object of type Entry, representing the schedule of a train
     * @return an iterator of Object of type Entry, representing the schedule of a train
     */
    Iterator<Entry<String, Date>> scheduleIterator();
}

/**
 * End of Train Interface
 */
