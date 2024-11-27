/**
 * Class Rede
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;
import Enums.*;
import Exceptions.*;
import dataStructures.*;

import java.io.Serial;


/**
 * Class Rede responsible to implement the methods prototyped in Rede interface
 */
public class RedeClass implements Rede{
    /**
     * Instance variables
     */
    // Map of stations. K - Station name; V - Station object
    private Dictionary<String, Station> stations;
    // Map of lines. K - Line name; V - Line object
    private Dictionary<String, Line> lines;


    @Serial
    private static final long serialVersionUID = 0L;

    /**
     * Constructor
     */
    public RedeClass(){
        stations = new SepChainHashTable<>(Constants.DEFAULT_STATIONS.getValue());
        lines = new SepChainHashTable<>(Constants.DEFAULT_LINES.getValue());
    }

    /**
     * Checks if a given line is on the system
     * @param line - The name of the line
     * @return true if the line is on the system. Otherwise, false
     */
    private boolean hasLine(String line) {
        return lines.find(line) != null;
    }

    /**
     * Checks if a given line has a train that stops at a given station at a given hour
     * @param line - The name of the line
     * @param startingStation - The name of the station
     * @param hour - The hour of the schedule
     * @return true if a given line has a train that stops at a given station at a given hour. Otherwise, false
     */
    private boolean hasSchedule(String line, String startingStation, String hour)  {
        Line l = lines.find(line);
        return l.hasSchedule(startingStation, hour);
    }

    /**
     * Checks if a given station is a terminal station of a given line
     * @param line - The name of the line
     * @param startingStation - The name of the station
     * @return true if a given station is a terminal station of a given line. Otherwise, false
     */
    private boolean isStartingStation(String line, String startingStation) {
        Line l = lines.find(line);
        return l.isTerminalStation(startingStation);
    }

    /**
     * Checks if a train in a given line stops at a given station
     * @param line - The name of the line
     * @param endingStation - The name of the station
     * @return true if a train in a given line stops at a given station. Otherwise, false
     */
    private boolean isPossible(String line, String endingStation) {
        Line l = lines.find(line);
        return l.hasStation(endingStation);
    }

    /**
     * Checks if the order of stations of a given list corresponds to the order of the stations of a given line
     * @param line - The name of the line
     * @param stations - The list of stations
     * @return true if the order of stations of a given list corresponds to the order of the stations of a given line. Otherwise, false
     */
    private boolean orderCorrect(String line, List<Entry<String, String>> stations) {
        Line l = lines.find(line);
        return l.orderCorrect(stations);
    }

    @Override
    public void insertLine(String line) throws LineAlreadyExistsException {
        if (hasLine(line.toLowerCase())){
            throw new LineAlreadyExistsException();
        }
        Line l = new LineClass(line);
        lines.insert(line.toLowerCase(), l);
    }

    @Override
    public void addStationToLine(String line, List<String> station)  {
        Iterator<String> ite = station.iterator();
        LineUpdatable l = (LineUpdatable) lines.find(line);
        if (l != null){
            while (ite.hasNext()){
                String stationName = ite.next();
                Station s = stations.find(stationName.toLowerCase());
                if (s == null){
                    s = new StationClass(stationName);
                    stations.insert(stationName.toLowerCase(), s);
                }
                l.addStation(s);
            }
        }
    }

    @Override
    public void removeLine(String line) throws NoLinesException {
        if (!hasLine(line)){
            throw new NoLinesException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        List<Station> stationsToRemove = l.removeLineFromStations();
        try {
            if (!stationsToRemove.isEmpty()){
                removeObsoleteStations(stationsToRemove);
            }
            lines.remove(line);
        } catch (EmptyDictionaryException ignore){}
    }

    /**
     * Removes obsolete stations after a line removal
     * @param obsoleteStations - The list of stations to be removed
     * @throws EmptyDictionaryException - No more entries to be removed
     */
    private void removeObsoleteStations(List<Station> obsoleteStations) throws EmptyDictionaryException {
        Iterator<Station> ite = obsoleteStations.iterator();
        while (ite.hasNext()){
            stations.remove(ite.next().getName().toLowerCase());
        }
    }

    @Override
    public void removeSchedule(String line, String startingStation, String hour) throws NoLinesException, NonExistantScheduleException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!hasSchedule(line, startingStation, hour)){
            throw new NonExistantScheduleException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        l.removeSchedule(startingStation, hour);
    }

    @Override
    public void insertSchedule(String line, int trainNr, List<Entry<String, String>> stations) throws NoLinesException, InvalidScheduleException{
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!isStartingStation(line, stations.getFirst().getKey().toLowerCase()) || !orderCorrect(line, stations)){
            throw new InvalidScheduleException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        l.addSchedule(trainNr, stations);
    }

    @Override
    public Iterator<Entry<String, Void>> stationLines(String station) throws StationDoesNotExistException {
            Station s = stations.find(station);
            if (s == null){
                throw new StationDoesNotExistException();
            }
            return s.linesIterator();
    }

    @Override
    public Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> stationTrains(String station) throws StationDoesNotExistException {
            Station s = stations.find(station);
            if (s == null) {
                throw new StationDoesNotExistException();
            }
            return s.trainsIterator();
    }

        @Override
    public Iterator<Station> iteratorByLine(String line) throws NoLinesException {
        Line l = lines.find(line);
        if (l == null){
            throw new NoLinesException();
        }
        return l.stationsIterator();
    }

    @Override
    public Iterator<Entry<Date, Train>> scheduleByLineIterator(String line, String startingStation) throws NoLinesException, NotStartingStationException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!isStartingStation(line, startingStation)){
            throw new NotStartingStationException();
        }
        Line l = lines.find(line);
        return l.trainsPerStationsIterator(startingStation);
    }

    @Override
    public Train bestTimeTable(String line, String startingStation, String endingStation, String hour) throws NoLinesException, NotStartingStationException, NotPossibleException {
        if(!hasLine(line)){
            throw new NoLinesException();
        } else if (!isPossible(line, startingStation)){
            throw new NotStartingStationException();
        } else if (!isPossible(line, endingStation)){
            throw new NotPossibleException();
        }
        Line l = lines.find(line);

        Train t = l.bestTimeTable(startingStation, endingStation, hour);
        if (t == null){
            throw new NotPossibleException();
        }
        return t;
    }
}

/**
 * End of Rede Class
 */
