package com.ef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@ToString
@AllArgsConstructor
public final class LogEntry {

    private Integer id;
    private String ip;
    private LocalDateTime timeOfOccurrence;
    private String banReason;
    private Integer occurrenceCount;

    public static LogEntry fromStringArray(String[] splitLogEntry) {
        return LogEntry.builder()
                .timeOfOccurrence(parseFrom(splitLogEntry[0]))
                .ip(splitLogEntry[1])
                .build();
    }

    public static LogEntry withOccurrenceCount(LogEntry logEntry, int count) {
        return LogEntry
                .builder()
                .ip(logEntry.getIp())
                .timeOfOccurrence(logEntry.getTimeOfOccurrence())
                .occurrenceCount(count)
                .banReason("Banned because of too many requests")
                .build();

    }

    private static LocalDateTime parseFrom(String input) {
        final DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(input, logDateFormat);
    }

    public boolean isWithinTimePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return timeOfOccurrence.isAfter(startDate) && timeOfOccurrence.isBefore(endDate);
    }
}
