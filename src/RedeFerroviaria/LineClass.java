/**
 * Class Line
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;
import Exceptions.*;
import dataStructures.*;
import Enums.Constants;

import java.io.Serial;

/**
 * Class Line responsible to implements the methods prototyped in the LineUpdatable interface
 */
public class LineClass implements LineUpdatable {
    /**
     * Constant variables
     */
    private static final int ZERO = Constants.ZERO.getValue();
    private static final int ONE = Constants.ONE.getValue();

    /**
     * Instance Variables
     */
    private String name;
    // Tree departure time of terminal 1 && 2
    private OrderedDictionary<Date, Train> departureTerminal1;
    private OrderedDictionary<Date, Train> departureTerminal2;
    // Stations by order of insertion
    private List<Station> stationsByInsertion;
    // Map of stations K - Name of station; V - Station
    private Dictionary<String, Station> stations;

    @Serial
    private static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param name - The name of the line
     */
    public LineClass(String name) {
        this.stations = new SepChainHashTable<>();
        this.stationsByInsertion = new DoubleList<>();
        this.departureTerminal1 = new AVLTree<>();
        this.departureTerminal2 = new AVLTree<>();
        this.name = name;
    }

    @Override
    public void addStation(Station station) {
        StationUpdatable s = (StationUpdatable) station;
        s.insertLine(this.name.toLowerCase(), this.name);
        stations.insert(station.getName().toLowerCase(), s);
        stationsByInsertion.addLast(s);
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Get the departure terminal according to a departure station
     * @param departureStation - The station of departure
     * @return an Object of type OrderedDictionary that match a Train with a given Date
     */
    private OrderedDictionary<Date, Train> getDepartureTerminal(Station departureStation){
        if (departureStation.getName().equalsIgnoreCase(stationsByInsertion.getFirst().getName())) {
            return departureTerminal1;
        } else {
            return departureTerminal2;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addSchedule(int trainNr, List<Entry<String, String>> stationsToAdd) {
        Iterator<Entry<String, String>> ite = stationsToAdd.iterator();

        String startingStation = stationsToAdd.getFirst().getKey().toLowerCase();
        String hour = stationsToAdd.getFirst().getValue();

        Date departureTime = new DateClass(hour);
        Station station = stations.find(startingStation);
        TrainUpdatable t = new TrainClass(trainNr);

        OrderedDictionary<Date, Train> departureTerminal = getDepartureTerminal(station);
        departureTerminal.insert(departureTime, t);

        List<Entry<String, Date>> trainSchedule = new DoubleList<>();
        while (ite.hasNext()) {
            Entry<String, String> nextEntry = ite.next();
            String stationName = nextEntry.getKey();
            Date time = new DateClass(nextEntry.getValue());
            trainSchedule.addLast(new EntryClass<>(stationName, time));
            StationUpdatable s = (StationUpdatable) stations.find(stationName.toLowerCase());
            s.addSchedule(t, time);
        }
        t.setSchedule(trainSchedule);
    }

    @Override
    public boolean hasSchedule(String stationName, String hour) {
        Date time = new DateClass(hour);

        if (!isTerminalStation(stationName)) {
            return false;
        }

        OrderedDictionary<Date, Train> departureTerminal = getDepartureTerminal(stations.find(stationName.toLowerCase()));

            Iterator<Entry<Date, Train>> iterator = departureTerminal.iterator();
            boolean found = false;
            if (iterator != null){
                while (iterator.hasNext() && !found) {
                    Entry<Date, Train> entry = iterator.next();
                    Date entryDate = entry.getKey();
                    if (entryDate.compareTo(time) == 0) {
                        found = true;
                    }
                }
            }
            return found;
    }

    @Override
    public boolean hasStation(String station) {
        return stations.find(station) != null;
    }

    @Override
    public void removeSchedule(String startingStation, String hour) {

            Date time = new DateClass(hour);
            Train t;
            OrderedDictionary<Date, Train> departureTerminal = getDepartureTerminal(stations.find(startingStation));
            t = departureTerminal.find(time);
            departureTerminal.remove(time);

            Iterator<Entry<String, Date>> scheduleIte = t.scheduleIterator();
            while (scheduleIte.hasNext()) {
                Entry<String, Date> entry = scheduleIte.next();
                StationUpdatable station = (StationUpdatable) stations.find(entry.getKey().toLowerCase());
                station.removeSchedule(entry.getValue(), t);
            }

    }

    @Override
    public List<Station> removeLineFromStations() {
        Iterator<Station> ite = stationsByInsertion.iterator();
        List<Station> stationsToRemove = new DoubleList<>();
        while (ite.hasNext()) {
            StationUpdatable s = (StationUpdatable) stations.find(ite.next().getName().toLowerCase());
            s.removeLine(this.name.toLowerCase());
            if (s.isObsolete()){
                stationsToRemove.addLast(s);
            }
            s.removeTrains(departureTerminal1, departureTerminal2);
        }
        return stationsToRemove;
    }

    @Override
    public boolean isTerminalStation(String station) {
        return stationsByInsertion.getFirst().getName().equalsIgnoreCase(station) || stationsByInsertion.getLast().getName().equalsIgnoreCase(station);
    }

    @Override
    public boolean orderCorrect(List<Entry<String, String>> stations) {
        return stationsInOrder(stations) && hourInOrder(stations);
    }

    /**
     * Checks if a given route follows the line's order of stations
     * @param stations - The list of stations to be added
     * @return true if the list of stations follows the line's order of stations. Otherwise, false
     */
    private boolean stationsInOrder(List<Entry<String, String>> stations) {
        String station = stations.getFirst().getKey();
        int i = ZERO;
        if (!station.equalsIgnoreCase(this.stationsByInsertion.getFirst().getName())) {
            i = stationsByInsertion.size() - ONE;
        }
        int j = ZERO;
        if (i == ZERO) {
            j = orderAuxiliary(stations, i, j, 1, 1, true);
        } else {
            j = orderAuxiliary(stations, i, j, -1, 1, false);
        }
        return j == stations.size();
    }

    /**
     * Aids the calculation to check if stations are in order
     * @param stations - The list of stations to check the order
     * @param i - System stations index
     * @param j - Stations to be inserted index
     * @param iInc - i index increment value
     * @param jInc - j index increment value
     * @param term - the comparison term
     * @return the value of j
     */
    private int orderAuxiliary(List<Entry<String, String>> stations, int i, int j, int iInc, int jInc, boolean term){
        boolean compare;
        if (term){ compare = i < stationsByInsertion.size(); }
        else { compare = i >= ZERO; }
        while (compare && j < stations.size()){
            Station s = stationsByInsertion.get(i);
            if (s.getName().equalsIgnoreCase(stations.get(j).getKey())) {
                i += iInc; j += jInc;
            } else { i += iInc; }

            if (term){ compare = i < stationsByInsertion.size(); }
            else { compare = i >= ZERO; }
        }
        return j;
    }

    /**
     * Checks if a given route has the hour of departure in each station in crescent order
     * @param stations - The list of stations
     * @return true if the list of stations have their departure hour in crescent order. Otherwise, false
     */
    private boolean hourInOrder(List<Entry<String, String>> stations) {
        Station starter = this.stations.find(stations.getFirst().getKey().toLowerCase());
        Date starterDate = new DateClass(stations.getFirst().getValue());

        Iterator<Entry<String, String>> ite = stations.iterator();
        boolean result = true;
        Date prev = null;
        while (ite.hasNext() && result) {
            Entry<String, String> entry = ite.next();
            Date currentDate = new DateClass(entry.getValue());
            Station currentStation = this.stations.find(entry.getKey().toLowerCase());
            if (prev != null) {
                result = hourAuxiliary(currentDate, prev);
            }
            if (hasOvercome(starter, starterDate, currentStation, currentDate)) {
                result = false;
            }
            prev = currentDate;
        }
        return result;
    }

    /**
     * Aids to check if the hours of a schedule to be inserted are corrected
     * @param currentDate - The date of a given train on a given station
     * @param prev - The previous date of a given station
     * @return true if the hour is by order. Otherwise, false
     */
    private boolean hourAuxiliary(Date currentDate, Date prev) {
        boolean result;
        int hour = currentDate.getHour() - prev.getHour();
        if (hour == ZERO) {
            int minute = currentDate.getMinutes() - prev.getMinutes();
            result = minute > ONE;
        } else {
            result = hour >= ONE;
        }
        return result;
    }

    /**
     * Checks if a train has overcome another
     * @param starter - The starting station
     * @param starterDate - The date of the starting station
     * @param currentStation - The current station
     * @param currentDate - The date of the current station
     * @return true if a train has overcome another. Otherwise, false
     */
    private boolean hasOvercome(Station starter, Date starterDate, Station currentStation, Date currentDate){
        OrderedDictionary<Date, Train> departureTerminal = getDepartureTerminal(starter);
        if (departureTerminal.isEmpty()) {
            return false;
        }
            Iterator<Entry<Date, Train>> iteTrains = departureTerminal.iterator();
            boolean result = false;
            if (iteTrains != null){
                while (iteTrains.hasNext() && !result) {
                    Entry<Date, Train> entry = iteTrains.next();
                    Date entryDate = entry.getKey();
                    Train train = entry.getValue();

                    if (starterDate != entryDate && Date.calculateDiff(starterDate, entryDate) == 0){
                        result = true;
                    }
                    if (starterDate != entryDate && Date.calculateDiff(starterDate, entryDate) < 0) {
                        result = overcomeAuxiliary(currentStation, train, currentDate, true);
                    } else if (starterDate != entryDate && Date.calculateDiff(starterDate, entryDate) > 0) {
                        result = overcomeAuxiliary(currentStation, train, currentDate, false);
                    }
                }
            }
            return result;
    }

    /**
     * Aids the calculation of an overcome of a train to another
     * @param currentStation - The current station
     * @param train - The train on the current station
     * @param currentDate - The departure that of the train on the current station
     * @param term - The comparison term
     * @return true there is an overcome. Otherwise, false
     */
    private boolean overcomeAuxiliary(Station currentStation, Train train, Date currentDate, boolean term) {
        Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> currentStationIterator = currentStation.trainsIterator();
        boolean result = false;
        boolean compare;
        if (currentStationIterator != null) {
            while (currentStationIterator.hasNext() && !result) {
                Entry<Date, OrderedDictionary<Integer, Train>> entry = currentStationIterator.next();
                Date entryDate = entry.getKey();
                Iterator<Entry<Integer, Train>> trainIterator = entry.getValue().iterator();
                if (trainIterator != null) {
                    while (trainIterator.hasNext() && !result) {
                        Entry<Integer, Train> entry3 = trainIterator.next();
                        Train entryTrain = entry3.getValue();
                        if (term){
                            compare = Date.calculateDiff(currentDate, entryDate) >= 0;
                        } else {
                            compare = Date.calculateDiff(currentDate, entryDate) <= 0;
                        }
                        if (entryTrain.equals(train) && compare) {
                            result = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Iterator<Station> stationsIterator() {
        return stationsByInsertion.iterator();
    }

    @Override
    public Iterator<Entry<Date, Train>> trainsPerStationsIterator(String startingStation){

            OrderedDictionary<Date, Train> departureTerminal = getDepartureTerminal(stations.find(startingStation.toLowerCase()));
            return departureTerminal.iterator();

    }

    /**
     * Gets the direction of the line, given a stater and ender station index
     * @param starterIdx - The index of a starting station
     * @param enderIdx - The index of an ending station
     * @return an Object of type Ordered Dictionary representing the terminal, i.e, direction of the line
     */
    private OrderedDictionary<Date, Train> getDirectionByIdx(int starterIdx, int enderIdx){
        if (starterIdx < enderIdx) {
            return departureTerminal1;
        } else {
            return departureTerminal2;
        }
    }

    @Override
    public Train bestTimeTable(String startingStation, String endingStation, String hour) {
        Date arrivalTime = new DateClass(hour);
        Station starter = stations.find(startingStation);
        Station ender = stations.find(endingStation);

        OrderedDictionary<Date, Train> direction = getDirectionByIdx(stationsByInsertion.find(starter), stationsByInsertion.find(ender));

            Iterator<Entry<Date, Train>> ite = direction.iterator();
            Train bestSuited = null;
            int leeway = Integer.MIN_VALUE;
            if (ite != null){
                while (ite.hasNext()) {
                    Entry<Date, Train> entry = ite.next();
                    Train t = entry.getValue();
                    if (starter.hasTrain(t) && ender.hasTrain(t)) {
                        int r = Date.calculateDiff(ender.getTrainSchedule(t), arrivalTime);
                        if (r > leeway && r <= 0) {
                            leeway = r;
                            bestSuited = t;
                        }
                    }
                }
            }
            return bestSuited;

    }

    @Override
    public int compareTo(Line o) {
        return this.name.compareTo(o.getName());
    }
}

/**
 * End of Class Line
 */
