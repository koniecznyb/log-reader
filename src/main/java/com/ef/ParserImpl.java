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


    public void persistLogFileIfPresent(Stream<String> logFile) {
        List<AccessRequestEntry> logEntries = logReader.mapToLogEntryList(logFile);
        if (!logEntries.isEmpty()) {
            databaseService.persistLogFile(logEntries);
        }
    }

    public List<AccessRequestEntry> findBannedIpAddresses(Duration duration, LocalDateTime startDate, int threshold) {
        return databaseService
                .findMultipleRequestsBetween(startDate, startDate.plus(duration), threshold)
                .map(AccessRequestEntry::bannedEntryWithReason)
                .collect(Collectors.toList());
    }

    public List<String> persistBannedAddresses(List<AccessRequestEntry> accessRequestEntries) {
        return databaseService
                .persistBlockedIpAddress(accessRequestEntries)
                .stream()
                .map(accessRequestEntry -> String.format("Blocked ipAddress=%s reason=%s",
                        accessRequestEntry.getIp(),
                        accessRequestEntry.getBanReason()
                ))
                .collect(Collectors.toList());
    }

}
