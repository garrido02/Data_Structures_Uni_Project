/**
 * Interface Date
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */



package RedeFerroviaria;
import java.io.Serializable;


/**
 * Interface Date responsible to prototype methods to handle a Date Object, extending Serializable
 */
public interface Date extends Serializable, Comparable<Date> {

    /**
     * Instance variable of the number of minutes in one hour
     */
    int MINUTES_IN_HOUR = 60;

    /**
     * Calculates how many minutes in a given number of hours
     * @param hour - Number of hours
     * @return The number of minutes in a given number of hours
     */
    static int hourToMin(int hour){
        return hour * MINUTES_IN_HOUR;
    }

    /**
     * Calculates the difference in minutes between two dates
     * @param d1 - Date 1
     * @param d2 - Date 2
     * @return The difference in minutes between the two dates
     */
    static int calculateDiff(Date d1, Date d2){
        return (hourToMin(d1.getHour()) + d1.getMinutes()) - (hourToMin(d2.getHour()) + d2.getMinutes());
    }

    /**
     * Returns the hour of a date
     * @return The hour of a date
     */
    int getHour();

    /**
     * Returns the minutes of a date
     * @return The minutes of a date
     */
    int getMinutes();
}


/**
 * End of Date
 */
