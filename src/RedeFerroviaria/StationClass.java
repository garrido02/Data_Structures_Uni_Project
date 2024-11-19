package RedeFerroviaria;
import Enums.Constants;
import Exceptions.EmptyTreeException;
import dataStructures.*;

public class StationClass implements StationUpdatable{
    private String name;

    // Lines Tree
    private OrderedDictionary<String, Line> linesTree;

    // Train date tree K - Date, V - Train number
    private OrderedDictionary<Date, Train> scheduleTree;

    // Map K - Train nr; V - Date
    private Dictionary<Integer, Date> trainsByDate;

    static final long serialVersionUID = 0L;


    public StationClass(String name) {
        this.name = name;
        this.linesTree = new AVLTree<>();
        this.scheduleTree = new AVLTree<>();
        this.trainsByDate = new SepChainHashTable<>(Constants.DEFAULT_SCHEDULES.getValue());
    }


    @Override
    public Train schedule(String date) {
        Date d = new DateClass(date);
        return scheduleTree.find(d);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<Entry<String, Line>> linesIterator() throws EmptyTreeException {
        return linesTree.iterator();
    }

    @Override
    public Iterator<Entry<Date, Train>> trainsIterator() throws EmptyTreeException {
        return scheduleTree.iterator();
    }

    @Override
    public Date getTrainSchedule(int trainNr) {
        return trainsByDate.find(trainNr);
    }

    @Override
    public boolean hasTrain(int trainNr){
        return trainsByDate.find(trainNr) != null;
    }

    @Override
    public Train removeSchedule(String hour) {
        Date d = new DateClass(hour);
        Train t = scheduleTree.find(d);
        if (t.isStartingStation(name)){
            trainsByDate.remove(t.getNr());
        }
        return t;
    }


    @Override
    public void insertLine(Line line) throws EmptyTreeException {
        linesTree.insert(line.getName(), line);
    }

    @Override
    public void addSchedule(Train t, Date departure) throws EmptyTreeException {
        Train train = scheduleTree.find(departure);
        int nr = t.getNr();
        scheduleTree.insert(departure, train);
        trainsByDate.insert(nr, departure);
    }

    @Override
    public int compareTo(Station o) {
        return this.name.compareTo(o.getName());
    }
}
