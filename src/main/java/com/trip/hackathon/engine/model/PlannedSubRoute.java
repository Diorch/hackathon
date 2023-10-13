package com.trip.hackathon.engine.model;

import lombok.Data;

import java.util.List;

/**
 * 子路线
 */
@Data
public class PlannedSubRoute {
    List<PlannedPath> plannedPathSequence;
}