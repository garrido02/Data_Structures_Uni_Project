/**
 * Interface Rede
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */



package RedeFerroviaria;
import Exceptions.*;
import dataStructures.Entry;
import dataStructures.Iterator;
import dataStructures.List;
import java.io.Serializable;


/**
 * Interface Rede responsible to prototype methods to handle a Rede Object, extending Serializable
 */
public interface Rede extends Serializable {

    /**
     * Returns an iterator of Objects of type String corresponding to the stations of a given line
     * @param line - The name of the line
     * @return an iterator of Objects of type String corresponding to the stations of a given line. Otherwise, false
     */
    Iterator<String> iteratorByLine(String line) throws NoLinesException;

    /**
     * Returns an iterator of Objects of type Train corresponding to the trains of a given line that start a given station
     * @param line - The name of the line
     * @param startingStation - The name of the station
     * @return an iterator of Objects of type String corresponding to the stations of a given line
     */
    Iterator<Train> scheduleByLineIterator(String line, String startingStation) throws NoLinesException, NotStartingStationException;

    /**
     * Finds the train that best suits a given arrival time in a station of a given line
     * @param line - The name of the line
     * @param startingStation - The name of the departure station
     * @param endingStation - The name of the arrival station
     * @param hour - The expected hour of arrival
     * @return an Object of type Train corresponding to the train that best suits a given arrival time in a station of a given line
     */
    Train bestTimeTable(String line, String startingStation, String endingStation, String hour) throws NoLinesException, NotStartingStationException, NotPossibleException;

    /**
     * Insert new Object of type Line to the system
     * @param line - The name of a line
     * @throws LineAlreadyExistsException - The line already exists
     */
    void insertLine(String line) throws LineAlreadyExistsException;

    /**
     * Insert a station to a given line corresponding to an Object of type Line
     * @param line - The name of the line
     * @param aux - The list of stations to be added to a given Line
     */
    void addStationToLine(String line, List<String> aux);

    /**
     * Removes an Object of type Line from the system
     * @param line - The name of the line
     * @throws NoLinesException - The line does not exist in the system
     */
    void removeLine(String line) throws NoLinesException;

    /**
     * Removes a train schedule from a given line which departures at a given station at a given hour
     * @param line - The name of the line
     * @param startingStation - The name of the departure station
     * @param hour - The hour of departure
     * @return true if the removal was successful. Otherwise, false
     * @throws NoLinesException - The line does not exist in the system
     * @throws NonExistantScheduleException - The schedule does not exist in the system
     */
    boolean removeSchedule(String line, String startingStation, String hour) throws NoLinesException, NonExistantScheduleException;

    /**
     * Inserts a train schedule in a given line
     * @param line - The name of the line
     * @param trainNr - The number of the train to be added
     * @param stations - The list of stations on the route of the train
     * @throws NoLinesException - The line does not exist in the system
     * @throws InvalidScheduleException - The schedule does not exist in the system
     */
    void insertSchedule(String line, int trainNr, List<Entry<String, String>> stations) throws NoLinesException, InvalidScheduleException;

    Iterator<Line> stationLines(String station) throws StationDoesNotExistException;

    Iterator<Train> stationTrains(String station) throws StationDoesNotExistException;
}

/**
 * End of Rede Interface
 */
