package edu.brown.cs.student.main.algo.graph;

public class Edge {

    private Node source;
    private Node destination;
    private double weight;

    public Edge(Node destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Node getSource() {
        return this.source;
    }

    public Node getDestination() {
        return this.destination;
    }

    public double getWeight() {
        return this.weight;
    }
}
