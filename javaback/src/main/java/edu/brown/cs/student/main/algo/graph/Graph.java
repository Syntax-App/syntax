package edu.brown.cs.student.main.algo.graph;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import edu.brown.cs.student.main.algo.snippets.Snippets.SnippetsJSON;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Graph {

    private List<Node> availableNodes;
    private Node head;
    private double standardWeight;
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
            double cumulativeProbability = 0;
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
        } else {
            return this.snippetIDs;
        }
        return new ArrayList<>();
    }

    public void constructGraph(double userExperience) {
        this.standardWeight = this.standardWeight - (0.05 * userExperience);
        // TODO: check if user is max experience level (10)?

        this.head = this.determineHead(userExperience);
        this.availableNodes.remove(this.head);
        this.head.setChoices(this.availableNodes);

        this.addEdges(this.availableNodes.size(), this.head, userExperience,
            this.head.getChoices());
    }

    private void addEdges(int nodesLeft, Node node, double exp, List<Node> nodes) {
        if (nodes.size() != 0) {
            // edge destinations
            List<Node> choices = new ArrayList<>();
            // differences between difficulty and experience
            double[] diffs = new double[3];
            // number of easier snippets
            double numEasy = 0;
            // sum of difficulty bumps
            double sum = 0;

            List<Integer> randList = new ArrayList<>();
            for (int i = 0; i < nodesLeft; i++) {
                randList.add(i);
            }
            Collections.shuffle(randList);
            for (int i = 0; i < Math.min(3, nodesLeft); i++) {
                // add random node to choices
                choices.add(nodes.get(randList.get(i)));
                double diff = nodes.get(randList.get(i)).getDifficultyScore() - exp;
                if (diff <= 0) {
                    numEasy++;
                }
                if (diff > 0) {
                    sum += diff;
                }
                //sum += diff;
                diffs[i] = diff;
            }

            double[] weights = new double[diffs.length];
            double proportionForHard;
            if (numEasy > 0) {
                proportionForHard = 1 - this.standardWeight;
            } else {
                proportionForHard = 1;
            }
            if (numEasy == diffs.length) {
                for (int i = 0; i < diffs.length; i++) {
                    weights[i] = 1 / numEasy;
                }
            } else {
                ArrayList<Integer> positiveDeltas = new ArrayList<>();
                for (int i = 0; i < diffs.length; i++) {
                    if (diffs[i] <= 0) {
                        weights[i] = (this.standardWeight / numEasy);
                    } else {
                        positiveDeltas.add(i);
                        //weights[i] = proportionForHard - (Math.abs(diffs[i]) / sum) * proportionForHard;
                        if (diffs[i] == sum) {
                            // case where there's only one harder
                            weights[i] = 1 - this.standardWeight;
                        } else {
                            // case where there's either 2 or 3
                            weights[i] = (diffs[i] / sum) * proportionForHard;
                        }
                    }
                }

                // flipping the positives
                if (positiveDeltas.size() > 1) {
                    double minVal = Double.POSITIVE_INFINITY;
                    double maxVal = Double.NEGATIVE_INFINITY;
                    int minIndex = 0;
                    int maxIndex = 0;
                    for (int j = 0; j < positiveDeltas.size(); j++) {
                        if (weights[positiveDeltas.get(j)] > maxVal) {
                            maxVal = weights[positiveDeltas.get(j)];
                            maxIndex = positiveDeltas.get(j);
                        }
                        if (weights[positiveDeltas.get(j)] < minVal) {
                            minVal = weights[positiveDeltas.get(j)];
                            minIndex = positiveDeltas.get(j);
                        }
                    }
                    weights[maxIndex] = minVal;
                    weights[minIndex] = maxVal;
                    //
                }
            }

            for (int i = 0; i < weights.length; i++) {
                node.getOutgoingEdges().add(new Edge(node, choices.get(i), weights[i]));
                List<Node> nodesCopy = new ArrayList<>(nodes);
                nodesCopy.remove(choices.get(i));
                choices.get(i).setChoices(nodesCopy);
                // recursively build graph
                this.addEdges(choices.get(i).getChoices().size(), choices.get(i), exp,
                    choices.get(i).getChoices());
            }
        }
    }

    private Node determineHead(double userExperience) {
        double min = 0;
        int minIndex = 0;
        for (int i = 0; i < this.availableNodes.size(); i++) {
            double diff = Math.abs(
                userExperience - this.availableNodes.get(i).getDifficultyScore());
            if (diff < min) {
                min = diff;
                minIndex = i;
            }
        }
        return this.availableNodes.get(minIndex);
    }


    private void populateNodeList() throws IOException {
        // TODO: insert strategy pattern here - different ways of loading in snippets
        // TODO: code below is one strategy pattern
        JSONUtils jsonUtils = new JSONUtils();
        File snippetsFile = new File(
            "src/main/java/edu/brown/cs/student/main/algo/snippets/JavaSnippets.json");
        Reader reader = new FileReader(snippetsFile);
        String snippetsString = jsonUtils.readerToString(reader);

        SnippetsJSON json = jsonUtils.fromJson(SnippetsJSON.class, snippetsString);
        for (int i = 0; i < json.array().length; i++) {
            this.availableNodes.add(new Node(i, json.array()[i].difficulty(), new HashSet<>()));
        }
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
