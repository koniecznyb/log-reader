package com.ef.reader;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

public class LogReader {

    private HashMap<String, Integer> ipCache = new HashMap<>();
    String delimiter = "|";

    public Stream<String> findIpAddresses(Stream<String> input, Duration duration, int threshold){
        input
                .map(entry -> entry.split(delimiter))
                .filter(splitEntry -> splitEntry[0])
                .map(strings -> {
                    String ip = strings[1];
                    String date = strings[0];

                    if(ipCache.containsKey(ip) && (ipCache.get(ip) >= threshold) ){
                        return input;
                    }
                });
    }

    private boolean isWithinTimePeriod(LocalDate startTime, LocalDate endTime, LocalDate )
}