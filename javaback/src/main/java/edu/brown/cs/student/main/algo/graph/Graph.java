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

    private List<Integer> availableIDs;
    private Node head;
    private double standardWeight;
    private List<Integer> snippetIDs;
    private SnippetsJSON json;
    private String lang;

    public Graph(String lang) {
        this.head = null;
        this.availableIDs = new ArrayList<>();
        this.standardWeight = 0.5f;
        this.snippetIDs = new ArrayList<>();
        this.lang = lang;

        try {
            JSONUtils jsonUtils = new JSONUtils();
            File snippetsFile;
            // extensible for multiple different languages
            switch(lang) {
                // typescript if specified
                case "typescript":
                    snippetsFile = new File();
                    break;
                // java by default
                default:
                    snippetsFile = new File(
                        "src/main/java/edu/brown/cs/student/main/algo/snippets/JavaSnippets.json");
                    break;
            }
            Reader reader = new FileReader(snippetsFile);
            String snippetsString = jsonUtils.readerToString(reader);

            this.json = jsonUtils.fromJson(SnippetsJSON.class, snippetsString);
            this.populateIDList();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Integer> findPath(Node node) {
        // if we aren't at end of graph
        // node.getOutgoingEdges().size() > 0
        if (node != null) {
            // "randomly" choose edge to take based on weights
            // Credit: https://stackoverflow.com/questions/9330394/how-to-pick-an-item-by-its-probability
            double p = Math.random();
            double cumulativeProbability = 0;
            Node nextNode = null;
            for (Edge edge : node.getOutgoingEdges()) {
                cumulativeProbability += edge.getWeight();
                // chose an edge
                if (p <= cumulativeProbability) {
                    nextNode = edge.getDestination();
                    // add snippet to list
                    this.snippetIDs.add(edge.getDestination().getSnippetID());
                    break;
                    // return this.findPath(nextNode);
                }
            }
            // recursively go down the chosen edge
            return this.findPath(nextNode);
            // base case: end of graph
        } else {
            return this.snippetIDs;
        }
    }

    public void constructGraph(double userExperience) {
        this.standardWeight = this.standardWeight - (0.05 * userExperience);
        // TODO: check if user is max experience level (10)?

        int minIndex = this.determineHead(userExperience);
        this.availableIDs.remove(minIndex);

        this.head = this.addEdges(this.availableIDs.size(), userExperience,
            this.availableIDs, minIndex);
    }

    private Node addEdges(int nodesLeft, double exp, List<Integer> availableIDs, int snippetID) {
        if (nodesLeft != 0) {
            // edge destinations
            List<Integer> destinationIDs = new ArrayList<>();
            // differences between difficulty and experience
            double[] diffs = new double[Math.min(3, nodesLeft)];
            // number of easier snippets
            double numEasy = 0;
            // sum of difficulty bumps
            double sum = 0;

            List<Integer> randList = new ArrayList<>(availableIDs);
//            for (int i = 0; i < nodesLeft; i++) {
//                randList.add(i);
//            }
            Collections.shuffle(randList);
            for (int i = 0; i < Math.min(3, nodesLeft); i++) {
                // add random node to choices
                destinationIDs.add(randList.get(i));
                double diff = this.json.array()[i].difficulty() - exp;
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

            Set<Edge> edges = new HashSet<>();
            for (int i = 0; i < weights.length; i++) {
//                for (Dataset subset: trainingDataSubset.partition(splitOnAttribute)) {
//                    ITreeNode subtree = this.generateTreeHelper(subset);
//                    edges.add(new ValueEdge(subset.getSharedValue(splitOnAttribute), subtree));
//                }
//
//                return new AttributeNode(splitOnAttribute, mostCommonClassification, edges);
                // make copy of available IDs
                List<Integer> copyOfIDs = new ArrayList<>(availableIDs);
                // remove destination's ID from available IDs
                copyOfIDs.remove(destinationIDs.get(i));
                // recursively build child node
                Node subNode = this.addEdges(copyOfIDs.size(), exp,
                    copyOfIDs, destinationIDs.get(i));
                // add new edge for parent node pointing towards child node
                edges.add(new Edge(subNode, weights[i]));
            }
            // return parent node
            return new Node(snippetID, this.json.array()[snippetID].difficulty(), edges);
        } else {
            return new Node(snippetID, this.json.array()[snippetID].difficulty(), new HashSet<>());
        }
    }

    private int determineHead(double userExperience) {
        double min = 0;
        int minIndex = 0;
        for (int i = 0; i < this.availableIDs.size(); i++) {
            double diff = Math.abs(
                userExperience - this.json.array()[i].difficulty());
            if (diff < min) {
                min = diff;
                minIndex = i;
            }
        }
        return minIndex;
    }


    private void populateIDList() throws IOException {
        // TODO: insert strategy pattern here - different ways of loading in snippets
        // TODO: code below is one strategy pattern
        for (int i = 0; i < this.json.array().length; i++) {
            this.availableIDs.add(i);
        }
    }

    public List<Integer> getSnippetIDs() {
        return this.snippetIDs;
    }

    public Node getHead() {
        return this.head;
    }

    public List<Integer> getAvailableIDs() {
        return this.availableIDs;
    }
}
