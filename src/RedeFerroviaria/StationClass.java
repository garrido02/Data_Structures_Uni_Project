/**
 * Class Station
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;

import Exceptions.*;
import dataStructures.*;

import java.io.Serial;

/**
 * Class Station responsible to implements the methods prototyped in the StationUpdatable interface
 */
public class StationClass implements StationUpdatable {
    private String name;

    // Lines Tree
    private OrderedDictionary<String, String> linesTree;

    // Train date tree K - Date, V - Train number
    private OrderedDictionary<Date, OrderedDictionary<Integer, Train>> scheduleTree;

    @Serial
    private static final long serialVersionUID = 0L;


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
    public Iterator<Entry<String, String>> linesIterator() {
            return linesTree.iterator();

    }

    @Override
    public Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> trainsIterator() {
            return scheduleTree.iterator();


    }

    @Override
    public boolean hasTrain(Train train) {
        return getTrainSchedule(train) != null;
    }

    @Override
    public Date getTrainSchedule(Train t){
            Date result = null;
            Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> ite = scheduleTree.iterator();
            boolean found = false;
            if (ite != null){
                while (ite.hasNext() && !found) {
                    Entry<Date, OrderedDictionary<Integer, Train>> entry = ite.next();
                    Date d = entry.getKey();
                    Iterator<Entry<Integer,Train>> ite2 = entry.getValue().iterator();
                    if (ite2 != null){
                        while (ite2.hasNext() && !found) {
                            if (ite2.next().getKey().equals(t.getNr())){
                                found = true;
                                result = d;
                            }
                        }
                    }
                }
            }
            return result;
    }

    @Override
    public boolean isObsolete() {
        return linesTree.isEmpty();
    }

    @Override
    public void removeSchedule(Date hour, Train t){
        Iterator<Entry<Integer, Train>> ite = scheduleTree.find(hour).iterator();
        if (ite != null){
            while (ite.hasNext()) {
                Entry<Integer, Train> entry = ite.next();
                if (t.equals(entry.getValue())) {
                    scheduleTree.find(hour).remove(entry.getKey());
                    if (scheduleTree.find(hour).isEmpty()) {
                        scheduleTree.remove(hour);
                    }
                }
            }
        }
    }


    @Override
    public void insertLine(String lineNameLower, String lineName){
        linesTree.insert(lineNameLower, lineName);
    }

    @Override
    public void addSchedule(Train t, Date departure) {
        OrderedDictionary<Integer, Train> dict = scheduleTree.find(departure);
        if (dict == null) {
            dict = new AVLTree<>();
            scheduleTree.insert(departure, dict);
        }

        dict.insert(t.getNr(), t);
    }

    @Override
    public void removeLine(String name) {
        linesTree.remove(name);
    }

    @Override
    public void removeTrains(OrderedDictionary<Date, Train> departureTerminal1, OrderedDictionary<Date, Train> departureTerminal2) {
        if (!departureTerminal1.isEmpty()){
            removeTrainsAuxiliary(departureTerminal1);
        }
        if (!departureTerminal2.isEmpty()){
            removeTrainsAuxiliary(departureTerminal2);
        }
    }

    private void removeTrainsAuxiliary(OrderedDictionary<Date, Train> terminal) {
        if (!terminal.isEmpty()) {
            Iterator<Entry<Date, Train>> terminalIterator = terminal.iterator();
            if (terminalIterator != null) {
                while (terminalIterator.hasNext()) {
                    Entry<Date, Train> entry = terminalIterator.next();
                    Train t = entry.getValue();
                    Date date = this.getTrainSchedule(t);
                    if (date != null) {
                        removeSchedule(date, t);
                    }
                }
            }
        }
    }


        @Override
    public int compareTo(Station o) {
        return this.name.compareTo(o.getName());
    }
}

/**
 * End of Class Station
 */