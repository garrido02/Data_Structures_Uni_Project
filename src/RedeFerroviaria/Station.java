/**
 * Interface Station
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;

import dataStructures.*;
import java.io.Serializable;


/**
 * Interface Station responsible to prototype read methods to handle a Station Object, extending Serializable
 */
public interface Station extends Serializable, Comparable<Station> {
    /**
     * Returns the name of the station
     * @return the name of the station
     */
    String getName();

    /**
     * Returns an iterator of type Entry, representing the lines crossed by the station
     * @return an iterator of type Entry, representing the lines crossed by the station
     */
    Iterator<Entry<String, Void>> linesIterator();

    /**
     * Returns an iterator of type Entry, representing the trains that pass on the station
     * @return an iterator of type Entry, representing the trains that pass on the station
     */
    Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> trainsIterator();

    /**
     * Checks if a given train passes on the station
     * @param train - The train
     * @return true if the train passes on the station. Otherwise, false
     */
    boolean hasTrain(Train train);

    /**
     * Gets the departure date of a given train from the current station
     * @param train - The train
     * @return an Object of type Date representing the departure date of a given train from the current station
     */
    Date getTrainSchedule(Train train) ;

    /**
     * Checks if the station is obsolete, i.e, does not cross any lines
     * @return true if the station is obsolete. Otherwise, false
     */
    boolean isObsolete();
}

/**
 * End of Station Interface
 */