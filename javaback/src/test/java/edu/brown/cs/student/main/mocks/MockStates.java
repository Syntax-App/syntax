package edu.brown.cs.student.main.mocks;

import com.google.cloud.firestore.Firestore;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.config.FirebaseConfig;
import java.util.ArrayList;
import java.util.List;

public class MockStates extends States {
    private List<String> activeFileHeader;
    private List<List<String>> activeFileContent;

    /**
     * Constructor for the class, which initializes both shared lists.
     */
    public MockStates() {
        this.activeFileHeader = new ArrayList<>();
        this.activeFileContent = new ArrayList<>();
    }

    @Override
    public void setupFirebase() {}

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
}
