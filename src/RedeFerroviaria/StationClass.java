package RedeFerroviaria;


import Exceptions.*;
import dataStructures.*;


public class StationClass implements StationUpdatable {
    private String name;

    // Lines Tree
    private OrderedDictionary<String, Void> linesTree;

    // Train date tree K - Date, V - Train number
    private OrderedDictionary<Date, OrderedDictionary<Integer, Train>> scheduleTree;

    static final long serialVersionUID = 0L;


    public StationClass(String name) {
        this.name = name;
        this.linesTree = new AVLTree<>();
        this.scheduleTree = new AVLTree<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterator<Entry<String, Void>> linesIterator() throws EmptyTreeException, FullStackException {
        return linesTree.iterator();
    }

    @Override
    public Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> trainsIterator() throws EmptyTreeException, FullStackException {
        return scheduleTree.iterator();
    }

    @Override
    public boolean hasTrain(Train train) throws EmptyStackException, EmptyTreeException, EmptyQueueException, FullStackException, FullQueueException {
        return getTrainSchedule(train) != null;
    }

    @Override
    public Date getTrainSchedule(Train t) throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException {
        Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> ite = scheduleTree.iterator();
        Date result = null;
        boolean found = false;
        while (ite.hasNext() && !found) {
            Date d = ite.next().getKey();
            Iterator<Entry<Integer,Train>> ite2 = ite.next().getValue().iterator();
            while (ite2.hasNext() && !found) {
                if (ite2.next().getValue().equals(t)){
                    found = true;
                    result = d;
                }
            }
        }
        return result;
    }

    @Override
    public void removeSchedule(String hour, Train t) throws EmptyStackException, EmptyTreeException, EmptyQueueException, FullStackException, FullQueueException {
        Date d = new DateClass(hour);
        Iterator<Entry<Integer, Train>> ite = scheduleTree.find(d).iterator();
         while (ite.hasNext()) {
             Entry<Integer, Train> entry = ite.next();
             if (t.equals(entry.getValue())) {
                 scheduleTree.find(d).remove(entry.getKey());
             }
         }
    }


    @Override
    public void insertLine(String lineName) throws EmptyStackException, EmptyTreeException, EmptyQueueException, FullStackException, FullQueueException {
        linesTree.insert(lineName, null);
    }

    @Override
    public void addSchedule(Train t, Date departure) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException {
        OrderedDictionary<Integer, Train> dict = scheduleTree.find(departure);
        if (dict == null) {
            dict = new AVLTree<>();
        }
        dict.insert(t.getNr(), t);
    }

    @Override
    public void removeLine(String name) {
        linesTree.remove(name);
    }

    @Override
    public int compareTo(Station o) {
        return this.name.compareTo(o.getName());
    }
}
