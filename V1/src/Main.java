/**
 * Class Main
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

import java.io.*;
import java.util.Scanner;
import Enums.*;
import Exceptions.*;
import RedeFerroviaria.*;
import dataStructures.*;


public class Main {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        redeFerroviaria(in);
    }

    /**
     * Starts the system
     * @param in - Scanner
     */
    private static void redeFerroviaria(Scanner in){
        Rede rede = load();
        String command;
        do {
            command = in.next().toUpperCase();
            Commands c = Commands.valueOf(command);
            switch (c) {
                case IL -> insertLine(in, rede);
                case RL -> removeLine(in, rede);
                case CL -> checkLine(in, rede);
                case CE -> stationLines(in, rede);
                case LC -> stationTrains(in, rede);
                case IH -> insertSchedule(in, rede);
                case RH -> removeSchedule(in, rede);
                case CH -> listLineSchedule(in, rede);
                case MH -> bestTimeTable(in, rede);
                case TA -> terminateApplication(rede);
            }
        } while (Commands.valueOf(command) != Commands.TA);
    }

    private static void stationLines(Scanner in, Rede rede) {
        String station = in.nextLine().trim();
        try {
            Iterator<Line> ite = rede.stationLines(station);
            while (ite.hasNext()){
                Line l = ite.next();
                System.out.printf(Outputs.STRING.getString(), l.getName());
            }
        } catch (StationDoesNotExistException e){
            System.out.println(Outputs.NONEXISTANT_STATION.getString());
        }
    }

    private static void stationTrains(Scanner in, Rede rede){
        String station = in.nextLine();
        try{
            Iterator<Train> ite = rede.stationTrains(station);
            while (ite.hasNext()){
                Train t = ite.next();
                System.out.printf(Outputs.TRAIN_HOUR.getString(), t.getNr(), t.getDepartureTime());
            }
        } catch (StationDoesNotExistException e){
            System.out.println(Outputs.NONEXISTANT_STATION.getString());
        }
    }

    /**
     * Inserts a new line to the system
     * @param in - Scanner
     * @param rede - The system
     */
    private static void insertLine(Scanner in, Rede rede) {
        String line = in.nextLine().trim();
        List<String> aux = new DoubleList<>();

        boolean end = false;
        while (!end){
            String station = in.nextLine().trim();
            if (station.isEmpty()){
                end = true;
            } else {
                aux.addLast(station);
            }
        }

        try{
            rede.insertLine(line);
            rede.addStationToLine(line, aux);
            System.out.println(Outputs.INSERT_LINE_OK.getString());
        } catch (LineAlreadyExistsException e) {
            System.out.println(Outputs.EXISTANT_LINE.getString());
        }
    }

    /**
     * Removes a line from the system
     * @param in - Scanner
     * @param rede - The system
     */
    private static void removeLine(Scanner in, Rede rede){
        String line = in.nextLine().trim();
        try {
            rede.removeLine(line);
            System.out.println(Outputs.REMOVE_LINE_OK.getString());
        } catch (NoLinesException e){
            System.out.println(Outputs.NONEXISTANT_LINE.getString());
        }
    }

    /**
     * Checks the stations of a given line
     * @param in - Scanner
     * @param rede - The system
     */
    private static void checkLine(Scanner in, Rede rede){
        String line = in.nextLine().trim();
        try {
            Iterator<String> ite = rede.iteratorByLine(line);
            while(ite.hasNext()){
                String station = ite.next();
                System.out.println(station);
            }
        } catch (NoLinesException e){
            System.out.println(Outputs.NONEXISTANT_LINE.getString());
        }
    }

    /**
     * Insert a train schedule to a given line
     * @param in - Scanner
     * @param rede - The system
     */
    private static void insertSchedule(Scanner in, Rede rede) {
        String line = in.nextLine().trim();

        int trainNr = in.nextInt();
        in.nextLine();
        boolean end = false;
        List<Entry<String, String>> stations = new DoubleList<>();
        while(!end){
            String stationAndHour = in.nextLine().trim();
            if (stationAndHour.isEmpty()){
                end = true;
            } else {
                int index = stationAndHour.indexOf(":");
                String station = stationAndHour.substring(0, index - 3);
                String hour = stationAndHour.substring(index - 2);
                Entry<String, String> entry = new EntryClass<>(station, hour);
                stations.addLast(entry);
            }
        }

        try {
            rede.insertSchedule(line, trainNr, stations);
            System.out.println(Outputs.SCHEDULE_INSERT_OK.getString());
        } catch (NoLinesException e){
            System.out.println(Outputs.NONEXISTANT_LINE.getString());
        } catch (InvalidScheduleException e){
            System.out.println(Outputs.INVALID_SCHEDULE.getString());
        }
    }

    /**
     * Removes a train schedule from a given line
     * @param in - Scanner
     * @param rede - The system
     */
    private static void removeSchedule(Scanner in, Rede rede){
        String line = in.nextLine().trim();

        String stationAndHour = in.nextLine().trim();
        int index = stationAndHour.indexOf(":");
        String station = stationAndHour.substring(0, index - 3);
        String hour = stationAndHour.substring(index - 2);

        try{
            rede.removeSchedule(line, station, hour);
            System.out.println(Outputs.SCHEDULE_REMOVE_OK.getString());
        } catch (NoLinesException e){
            System.out.println(Outputs.NONEXISTANT_LINE.getString());
        } catch (NonExistantScheduleException e){
            System.out.println(Outputs.NONEXISTANT_SCHEDULE.getString());
        }
    }

    /**
     * List the train schedules of a given line
     * @param in - Scanner
     * @param rede - The system
     */
    private static void listLineSchedule(Scanner in, Rede rede) {
        String line = in.nextLine().trim();
        String startingStation = in.nextLine().trim();

        try {
            Iterator<Train> ite = rede.scheduleByLineIterator(line, startingStation);
            if (ite != null){
                while (ite.hasNext()) {
                    Train t = ite.next();
                    System.out.printf("%d\n", t.getNr());
                    Iterator<Entry<String, Date>> scheduleIte = t.scheduleIterator();
                    while (scheduleIte.hasNext()) {
                        Entry<String, Date> entry = scheduleIte.next();
                        System.out.printf(Outputs.STATION_HOUR.getString(), entry.getKey(), entry.getValue().getHour(), entry.getValue().getMinutes());
                    }
                }
            }
        } catch (NoLinesException e){
            System.out.println(Outputs.NONEXISTANT_LINE.getString());
        } catch (NotStartingStationException e){
            System.out.println(Outputs.NONEXISTANT_START_STATION.getString());
        } catch (NoTrainsException ignored){};
    }

    /**
     * Checks the best train to catch given an arrival station and time
     * @param in - Scanner
     * @param rede - The system
     */
    private static void bestTimeTable(Scanner in, Rede rede){
        String line = in.nextLine().trim();
        String startingStation = in.nextLine().trim();
        String endingStation = in.nextLine().trim();
        String hour = in.nextLine().trim();

        try {
            Train t = rede.bestTimeTable(line, startingStation, endingStation, hour);
            if (t != null){
                System.out.println(t.getNr());
                Iterator<Entry<String, Date>> ite = t.scheduleIterator();
                while (ite.hasNext()){
                    Entry<String, Date> entry = ite.next();
                    System.out.printf(Outputs.STATION_HOUR.getString(), entry.getKey(), entry.getValue().getHour(), entry.getValue().getMinutes());
                }
            } else {
                System.out.println(Outputs.IMPOSSIBLE_ROUTE.getString());
            }
        } catch (NoLinesException e){
            System.out.println(Outputs.NONEXISTANT_LINE.getString());
        } catch (NotStartingStationException e){
            System.out.println(Outputs.NONEXISTANT_START_STATION.getString());
        } catch (NotPossibleException | NoTrainsException e){
            System.out.println(Outputs.IMPOSSIBLE_ROUTE.getString());
        }
    }

    /**
     * Terminates the system
     * @param rede - The system
     */
    private static void terminateApplication(Rede rede){
        save(rede);
        System.out.println(Outputs.APP_TERM.getString());
    }

    /**
     * Saves the current state
     * @param rede - The system
     */
    private static void save(Rede rede){
        try{
            ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(String.valueOf(Outputs.FILE)));
            file.writeObject(rede);
            file.flush();
            file.close();
        } catch (IOException e){
            System.out.println(Outputs.SAVE_ERROR.getString());
        }
    }

    /**
     * Loads a save state to the system
     * @return - Object of type Rede corresponding to a system already in memory
     */
    private static Rede load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(String.valueOf(Outputs.FILE)));
            Rede rede = (Rede) ois.readObject();
            ois.close();
            return rede;
        } catch (ClassNotFoundException | IOException e) {
            return new RedeClass();
        }
    }
}

/**
 * End of Class Main
 */