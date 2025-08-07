package specs;

import config.ConfigurationManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecs {
    private RequestSpecs() {}

    public static final String JSON_V10 = "application/json; charset=utf-8; v=1.0";
    public static final String ACCEPT_ANY = "*/*";
    
    
    public static RequestSpecification jsonV10() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.getInstance().getBaseUrl())
                .addHeader("Content-Type", JSON_V10)
                .addHeader("Accept", ACCEPT_ANY)
                .log(LogDetail.ALL)
                .build();
    }
    
}
