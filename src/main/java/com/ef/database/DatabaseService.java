package com.ef.database;

import com.ef.LogEntry;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
public class DatabaseService {

    private DatabaseClient databaseClient;


    public Stream<LogEntry> findMultipleRequestsBetween(LocalDateTime startDate, LocalDateTime plus, int threshold) {
        return Stream.empty();
    }

    public Stream<LogEntry> persistBlockedIpAddress(LogEntry logEntry) {
        return Stream.empty();
    }

    public void persistLogFile(List<LogEntry> logEntries) {
        databaseClient.persistLogEntries(logEntries);
    }
}
