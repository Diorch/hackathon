package com.trip.hackathon.engine.model;

import lombok.Data;

@Data
public class Path {
    public static final String TRANS_WALK = "WALK";
    public static final String TRANS_CAR = "CAR";
    public static final String TRANS_BUS = "BUS";

    POI from;
    POI to;
    // 交通方式：步行、驾车、公共交通
    String trans;
    // 里程，单位米
    int distance;
    // 耗时，单位秒
    int spentTime;
}
