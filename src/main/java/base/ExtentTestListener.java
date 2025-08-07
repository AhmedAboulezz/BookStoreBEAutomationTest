package base;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestListener implements ITestListener {

    private static ExtentReports extent;

    private static final Map<String, ExtentTest> suiteParents = new ConcurrentHashMap<>();
    private static final Map<String, ExtentTest> classParents = new ConcurrentHashMap<>();
    private static final ThreadLocal<ExtentTest> methodNode = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext ctx) {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setReportName("Bookstore API Tests");
            spark.config().setDocumentTitle("Bookstore API Report");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Tester", "Ahmed Aboelezz");

            Runtime.getRuntime().addShutdownHook(new Thread(extent::flush));
        }
    }

    @Override
    public void onTestStart(ITestResult res) {
        String suiteName = res.getTestContext().getName();
        ExtentTest suiteParent = suiteParents.computeIfAbsent(
                suiteName, name -> extent.createTest(name));

        String className = res.getTestClass().getRealClass().getSimpleName();
        String classKey  = suiteName + "|" + className;          // composite key
        ExtentTest classParent = classParents.computeIfAbsent(
                classKey, key -> suiteParent.createNode(className));

        ExtentTest method = classParent.createNode(res.getMethod().getMethodName());
        methodNode.set(method);
    }


    @Override public void onTestSuccess (ITestResult r){ methodNode.get().pass("Passed"); }
    @Override public void onTestFailure (ITestResult r){ methodNode.get().fail(r.getThrowable()); }
    @Override public void onTestSkipped (ITestResult r){ methodNode.get().skip(r.getThrowable()); }

    @Override public void onFinish(ITestContext ctx){ /* everything flushed via shutdown-hook */ }

    public static ExtentTest currentNode() { return methodNode.get(); }
}
