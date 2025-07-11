/**
 * Interface StationUpdatable
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;

import dataStructures.OrderedDictionary;

/**
 * Interface StationUpdatable responsible to prototype set methods to handle a Station Object, extending the Station interface and Serializable
 */
public interface StationUpdatable extends Station {
    /**
     * Inserts a line with a given name
     * @param lineName - The name of the line
     */
    void insertLine(String lineNameLowerCase, String lineName);

    /**
     * Adds a schedule to the current station
     * @param train - The train that departures from the station
     * @param departure - The date of departure of said train
     */
    void addSchedule(Train train, Date departure);

    /**
     * Removes a schedule from the given station
     * @param departure - The date of departure of a train
     * @param train - The train that departures from the station at said date
     */
    void removeSchedule(Date departure, Train train);

    /**
     * Removes a line from the "list" of crossed lines from the current station
     * @param name - The name of the line
     */
    void removeLine(String name);

    /**
     * Removes the trains of the removed line in case the station in question is not obsolete
     * @param departureTerminal1 - Trains of terminal 1
     * @param departureTerminal2 - Trains of terminal 2
     */
    void removeTrains(OrderedDictionary<Date, Train> departureTerminal1, OrderedDictionary<Date, Train> departureTerminal2);
}

/**
 * End of StationUpdatable Interface
 */

