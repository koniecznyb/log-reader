package com.ef.database;

import com.ef.AccessRequestEntry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseServiceTest {

    @InjectMocks
    private DatabaseService databaseService;

    @Mock
    private DatabaseClient databaseClient;

    @Test
    public void shouldFindMultipleRequestsBetween() {
//        given
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime startDate = LocalDateTime.parse("2017-01-01 13:00:00.123", dateTimeFormatter);
        LocalDateTime endDate = LocalDateTime.parse("2017-01-01 13:00:00.123", dateTimeFormatter);

        when(databaseClient.findRequestsBetween("2017-01-01 13:00:00.123", "2017-01-01 13:00:00.123", 200)).thenReturn(Arrays.asList(
                AccessRequestEntry.builder().occurrenceCount(201).build(),
                AccessRequestEntry.builder().occurrenceCount(202).build()
        ));

//        when
        Stream<AccessRequestEntry> multipleRequestsBetween = databaseService.findMultipleRequestsBetween(startDate, endDate, 200);
        List<AccessRequestEntry> result = multipleRequestsBetween.collect(Collectors.toList());
//        then
        verify(databaseClient, only()).findRequestsBetween(anyString(), anyString(), anyInt());
        assertFalse(result.isEmpty());
        assertEquals(201, (int) result.get(0).getOccurrenceCount());
        assertEquals(202, (int) result.get(1).getOccurrenceCount());
    }

    @Test
    public void shouldPersistBlockedIpAddress() {
//        given
        List<AccessRequestEntry> blockedAddresses = Arrays.asList(
                AccessRequestEntry.builder().occurrenceCount(201).build(),
                AccessRequestEntry.builder().occurrenceCount(202).build()
        );

//        when
        List<AccessRequestEntry> result = databaseService.persistBlockedIpAddress(blockedAddresses);

//        then
        verify(databaseClient, only()).persistBlockedIpAddresses(blockedAddresses);
        assertEquals(blockedAddresses, result);
    }

    @Test
    public void shouldPersistLogFile() {
        //        given
        List<AccessRequestEntry> logFiles = Arrays.asList(
                AccessRequestEntry.builder().occurrenceCount(201).build(),
                AccessRequestEntry.builder().occurrenceCount(202).build()
        );

//        when
        databaseService.persistLogFile(logFiles);

//        then
        verify(databaseClient, only()).persistLogEntries(logFiles);
    }
}