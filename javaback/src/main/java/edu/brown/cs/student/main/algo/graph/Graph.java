package edu.brown.cs.student.main.algo.graph;

import edu.brown.cs.student.main.algo.snippets.Snippets.SnippetsJSON;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents a Graph consisting of Nodes connected by weighted Edges,
 * each Node associated with a specific snippet
 */
public class Graph {

    private List<Integer> availableIDs;
    private Node head;
    private double standardWeight;
    private List<Integer> snippetIDs;
    private SnippetsJSON json;
    private String lang;

    /**
     * This is the Graph's constructor, initializing fields and reading a JSON for
     * snippet preparation.
     * @param lang - string representing the language selected by the user
     */
    public Graph(String lang) {
        this.head = null;
        this.availableIDs = new ArrayList<>();
        this.standardWeight = 0.5;
        this.snippetIDs = new ArrayList<>();
        this.lang = lang;

        try {
            JSONUtils jsonUtils = new JSONUtils();
            File snippetsFile;
            // extensible for multiple different languages
            switch(this.lang) {
                // typescript if specified
                case "TYPESCRIPT":
                    snippetsFile = new File("src/main/java/edu/brown/cs/student/main/algo/snippets/TSXSnippets.json");
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
            // store all snippet IDs
            this.populateIDList();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * This method recursively performs a weighted random walk through the built Graph to
     * create a list of snippet IDs to be used in snippet generation for the user
     * @param node - the Node to decide where to go from
     * @return - List of Integers representing the sequence of snippet IDs determined
     */
    public List<Integer> findPath(Node node) {
        // if we aren't at end of graph
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
                }
            }
            // recursively go down the chosen edge
            return this.findPath(nextNode);
        } else {
            // base case: end of graph so return whole path
            return this.snippetIDs;
        }
    }

    /**
     * This method initiates the construction of the Graph.
     * @param userExperience - double between 1-10 representing a rating of the user's experience
     */
    public void constructGraph(double userExperience) {
        // this calculation sets the standardized weight of easier-difficulty snippet probabilities
        // based on user experience; the higher the experience, the less chance for the user to
        // encounter easier snippets
        this.standardWeight = this.standardWeight - (0.05 * userExperience);

        // set start of graph
        int minIndex = this.determineHead(userExperience);
        this.availableIDs.remove(minIndex);

        // build graph
        this.head = this.addEdges(this.availableIDs.size(), userExperience,
            this.availableIDs, minIndex);
        this.snippetIDs.add(this.head.getSnippetID());
    }

    /**
     * This method recursively builds the graph by adding edges with calculated weights
     * to the current Node.
     * @param nodesLeft - int representing the number of Nodes left to choose from for this Node
     * @param exp - double representing the user's experience
     * @param availableIDs - List of Integers representing the IDs left to choose from for this Node
     * @param snippetID - int representing the snippet ID of the Node to be returned
     * @return the current Node with recursively-added Edges
     */
    private Node addEdges(int nodesLeft, double exp, List<Integer> availableIDs, int snippetID) {
        // add edges as long as there are nodes left
        if (nodesLeft != 0) {
            // IDs of edge destination Nodes
            List<Integer> destinationIDs = new ArrayList<>();
            // differences between difficulty and experience
            // maximum of 3 choices or however many nodes are left under 3
            double[] diffs = new double[Math.min(3, nodesLeft)];

            double numEasy = 0;
            double sum = 0;

            List<Integer> randList = new ArrayList<>(availableIDs);
            Collections.shuffle(randList);
            for (int i = 0; i < Math.min(3, nodesLeft); i++) {
                // add random node ID and difference to choices
                destinationIDs.add(randList.get(i));
                double diff = this.json.array()[i].difficulty() - exp;
                diffs[i] = diff;
                // keep track of number of easier snippets
                if (diff <= 0) {
                    numEasy++;
                }
                // keep track of sum of differences independent of easier snippets
                if (diff > 0) {
                    sum += diff;
                }
            }

            // calculate weights
            double[] weights = this.calculateWeights(numEasy, diffs, sum);

            Set<Edge> edges = new HashSet<>();
            for (int i = 0; i < weights.length; i++) {
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
            // base case
            return new Node(snippetID, this.json.array()[snippetID].difficulty(), new HashSet<>());
        }
    }

    public double[] calculateWeights(double numEasy, double[] diffs, double sum) {
        double[] weights = new double[diffs.length];
        double proportionForHard;

        // if there are easier snippet choices, set the proportion accordingly
        if (numEasy > 0) {
            proportionForHard = 1 - this.standardWeight;
        } else {
            // if there are only harder snippet choices
            proportionForHard = 1;
        }
        // if all snippet choices are easy, the weights are 1 divided by the number of choices
        if (numEasy == diffs.length) {
            for (int i = 0; i < diffs.length; i++) {
                weights[i] = 1 / numEasy;
            }
        } else {
            // keep track of harder snippets
            ArrayList<Integer> positiveDeltas = new ArrayList<>();
            for (int i = 0; i < diffs.length; i++) {
                // if snippet is easy, divide standard weight by number of easy snippets
                if (diffs[i] <= 0) {
                    weights[i] = (this.standardWeight / numEasy);
                } else {
                    positiveDeltas.add(i);
                    // case where there's only one hard snippet
                    if (diffs[i] == sum) {
                        weights[i] = 1 - this.standardWeight;
                    } else {
                        // case where there's either 2 or 3
                        weights[i] = (diffs[i] / sum) * proportionForHard;
                    }
                }
            }

            // swap the weights of the snippet with highest difficulty
            // and lower difficulty
            if (positiveDeltas.size() > 1) {
                this.swapWeights(weights, positiveDeltas);
            }
        }
        return weights;
    }

    /**
     * Helper method to swap weights. Made public for testing
     */
    public void swapWeights(double[] weights, ArrayList<Integer> positiveDeltas) {
        double minVal = Double.POSITIVE_INFINITY;
        double maxVal = Double.NEGATIVE_INFINITY;
        int minIndex = 0;
        int maxIndex = 0;
        // find max and min weights
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
        // swap max and min weights
        weights[maxIndex] = minVal;
        weights[minIndex] = maxVal;
    }

    /**
     * This method determines the head Node of the Graph, which is the Node with
     * the least difference between user experience and snippet difficulty
     * @param userExperience - double representing user experience
     * @return - snippet ID of head node
     */
    private int determineHead(double userExperience) {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        // find minimum difference between user experience and snippet difficulty
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

    /**
     * This method populates the List of available snippet IDs of a Graph
     * using JSON data
     */
    private void populateIDList() {
        for (int i = 0; i < this.json.array().length; i++) {
            this.availableIDs.add(i);
        }
    }

    /**
     * This accessor method gets the head Node of the Graph
     * @return - the head Node of the graph
     */
    public Node getHead() {
        return this.head;
    }

    /**
     * This accessor method gets the head node's ID
     * @return - ID of head node
     */
    public int getHeadID() {
        return this.head.getSnippetID();
    }

    /**
     * This accessor method gets the List of available IDs
     * @return - List of Integers representing available IDs
     */
    public List<Integer> getAvailableIDs() {
        return this.availableIDs;
    }

    /**
     * This accessor method gets the JSON containing snippet data
     * @return - SnippetsJSON containing snippet data
     */
    public SnippetsJSON getJson() {
        return this.json;
    }

    public double getStandardWeight() {
        return this.standardWeight;
    }
}
