package specs;

import config.ConfigurationManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecs {
    private RequestSpecs() {}

    public static final String JSON_V10 = "application/json; charset=utf-8; v=1.0";
    public static final String ACCEPT_ANY = "*/*";
    public static final String ACCEPT_PLAIN_V10 = "text/plain; v=1.0";
    public static final String ACCEPT_JSON_V10 = "application/json; v=1.0";
    
    


    /** Default spec for JSON v1.0 APIs (POST/PUT/typical GETs) */
    public static RequestSpecification jsonV10() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Content-Type", JSON_V10)
                .addHeader("Accept", ACCEPT_ANY)
                .log(LogDetail.ALL)
                .build();
    }

    /** Spec for GET /Books to mirror cURL: Accept: text/plain; v=1.0 */
    public static RequestSpecification acceptPlainV10() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Accept", ACCEPT_PLAIN_V10)
                .log(LogDetail.ALL)
                .build();
    }
    
    public static RequestSpecification acceptWildcard() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Accept", "*/*")
                .log(LogDetail.ALL)
                .build();
    }

    public static RequestSpecification acceptJsonV10Gzip() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Accept", "application/json; v=1.0")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .log(LogDetail.ALL)
                .build();
    }
    
    public static RequestSpecification acceptJsonV10() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Accept", ACCEPT_JSON_V10)
                .log(LogDetail.ALL)
                .build();
    }

    /** Minimal base spec (no forced Content-Type) – good for DELETE, etc. */
    public static RequestSpecification base() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Accept", ACCEPT_ANY)
                .log(LogDetail.ALL)
                .build();
    }
    
}
