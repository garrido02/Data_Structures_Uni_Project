/**
 * Interface TrainUpdatable
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */

package RedeFerroviaria;

import dataStructures.Entry;
import dataStructures.List;

/**
 * Interface TrainUpdatable responsible to prototype set methods to handle a Train Object, extending the Train interface and Serializable
 */
public interface TrainUpdatable extends Train{
    /**
     * Sets the schedule of a train
     * @param trainSchedule - The schedule of a train
     */
    void setSchedule(List<Entry<String, Date>> trainSchedule);
}

/**
 * End of TrainUpdatable Interface
 */
