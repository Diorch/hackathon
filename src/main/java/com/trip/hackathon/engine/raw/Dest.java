package com.trip.hackathon.engine.raw;

import lombok.Data;

import java.util.Set;

/**
 * 目的地
 */
@Data
public class Dest {
    long id;
    String name;
    Set<POI> poiSet;

    int hardRegion;
    int softRegion;
}
