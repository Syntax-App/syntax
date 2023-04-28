package edu.brown.cs.student.main;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.storage.Acl.User;
import com.squareup.moshi.Json;
import edu.brown.cs.student.main.mocks.MockJSON;
import edu.brown.cs.student.main.server.States;
import edu.brown.cs.student.main.server.handlers.user.UserCreateHandler;
import edu.brown.cs.student.main.server.handlers.user.UserGetHandler;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestSyntaxUnit {
    @Test
    public void testCheckData() {
        States state = new States();
        List<String> data = MockJSON.mockDocs;
        UserGetHandler getHandler = new UserGetHandler(state);
        assert getHandler.checkEmpty(data) == null;
    }

    @Test
    public void testCheckEmptyData() throws IOException {
        States state = new States();
        List<String> data = new ArrayList<>();
        UserGetHandler getHandler = new UserGetHandler(state);
        JSONUtils jsonUtils = new JSONUtils();
        Map<String, Object> response = jsonUtils.fromJson((String) getHandler.checkEmpty(data));
        assert response.get("status") == "error";
        assert response.get("error_message") == "user with given email does not exist!";
    }

    @Test
    public void testSerializeSuccess() throws IOException {
        States state = new States();
        UserGetHandler getHandler = new UserGetHandler(state);
        JSONUtils jsonUtils = new JSONUtils();
        Map<String, Object> data = jsonUtils.fromJson(MockJSON.mockDocs.get(0));
        assert getHandler.getSerializedSuccess("success", data) == "{\"status\": \"success\", \"data: " + MockJSON.mockDocs.get(0) + "\"}";
    }

    @Test
    public void testSerializeFailure() {
        States state = new States();
        UserGetHandler getHandler = new UserGetHandler(state);
        assert getHandler.getSerializedFailure("error", "failed") == "{\"status\": \"error\", \"error_message\": \"failed\"}";
    }

    @Test
    public void testCreateBodyParams() throws IOException {
        States state = new States();
        UserCreateHandler createHandler = new UserCreateHandler(state);
        Map<String, Object> bodyParams = createHandler.getBodyParams(MockJSON.mockRequestBody);
        assert bodyParams.get("name") == "Daniel Liu";
        assert bodyParams.get("email") == "daniel_liu2@brown.edu";
        assert bodyParams.get("pic") == "google.com";
    }

    @Test
    public void testCreateUserObject() throws IOException {
        States state = new States();
        UserCreateHandler createHandler = new UserCreateHandler(state);
        Map<String, Object> bodyParams = createHandler.getBodyParams(MockJSON.mockRequestBody);
        Map<String, Object> userObject = createHandler.createUserObject(bodyParams);
        assert userObject.get("uuid") instanceof Integer;
        assert userObject.get("name") == "Daniel Liu";
        assert userObject.get("email") == "daniel_liu2@brown.edu";
        assert userObject.get("pic") == "google.com";

        HashMap<String, Integer> stats = new HashMap<>();
        stats.put("highlpm", 0);
        stats.put("highacc", 0);
        stats.put("avgacc", 0);
        stats.put("avglpm", 0);
        stats.put("numraces", 0);
        assert userObject.get("stats") == stats;
    }
}
