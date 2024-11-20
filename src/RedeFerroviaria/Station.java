package RedeFerroviaria;

import Exceptions.EmptyTreeException;
import Exceptions.FullStackException;
import dataStructures.*;
import java.io.Serializable;

public interface Station extends Serializable, Comparable<Station> {
    Train schedule(String date);
    String getName();
    Iterator<Entry<String, Line>> linesIterator() throws EmptyTreeException, FullStackException;
    Iterator<Entry<Date, Train>> trainsIterator() throws EmptyTreeException;
    Date getTrainSchedule(int trainNr);
    boolean hasTrain(int trainNr);
}
