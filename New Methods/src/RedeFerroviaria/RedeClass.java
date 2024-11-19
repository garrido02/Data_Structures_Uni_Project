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
     * Constant variables
     */
    private static int NOT_FOUND = Constants.NOT_FOUND.getValue();
    private static int ZERO = Constants.ZERO.getValue();
    private static int DEFAULT_CAPACITY = Constants.DEFAULT_CAPACITY.getValue();

    /**
     * Instance variables
     */
    private Dictionary<String, Line> lines;
    private String[] orderedLines;
    private int size;


    static final long serialVersionUID = 0L;

    /**
     * Constructor
     */
    public RedeClass(){
        lines = new DictionaryClass<>();
        orderedLines = new String[DEFAULT_CAPACITY];
        size = ZERO;
    }

    /**
     * Checks if the class array is full
     * @return true if is full. Otherwise, false
     */
    private boolean isFull(){
       return orderedLines.length == size;
    }


    /**
     * Checks if a given line is on the system
     * @param line - The name of the line
     * @return true if the line is on the system. Otherwise, false
     */
    private boolean hasLine(String line) {
        return SortAndSearch.binarySearch(orderedLines, line, size) != NOT_FOUND;
    }


    /**
     * Checks if a given line has a train that stops at a given station at a given hour
     * @param line - The name of the line
     * @param startingStation - The name of the station
     * @param hour - The hour of the schedule
     * @return true if a given line has a train that stops at a given station at a given hour. Otherwise, false
     */
    private boolean hasSchedule(String line, String startingStation, String hour) {
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
        if (hasLine(line)){
            throw new LineAlreadyExistsException();
        }
        Line l = new LineClass(line);
        int idx = findNewLinePosition(line);
        lines.insertAtPos(idx, line, l);
        insertOrdered(idx, line);
    }

    /**
     * Insert in order a given line
     * @param idx - The index in which to add the line
     * @param line - The name of the line to add
     */
    private void insertOrdered(int idx, String line){
        if (isFull()){
            orderedLines = SortAndSearch.grow(orderedLines);
        }
        SortAndSearch.insertSorted(orderedLines, size, idx, line);
        size++;
    }

    /**
     * Find the new position of the line to be added
     * @param line - The name of the line to be added
     * @return the index in which the new line should be added
     */
    private int findNewLinePosition(String line){
        int idx = NOT_FOUND;
        int i = ZERO;
        Iterator<Entry<String, Line>> ite = lines.iterator();
        while(ite.hasNext() && idx == NOT_FOUND){
            Entry<String, Line> entry = ite.next();
            if (line.compareTo(entry.getKey()) < ZERO){
                idx = i;
            }
            i++;
        }
        if (idx == NOT_FOUND){
            return lines.size();
        }
        return idx;
    }


    @Override
    public void addStationToLine(String line, List<String> station) {
        Iterator<String> ite = station.iterator();
        LineUpdatable l = (LineUpdatable) lines.find(line);
        l.setStationNumber(station.size());
        if (l != null){
            while (ite.hasNext()){
                String s = ite.next();
                l.addStation(s);
            }
        }
        l.sortStations();
    }

    @Override
    public void removeLine(String line) throws NoLinesException {
        int idx = SortAndSearch.binarySearch(orderedLines, line, size);
        if (idx == NOT_FOUND){
            throw new NoLinesException();
        }
        String l = orderedLines[idx];
        removeArray(idx);
        size--;
        lines.remove(l);
    }

    /**
     * Removes a given index from the array and shift the other elements to the left
     * @param idx - The index corresponding to the element to be removed
     */
    private void removeArray(int idx) {
        for (int i = idx; i < orderedLines.length - 1; i++){
            orderedLines[i] = orderedLines[i+1];
        }
    }

    @Override
    public boolean removeSchedule(String line, String startingStation, String hour) throws NoLinesException, NonExistantScheduleException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!hasSchedule(line, startingStation, hour)){
            throw new NonExistantScheduleException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        return l.removeSchedule(startingStation, hour);
    }

    @Override
    public void insertSchedule(String line, int trainNr, List<Entry<String, String>> stations) throws NoLinesException, InvalidScheduleException {
        if (!hasLine(line)){
            throw new NoLinesException();
        } else if (!isStartingStation(line, stations.getFirst().getKey()) || !orderCorrect(line, stations)){
            throw new InvalidScheduleException();
        }
        LineUpdatable l = (LineUpdatable) lines.find(line);
        l.addSchedule(trainNr, stations);
    }

    @Override
    public Iterator<Line> stationLines(String station) throws StationDoesNotExistException {
        try {
            return null;
        } catch (NoSuchElementException e){
            throw new StationDoesNotExistException();
        }
    }

    @Override
    public Iterator<Train> stationTrains(String station) throws StationDoesNotExistException {
        try{
            return null;
        } catch (NoSuchElementException e){
            throw new StationDoesNotExistException();
        }
    }

    @Override
    public Iterator<String> iteratorByLine(String line) throws NoLinesException {
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
    public Train bestTimeTable(String line, String startingStation, String endingStation, String hour) throws NoLinesException, NotStartingStationException, NotPossibleException {
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
