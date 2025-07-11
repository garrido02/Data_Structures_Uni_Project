/**
 * Class Line
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;
import dataStructures.*;
import Enums.Constants;

/**
 * Class Line responsible to implements the methods prototyped in the LineUpdatable interface
 */
public class LineClass implements LineUpdatable{
    /**
     * Constant variables
     */
    private static final int NOT_FOUND = Constants.NOT_FOUND.getValue();
    private static final int ZERO = Constants.ZERO.getValue();
    private static final int ONE = Constants.ONE.getValue();

    /**
     * Instance Variables
     */
    private String name;
    private List<String> stations;
    private String[] stationsInOrder;
    private int size = 0;
    private Dictionary<String, List<Train>> trainsByStation;
    private List<Train> trains;

    static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param name - The name of the line
     */
    public LineClass(String name){
        this.trains = new DoubleList<>();
        stations = new DoubleList<>();
        trainsByStation = new DictionaryClass<>();
        this.name = name;
    }

    @Override
    public void addStation(String station) {
        stations.addLast(station);
        stationsInOrder[size++] = station;
    }

    @Override
    public void sortStations(){
        SortAndSearch.sort(stationsInOrder, size);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addSchedule(int trainNr,List<Entry<String, String>> stationsToAdd) {
        Iterator<Entry<String, String>> ite = stationsToAdd.iterator();

        List<Entry<String, Date>> trainSchedule = new DoubleList<>();
        while (ite.hasNext()){
            Entry<String, String> nextEntry = ite.next();
            String s = nextEntry.getKey();
            Date time = new DateClass(nextEntry.getValue());
            trainSchedule.addLast(new EntryClass<>(s, time));
        }

        Date departure = new DateClass(trainSchedule.getFirst().getValue().getHour(), trainSchedule.getFirst().getValue().getMinutes());
        String startingStation = stationsToAdd.getFirst().getKey();
        Train train = new TrainClass(trainNr, trainSchedule);

        List<Train> trainList = trainsByStation.find(startingStation);
        if (trainList == null){
            trainList = new DoubleList<>();
            trainsByStation.insert(startingStation, trainList);
        }
        int idx = findNewTrainPosition(trainList, departure);
        trainList.add(idx, train);
        trains.addLast(train);
    }

    /**
     * Finds the new index of a new train to be added given its departure hour
     * @param trainList - The list of currently available trains
     * @param departure - The departure hour of the train to be added
     * @return the index in which the new train should be added
     */
    private int findNewTrainPosition(List<Train> trainList, Date departure){
        int idx = NOT_FOUND;
        int i = 0;
        Iterator<Train> trainIterator = trainList.iterator();
        while(trainIterator.hasNext() && idx == NOT_FOUND){
            Train train = trainIterator.next();
            if (departure.getHour() < train.getDepartureTime().getHour()){
                idx = i;
            } else if (departure.getHour() == train.getDepartureTime().getHour()){
                if (departure.getMinutes() < train.getDepartureTime().getMinutes()){
                    idx = i;
                }
            }
            i++;
        }
        if (idx == NOT_FOUND){
            return trainList.size();
        }
        return idx;
    }

    @Override
    public boolean hasSchedule(String stationName, String hour) {
        return findTrainIdxBySchedule(stationName, hour) != NOT_FOUND;
    }

    @Override
    public boolean hasStation(String station) {
        return SortAndSearch.binarySearch(stationsInOrder, station, size) != NOT_FOUND;
    }

    /**
     * Find the index of a train which departs from a given station at a given hour
     * @param stationName - The name of the departure station
     * @param hour - The hour of departure
     * @return the index of the train which departs from a given station at a given hour
     * or -1 AKA NOT_FOUND if the index is not found
     */
    private int findTrainIdxBySchedule(String stationName, String hour){
        List<Train> trainList = trainsByStation.find(stationName);
        if (trainList != null){
            Iterator<Train> trainIterator = trainList.iterator();
            Date time = new DateClass(hour);
            int idx = NOT_FOUND;
            int i = 0;
            while(trainIterator.hasNext() && idx == NOT_FOUND){
                Train t = trainIterator.next();
                if (t.hasSchedule(stationName, time)){
                    idx = i;
                }
                i++;
            }
            return idx;
        } else {
            return NOT_FOUND;
        }
    }

    @Override
    public boolean removeSchedule(String startingStation, String hour) {
        int idx = findTrainIdxBySchedule(startingStation, hour);
        if (idx != NOT_FOUND){
            List<Train> trainList = trainsByStation.find(startingStation);
            Train t = trainList.get(idx);
            trainList.remove(idx);
            trains.remove(t);
            return true;
        }
        return false;
    }

    @Override
    public void setStationNumber(int size) {
        stationsInOrder = new String[size];
    }


    @Override
    public boolean isTerminalStation(String station) {
        boolean first = stations.getFirst().equalsIgnoreCase(station);
        boolean last = stations.getLast().equalsIgnoreCase(station);
        return first || last;
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
    private boolean stationsInOrder(List<Entry<String, String>> stations){
        String station = stations.getFirst().getKey();
        int i = ZERO;
        if (!station.equalsIgnoreCase(this.stations.getFirst())){
            i = this.size - ONE;
        }
        int j = ZERO;
        if (i == ZERO){
            while (i < this.size && j < stations.size()){
                if (this.stations.get(i).equalsIgnoreCase(stations.get(j).getKey())){
                    i++; j++;
                } else {
                    i++;
                }
            }
        } else {
            while (i >= ZERO && j < stations.size()){
                if(this.stations.get(i).equalsIgnoreCase(stations.get(j).getKey())){
                    i--; j++;
                } else {
                    i--;
                }
            }
        }
        return j == stations.size();
    }

    /**
     * Checks if a given route has the hour of departure in each station in crescent order
     * @param stations - The list of stations
     * @return true if the list of stations have their departure hour in crescent order. Otherwise, false
     */
    private boolean hourInOrder(List<Entry<String, String>> stations) {
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
    public Iterator<String> stationsIterator() {
        return stations.iterator();
    }

    @Override
    public Iterator<Train> trainsPerStationsIterator(String startingStation){
        List<Train> trainList = trainsByStation.find(startingStation);
        if (trainList == null){
            return null;
        }
        return trainList.iterator();
    }

    @Override
    public Train bestTimeTable(String startingStation, String endingStation, String hour) {
        Date arrivalTime = new DateClass(hour);
        int idxStarter = stations.find(startingStation);
        int idxEnding = stations.find(endingStation);
        boolean insertionDirection = idxStarter < idxEnding;
        Iterator<Train> trainIte;
        if (insertionDirection){
            trainIte = trainsByStation.find(stations.getFirst()).iterator();
        } else {
            trainIte = trainsByStation.find(stations.getLast()).iterator();
        }

        Train bestSuited = null;
        int leeway = Integer.MAX_VALUE;
        while(trainIte.hasNext()){
            Train t = trainIte.next();
            if (t.hasStation(startingStation) && t.hasStation(endingStation)){
                int r = t.leeway(arrivalTime, endingStation);
                if (r < leeway){
                    leeway = r;
                    bestSuited = t;
                }
            }
        }
        return bestSuited;
    }
}

/**
 * End of Class Line
 */
