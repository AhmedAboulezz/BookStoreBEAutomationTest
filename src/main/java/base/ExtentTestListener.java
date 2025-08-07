package base;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentTestListener implements ITestListener {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> node = new ThreadLocal<>();

    private static final Map<String, ExtentTest> classParents = new ConcurrentHashMap<>();

    @Override
    public void onStart(ITestContext ctx) {
        ExtentSparkReporter spark = new ExtentSparkReporter("test-output/ExtentReport.html");
        spark.config().setTheme(Theme.STANDARD);
        spark.config().setReportName("Bookstore API Tests");
        spark.config().setDocumentTitle("Bookstore API Report");

        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester", "Ahmed Aboelezz");

        Runtime.getRuntime().addShutdownHook(new Thread(extent::flush));
    }

    @Override
    public void onTestStart(ITestResult res) {
        String className  = res.getTestClass().getRealClass().getSimpleName();
        String methodName = res.getMethod().getMethodName();

        ExtentTest parent = classParents.computeIfAbsent(className,
                cn -> extent.createTest(cn));          

        ExtentTest child = parent.createNode(methodName);
        node.set(child);
    }

    @Override public void onTestSuccess (ITestResult r){ node.get().pass("Passed"); }
    @Override public void onTestFailure (ITestResult r){ node.get().fail(r.getThrowable()); }
    @Override public void onTestSkipped (ITestResult r){ node.get().skip(r.getThrowable()); }

    @Override public void onFinish(ITestContext ctx){ extent.flush(); }

    public static ExtentTest current(){ return node.get(); }
}
