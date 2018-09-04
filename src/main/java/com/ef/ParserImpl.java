package com.ef;

import com.ef.database.DatabaseClient;
import com.ef.reader.LogReader;
import lombok.AllArgsConstructor;
import sun.rmi.runtime.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;


@AllArgsConstructor
public class ParserImpl {

    private DatabaseClient databaseClient;
    private LogReader logReader;

    public void parse(Stream<String> logFile) {
        logReader.findIpAddresses(logFile)
                .stream()
                .map(databaseClient::persist)
                .forEach(System.out::println);
    }
}
