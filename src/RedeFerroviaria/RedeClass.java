/**
 * Class Rede
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;
import Enums.Constants;
import Exceptions.*;
import dataStructures.*;


/**
 * Class Rede responsible to implement the methods prototyped in Rede interface
 */
public class RedeClass implements Rede{
    /**
     * Instance variables
     */
    private Dictionary<String, Station> stations;
    private Dictionary<String, Line> lines;


    static final long serialVersionUID = 0L;

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
    private boolean hasSchedule(String line, String startingStation, String hour) throws EmptyTreeException {
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
    private boolean orderCorrect(String line, List<Entry<String, String>> stations) throws EmptyTreeException {
        Line l = lines.find(line);
        return l.orderCorrect(stations);
    }

    @Override
    public void insertLine(String line) throws LineAlreadyExistsException, EmptyTreeException {
        if (hasLine(line)){
            throw new LineAlreadyExistsException();
        }
        Line l = new LineClass(line);
        lines.insert(line, l);
    }

    @Override
    public void addStationToLine(String line, List<String> station) throws EmptyTreeException {
        Iterator<String> ite = station.iterator();
        LineUpdatable l = (LineUpdatable) lines.find(line);
        if (l != null){
            while (ite.hasNext()){
                String s = ite.next();
                l.addStation(s);
            }
        }
    }

    @Override
    public void removeLine(String line) throws NoLinesException {
        if (!hasLine(line)){
            throw new NoLinesException();
        }
        Line l = lines.find(line);
        lines.remove(line);
    }

    @Override
    public boolean removeSchedule(String line, String startingStation, String hour) throws NoLinesException, NonExistantScheduleException, EmptyTreeException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!hasSchedule(line, startingStation, hour)){
            throw new NonExistantScheduleException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        return l.removeSchedule(startingStation, hour);
    }

    @Override
    public void insertSchedule(String line, int trainNr, List<Entry<String, String>> stations) throws NoLinesException, InvalidScheduleException, EmptyTreeException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!isStartingStation(line, stations.getFirst().getKey()) || !orderCorrect(line, stations)){
            throw new InvalidScheduleException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        l.addSchedule(trainNr, stations);
    }

    @Override
    public Iterator<Entry<String, Line>> stationLines(String station) throws StationDoesNotExistException {
        try {
            Station s = stations.find(station);
            return s.linesIterator();
        } catch (NoSuchElementException e){
            throw new StationDoesNotExistException();
        } catch (EmptyTreeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<Entry<Date, Train>> stationTrains(String station) throws StationDoesNotExistException {
        try{
            Station s = stations.find(station);
            return s.trainsIterator();
        } catch (NoSuchElementException e){
            throw new StationDoesNotExistException();
        } catch (EmptyTreeException e) {
            throw new RuntimeException(e);
        }
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
    public Iterator<Train> scheduleByLineIterator(String line, String startingStation) throws NoLinesException, NotStartingStationException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!isStartingStation(line, startingStation)){
            throw new NotStartingStationException();
        }
        Line l = lines.find(line);
        return l.trainsPerStationsIterator(startingStation);
    }

    @Override
    public Train bestTimeTable(String line, String startingStation, String endingStation, String hour) throws NoLinesException, NotStartingStationException, NotPossibleException, EmptyTreeException {
        if(!hasLine(line)){
            throw new NoLinesException();
        } else if (!isPossible(line, startingStation)){
            throw new NotStartingStationException();
        } else if (!isPossible(line, endingStation)){
            throw new NotPossibleException();
        }
        Line l = lines.find(line);
        return l.bestTimeTable(startingStation, endingStation, hour);
    }
}

/**
 * End of Rede Class
 */
