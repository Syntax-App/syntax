package edu.brown.cs.student.main.algo.graph;

import java.util.Set;

public class Node {

    private int snippetId;
    private float difficultyScore;
    private Set<Edge> outgoingEdges;

    public Node(int snippetId, float difficultyScore, Set<Edge> outgoingEdges) {
        this.snippetId = snippetId;
        this.difficultyScore = difficultyScore;
        this.outgoingEdges = outgoingEdges;
    }

    public int getSnippetId() {
        return this.snippetId;
    }

    public float getDifficultyScore() {
        return this.difficultyScore;
    }

    public Set<Edge> getOutgoingEdges() {
        return this.outgoingEdges;
    }
}
