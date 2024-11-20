/**
 * Class Line
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;
import Exceptions.EmptyTreeException;
import dataStructures.*;
import Enums.Constants;

/**
 * Class Line responsible to implements the methods prototyped in the LineUpdatable interface
 */
public class LineClass implements LineUpdatable{
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
    // Map of Trains K - Nr of Train, V - Train
    private Dictionary<Integer, Train> trains;



    static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param name - The name of the line
     */
    public LineClass(String name){
        this.stations = new SepChainHashTable<>(Constants.DEFAULT_STATIONS.getValue());
        this.stationsByInsertion = new DoubleList<>();
        this.trains = new SepChainHashTable<>(Constants.DEFAULT_SCHEDULES.getValue());
        this.name = name;
    }

    @Override
    public void addStation(String station) throws EmptyTreeException {
        StationUpdatable s = new StationClass(station);
        s.insertLine(this);
        stations.insert(station, s);
        stationsByInsertion.addLast(s);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addSchedule(int trainNr,List<Entry<String, String>> stationsToAdd) throws EmptyTreeException {
        Iterator<Entry<String, String>> ite = stationsToAdd.iterator();
        String startingStation = stationsToAdd.getFirst().getKey();

        List<Entry<String, Date>> trainSchedule = new DoubleList<>();
        while (ite.hasNext()){
            Entry<String, String> nextEntry = ite.next();
            String stationName = nextEntry.getKey();
            Date time = new DateClass(nextEntry.getValue());

            trainSchedule.addLast(new EntryClass<>(stationName, time));
            StationUpdatable s = (StationUpdatable) stations.find(stationName);
            Train t = new TrainClass(trainNr, trainSchedule);
            s.addSchedule(t, time);
            trains.insert(trainNr, t);
        }

        List<Train> trainList = trainsByTerminal.find(startingStation);
        if (trainList == null){
            trainList = new DoubleList<>();
            trainsByTerminal.insert(startingStation, trainList);
        }
    }

    @Override
    public boolean hasSchedule(String stationName, String hour) throws EmptyTreeException {
        Station s = stations.find(stationName);
        Train t = s.schedule(hour);
        return t.isStartingStation(stationName);
    }

    @Override
    public boolean hasStation(String station) {
        return stations.find(station) != null;
    }

    @Override
    public boolean removeSchedule(String startingStation, String hour) throws EmptyTreeException {
        if (!hasStation(startingStation)){
            return false;
        }
        StationUpdatable s = (StationUpdatable) stations.find(startingStation);
        Train t = s.removeSchedule(hour);

        List<Train> trains = trainsByTerminal.find(startingStation);
        return trains.remove(t);
    }

    @Override
    public boolean isTerminalStation(String station) {
        return stationsByInsertion.getFirst().getName().equals(station) || stationsByInsertion.getLast().getName().equals(station);
    }


    @Override
    public boolean orderCorrect(List<Entry<String, String>> stations) throws EmptyTreeException {
        return stationsInOrder(stations) && hourInOrder(stations);
    }

    /**
     * Checks if a given route follows the line's order of stations
     * @param stations - The list of stations to be added
     * @return true if the list of stations follows the line's order of stations. Otherwise, false
     */
    private boolean stationsInOrder(List<Entry<String, String>> stations) throws EmptyTreeException {
        String station = stations.getFirst().getKey();
        int i = ZERO;
        if (!station.equalsIgnoreCase(this.stationsByInsertion.getFirst().getName())){
            i = stationsByInsertion.size() - ONE;
        }
        int j = ZERO;
        if (i == ZERO){
            while (i < stationsByInsertion.size() && j < stations.size()){
                Station s = stationsByInsertion.get(i);
                if (s.getName().equalsIgnoreCase(stations.get(j).getKey()) && scheduleAvailable(stationsByInsertion.getFirst(), stations.getFirst(), stations.get(j))){
                    i++; j++;
                } else {
                    i++;
                }
            }
        } else {
            while (i >= ZERO && j < stations.size()){
                Station s = stationsByInsertion.get(i);
                if(s.getName().equalsIgnoreCase(stations.get(j).getKey()) && scheduleAvailable(stationsByInsertion.getLast(), stations.getFirst(), stations.get(j))){
                    i--; j++;
                } else {
                    i--;
                }
            }
        }
        return j == stations.size();
    }

    private boolean scheduleAvailable(Station starter, Entry<String, String> firstEntry, Entry<String, String> currentEntry) throws EmptyTreeException {
        Station current = stations.find(currentEntry.getKey());
        Train train = current.schedule(currentEntry.getValue());
        Date startingDate = new DateClass(firstEntry.getValue());
        Date currentDate = new DateClass(currentEntry.getValue());
        return !train.isStartingStation(starter.getName()) && !hasOvercome(train, startingDate, currentDate, current);
    }

    private boolean hasOvercome(Train t, Date startingDate, Date currentDate, Station current){
        Date currentStationDate = current.getTrainSchedule(t.getNr());
        int resultStarter = Date.calculateDiff(t.getDepartureTime(), startingDate);
        int resultCurrent = Date.calculateDiff(currentStationDate, currentDate);
        if (resultStarter < 0 && resultCurrent < 0 || resultStarter > 0 && resultCurrent > 0){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Checks if a given route has the hour of departure in each station in crescent order
     * @param stations - The list of stations
     * @return true if the list of stations have their departure hour in crescent order. Otherwise, false
     */
    private boolean hourInOrder(List<Entry<String, String>> stations) throws EmptyTreeException {
        Iterator<Entry<String, String>> ite = stations.iterator();
        boolean result = true;
        Date prev = null;
        while (ite.hasNext() && result){
            Entry<String, String> entry = ite.next();
            Date date = new DateClass(entry.getValue());
            if (prev != null){
                int hour =  date.getHour() - prev.getHour();
                if (hour == ZERO){
                    int minute = date.getMinutes() - prev.getMinutes();
                    result = minute >= ONE;
                } else {
                    result = hour >= ONE;
                }
            }
            prev = date;
        }
        return result;
    }

    @Override
    public Iterator<Station> stationsIterator() {
        return stationsByInsertion.iterator();
    }

    @Override
    public Iterator<Train> trainsPerStationsIterator(String startingStation){
        List<Train> trainList = trainsByTerminal.find(startingStation);
        if (trainList == null){
            return null;
        }
        return trainList.iterator();
    }

    @Override
    public Train bestTimeTable(String startingStation, String endingStation, String hour) throws EmptyTreeException {
        Date arrivalTime = new DateClass(hour);
        int idxStarter = stationsByInsertion.find(stations.find(startingStation));
        int idxEnding = stationsByInsertion.find(stations.find(endingStation));
        boolean insertionDirection = idxStarter < idxEnding;
        Iterator<Train> trainIte;
        if (insertionDirection){
            trainIte = trainsByTerminal.find(stationsByInsertion.getFirst().getName()).iterator();
        } else {
            trainIte = trainsByTerminal.find(stationsByInsertion.getLast().getName()).iterator();
        }

        Station starter = stations.find(startingStation);
        Station ender = stations.find(endingStation);
        Train bestSuited = null;
        int leeway = Integer.MAX_VALUE;
        while(trainIte.hasNext()){
            Train t = trainIte.next();
            int trainNr = t.getNr();
            if (starter.hasTrain(trainNr) && ender.hasTrain(trainNr)){
                int r = Date.calculateDiff(ender.getTrainSchedule(trainNr), arrivalTime);
                if (r < leeway){
                    leeway = r;
                    bestSuited = t;
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
