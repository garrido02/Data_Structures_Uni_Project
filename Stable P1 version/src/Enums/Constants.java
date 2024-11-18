/**
 * Enum Constants
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package Enums;


/**
 * Enum Constants responsible for supporting the integration of constant variables into classes
 */
public enum Constants {
    /**
     * Constants
     */
    NOT_FOUND(-1),
    ZERO(0),
    ONE(1),
    DEFAULT_CAPACITY(30);


    int i;

    /**
     * Returns the value of the constant
     * @return
     */
    public int getValue(){ return this.i; }

    /**
     * Constructor
     * @param i - The value of the constant
     */
    Constants(int i) {
        this.i = i;
    }
}


/**
 * End of Enum Constants
 */
