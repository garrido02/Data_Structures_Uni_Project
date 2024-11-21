/**
 * Class Line
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;
import Exceptions.*;
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

    static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param name - The name of the line
     */
    public LineClass(String name){
        this.stations = new SepChainHashTable<>(Constants.DEFAULT_STATIONS.getValue());
        this.stationsByInsertion = new DoubleList<>();
        this.name = name;
    }

    @Override
    public void addStation(Station station) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        StationUpdatable s = (StationUpdatable) station;
        s.insertLine(this.name);
        stations.insert(station.getName(), s);
        stationsByInsertion.addLast(s);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void addSchedule(int trainNr,List<Entry<String, String>> stationsToAdd) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        Iterator<Entry<String, String>> ite = stationsToAdd.iterator();


        String startingStation = stationsToAdd.getFirst().getKey();
        String hour = stationsToAdd.getFirst().getValue();

        Date departureTime = new DateClass(hour);
        Station station = stations.find(startingStation);

        // Handle Train
        TrainUpdatable t = new TrainClass(trainNr);
        t.setDepartureStation(station);
        t.setDepartureTime(departureTime);

        // Handle insertions
        if (station.getName().equalsIgnoreCase(stationsByInsertion.getFirst().getName())){
            departureTerminal1.insert(departureTime, t);
        } else {
            departureTerminal2.insert(departureTime, t);
        }

        List<Entry<String, Date>> trainSchedule = new DoubleList<>();
        // Handle stations schedule insertions
        while (ite.hasNext()){
            Entry<String, String> nextEntry = ite.next();
            String stationName = nextEntry.getKey();
            Date time = new DateClass(nextEntry.getValue());
            trainSchedule.addLast(new EntryClass(stationName, time));

            StationUpdatable s = (StationUpdatable) stations.find(stationName);;
            s.addSchedule(t, time);
        }
        t.setSchedule(trainSchedule);
    }

    @Override
    public boolean hasSchedule(String stationName, String hour) throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException {
        Date time = new DateClass(hour);

        if (!isTerminalStation(stationName)){
            return false;
        }

        Iterator<Entry<Date, Train>> iterator;
        if (stationName.equalsIgnoreCase(stationsByInsertion.getFirst().getName())){
            iterator = departureTerminal1.iterator();
        } else {
            iterator = departureTerminal2.iterator();
        }

        boolean found = false;
        while (iterator.hasNext() && !found){
            Entry<Date, Train> entry = iterator.next();
            Date d = entry.getKey();
            if (d.equals(time)){
                found = true;
            }
        }
        return found;
    }

    @Override
    public boolean hasStation(String station) {
        return stations.find(station) != null;
    }

    @Override
    public void removeSchedule(String startingStation, String hour) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        StationUpdatable s = (StationUpdatable) stations.find(startingStation);
        Date time = new DateClass(hour);
        Train t;

        if (startingStation.equalsIgnoreCase(stationsByInsertion.getFirst().getName())){
            t = departureTerminal1.find(time);
            departureTerminal1.remove(time);
        } else {
            t = departureTerminal1.find(time);
            departureTerminal2.remove(time);
        }

        for (int i = 0; i < stationsByInsertion.size(); i++){
            StationUpdatable station = (StationUpdatable) stationsByInsertion.get(i);
            station.removeSchedule(hour, t);
        }
    }

    @Override
    public void removeLineFromStations() throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException {
        Iterator<Entry<String, Station>> ite = stations.iterator();
        while (ite.hasNext()){
            Entry<String,Station> entry = ite.next();
            String stationName = entry.getKey();
            StationUpdatable s = (StationUpdatable) stations.find(stationName);
            s.removeLine(this.name);
        }
    }

    @Override
    public boolean isTerminalStation(String station) {
        return stationsByInsertion.getFirst().getName().equals(station) || stationsByInsertion.getLast().getName().equals(station);
    }


    @Override
    public boolean orderCorrect(List<Entry<String, String>> stations) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
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
                if (s.getName().equalsIgnoreCase(stations.get(j).getKey())){
                    i++; j++;
                } else {
                    i++;
                }
            }
        } else {
            while (i >= ZERO && j < stations.size()){
                Station s = stationsByInsertion.get(i);
                if(s.getName().equalsIgnoreCase(stations.get(j).getKey())){
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
    private boolean hourInOrder(List<Entry<String, String>> stations) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        Station starter = this.stations.find(stations.getFirst().getKey());
        Date starterDate = new DateClass(stations.getFirst().getValue());

        Iterator<Entry<String, String>> ite = stations.iterator();
        boolean result = true;
        Date prev = null;
        while (ite.hasNext() && result){
            Entry<String, String> entry = ite.next();
            Date currentDate = new DateClass(entry.getValue());
            Station currentStation = this.stations.find(entry.getKey());
            if (prev != null){
                int hour =  currentDate.getHour() - prev.getHour();
                if (hour == ZERO){
                    int minute = currentDate.getMinutes() - prev.getMinutes();
                    result = minute >= ONE;
                } else {
                    result = hour >= ONE;
                }
            }
            if (hasOvercome(starter, starterDate, currentStation, currentDate)){
                result = false;
            }
            prev = currentDate;
        }
        return result;
    }

    private boolean hasOvercome(Station starter, Date starterDate, Station currentStation, Date currentdDate) throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException {
        Iterator<Entry<Date,Train>> ite;
        boolean result = false;
        if (starter.getName().equalsIgnoreCase(stationsByInsertion.getFirst().getName())){
            ite = departureTerminal1.iterator();
        } else {
            ite = departureTerminal2.iterator();
        }
        while (ite.hasNext() && !result){
            Entry<Date, Train> entry = ite.next();
            Date d = entry.getKey();
            Train t = entry.getValue();
            Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> ite2 = currentStation.trainsIterator();;
            if (starterDate != d && Date.calculateDiff(starterDate, d) < 0){
                while (ite2.hasNext() && !result){
                    Entry<Date, OrderedDictionary<Integer, Train>> entry2 = ite2.next();
                    Date d2 = entry2.getKey();
                    Iterator<Entry<Integer, Train>> trainIte = entry2.getValue().iterator();
                    while (trainIte.hasNext() && !result){
                        Entry<Integer, Train> entry3 = trainIte.next();
                        Train t2 = entry3.getValue();
                        if (t2.equals(t) && Date.calculateDiff(currentdDate, d2) > 0){
                            result = true;
                        }
                    }
                }
            } else if (starterDate != d && Date.calculateDiff(starterDate, d) > 0){
                while (ite2.hasNext() && !result){
                    Entry<Date, OrderedDictionary<Integer, Train>> entry2 = ite2.next();
                    Date d2 = entry2.getKey();
                    Iterator<Entry<Integer, Train>> trainIte = entry2.getValue().iterator();
                    while (trainIte.hasNext() && !result){
                        Entry<Integer, Train> entry3 = trainIte.next();
                        Train t2 = entry3.getValue();
                        if (t2.equals(t) && Date.calculateDiff(currentdDate, d2) > 0){
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
    public Iterator<Entry<Date, Train>> trainsPerStationsIterator(String startingStation) throws EmptyTreeException, FullStackException {
        Iterator<Entry<Date, Train>> ite;
        if (stationsByInsertion.getFirst().getName().equalsIgnoreCase(startingStation)){
            ite = departureTerminal1.iterator();
        } else {
            ite = departureTerminal2.iterator();
        }
        return ite;
    }

    @Override
    public Train bestTimeTable(String startingStation, String endingStation, String hour) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        Date arrivalTime = new DateClass(hour);
        Iterator<Entry<Date, Train>> ite;
        if (startingStation.equalsIgnoreCase(stationsByInsertion.getFirst().getName())){
            ite = departureTerminal1.iterator();
        } else {
            ite = departureTerminal2.iterator();
        }

        Station starter = stations.find(startingStation);
        Station ender = stations.find(endingStation);
        Train bestSuited = null;
        int leeway = Integer.MAX_VALUE;
        while(ite.hasNext()){
            Entry<Date, Train> entry = ite.next();
            Train t = entry.getValue();

            // Ver se ambas as estações têem este comboio
            if (starter.hasTrain(t) && ender.hasTrain(t)){
                int r = Date.calculateDiff(ender.getTrainSchedule(t), arrivalTime);
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
