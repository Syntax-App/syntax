package edu.brown.cs.student.main.algo.graph;

/**
 * This class represents an Edge in a Graph
 */
public class Edge {

    private Node destination;
    private double weight;

    /**
     * This is the Edge constructor that initialize an Edge's
     * destination and weight
     * @param destination - the Node that the edge goes towards
     * @param weight - the double representing the edge's probability as its weight
     */
    public Edge(Node destination, double weight) {
        this.destination = destination;
        this.weight = weight;
    }

    /**
     * Accessor method that gets the destination of the edge
     * @return - the Node that the edge goes towards
     */
    public Node getDestination() {
        return this.destination;
    }

    /**
     * Accessor method that gets the weight of the edge
     * @return - the double representing the edge's probability as its weight
     */
    public double getWeight() {
        return this.weight;
    }
}
