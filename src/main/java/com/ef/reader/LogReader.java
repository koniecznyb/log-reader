package com.ef.reader;

import com.ef.AccessRequestEntry;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LogReader {

    public List<AccessRequestEntry> mapToLogEntryList(Stream<String> logFileStream) {
        final String delimiter = "[|]";

        return logFileStream
                .map(entry -> entry.split(delimiter))
                .map(AccessRequestEntry::fromStringArray)
                .collect(Collectors.toList());
    }
}