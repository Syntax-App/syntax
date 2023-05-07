package edu.brown.cs.student.main.algo.graph;

import java.util.Set;

/**
 * This class represents a Node of a Graph.
 */
public class Node {

    private int snippetID;
    private double difficultyScore;
    private Set<Edge> outgoingEdges;

    /**
     * This is the Node constructor, which initializes a snippet ID, difficulty score, and outgoing edges
     * @param difficultyScore - double representing the difficulty rating of the snippet
     * @param snippetID - int representing the ID number of the Node's snippet
     * @param outgoingEdges - a Set of outgoing Edges from this Node
     */
    public Node(int snippetID, double difficultyScore, Set<Edge> outgoingEdges) {
        this.snippetID = snippetID;
        this.difficultyScore = difficultyScore;
        this.outgoingEdges = outgoingEdges;
    }

    /**
     * This accessor method gets the snippet ID of the Node
     * @return - int representing the ID number of the Node's snippet
     */
    public int getSnippetID() {
        return this.snippetID;
    }

    /**
     * This accessor method gets the outgoing edges of the Node
     * @return - a Set of outgoing Edges from this Node
     */
    public Set<Edge> getOutgoingEdges() {
        return this.outgoingEdges;
    }
}
