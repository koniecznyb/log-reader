package com.ef;

import com.ef.database.DatabaseService;
import com.ef.reader.LogReader;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;


@AllArgsConstructor
public class ParserImpl {

    private DatabaseService databaseService;
    private LogReader logReader;

    private Duration duration;
    private LocalDateTime startDate;
    private int threshold;


    public void execute(Stream<String> streamLogFile) {
        List<LogEntry> logEntries = logReader.mapToLogEntry(streamLogFile);
        databaseService.persistLogFile(logEntries);

        databaseService
                .findMultipleRequestsBetween(startDate, startDate.plus(duration), threshold)
                .map(databaseService::persistBlockedIpAddress)
                .forEach(System.out::println);
    }
}
