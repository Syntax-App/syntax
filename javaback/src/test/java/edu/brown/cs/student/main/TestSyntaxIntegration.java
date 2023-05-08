package edu.brown.cs.student.main;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.handlers.race.EndHandler;
import edu.brown.cs.student.main.server.handlers.race.StartHandler;
import edu.brown.cs.student.main.server.handlers.user.UserCreateHandler;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler;
import edu.brown.cs.student.main.server.handlers.user.UserRankHandler;
import edu.brown.cs.student.main.server.types.User;
import edu.brown.cs.student.main.server.types.UserStats;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import okio.Buffer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spark.Spark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSyntaxIntegration {
    // shared state
    private final States states = new States();

    // set up server
    @BeforeAll
    public void setup() {
        Spark.port(0);
        Logger.getLogger("").setLevel(Level.WARNING); // empty name = root logger

        // Setting up the handler for the GET /order endpoint
        // User routes!
        Spark.get("user/get", new UserGetHandler(states));
        Spark.post("user/create", new UserCreateHandler(states));
        Spark.get("user/ranking", new UserRankHandler(states));

        // Race routes!
        Spark.get("race/start", new StartHandler(states));
        Spark.post("race/end", new EndHandler(states));

        Spark.init();
        Spark.awaitInitialization();
    }

    /**
     * Helper to start a connection to a specific API endpoint/params
     * @param apiCall the call string, including endpoint
     *                (NOTE: this would be better if it had more structure!)
     * @return the connection for the given URL, just after connecting
     * @throws java.io.IOException if the connection fails for some reason
     */
    static private HttpURLConnection tryGETRequest(String apiCall) throws IOException {
        // Configure the connection (but don't actually send the request yet)
        URL requestURL = new URL("http://localhost:"+Spark.port()+"/"+apiCall);
        HttpURLConnection clientConnection = (HttpURLConnection) requestURL.openConnection();

        clientConnection.connect();
        return clientConnection;
    }

    // helper to make post request
    static private HttpResponse<String> tryPOSTRequest(String apiCall, String body) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:"+Spark.port()+"/"+apiCall))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    /**
     * Helper method to parse a response return by our Server itself in a custom
     * given format.
     * @param clientConnection - connection to the API (ourselves)
     * @param customClass - format in which we want to parse the JSON data in.
     * @param <T> - generic return type
     * @return - instance of CustomClass, with parsed data
     * @throws IOException - potential exception thrown by Moshi's fromJson.
     */
    static private <T> T getResponse(HttpURLConnection clientConnection, Class<T> customClass) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(customClass).fromJson(new Buffer().readFrom(clientConnection.getInputStream()));
    }

    /**
     * Helper method to parse a response return by our Server itself in a custom
     * given format.
     * @param clientConnection - connection to the API (ourselves)
     * @param customClass - format in which we want to parse the JSON data in.
     * @param <T> - generic return type
     * @return - instance of CustomClass, with parsed data
     * @throws IOException - potential exception thrown by Moshi's fromJson.
     */
    static private <T> T getResponse(HttpResponse<String> response, Class<T> customClass) throws IOException {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(customClass).fromJson(response.body());
    }

    // helper to turn connection response into string
    static private String request(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder string = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            string.append(line);
        }
        reader.close();
        return string.toString();
    }

    // helper to delete document from database
    private void deleteDocument(String email) throws ExecutionException, InterruptedException {
        Firestore db = this.states.getDb();
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        if (!querySnapshot.get().getDocuments().isEmpty()) {
            querySnapshot.get().getDocuments().get(0).getReference().delete();
        }
    }

    /**
     * Tests for User Creation Handler,
     * and interactions
     */
    @Test
    public void testCreateHandlerValid() throws IOException, InterruptedException, ExecutionException {
        HttpResponse<String> response = tryPOSTRequest("/user/create", "{\"email\":\"testcreateuseremail@testemail.com\",\"name\":\"Test User\",\"pic\":\"example.org\"}");
        assertEquals(200, response.statusCode());
        UserCreateHandler.CreateSuccessResponse createResponse = getResponse(response, UserCreateHandler.CreateSuccessResponse.class);
        assertNotNull(createResponse);

        User userObject = createResponse.data().get("user");
        assertEquals("success", createResponse.status());
        assertEquals("testcreateuseremail@testemail.com", userObject.getEmail());
        assertEquals("Test User", userObject.getName());
        assertEquals("example.org", userObject.getPic());
        assertNotNull(userObject.getUuid());

        assertEquals(0, userObject.getStats().getAvgacc());
        assertEquals(0, userObject.getStats().getExp());
        assertEquals(0, userObject.getStats().getHighlpm());
        assertEquals(0, userObject.getStats().getNumraces());
        assertEquals(0, userObject.getStats().getAvglpm());
        assertEquals(0, userObject.getStats().getHighacc());

        deleteDocument("testcreateuseremail@testemail.com");
    }

    // tests creation of a new user and that they are stored in the database
    @Test
    public void testCreateUserExists() throws IOException, InterruptedException, ExecutionException {
        HttpResponse<String> response = tryPOSTRequest("/user/create", "{\"email\":\"testcreateuseremail@testemail.com\",\"name\":\"Test User\",\"pic\":\"example.org\"}");
        assertEquals(200, response.statusCode());
        UserCreateHandler.CreateSuccessResponse createResponse = getResponse(response, UserCreateHandler.CreateSuccessResponse.class);
        assertNotNull(createResponse);

        response = tryPOSTRequest("/user/create", "{\"email\":\"testcreateuseremail@testemail.com\",\"name\":\"Test User\",\"pic\":\"example.org\"}");
        UserCreateHandler.CreateFailureResponse failureResponse = getResponse(response, UserCreateHandler.CreateFailureResponse.class);

        assertEquals("error", failureResponse.status());
        assertEquals("User with given email already exists!", failureResponse.error_message());

        deleteDocument("testcreateuseremail@testemail.com");
    }

    // tests invalid user creation due to missing field
    @Test
    public void testCreateUserInvalid() throws IOException, InterruptedException, ExecutionException {
        HttpResponse<String> response = tryPOSTRequest("/user/create", "{\"name\":\"Test User\",\"pic\":\"example.org\"}");
        UserCreateHandler.CreateFailureResponse failureResponse = getResponse(response, UserCreateHandler.CreateFailureResponse.class);

        assertEquals("error", failureResponse.status());
        assertEquals("One or more fields were not provided!", failureResponse.error_message());
    }

    // tests valid getting of user data
    @Test
    public void testGetUserValid() throws IOException, InterruptedException, ExecutionException {
        HttpResponse<String> response = tryPOSTRequest("/user/create", "{\"email\":\"testcreateuseremail@testemail.com\",\"name\":\"Test User\",\"pic\":\"example.org\"}");
        assertEquals(200, response.statusCode());
        HttpURLConnection clientConnection = tryGETRequest("user/get?email=testcreateuseremail@testemail.com");
        assertEquals(200, clientConnection.getResponseCode());

        UserGetHandler.GetUserSuccessResponse successResponse = getResponse(clientConnection, UserGetHandler.GetUserSuccessResponse.class);

        User userObject = successResponse.data().get("user");

        assertEquals("success", successResponse.status());
        assertEquals("testcreateuseremail@testemail.com", userObject.getEmail());
        assertEquals("Test User", userObject.getName());
        assertEquals("example.org", userObject.getPic());
        assertNotNull(userObject.getUuid());

        assertEquals(0, userObject.getStats().getAvgacc());
        assertEquals(0, userObject.getStats().getExp());
        assertEquals(0, userObject.getStats().getHighlpm());
        assertEquals(0, userObject.getStats().getNumraces());
        assertEquals(0, userObject.getStats().getAvglpm());
        assertEquals(0, userObject.getStats().getHighacc());

        this.deleteDocument("testcreateuseremail@testemail.com");
    }

    // tests error from getting user that doesn't already exist in database
    @Test
    public void testGetUserNotExist() throws IOException, ExecutionException, InterruptedException {
        this.deleteDocument("testcreateuseremail@testemail.com");
        HttpURLConnection clientConnection = tryGETRequest("user/get?email=testcreateuseremail@testemail.com");
        assertEquals(200, clientConnection.getResponseCode());

        UserGetHandler.GetUserFailureResponse failureResponse = getResponse(clientConnection, UserGetHandler.GetUserFailureResponse.class);
        assertEquals("error", failureResponse.status());
        assertEquals("User with given email does not exist!", failureResponse.error_message());
    }

    // tests error from getting user without specifying email
    @Test
    public void testGetUserNoEmail() throws IOException {
        HttpURLConnection clientConnection = tryGETRequest("user/get");
        assertEquals(200, clientConnection.getResponseCode());

        UserGetHandler.GetUserFailureResponse failureResponse = getResponse(clientConnection, UserGetHandler.GetUserFailureResponse.class);
        assertEquals("error", failureResponse.status());
        assertEquals("No email parameter was provided!", failureResponse.error_message());
    }

    // tests ranking all users
    @Test
    public void testRankUsers() throws IOException, ExecutionException, InterruptedException {
        HttpURLConnection clientConnection = tryGETRequest("user/ranking");
        assertEquals(200, clientConnection.getResponseCode());

        UserRankHandler.RankSuccessResponse successResponse = getResponse(clientConnection, UserRankHandler.RankSuccessResponse.class);
        assertEquals(successResponse.data().get("ranking").size(), this.states.getDb().collection("users").get().get().getDocuments().size());
    }

    // tests getting response with snippet when specifying email
    // NOTE: can't test for specific snippet since snippet generation is random
    @Test
    public void testStartHandlerValid() throws IOException {
        HttpURLConnection clientConnection = tryGETRequest("race/start?email=ayman_benjelloun_touimi@brown.edu");
        assertEquals(200, clientConnection.getResponseCode());
        String response = request(clientConnection);
        assert(response.contains("{\"status\":\"success\",\"data\":{\"snippet\":\""));
        assert(response.contains("\"explanation\":\""));
    }

    // tests getting response with snippet without specifying email
    // NOTE: can't test for specific snippet since snippet generation is random
    @Test
    public void testStartHandlerNoEmail() throws IOException {
        HttpURLConnection clientConnection = tryGETRequest("race/start");
        assertEquals(200, clientConnection.getResponseCode());

        String response = request(clientConnection);
        assert(response.contains("{\"status\":\"success\",\"data\":{\"snippet\":\""));
        assert(response.contains("\"explanation\":\""));
    }

    // tests error from starting race with invalid email
    @Test
    public void testStartHandlerEmailNotFound() throws IOException {
        HttpURLConnection clientConnection = tryGETRequest("race/start?email=nim_telson@brown.edu");
        assertEquals(200, clientConnection.getResponseCode());

        StartHandler.StartFailureResponse failureResponse = getResponse(clientConnection, StartHandler.StartFailureResponse.class);
        assertEquals("error", failureResponse.status());
        assertEquals("User not found", failureResponse.error_message());
    }

    // tests response with new stats with valid email
    @Test
    public void testEndHandlerValid() throws IOException, InterruptedException {
        HttpResponse<String> response = tryPOSTRequest("/race/end", "{\"email\":\"ayman_benjelloun_touimi@brown.edu\",\"recentlpm\":\"100\",\"recentacc\":\"99\"}");
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("{\"status\":\"success\",\"data\":{\"updated_stats\":{\"avgacc\":99.0,\"avglpm\":100.0,\"exp\":"));
    }

    // tests error from ending race without specifying email
    @Test
    public void testEndHandlerNoEmail() throws IOException, InterruptedException {
        HttpResponse<String> response = tryPOSTRequest("/race/end", "{\"recentlpm\":\"100\",\"recentacc\":\"99\"}");
        assertEquals(200, response.statusCode());
        System.out.println(response.body());
        assertEquals("{\"status\":\"error\",\"error_message\":\"User with given email does not exist!\"}", response.body());
    }

    // tests error from ending race with an invalid email
    @Test
    public void testEndHandlerEmailNotFound() throws IOException, InterruptedException {
        HttpResponse<String> response = tryPOSTRequest("/race/end", "{\"email\":\"chiqwat@casablanca.edu\",\"recentlpm\":\"100\",\"recentacc\":\"99\"}");
        assertEquals(200, response.statusCode());
        assertEquals("{\"status\":\"error\",\"error_message\":\"User with given email does not exist!\"}", response.body());
    }
}
