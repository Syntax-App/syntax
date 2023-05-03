package edu.brown.cs.student.main.algo.graph;

public class Edge {

    private Node source;
    private Node destination;
    private float weight;

    public Edge(Node source, Node destination, float weight) {
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

    public float getWeight() {
        return this.weight;
    }
}