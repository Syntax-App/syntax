package edu.brown.cs.student.main.algo.graph;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Graph {

    private List<Node> availableNodes;
    private Node head;
    private float standardWeight;
    private List<Integer> snippetIDs;

    public Graph() {
        this.head = null;
        this.availableNodes = new ArrayList<>();
        this.standardWeight = 0.5f;
        this.snippetIDs = new ArrayList<>();
        try {
            this.populateNodeList();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Integer> findPath(Node node) {
        // if we aren't at end of graph
        if (node.getOutgoingEdges().size() > 0) {
            // "randomly" choose edge to take based on weights
            // Credit: https://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
            double p = Math.random();
            float cumulativeProbability = 0f;
            Node nextNode;
            for (Edge edge : node.getOutgoingEdges()) {
                cumulativeProbability += edge.getWeight();
                // chose an edge
                if (p <= cumulativeProbability) {
                    nextNode = edge.getDestination();
                    // add snippet to list
                    this.snippetIDs.add(edge.getDestination().getSnippetID());
                    // recursively go down the chosen edge
                    return this.findPath(nextNode);
                }
            }
        // base case: end of graph
        }

        return this.snippetIDs;

    }

    public void constructGraph(Firestore db, String email)
        throws ExecutionException, InterruptedException {
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        float userExperience =  (float) querySnapshot.get().getDocuments().get(0).getData().get("exp");
        this.standardWeight = this.standardWeight - (0.05f * userExperience);
        // TODO: check if user is max experience level (10)?

        this.head = this.determineHead(userExperience);
        this.availableNodes.remove(this.head);
        this.head.setChoices(this.availableNodes);

        this.addEdges(this.availableNodes.size(), this.head, userExperience, this.head.getChoices());
    }

    private void addEdges(int nodesLeft, Node node, float exp, List<Node> nodes) {
        if (nodes.size() != 0) {
            // edge destinations
            List<Node> choices = new ArrayList<>();
            // differences between difficulty and experience
            float[] diffs = new float[nodesLeft];
            // number of easier snippets
            float numEasy = 0;
            // sum of difficulty bumps
            float sum = 0;

            List<Integer> randList = new ArrayList<>();
            for (int i = 1; i < nodes.size(); i++) {
                randList.add(i);
            }
            Collections.shuffle(randList);
            for (int i = 0; i < 3; i++) {
                // add random node to choices
                choices.add(nodes.get(randList.get(i)));
                float diff = nodes.get(randList.get(i)).getDifficultyScore() - exp;
                if (diff < 0) {
                    numEasy++;
                }
                sum += Math.abs(diff);
                diffs[i] = diff;
            }

            float[] weights = new float[diffs.length];
            for(int i = 0; i < diffs.length; i++) {
                if (diffs[i] < 0) {
                    weights[i] =  this.standardWeight / numEasy;
                } else {
                    weights[i] = 1 - (Math.abs(diffs[i]) / sum);
                }
            }

            for (int i = 0; i < weights.length; i++) {
                node.getOutgoingEdges().add(new Edge(node, choices.get(i), weights[i]));
                List<Node> nodesCopy = new ArrayList<>(nodes);
                nodesCopy.remove(choices.get(i));
                choices.get(i).setChoices(nodesCopy);
                // recursively build graph
                this.addEdges(choices.get(i).getChoices().size(), choices.get(i), exp, choices.get(i).getChoices());
            }
        }
    }

    private Node determineHead(float userExperience) {
        float min = 0;
        int minIndex = 0;
        for (int i = 0; i < this.availableNodes.size(); i++) {
            float diff = Math.abs(userExperience - this.availableNodes.get(i).getDifficultyScore());
            if (diff < min) {
                min = diff;
                minIndex = i;
            }
        }
        return this.availableNodes.get(minIndex);
    }


    private void populateNodeList() throws IOException {
        this.availableNodes.add(new Node(1, 5, new HashSet<>()));
    }

    public List<Integer> getSnippetIDs() {
        return this.snippetIDs;
    }

    public Node getHead() {
        return this.head;
    }

    public List<Node> getAvailableNodes() {
        return this.availableNodes;
    }
}
