/**
 * Class Train
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package RedeFerroviaria;
import Exceptions.EmptyTreeException;
import Exceptions.NoTrainsException;
import dataStructures.Entry;
import dataStructures.Iterator;
import dataStructures.List;
import Enums.Constants;


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

    static final long serialVersionUID = 0L;

    /**
     * Constructor
     * @param nr - The number of the train
     * @param schedule - The route of the train
     */
    public TrainClass(int nr, List<Entry<String, Date>> schedule){
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
    public boolean isStartingStation(String startingStation) {
        return departureStation.getName().equalsIgnoreCase(startingStation);
    }

    @Override
    public void setDepartureTime(Date time) {
        departureTime = time;
    }

    @Override
    public void setDepartureStation(Station station) {
        departureStation = station;
    }
}

/**
 * End of Class Train
 */
