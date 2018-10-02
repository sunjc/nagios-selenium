package org.itrunner.tests.utils;

public class CommonUtil {
    private CommonUtil() {

    }

    public static String getJarPath() {
        String path = CommonUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        if (path.endsWith(".jar")) {
            path = path.substring(0, path.lastIndexOf("/") + 1);
        }
        return path;
    }
}