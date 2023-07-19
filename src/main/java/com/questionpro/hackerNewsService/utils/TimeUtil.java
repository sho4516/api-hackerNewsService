package com.questionpro.hackerNewsService.utils;

import com.questionpro.hackerNewsService.Payload.Dto.ItemDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeUtil {

    public static boolean isWithinLast15Minutes(ItemDto story) {
        Instant fifteenMinutesAgo = Instant.now().minus(500, ChronoUnit.MINUTES);
        Instant storyTime = Instant.ofEpochSecond(story.getTime());
        return storyTime.isAfter(fifteenMinutesAgo);
    }

    public static int getEpochSecond(int offset){
        return (int) Instant.now().minus(offset, ChronoUnit.HOURS).getEpochSecond();
    }
}
