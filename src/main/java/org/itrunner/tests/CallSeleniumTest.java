package org.itrunner.tests;

import org.apache.commons.cli.*;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.firefox.NotConnectedException;

public class CallSeleniumTest {
    private static int timeout = 30;

    private static final int NAGIOS_OK = 0;
    private static final int NAGIOS_WARNING = 1;
    private static final int NAGIOS_CRITICAL = 2;
    private static final int NAGIOS_UNKNOWN = 3;

    private static final String NAGIOS_TEXT_OK = "OK";
    private static final String NAGIOS_TEXT_WARNING = "WARNING";
    private static final String NAGIOS_TEXT_CRITICAL = "CRITICAL";
    private static final String NAGIOS_TEXT_UNKNOWN = "UNKNOWN";

    private Options options = null;

    private TestResult runTest(String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<TestBase> seleniumTestClass = (Class<TestBase>) Class.forName(className);
        return seleniumTestClass.newInstance().run();
    }

    public static void main(String[] args) throws Exception {
        CallSeleniumTest seTest = new CallSeleniumTest();

        Option optionClass = new Option("c", "class", true, "full classname of test case (required) e.g. \"org.itrunner.tests.hosts.Baidu\"");
        Option optionTimeout = new Option("t", "timeout", true, "timeout, default is 30");
        Option optionVerbose = new Option("v", "verbose", false, "show a lot of information (useful in case of problems)");
        Option optionHelp = new Option("h", "help", false, "show this help screen");

        seTest.options = new Options();
        seTest.options.addOption(optionClass);
        seTest.options.addOption(optionTimeout);
        seTest.options.addOption(optionVerbose);
        seTest.options.addOption(optionHelp);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        String output = seTest.NAGIOS_TEXT_UNKNOWN + " - Upps";
        int nagios = seTest.NAGIOS_UNKNOWN;

        try {
            cmd = parser.parse(seTest.options, args);
            // has to be checked manually, otherwise you can't access the help message without specifying correct parameters
            if (cmd.hasOption("h") || !cmd.hasOption("c")) {
                usage(seTest.options);
                System.exit(nagios); //NOSONAR
            }

            if (cmd.hasOption("t")) {
                timeout = Integer.parseInt(cmd.getOptionValue("t"));
            }

            TestResult result = seTest.runTest(cmd.getOptionValue("c"));
            output = seTest.NAGIOS_TEXT_OK + " - " + cmd.getOptionValue("c") + " Tests passed - " + result.toString();
            nagios = seTest.NAGIOS_OK;

        } catch (ParseException e) {
            output = seTest.NAGIOS_TEXT_UNKNOWN + " - Parameter problems: " + e.getMessage();
            nagios = seTest.NAGIOS_UNKNOWN;
            usage(seTest.options);
        } catch (ClassNotFoundException e) {
            output = seTest.NAGIOS_TEXT_UNKNOWN + " - Test case class: " + e.getMessage() + " not found!";
            nagios = seTest.NAGIOS_UNKNOWN;
        } catch (TimeoutException | CriticalException e) {
            output = seTest.NAGIOS_TEXT_CRITICAL + " - Test Failures: " + e.getMessage();
            nagios = seTest.NAGIOS_CRITICAL;
        } catch (Exception e) {
            output = seTest.NAGIOS_TEXT_WARNING + " - Test Failures: " + processException(cmd, e);
            nagios = seTest.NAGIOS_WARNING;
        } finally {
            println(output);
            System.exit(nagios); //NOSONAR
        }
    }

    private static String processException(CommandLine cmd, Exception e) {
        if (cmd.hasOption("v")) {
            e.printStackTrace(); //NOSONAR
        }

        if (isCausedBy(e, NotConnectedException.class)) {
            return "Failed to connect to binary FirefoxBinary";
        }

        return e.getMessage();
    }

    private static void usage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("check_selenium", options);
        println("This version of check_selenium was tested with:");
        println("  - selenium 3.14.0");
        println("  - selenium ide 3.3.1");
        println("  - test case exported as Java / JUnit 4 / WebDriver");
        println("Some example calls:");
        println(" ./check_selenium.sh -c \"org.itrunner.tests.hosts.Baidu\"");
        println(" ./check_selenium.sh --class \"org.itrunner.tests.hosts.Baidu\"");
    }

    public static int getTimeout() {
        return timeout;
    }

    private static void println(String x) {
        System.out.println(x); //NOSONAR
    }

    private static <T extends Throwable> boolean isCausedBy(final Throwable exception, Class<T> clazz) {
        Throwable cause = exception;
        while (cause != null) {
            if (clazz.isInstance(cause)) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }
}