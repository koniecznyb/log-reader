package com.ef;

import com.ef.database.DatabaseService;
import com.ef.reader.LogReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ParserImplTest {

    @InjectMocks
    private ParserImpl parserImpl;

    @Mock
    private DatabaseService databaseService;

    @Mock
    private LogReader logReader;

    @Test
    public void shouldIgnoreEmptyStream() {
//        given
        Stream<String> logFile = Stream.empty();
        when(logReader.mapToLogEntryList(logFile)).thenReturn(Collections.emptyList());

//        when
        parserImpl.persistLogFileIfPresent(logFile);

//        then
        verify(logReader, only()).mapToLogEntryList(logFile);
        verifyZeroInteractions(databaseService);
    }

    @Test
    public void shouldPersistLogFileIfPresent() {
//        given
        List<AccessRequestEntry> accessRequestEntries = TestUtils.buildAccessRequestEntryList();
        Stream<String> logFile = Stream.of("row1", "row2", "row3");
        when(logReader.mapToLogEntryList(logFile)).thenReturn(accessRequestEntries);

//        when
        parserImpl.persistLogFileIfPresent(logFile);

//        then
        verify(logReader, only()).mapToLogEntryList(logFile);
        verify(databaseService, only()).persistLogFile(accessRequestEntries);
    }


    @Test
    public void shouldFindBannedIpAddresses() {
//        given
        List<AccessRequestEntry> accessRequestEntries = TestUtils.buildAccessRequestEntryList();
        Duration duration = Duration.ofHours(1);
        LocalDateTime startDate = LocalDateTime.of(2018, 1, 28, 1, 1);
        int threshold = 200;

        when(databaseService.findMultipleRequestsBetween(startDate, startDate.plus(duration), threshold))
                .thenReturn(Stream.of(accessRequestEntries.get(0), accessRequestEntries.get(1)));

//        when
        List<AccessRequestEntry> bannedIpAddresses = parserImpl.findBannedIpAddresses(duration, startDate, threshold);

//        then
        verify(databaseService, only()).findMultipleRequestsBetween(startDate, startDate.plus(duration), threshold);
        assertFalse(bannedIpAddresses.isEmpty());
    }

    @Test
    public void shouldPersistBannedAddresses() {
        //        given
        List<AccessRequestEntry> accessRequestEntries = TestUtils.buildAccessRequestEntryList();
        when(databaseService.persistBlockedIpAddress(accessRequestEntries)).thenReturn(accessRequestEntries);

//        when
        List<String> result = parserImpl.persistBannedAddresses(accessRequestEntries);

//        then
        verify(databaseService, only()).persistBlockedIpAddress(accessRequestEntries);
        assertFalse(result.isEmpty());
        assertEquals("Blocked ipAddress=192.168.0.1 reason=reason1", result.get(0));
        assertEquals("Blocked ipAddress=192.168.0.3 reason=reason2", result.get(1));
    }
}