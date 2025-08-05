package base;

import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.config.RestAssuredConfig;

import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import config.ConfigurationManager;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.specification.RequestSpecification;

public class TestBase {

    protected static RequestSpecification requestSpec;

    @BeforeSuite(alwaysRun = true)
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);


        RestAssured.config = RestAssuredConfig.config()
            .objectMapperConfig(new ObjectMapperConfig(ObjectMapperType.JACKSON_2).jackson2ObjectMapperFactory((cls, charset) -> objectMapper));

        RestAssured.baseURI = ConfigurationManager.getInstance().getBaseUrl();
    }
    
    protected void logInfo(String message) {
        if (ExtentTestListener.getTest() != null)
            ExtentTestListener.getTest().log(Status.INFO, message);
    }

    protected void logPass(String message) {
        if (ExtentTestListener.getTest() != null)
            ExtentTestListener.getTest().pass(message);
    }

    protected void logFail(String message) {
        if (ExtentTestListener.getTest() != null)
            ExtentTestListener.getTest().fail(message);
    }
    
    
}
