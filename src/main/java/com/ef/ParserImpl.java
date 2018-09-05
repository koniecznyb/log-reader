package com.ef;

import com.ef.database.DatabaseService;
import com.ef.reader.LogReader;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
public class ParserImpl {

    private DatabaseService databaseService;
    private LogReader logReader;

    private Duration duration;
    private LocalDateTime startDate;
    private int threshold;


    public void execute(Stream<String> streamLogFile) {
        List<AccessRequestEntry> logEntries = logReader.mapToLogEntryList(streamLogFile);
        databaseService.persistLogFile(logEntries);

        List<AccessRequestEntry> bannedIpAddresses = databaseService
                .findMultipleRequestsBetween(startDate, startDate.plus(duration), threshold)
                .map(AccessRequestEntry::bannedEntryWithReason)
                .collect(Collectors.toList());

        databaseService.persistBlockedIpAddress(bannedIpAddresses)
                .forEach(System.out::println);
    }
}
