package edu.brown.cs.student.main.server.types;

/**
 * This class represents UserStats
 */
public class UserStats {
    int highlpm;
    double highacc;
    int numraces;
    double avglpm;
    double avgacc;
    double exp;

    /**
     * UserStats constructor
     */
    public UserStats() {
    }

    /**
     * UserStats constructor that initializes the various statistics
     */
    public UserStats(int highlpm, double highacc, int numraces, double avglpm, double avgacc, double exp) {
        this.highlpm = highlpm;
        this.highacc = highacc;
        this.numraces = numraces;
        this.avglpm = avglpm;
        this.avgacc = avgacc;
        this.exp = exp;
    }

    /**
     * Accessor method to get a user's high score of lines per minute
     * @return - int representing user's highest lpm
     */
    public int getHighlpm() {
        return this.highlpm;
    }

    /**
     * Accessor method to get a user's highest accuracy
     * @return - double representing user's highest accuracy
     */
    public double getHighacc() {
        return this.highacc;
    }

    /**
     * Accessor method to get a user's number of completed races
     * @return - integer representing number of races a user has completed
     */
    public int getNumraces() {
        return this.numraces;
    }

    /**
     * Accessor method to get a user's average lines per minute
     * @return - double representing average lines per minute of user
     */
    public double getAvglpm() {
        return this.avglpm;
    }

    /**
     * Accessor method to get a user's average accuracy
     * @return - double representing user's average accuracy
     */
    public double getAvgacc() {
        return this.avgacc;
    }

    /**
     * Accessor method to get a user's experience level
     * @return - double representing user's experience level
     */
    public double getExp() {
        return this.exp;
    }
}
