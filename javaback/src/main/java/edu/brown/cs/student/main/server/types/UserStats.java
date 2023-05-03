package edu.brown.cs.student.main.server.types;

public class UserStats {
    int highlpm;
    double highacc;
    int numraces;
    double avglpm;
    double avgacc;
    double exp;

    public UserStats() {
    }

    public UserStats(int highlpm, double highacc, int numraces, double avglpm, double avgacc, double exp) {
        this.highlpm = highlpm;
        this.highacc = highacc;
        this.numraces = numraces;
        this.avglpm = avglpm;
        this.avgacc = avgacc;
        this.exp = exp;
    }

    public int getHighlpm() {
        return this.highlpm;
    }

    public double getHighacc() {
        return this.highacc;
    }

    public int getNumraces() {
        return this.numraces;
    }

    public double getAvglpm() {
        return this.avglpm;
    }

    public double getAvgacc() {
        return this.avgacc;
    }

    public double getExp() {
        return this.exp;
    }
}
