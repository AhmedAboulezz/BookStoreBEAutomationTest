package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;

public final class ResponseSpecs {
    private ResponseSpecs() {}

    private static final ResponseSpecification OK_200 =
            new ResponseSpecBuilder().expectStatusCode(200).build();
    private static final ResponseSpecification BAD_REQUEST_400 =
            new ResponseSpecBuilder().expectStatusCode(400).build();
    private static final ResponseSpecification NOT_FOUND_404 =
            new ResponseSpecBuilder().expectStatusCode(404).build();
    private static final ResponseSpecification METHOD_NOT_ALLOWED_405 =
            new ResponseSpecBuilder().expectStatusCode(405).build();

    public static ResponseSpecification ok200() { return OK_200; }
    public static ResponseSpecification badRequest400() { return BAD_REQUEST_400; }
    public static ResponseSpecification notFound404() { return NOT_FOUND_404; }
    public static ResponseSpecification methodNotAllowed405() { return METHOD_NOT_ALLOWED_405; }
}
