package com.ef;

import com.ef.reader.LogReader;
import lombok.AllArgsConstructor;
import picocli.CommandLine;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Stream;


@AllArgsConstructor
public class ParserImpl {

    private LogReader logReader;

    public void parse(Stream<String> logFile, LocalDate startDate, LocalDate endDate, int threshold) {
        Set<String> ilogReader.findIpAddresses(logFile, startDate, endDate, threshold);
    }
}
