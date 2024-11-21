/**
 * Interface Line
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */



package RedeFerroviaria;
import Exceptions.*;
import dataStructures.*;
import java.io.Serializable;


/**
 * Interface Line responsible to prototype read methods to handle a Line Object, extending Serializable
 */
public interface Line extends Serializable, Comparable<Line> {

    /**
     * Checks if a given station is a terminal station
     * @param station - The name of the station
     * @return true if it's a terminal station. Otherwise, false
     */
    boolean isTerminalStation(String station);

    /**
     * Checks if there is a schedule for a given station and hour
     * @param station - The name of the station
     * @param hour - The hour of the schedule
     * @return true if the station has a schedule in that hour. Otherwise, false
     */
    boolean hasSchedule(String station, String hour) throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException;

    /**
     * Checks if a given station is on the current line
     * @param station - The name of the station
     * @return true if the station is on the line. Otherwise, false
     */
    boolean hasStation(String station);

    /**
     * Checks if a given list of stations follows the order of the current line
     * @param stations - A list of stations to be added to the current line
     * @return true if the given list of stations follows the order of the current line. Otherwise, false
     */
    boolean orderCorrect(List<Entry<String, String>> stations) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException;

    /**
     * Returns an iterator of stations
     * @return An iterator of Objects of type String of all stations on the line
     */
    Iterator<Station> stationsIterator();

    /**
     * Returns an iterator of trains that start at a given station
     * @param startingStation - The station in which the train departs
     * @return An iterator of Objects of type Train that start at a given station
     */
    Iterator<Entry<Date, Train>> trainsPerStationsIterator(String startingStation) throws EmptyTreeException, FullStackException;

    /**
     * Return the train that best fits the hour in which we want to arrive at a given station
     * @param startingStation - The station in which we want to depart
     * @param endingStation - The station in which we want to arrive
     * @param hour - The hour in which we want to arrive
     * @return An Object of type Train that best fits the hour in which we want to arrive at a given station
     */
    Train bestTimeTable(String startingStation, String endingStation, String hour) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException;

    /**
     * Returns the name of the line
     * @return The name of the line
     */
    String getName();
}


/**
 * End of Line Interface
 */
