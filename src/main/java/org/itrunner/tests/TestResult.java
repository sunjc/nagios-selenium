package org.itrunner.tests;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestResult {
    private long totalTime;

    private Map<String, Long> stepTime = new LinkedHashMap<>();

    private String message;

    public void putStepTime(String key, Long value) {
        stepTime.put(key, value);
    }

    @Override
    public String toString() {
        if (message != null) {
            return message;
        }

        StringBuilder result = new StringBuilder();
        result.append("Total Time(ms): ").append(totalTime).append(" {");

        for (Map.Entry<String, Long> entry : stepTime.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        return result.substring(0, result.length() - 2) + "}";
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public Map<String, Long> getStepTime() {
        return stepTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
