package com.ef.reader;

import com.ef.LogEntry;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
public class LogReader {

    public List<LogEntry> mapToLogEntry(Stream<String> logFileStream) {
        final String delimiter = "[|]";

        return logFileStream
                .map(entry -> entry.split(delimiter))
                .map(LogEntry::fromStringArray)
                .collect(Collectors.toList());
    }
}