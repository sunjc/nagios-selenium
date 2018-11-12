package org.itrunner.tests;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static java.lang.System.currentTimeMillis;
import static org.openqa.selenium.OutputType.BYTES;
import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public abstract class TestBase {
    protected RemoteWebDriver driver;

    private long startTime;

    private TestResult result = new TestResult();

    @Before
    public void setup() {
        startTime = currentTimeMillis();
        nextStep(this::createDriver, "init");
        setTimeout();
        addShutdownHook();
    }

    TestResult run() {
        setup();
        test();
        tearDown();
        setTotalTime();
        return result;
    }

    @After
    public void tearDown() {
        nextStep(() -> DriverFactory.quit(), "destroy");
    }

    private void createDriver() {
        driver = DriverFactory.createDriver();
    }

    public abstract void test();

    protected void savePageSource(String host) {
        try {
            File sourceFile = new File("reports/" + host + ".html");
            FileUtils.writeStringToFile(sourceFile, driver.getPageSource(), Charset.defaultCharset());
        } catch (IOException e) { //NOSONAR
            // do nothing
        }
    }

    protected void takeScreenshot(String host) {
        File imageFile = new File("reports/" + host + ".png");
        try {
            FileUtils.writeByteArrayToFile(imageFile, driver.getScreenshotAs(BYTES));
        } catch (IOException e) { //NOSONAR
            // do nothing
        }
    }

    protected void nextStep(TestStep step, String stepName) {
        long beginTime = currentTimeMillis();

        try {
            step.run();
        } catch (TimeoutException | NoSuchElementException e) {
            throw new CriticalException(stepName + " failed: " + e.getMessage(), e);
        }

        result.putStepTime(stepName, currentTimeMillis() - beginTime);
    }

    public boolean isElementPresent(By by) {
        try {
            return waitForElementPresent(by);
        } catch (TimeoutException e) { //NOSONAR
            return false;
        }
    }

    protected void open(final String url) {
        nextStep(() -> driver.get(url), "loading page");
    }

    public void click(final By by, String stepName) {
        nextStep(() -> driver.findElement(by).click(), stepName);
    }

    protected void waitForTitlePresent(final String title) {
        nextStep(() -> waitForCondition(titleIs(title)), "finding title");
    }

    private boolean waitForElementPresent(By by) {
        return waitForCondition(visibilityOfElementLocated(by)).isDisplayed();
    }

    private <T> T waitForCondition(ExpectedCondition<T> condition) {
        return (new WebDriverWait(driver, CallSeleniumTest.getTimeout())).until(condition);
    }

    private void setTimeout() {
        driver.manage().timeouts().implicitlyWait(CallSeleniumTest.getTimeout(), TimeUnit.SECONDS);
    }

    private void setTotalTime() {
        result.setTotalTime(currentTimeMillis() - startTime);
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("Selenium Quit Hook") {
            @Override
            public void run() {
                DriverFactory.quit();
            }
        });
    }
}