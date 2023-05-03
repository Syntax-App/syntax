package edu.brown.cs.student.main;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.storage.Acl.User;
import com.squareup.moshi.Json;
import edu.brown.cs.student.main.mocks.MockJSON;
import edu.brown.cs.student.main.mocks.MockStates;
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
        MockStates state = new MockStates();
        List<String> data = MockJSON.mockDocs;
        UserGetHandler getHandler = new UserGetHandler(state);
        assert getHandler.checkEmpty(data) == null;
    }

    @Test
    public void testCheckEmptyData() throws IOException {
        MockStates state = new MockStates();
        List<String> data = new ArrayList<>();
        UserGetHandler getHandler = new UserGetHandler(state);
        JSONUtils jsonUtils = new JSONUtils();
        Map<String, Object> response = jsonUtils.fromJson((String) getHandler.checkEmpty(data));
        assert response.get("status").equals("error");
        assert response.get("error_message").equals("user with given email does not exist!");
    }

    @Test
    public void testSerializeSuccess() throws IOException {
        MockStates state = new MockStates();
        UserGetHandler getHandler = new UserGetHandler(state);
        JSONUtils jsonUtils = new JSONUtils();
        Map<String, Object> data = jsonUtils.fromJson(MockJSON.mockDocs.get(0));
        assert getHandler.getSerializedSuccess("success", data).equals("{\"status\":\"success\",\"data\":{\"user\":{\"uuid\":123.0,\"name\":\"Daniel Liu\",\"email\":\"daniel_liu2@brown.edu\",\"pic\":\"google.com\",\"stats\":{\"highlpm\":0.0,\"highacc\":0.0,\"avgacc\":0.0,\"avglpm\":0.0,\"numraces\":0.0}}}}");
    }

    @Test
    public void testSerializeFailure() {
        MockStates state = new MockStates();
        UserGetHandler getHandler = new UserGetHandler(state);
        assert getHandler.getSerializedFailure("error", "failed").equals("{\"status\":\"error\",\"error_message\":\"failed\"}");
    }

    @Test
    public void testCreateBodyParams() throws IOException {
        MockStates state = new MockStates();
        UserCreateHandler createHandler = new UserCreateHandler(state);
        Map<String, Object> bodyParams = createHandler.getBodyParams(MockJSON.mockRequestBody);
        assert bodyParams.get("name").equals("Daniel Liu");
        assert bodyParams.get("email").equals("daniel_liu2@brown.edu");
        assert bodyParams.get("pic").equals("google.com");
    }

    @Test
    public void testCreateUserObject() throws IOException {
//        MockStates state = new MockStates();
//        UserCreateHandler createHandler = new UserCreateHandler(state);
//        Map<String, Object> bodyParams = createHandler.getBodyParams(MockJSON.mockRequestBody);
//        Map<String, Object> userObject = createHandler.createUserObject(bodyParams);
//        assert userObject.get("uuid") != null;
//        assert userObject.get("name").equals("Daniel Liu");
//        assert userObject.get("email").equals("daniel_liu2@brown.edu");
//        assert userObject.get("pic").equals("google.com");
//
//        HashMap<String, Integer> stats = new HashMap<>();
//        stats.put("highlpm", 0);
//        stats.put("highacc", 0);
//        stats.put("avgacc", 0);
//        stats.put("avglpm", 0);
//        stats.put("numraces", 0);
//        System.out.println(userObject.get("stats"));
//        System.out.println(stats);
//        assert userObject.get("stats").equals(stats);
    }
}
