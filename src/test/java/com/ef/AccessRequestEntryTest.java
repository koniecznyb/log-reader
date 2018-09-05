package com.ef;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccessRequestEntryTest {

    @Test
    public void shouldBuildObjectFromStringArray() {
//        given
        String[] logRow = {"2017-01-01 00:00:11.763", "192.168.234.82", "\"GET / HTTP/1.1\"", "200", "\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\""};

//        when
        AccessRequestEntry result = AccessRequestEntry.fromStringArray(logRow);

//        then
        assertEquals("2017-01-01 00:00:11.763", result.getTimeOfOccurrence());
        assertEquals("192.168.234.82", result.getIp());
        assertEquals("\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"", result.getRequestContent());
    }

    @Test
    public void bannedEntryWithReason() {
        //        given
        AccessRequestEntry old = AccessRequestEntry.builder().ip("192.168.0.1").occurrenceCount(200).build();

//        when
        AccessRequestEntry result = AccessRequestEntry.bannedEntryWithReason(old);

//        then
        assertEquals("192.168.0.1", result.getIp());
        assertEquals(200, (int) result.getOccurrenceCount());
        assertEquals("Too many requests found 200", result.getBanReason());
    }
}