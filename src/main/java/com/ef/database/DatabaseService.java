package com.ef.database;

import com.ef.AccessRequestEntry;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public class DatabaseService {

    private DatabaseClient databaseClient;
    private final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

    public Stream<AccessRequestEntry> findMultipleRequestsBetween(LocalDateTime startDate, LocalDateTime endDate, int threshold) {
        String formattedStartDate = startDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        String formattedEndDate = endDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
        return databaseClient.findRequestsBetween(formattedStartDate, formattedEndDate, threshold).stream();
    }


    public List<AccessRequestEntry> persistBlockedIpAddress(List<AccessRequestEntry> blockedIpAddresses) {
        databaseClient.persistBlockedIpAddresses(blockedIpAddresses);
        return blockedIpAddresses;
    }

    public void persistLogFile(List<AccessRequestEntry> accessRequestEntry) {
        databaseClient.persistLogEntries(accessRequestEntry);
    }
}
