package RedeFerroviaria;

public interface TrainUpdatable extends Train{
    void setDepartureTime(Date time);
    void setDepartureStation(Station station);
}
