/**
 * Class Date
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;


/**
 * Class Date responsible to implement the methods of the Date interface
 */
public class DateClass implements Date{
    /**
     * Instance variables
     */
    private int minutes;
    private int hour;

    static final long serialVersionUID = 0L;

    /**
     * Constructor given an hour timestamp
     * @param time - The hour timestamp
     */
    public DateClass(String time){
        String[] t = time.split(":");
        hour = Integer.parseInt(t[0]);
        minutes = Integer.parseInt(t[1]);
    }

    /**
     * Constructor given hour and minutes discriminated
     * @param hour - Hour
     * @param minutes -Minutes
     */
    public DateClass(int hour, int minutes){
        this.hour = hour;
        this.minutes = minutes;
    }

    @Override
    public int getHour() {
        return hour;
    }

    @Override
    public int getMinutes() {
        return minutes;
    }

    @Override
    public int compareTo(Date o) {
        return (Date.hourToMin(hour) + minutes) - (Date.hourToMin(o.getHour()) + o.getMinutes());
    }
}

/**
 * End of Class Date
 */