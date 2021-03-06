package javacore.benchmarktool.commandline;

import javacore.benchmarktool.benchmark.BenchmarkSettingsImpl;
import org.apache.commons.cli.*;
import java.time.Duration;
import java.net.URL;
import java.net.MalformedURLException;

public class CommandLineHandler {

    private BenchmarkSettingsImpl benchmarkSettingsImpl = new BenchmarkSettingsImpl();

    public CommandLineHandler(String[] args) throws RuntimeException{
        parse(args);
    }

    public BenchmarkSettingsImpl getSettings() {
        return benchmarkSettingsImpl;
    }

    private void parse(String[] args) throws RuntimeException {

        CommandLineParser commandLineParser = new DefaultParser();
        Options options = getOptions();

        try {
            CommandLine line = commandLineParser.parse(options, args);
            setParameters(line);
            validateParameters();
        }
        catch( ParseException exp ) {
            printHelp();
            throw new RuntimeException("Parsing error", exp);
        }
        catch (RuntimeException exp) {
            printHelp();
            throw new RuntimeException(exp.getMessage(), exp);
        }
    }

    private void validateParameters() throws RuntimeException {
        if (benchmarkSettingsImpl.getTargetUrl() == null) {
            throw new RuntimeException("url should be initialized");
        }
    }

    private void setParameters(CommandLine parameters) throws RuntimeException {
        if (parameters.getOptionValue("url") != null) {
            setUrl(parameters.getOptionValue("url"));
        }

        if (parameters.getOptionValue("num") != null) {
            setNumOfRequests(parameters.getOptionValue("num"));
        }

        if (parameters.getOptionValue("concurrency") != null) {
            setNumOfConcurrency(parameters.getOptionValue("concurrency"));
        }

        if (parameters.getOptionValue("timeout") != null) {
            setTimeout(parameters.getOptionValue("timeout"));
        }
    }

    private void setUrl(String url) throws RuntimeException {
        try {
             benchmarkSettingsImpl.setTargetUrl(new URL(url));
        } catch (MalformedURLException exp) {
            throw new RuntimeException("Invalid url", exp);
        }
    }

    private void setNumOfRequests(String numOfRequests) throws RuntimeException {
        try {
            benchmarkSettingsImpl.setRequestCount(Integer.parseUnsignedInt(numOfRequests));
        } catch (NumberFormatException exp) {
            throw new RuntimeException("number of requests is not a positive number", exp);
        }
    }

    private void setNumOfConcurrency(String numOfConcurrency) throws RuntimeException {
        try {
            benchmarkSettingsImpl.setConcurrencyLevel(Integer.parseUnsignedInt(numOfConcurrency));
        } catch (NumberFormatException exp) {
            throw new RuntimeException("number of concurrency is not a positive number", exp);
        }
    }

    private void setTimeout(String timeout) throws RuntimeException {
        try {
            benchmarkSettingsImpl.setTimeout(Duration.ofMillis(Integer.parseUnsignedInt(timeout)));
        } catch (NumberFormatException exp) {
            throw new RuntimeException("timeout is not a positive number", exp);
        }
    }

    private void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp( "Benchmark tool", getOptions() );
    }

    private Options getOptions() {
        Options options = new Options();

        options.addOption( "u", "url", true, "sets url for benchmarking" );
        options.addOption( "n", "num", true, "sets number of requests" );
        options.addOption( "c", "concurrency", true, "sets number of concurrency");
        options.addOption( "t", "timeout", true, "sets timeout number");

        return options;
    }
}
