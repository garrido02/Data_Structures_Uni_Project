package RedeFerroviaria;
import Exceptions.*;

public interface StationUpdatable extends Station {
    Train removeSchedule(String hour);
    void insertLine(Line line) throws EmptyTreeException;
    void addSchedule(Train t, Date departure) throws EmptyTreeException;
}
