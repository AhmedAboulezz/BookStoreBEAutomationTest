package utils;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import io.restassured.response.Response;

public class helpers {

    public static void assertFirstErrorContains(Response resp, String jsonPointer, String expectedSubstring) {
        String path = "errors.'" + jsonPointer + "'[0]";
        String actual = resp.jsonPath().getString(path);
        assertNotNull(actual, "Expected error message for key: " + jsonPointer);
        assertTrue(actual.contains(expectedSubstring),
            "Expected error for '" + jsonPointer + "' to contain:\n" + expectedSubstring + "\nbut got:\n" + actual);
    }
    
    public static void assertErrorsIdEquals(Response resp, String expected) {
        String actual = resp.jsonPath().getString("errors.id[0]");
        assertEquals(actual, expected, "errors.id[0] mismatch");
    }
}
