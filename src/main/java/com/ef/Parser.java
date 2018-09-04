package com.ef;

import com.ef.database.DatabaseClient;
import com.ef.reader.LogReader;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

@CommandLine.Command(description = "The tool takes \"startDate\", \"durationInput\" and \"threshold\" as command line " +
        "arguments. \"startDate\" is of \"yyyy-MM-dd.HH:mm:ss\" format, \"durationInput\" can take only \"hourly\", " +
        "\"daily\" as inputs and \"threshold\" can be an integer." +
        " The log file assumes 200 as hourly limit and 500 as daily limit.",
        name = "java cp parser.jar com.ef.Parser")
public class Parser implements Callable<Void> {

    @CommandLine.Option(names = "--accesslog", description = "log file", required = true)
    private static String logFilePath;

    @CommandLine.Option(names = "--startDate", description = "is of \"yyyy-MM-dd.HH:mm:ss\" format", required = true)
    private static String startDateInput;

    @CommandLine.Option(names = "--duration", description = "can take only \"hourly\", \"daily\"", required = true)
    private static String durationInput;

    @CommandLine.Option(names = "--threshold", description = "threshold can be an integer", required = true)
    private static int threshold;

    public static void main(String[] args) {
        CommandLine.call(new Parser(), args);
    }

    @Override
    public Void call() throws Exception {
        Duration duration = parseDuration(durationInput);
        LocalDateTime startDate = parseStartDate(startDateInput);
        Stream<String> logFile = streamLogFile(logFilePath);

        ParserImpl parser = new ParserImpl(new DatabaseClient(), new LogReader(startDate, startDate.plus(duration), threshold));
        parser.parse(logFile);
        return null;
    }

    private Stream<String> streamLogFile(String logFilePath) {
        try {
            return Files.lines(Paths.get(logFilePath));
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not find provided file");
        }
    }

    private LocalDateTime parseStartDate(String startDateInput) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss");
        try {
            return LocalDateTime.parse(startDateInput, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Start time must have following pattern yyyy-MM-dd.HH:mm:ss");
        }
    }

    private Duration parseDuration(String durationInput) {
        switch (durationInput) {
            case "hourly":
                return Duration.ofHours(1);
            case "daily":
                return Duration.ofHours(24);
            default:
                throw new IllegalArgumentException("Duration must be either hourly or daily");
        }

    }
}
