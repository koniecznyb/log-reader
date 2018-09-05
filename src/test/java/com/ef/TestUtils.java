package com.ef;

import java.util.Arrays;
import java.util.List;

public class TestUtils {
    public static List<AccessRequestEntry> buildAccessRequestEntryList() {
        return Arrays.asList(
                buildAccessRequestEntry(
                        "192.168.0.1",
                        "2017-01-01 05:38:36.508",
                        "GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36",
                        "reason1"),
                buildAccessRequestEntry(
                        "192.168.0.3",
                        "2017-03-01 04:38:36.508",
                        "GET / HTTP/1.1\"|200|\"Mozilla/5.0 (Linux; Android 5.1.1; Nexus 4 Build/LMY48T) AppleWebKit/537.36 (" +
                                "KHTML, like Gecko) Chrome/60.0.3112.116 Mobile Safari/537.36",
                        "reason2")
        );
    }

    private static AccessRequestEntry buildAccessRequestEntry(String ip, String time, String content, String banReason) {
        return AccessRequestEntry.builder().ip(ip).timeOfOccurrence(time).requestContent(content).banReason(banReason).build();
    }
}
