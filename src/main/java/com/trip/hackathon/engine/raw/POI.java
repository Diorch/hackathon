package com.trip.hackathon.engine.raw;


import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class POI {
    Dest dest;
    int id;
    String name;
    double hotScore;
    //最小游玩时间，单位小时
    float minSpentTime;
    //最大游玩时间，单位小时
    float maxSpentTime;
    Set<OpeningHours> openingHoursSet;

    BigDecimal lat;
    BigDecimal lng;

    @Data
    static class OpeningHours {
        int ruleId;
        // week, holiday, descriptive
        String rule;
        //{最晚入场时间}-{停止售票时间}
        // eg.1 12:00-14:00
        // eg.2 12:00-
        String planTimeRange;
        String openWeekRange;
        // 1表示全天；-1表示不开放；
        String openTimeRange;
        // 1表示全年
        String openDateRange;
    }
}
