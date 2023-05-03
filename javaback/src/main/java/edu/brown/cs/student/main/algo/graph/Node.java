package edu.brown.cs.student.main.algo.graph;

import java.util.List;
import java.util.Set;

public class Node {

    private int snippetID;
    private double difficultyScore;
    private Set<Edge> outgoingEdges;
    private List<Node> choices;

    public Node(int snippetID, double difficultyScore, Set<Edge> outgoingEdges) {
        this.snippetID = snippetID;
        this.difficultyScore = difficultyScore;
        this.outgoingEdges = outgoingEdges;
    }

    public int getSnippetID() {
        return this.snippetID;
    }

    public double getDifficultyScore() {
        return this.difficultyScore;
    }

    public Set<Edge> getOutgoingEdges() {
        return this.outgoingEdges;
    }

    public void setChoices(List<Node> choices) {
        this.choices = choices;
    }

    public List<Node> getChoices() {
        return this.choices;
    }
}
