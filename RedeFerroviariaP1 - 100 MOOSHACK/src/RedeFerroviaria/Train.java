/**
 * Interface Train
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */



package RedeFerroviaria;
import Exceptions.NoTrainsException;
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
     * Checks if there is a schedule for a given station and hour
     * @param startingStation - The name of the station
     * @param time - The time of the schedule
     * @return true if the station has a schedule in that hour. Otherwise, false
     */
    boolean hasSchedule(String startingStation, Date time);

    /**
     * Returns an iterator of entries of the schedule of the current train
     * @return An iterator of Objects of type Entry representing the schedule of the current train
     */
    Iterator<Entry<String, Date>> scheduleIterator() throws NoTrainsException;

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
    boolean hasStation(String startingStation);

    /**
     * Checks the leeway between the arrival time of a train and the expected arrival time of the user to a given station
     * @param arrivalTime - The expected arrival time of the user
     * @param endingStation - The name of the station
     * @return The difference between the expected arrival time of the user to a given station and arrival time a train to a given station
     */
    int leeway(Date arrivalTime, String endingStation);
}

/**
 * End of Train Interface
 */
