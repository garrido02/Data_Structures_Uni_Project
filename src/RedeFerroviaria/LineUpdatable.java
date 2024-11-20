/**
 * Interface LineUpdatable
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;
import Exceptions.*;
import dataStructures.*;


/**
 * Interface LineUpdatable responsible to prototype set methods to handle a Line Object, extending the Line interface and Serializable
 */
public interface LineUpdatable extends Line {
    /**
     * Adds a station to the line
     * @param station - The name of the station
     */
    void addStation(String station) throws EmptyTreeException;

    /**
     * Adds a train schedule to the line
     * @param trainNr - The number of the train to be added
     * @param stations - The list of stations on the route of the train
     */
    void addSchedule(int trainNr, List<Entry<String, String>> stations) throws EmptyTreeException;

    /**
     * Removes a train schedule from a given line which departures at a given station at a given hour
     * @param startingStation - The name of the departure station
     * @param hour - The hour of departure
     * @return true if the removal was successful. Otherwise, false
     */
    boolean removeSchedule(String startingStation, String hour) throws EmptyTreeException;
}

/**
 * End of LineUpdatable Interface
 */
