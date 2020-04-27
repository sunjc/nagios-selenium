package org.itrunner.tests;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.itrunner.tests.utils.Config.CONFIG;


public class DriverFactory {
    private static final String NONE_PROXY_FLAG = "none";
    private static final String DEFAULT_PROXY_FLAG = "default";
    private static final String SIN_PROXY_FLAG = "sin";

    private DriverFactory() {
    }

    public static RemoteWebDriver createWebDriver() {
        if (CONFIG.getDriverType().equals("firefox")) {
            return createFirefoxDriver();
        }
        return createChromeDriver();
    }

    public static RemoteWebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);

        if (hasProxy()) {
            options.setProxy(getProxy());
        }

        return new RemoteWebDriver(CONFIG.getDriverServiceUrl(), options);
    }

    public static RemoteWebDriver createFirefoxDriver() {
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        options.setLogLevel(FirefoxDriverLogLevel.fromString(CONFIG.getLogLevel()));

        if (hasProxy()) {
            options.setProxy(getProxy());
        }

        return new RemoteWebDriver(CONFIG.getDriverServiceUrl(), options);
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