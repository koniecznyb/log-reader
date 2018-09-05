package com.ef.reader;

import com.ef.AccessRequestEntry;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class LogReaderTest {

    LogReader logReader = new LogReader();

    @Test
    public void shouldCorrectlyMapLogFile() {
//        given
        Stream<String> logFile = Stream.of(
                "2017-01-01 05:38:36.508|192.168.173.123|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
                "2017-01-01 05:38:36.699|192.168.229.53|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:35.0) Gecko/20100101 Firefox/35.0",
                "2017-01-01 05:38:37.338|192.168.145.75|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1",
                "2017-01-01 05:38:39.228|192.168.187.167|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36",
                "2017-01-01 05:38:40.512|192.168.104.10|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:55.0) Gecko/20100101 Firefox/55.0",
                "2017-01-01 05:38:40.887|192.168.229.103|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
                "2017-01-01 05:38:42.089|192.168.206.141|\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 4 Build/LMY48T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.116 Mobile Safari/537.36");

//        when
        List<AccessRequestEntry> accessRequestEntries = logReader.mapToLogEntryList(logFile);


//        then
        assertEquals(7, accessRequestEntries.size());
        AccessRequestEntry lastLogEntry = accessRequestEntries.get(6);
        assertEquals("2017-01-01 05:38:42.089", lastLogEntry.getTimeOfOccurrence());
        assertEquals("\"GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 4 Build/LMY48T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.116 Mobile Safari/537.36", lastLogEntry.getRequestContent());
    }

    @Test
    public void shouldIgnoreEmptyStream() {
//        given
        Stream<String> logFile = Stream.empty();

//        when
        List<AccessRequestEntry> accessRequestEntries = logReader.mapToLogEntryList(logFile);

//        then
        assertEquals(0, accessRequestEntries.size());
    }
}