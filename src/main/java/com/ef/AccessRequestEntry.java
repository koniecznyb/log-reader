package com.ef;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@AllArgsConstructor
public final class AccessRequestEntry {

    private Integer id;
    private String ip;
    private String requestContent;
    private String timeOfOccurrence;
    private String banReason;
    private Integer occurrenceCount;

    public static AccessRequestEntry fromStringArray(String[] splitLogEntry) {
        return AccessRequestEntry.builder()
                .timeOfOccurrence(splitLogEntry[0])
                .ip(splitLogEntry[1])
                .requestContent(splitLogEntry[2] + "|" + splitLogEntry[3] + "|" + splitLogEntry[4])
                .build();
    }

    public static AccessRequestEntry bannedEntryWithReason(AccessRequestEntry old) {
        return AccessRequestEntry.builder()
                .ip(old.getIp())
                .occurrenceCount(old.getOccurrenceCount())
                .banReason("Too many requests found " + old.getOccurrenceCount())
                .build();
    }
}
