/**
 * Class Train
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;
import Exceptions.NoTrainsException;
import dataStructures.Entry;
import dataStructures.Iterator;
import dataStructures.List;
import Enums.Constants;


/**
 * Class Train responsible to implement the prototyped methods in the Train interface
 */
public class TrainClass implements Train {
    /**
     * Constant variables
     */
    private static int NOT_FOUND = Constants.NOT_FOUND.getValue();
    /**
     * Instance variables
     */
    private int nr;
    private Date departureTime;
    private String[] stops;
    private int size;
    private List<Entry<String, Date>> schedule;

    static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param nr - The number of the train
     * @param schedule - The route of the train
     */
    public TrainClass(int nr, List<Entry<String, Date>> schedule){
        this.nr = nr;
        this.departureTime = schedule.getFirst().getValue();
        this.schedule = schedule;
        this.stops = new String[this.schedule.size()];
        size = 0;
        sortStations();
    }

    public void sortStations(){
        addStations();
        SortAndSearch.sort(stops, size);
    }

    /**
     * Adds the route of the train to an array which will be sorted by alphabetical order
     */
    private void addStations(){
        try {
            Iterator<Entry<String, Date>> ite = scheduleIterator();
            while (ite.hasNext()){
                Entry<String, Date> entry = ite.next();
                stops[size++] = entry.getKey();
            }
        } catch (NoTrainsException ignored){}
    }

    @Override
    public int compareTo(Train o) {
        return Integer.compare(nr, o.getNr());
    }

    @Override
    public int getNr() {
        return nr;
    }

    @Override
    public boolean hasSchedule(String startingStation, Date time) {
        boolean isStartingStation = schedule.getFirst().getKey().equalsIgnoreCase(startingStation) || schedule.getLast().getKey().equalsIgnoreCase(startingStation);
        if (!isStartingStation){
            return false;
        } else {
            return stationHasTime(time);
        }
    }

    @Override
    public Iterator<Entry<String, Date>> scheduleIterator() throws NoTrainsException {
        if (schedule == null){
            throw new NoTrainsException();
        }
        return schedule.iterator();
    }

    @Override
    public Date getDepartureTime() {
        return departureTime;
    }

    @Override
    public boolean hasStation(String startingStation) {
        return SortAndSearch.binarySearch(stops, startingStation, size) != NOT_FOUND;
    }

    @Override
    public int leeway(Date arrivalTime, String endingStation) {
        int diff = Integer.MAX_VALUE;
        boolean found = false;
        Iterator<Entry<String, Date>> ite = schedule.iterator();
        while (ite.hasNext() && !found){
            Entry<String, Date> entry = ite.next();
            if (entry.getKey().equalsIgnoreCase(endingStation)){
                diff = Date.calculateDiff(arrivalTime, entry.getValue());
                found = true;
            }
        }
        if (diff < 0){
            return Integer.MAX_VALUE;
        }
        return diff;
    }

    /**
     * Checks if the route of the train departures at a given time from a certain station
     * @param time - The time to look for
     * @return true if the route of the train departures at a given time from a certain station. Otherwise, false
     */
    private boolean stationHasTime(Date time){
       return schedule.getFirst().getValue().getHour() == time.getHour() && schedule.getFirst().getValue().getMinutes() == time.getMinutes() || schedule.getLast().getValue().getHour() == time.getHour() && schedule.getLast().getValue().getMinutes() == time.getMinutes();
    }
}

/**
 * End of Class Train
 */
