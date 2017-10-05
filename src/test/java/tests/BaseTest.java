package tests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.annotations.*;
import org.testng.reporters.FailedReporter;
import testconfigs.ExtentManager;

import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTest {
    ExtentReports extentReports;
    ExtentTest extentTest;

    @BeforeSuite
    public void beforeSuite() {
        System.out.println("Before Suite");
        extentReports = ExtentManager.GetExtent();
    }
    @BeforeMethod
    public void beforeMethod(Method caller) {
        System.out.println("Before Method");
        System.out.println(Thread.currentThread().getId());
        extentTest = extentReports.createTest(caller.getName());
    }

    @AfterMethod
    public void afterMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.SUCCESS) {
            extentTest.pass(result.getName() + " - Passed")
        } else if (result.getStatus() == ITestResult.FAILURE) {
            extentTest.addScreenCaptureFromPath("./screenshot.png", "Failed test screenshot for " + result.getName());
            extentTest.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip(result.getName() + " - Skipped");
        } else {
            extentTest.error(result.getThrowable());
        }
    }

    @AfterSuite
    public void afterSuite() {
        System.out.println("After Suite");
        extentReports.flush();
    }

    @Test
    public void passTest() {
        System.out.println("This test is passed");
        extentTest.pass("This test is passed");
    }

    @Test
    public void failTest() throws Exception {
        System.out.println("This test if failed");
        extentTest.fail("This test is failed");
        throw new Exception("Exception from method");
    }

    @Test
    public void skipTest() {
        System.out.println("This test is skipped");
        extentTest.skip("This test is skipped");
    }
}