/**
 * Class Train
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;


import dataStructures.Entry;
import dataStructures.Iterator;
import dataStructures.List;

/**
 * Class Train responsible to implement the prototyped methods in the Train interface
 */
public class TrainClass implements TrainUpdatable {
    /**
     * Instance variables
     */
    private int nr;
    private Station departureStation;
    private Date departureTime;
    private List<Entry<String, Date>> schedule;

    static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param nr - The number of the train
     */
    public TrainClass(int nr){
        this.nr = nr;
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
    public Date getDepartureTime() {
        return departureTime;
    }

    @Override
    public Iterator<Entry<String, Date>> scheduleIterator() {
        return schedule.iterator();
    }

    @Override
    public Station getStartingStation() {
        return departureStation;
    }

    @Override
    public void setDepartureTime(Date time) {
        departureTime = time;
    }


    @Override
    public void setDepartureStation(Station station) {
        departureStation = station;
    }

    @Override
    public void setSchedule(List<Entry<String, Date>> trainSchedule) {
        schedule = trainSchedule;
    }
}

/**
 * End of Class Train
 */
