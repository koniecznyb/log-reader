package com.ef.reader;

import com.ef.LogEntry;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class LogReader {

    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final int threshold;

    public List<LogEntry> findIpAddresses(Stream<String> logFileStream) {
        final String delimiter = "[|]";
        final HashMap<String, Integer> ipCache = new HashMap<>();

        return logFileStream
                .map(entry -> entry.split(delimiter))
                .map(LogEntry::fromStringArray)
                .filter(logEntry -> logEntry.isWithinTimePeriod(startDate, endDate))
                .filter(logEntry -> isRequestsCountAboveThreshold(ipCache, logEntry.getIp()))
                .map(logEntry -> LogEntry.withOccurrenceCount(logEntry, ipCache.get(logEntry.getIp())))
                .collect(Collectors.toList());
    }

    private boolean isRequestsCountAboveThreshold(HashMap<String, Integer> ipCache, String requestIP) {
        return ipCache.compute(requestIP, this::incrementIfExists) >= threshold;
    }

    private Integer incrementIfExists(String __, Integer count) {
        return count == null ? 0 : count + 1;
    }

}