package edu.brown.cs.student.main;

import edu.brown.cs.student.main.mocks.MockJSON;
import edu.brown.cs.student.main.server.utils.JSONUtils;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class TestSyntaxUnit {
    @Test
    public void testMockReqBodyParams() throws IOException {
        String reqBody = MockJSON.mockRequestBody;
        JSONUtils jsonUtils = new JSONUtils();
        Map<String, Object> bodyParams = jsonUtils.fromJson(reqBody);
        assert bodyParams.get("name") == "Daniel Liu";

    }
}
