package edu.brown.cs.student.main.server;

import com.google.cloud.firestore.Firestore;
import edu.brown.cs.student.main.server.config.FirebaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class States {
    private Firestore db;
    private Map<String, LinkedList<Integer>> snippetStacks;
    private List<String> activeFileHeader;
    private List<List<String>> activeFileContent;

    /**
     * Constructor for the class, which initializes both shared lists.
     */
    public States() {
        this.activeFileHeader = new ArrayList<>();
        this.activeFileContent = new ArrayList<>();
        this.setupFirebase();
        this.snippetStacks = new HashMap<>();
    }

    /**
     * Gets the active CSV's data.
     * @return - A list of the CSV rows.
     */
    public List<List<String>> getActiveFileContent() {
        return this.activeFileContent;
    }

    /**
     * Gets the active CSV's header.
     * @return - A list of the CSV column titles.
     */
    public List<String> getActiveFileHeader() {
        return this.activeFileHeader;
    }

    public void setActiveFileContent(List<List<String>> activeFileContent) {
        this.activeFileContent = activeFileContent;
    }

    /**
     * Setter method for the active file header.
     * @param activeFileHeader - a list of String, representing the new CSV header.
     */
    public void setActiveFileHeader(List<String> activeFileHeader) {
        this.activeFileHeader = activeFileHeader;
    }

    /**
     * Clears all states. *for testing purposes*
     */
    public void resetAll() {
        this.activeFileHeader = new ArrayList<>();
        this.activeFileContent = new ArrayList<>();
    }

    public void setupFirebase() {
        FirebaseConfig firebaseConfig = new FirebaseConfig();
        firebaseConfig.initializeFirebase();
        this.db = firebaseConfig.getDb();
    }

    public Firestore getDb() {
        return this.db;
    }

    public Map<String, LinkedList<Integer>> getSnippetStacks() {
        return this.snippetStacks;
    }
}
