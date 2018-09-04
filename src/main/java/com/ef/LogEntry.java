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
    private String requestContent;
    private LocalDateTime timeOfOccurrence;
    private String banReason;

    public static LogEntry fromStringArray(String[] splitLogEntry) {
        return LogEntry.builder()
                .timeOfOccurrence(parseFrom(splitLogEntry[0]))
                .ip(splitLogEntry[1])
                .requestContent(splitLogEntry[2])
                .build();
    }

    private static LocalDateTime parseFrom(String input) {
        final DateTimeFormatter logDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return LocalDateTime.parse(input, logDateFormat);
    }
}
