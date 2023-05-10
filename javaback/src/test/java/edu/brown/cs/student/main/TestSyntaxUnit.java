package edu.brown.cs.student.main;

import edu.brown.cs.student.main.algo.graph.Graph;
import edu.brown.cs.student.main.algo.snippets.GPTProxyCache;
import edu.brown.cs.student.main.algo.snippets.GPTRequester;

import edu.brown.cs.student.main.mocks.MockJSON;
import edu.brown.cs.student.main.mocks.MockStates;
import edu.brown.cs.student.main.server.handlers.user.UserCreateHandler;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Arrays;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/**
 * Unit testing class - includes mocking, serialization testing, algo testing, and cache testing, and fuzz testing
 */
public class TestSyntaxUnit {

    // mocks getting user data
    @Test
    public void testCheckData() {
        MockStates state = new MockStates();
        List<String> data = MockJSON.mockDocs;
        UserGetHandler getHandler = new UserGetHandler(state);
        assert getHandler.checkEmpty(data) == null;
    }

    // mocks getting user data that doesn't exist
    @Test
    public void testCheckEmptyData() throws IOException {
        MockStates state = new MockStates();
        List<String> data = new ArrayList<>();
        UserGetHandler getHandler = new UserGetHandler(state);
        JSONUtils jsonUtils = new JSONUtils();
        Map<String, Object> response = jsonUtils.fromJson(getHandler.checkEmpty(data));
        assert response.get("status").equals("error");
        System.out.println(response.get("error_message"));
        assert response.get("error_message").equals("User with given email does not exist!");
    }

    // test proper creation of request body
    @Test
    public void testCreateBodyParams() throws IOException {
        MockStates state = new MockStates();
        UserCreateHandler createHandler = new UserCreateHandler(state);
        Map<String, Object> bodyParams = createHandler.getBodyParams(MockJSON.mockRequestBody);
        assert bodyParams.get("name").equals("Daniel Liu");
        assert bodyParams.get("email").equals("daniel_liu2@brown.edu");
        assert bodyParams.get("pic").equals("google.com");
    }

    // test proper standard weight calculation
    @Test
    public void testStandardWeightCalculation(){
        Graph graph = new Graph("");
        graph.constructGraph(2.0);
        assert 0.40 == graph.getStandardWeight();

        Graph graph2 = new Graph("");
        graph2.constructGraph(0.0);
        assert 0.5 == graph2.getStandardWeight();

        Graph graph3 = new Graph("");
        graph3.constructGraph(10.0);
        assert 0.0 == graph3.getStandardWeight();
    }

    // test proper swapping of weights
    @Test
    public void testSwapWeights() {
        double[] weights = new double[] {0.33, 0.13, 0.54};
        ArrayList<Integer> positiveDeltas = new ArrayList<>(Arrays.asList(0, 1, 2));
        Graph graph = new Graph("");
        graph.swapWeights(weights, positiveDeltas);
        assert weights[1] == 0.54;
        assert weights[0] == 0.33;
        assert weights[2] == 0.13;
    }

    // test proper weight calculation
    @Test
    public void testCalculateWeights() {
        Graph graph = new Graph("");

        double[] weights = graph.calculateWeights(0, new double[] {5.0, 2.0, 4.0}, 11.0);
        assert weights.length == 3;
        assert weights[0] == 0.18181818181818182;
        assert weights[1] == 0.45454545454545453;
        assert weights[2] == 0.36363636363636365;
        assert Math.ceil(weights[0] + weights[1] + weights[2]) == 1;

        double[] weights2 = graph.calculateWeights(3, new double[] {-3, -7, -4}, 0);
        assert weights2.length == 3;
        assert weights2[0] == 0.3333333333333333;
        assert weights2[1] == 0.3333333333333333;
        assert weights2[2] == 0.3333333333333333;
        assert Math.ceil(weights2[0] + weights2[1] + weights2[2]) == 1;

        double[] weights3 = graph.calculateWeights(1, new double[] {-1, 3.0, 3.0}, 6);
        assert weights3[0] == 0.5;
        assert weights3[1] == 0.25;
        assert weights3[2] == 0.25;
        assert Math.ceil(weights3[0] + weights3[1] + weights3[2]) == 1;
    }

    // fuzz testing to generate random diffs and ensure weights add up to 1
    @Test
    public void fuzzTest() {
        Graph graph = new Graph("");

        for (int i = 0; i < 100; i++) {
            double[] diffs = new double[3];

            for (int j = 0; j < 3; j++) {
                diffs[j] = -10 + (int)(Math.random() * ((10 - (-10)) + 1));
            }

            double numEasy = 0;
            double sum = 0;
            for (double diff : diffs) {
                if (diff <= 0) {
                    numEasy++;
                }
                if (diff > 0) {
                    sum += diff;
                }
            }

            double[] weights = graph.calculateWeights(numEasy, diffs, sum);
            assert weights != null;
            assert weights.length == 3;
            double weightSum = 0;
            for (double weight : weights) {
                weightSum += weight;
            }

            assert Math.ceil(weightSum) == 1;
        }
    }

    // tests that explanations are cached automatically.
    @Test
    public void testGPTProxyCache() {
        GPTProxyCache cache = new GPTProxyCache(new GPTRequester(), 100, 24, TimeUnit.HOURS);
        String explanation = cache.getExplanation("this is a test");
        assert cache.isCachedExact("this is a test");
        // test that using the same snippet gets the cached explanation instead of a new one
        assert cache.getExplanation("this is a test").equals(explanation);
    }

    // test proxy cache expiration
    @Test
    public void testGPTProxyCacheExpiry() throws InterruptedException {
        GPTProxyCache cache = new GPTProxyCache(new GPTRequester(), 100, 1, TimeUnit.SECONDS);
        cache.getExplanation("testing");
        assert cache.isCachedExact("testing");
        TimeUnit.SECONDS.sleep(1);
        assert !(cache.isCachedExact("testing"));
    }

    // test proxy cache replacement if size is filled
    @Test
    public void testGPTProxyCacheSize() {
        GPTProxyCache cache = new GPTProxyCache(new GPTRequester(), 1, 24, TimeUnit.HOURS);
        cache.getExplanation("test1");
        assert cache.isCachedExact("test1");
        cache.getExplanation("test2");
        assert cache.isCachedExact("test2");
        assert !(cache.isCachedExact("test1"));
    }
}