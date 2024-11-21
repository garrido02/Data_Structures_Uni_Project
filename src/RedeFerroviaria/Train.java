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
public interface Train extends Comparable<Train>, Serializable {
    /**
     * Gets the number of the current train
     * @return The number of the current train
     */
    int getNr();

    /**
     * Gets the departure time of the current train
     * @return An Object of type Date representing the departure time of the current train
     */
    Date getDepartureTime();

    /**
     * Checks if the current train stops in a given station
     * @param startingStation - The name of the station
     * @return true if the station is on the train's path. Otherwise, false
     */

    boolean isStartingStation(String startingStation);
    Iterator<Entry<String, Date>> scheduleIterator();
}

/**
 * End of Train Interface
 */
