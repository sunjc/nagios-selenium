package org.itrunner.tests.utils;

import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public enum Config {
    CONFIG;

    private static final String FILE_NAME = "config.ini";

    private Properties properties;

    Config() {
        properties = new Properties();
        try {
            properties.load(new FileReader(CommonUtil.getJarPath() + FILE_NAME));
        } catch (IOException e) { //NOSONAR
            e.printStackTrace(); //NOSONAR
        }
    }

    public String getDriverType() {
        return getProperty("driver.type");
    }

    public URL getDriverServiceUrl() {
        try {
            return new URL(getProperty("driver.service.url"));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String getProxyHost() {
        return getProperty("proxy.host");
    }

    public String getBaiduUrl() {
        return getProperty("baidu.url");
    }

    public String getBaiduUsername() {
        return getProperty("baidu.username");
    }

    public String getBaiduPassword() {
        return getProperty("baidu.password");
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

}
