package RedeFerroviaria;

import Exceptions.*;


public interface StationUpdatable extends Station {
    void insertLine(String lineName) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException;
    void addSchedule(Train t, Date departure) throws EmptyTreeException, EmptyStackException, EmptyQueueException, FullStackException, FullQueueException;
    void removeSchedule(String hour, Train t) throws EmptyTreeException, FullStackException, EmptyStackException, EmptyQueueException, FullQueueException;
    void removeLine(String name);
}
