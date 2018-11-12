package org.itrunner.tests;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.itrunner.tests.utils.Config.CONFIG;
import static org.openqa.selenium.phantomjs.PhantomJSDriverService.*;

public class DriverFactory {
    private static DriverService service;
    private static RemoteWebDriver driver;

    private DriverFactory() {
    }

    public static RemoteWebDriver createDriver() {
        String driverType = CONFIG.getDriverType();

        if (driverType.equals("phantomjs")) {
            return createPhantomJSDriver();
        }

        if (driverType.equals("firefox")) {
            return createFirefoxDriver();
        }

        return createChromeDriver();
    }

    public static RemoteWebDriver createChromeDriver() {
        try {
            service = new ChromeDriverService.Builder()
                    .usingDriverExecutable(new File(CONFIG.getChromeDriver()))
                    .usingAnyFreePort()
                    .withSilent(true)
                    .withVerbose(false)
                    .withLogFile(new File(CONFIG.getLogFile()))
                    .build();
            service.start();

            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);

            if (hasProxy()) {
                options.setProxy(getProxy());
            }

            driver = new RemoteWebDriver(service.getUrl(), options);
        } catch (Exception e) {
            // do nothing
        }
        return driver;
    }

    public static RemoteWebDriver createFirefoxDriver() {
        try {
            service = new GeckoDriverService.Builder()
                    .usingDriverExecutable(new File(CONFIG.getGeckoDriver()))
                    .usingAnyFreePort()
                    .withLogFile(new File(CONFIG.getLogFile()))
                    .build();
            service.start();

            FirefoxOptions options = new FirefoxOptions();
            options.setHeadless(true);
            options.setLogLevel(FirefoxDriverLogLevel.fromString(CONFIG.getLogLevel()));

            if (hasProxy()) {
                options.setProxy(getProxy());
            }

            driver = new RemoteWebDriver(service.getUrl(), options);
        } catch (Exception e) {
            // do nothing
        }
        return driver;
    }

    public static RemoteWebDriver createPhantomJSDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, CONFIG.getPhantomJsBinaryPath());
        capabilities.setCapability(PHANTOMJS_GHOSTDRIVER_PATH_PROPERTY, CONFIG.getPhantomJsGhostDriverPath());

        List<String> cliArgs = new ArrayList<>();
        cliArgs.add("--web-security=false");
        cliArgs.add("--ssl-protocol=any");
        cliArgs.add("--ignore-ssl-errors=true");
        capabilities.setCapability(PHANTOMJS_CLI_ARGS, cliArgs);

        // Control LogLevel for GhostDriver, via CLI arguments
        String[] ghostDriverCliArgs = {"--logFile=" + CONFIG.getLogFile(), "--logLevel=" + CONFIG.getLogLevel()};
        capabilities.setCapability(PHANTOMJS_GHOSTDRIVER_CLI_ARGS, ghostDriverCliArgs);
        if (hasProxy()) {
            capabilities.setCapability("proxy", getProxy());
        }
        driver = new PhantomJSDriver(capabilities);
        return driver;
    }

    public static void quit() {
        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.stop();
        }
        driver = null;
        service = null;
    }

    private static Proxy getProxy() {
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(CONFIG.getProxyHost());
        return proxy;
    }

    private static boolean hasProxy() {
        return CONFIG.getProxyHost() != null && !CONFIG.getProxyHost().equals("");
    }
}
