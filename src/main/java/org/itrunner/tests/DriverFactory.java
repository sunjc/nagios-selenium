package org.itrunner.tests;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.ArrayList;
import java.util.List;

import static org.itrunner.tests.utils.Config.CONFIG;
import static org.openqa.selenium.phantomjs.PhantomJSDriverService.*;

public class DriverFactory {

    public static RemoteWebDriver createDriver() {
        if (CONFIG.getDriverType().equals("firefox")) {
            return createFirefoxDriver();
        }
        return createPhantomJSDriver();
    }

    public static RemoteWebDriver createPhantomJSDriver() {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(PHANTOMJS_EXECUTABLE_PATH_PROPERTY, CONFIG.getPhantomJsBinaryPath());

        List<String> cliArgs = new ArrayList<>();
        cliArgs.add("--web-security=false");
        cliArgs.add("--ssl-protocol=any");
        cliArgs.add("--ignore-ssl-errors=true");
        capabilities.setCapability(PHANTOMJS_CLI_ARGS, cliArgs);

        // Control LogLevel for GhostDriver, via CLI arguments
        String[] ghostDriverCliArgs = {"--logLevel=" + CONFIG.getLogLevel()};
        capabilities.setCapability(PHANTOMJS_GHOSTDRIVER_CLI_ARGS, ghostDriverCliArgs);
        if (hasProxy()) {
            capabilities.setCapability("proxy", getProxy());
        }
        return new PhantomJSDriver(capabilities);
    }

    public static RemoteWebDriver createFirefoxDriver() {
        System.setProperty("webdriver.gecko.driver", CONFIG.getGeckoDriver());

        FirefoxOptions options = new FirefoxOptions();
        options.setLogLevel(FirefoxDriverLogLevel.fromString(CONFIG.getLogLevel()));

        if (hasProxy()) {
            options.setProxy(getProxy());
        }

        RemoteWebDriver driver = new FirefoxDriver(options);
        driver.manage().window().maximize();
        return driver;
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