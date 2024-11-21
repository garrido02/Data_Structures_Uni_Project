package RedeFerroviaria;

import Exceptions.*;
import dataStructures.*;
import java.io.Serializable;

public interface Station extends Serializable, Comparable<Station> {
    String getName();
    Iterator<Entry<String, Void>> linesIterator() throws EmptyTreeException, FullStackException;
    Iterator<Entry<Date, OrderedDictionary<Integer, Train>>> trainsIterator() throws EmptyTreeException, FullStackException;
    boolean hasTrain(Train train) throws EmptyStackException, EmptyTreeException, EmptyQueueException, FullStackException, FullQueueException;
    Date getTrainSchedule(Train t) throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException;
}
