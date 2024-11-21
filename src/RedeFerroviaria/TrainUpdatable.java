package RedeFerroviaria;

import dataStructures.Entry;
import dataStructures.List;

public interface TrainUpdatable extends Train{
    void setDepartureTime(Date time);
    void setDepartureStation(Station station);

    void setSchedule(List<Entry<String, Date>> trainSchedule);
}
