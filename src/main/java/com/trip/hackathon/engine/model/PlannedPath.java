package com.trip.hackathon.engine.model;

import lombok.Data;

@Data
public class PlannedPath {

    POI from;
    POI to;
    Path path;
}
