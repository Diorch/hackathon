package com.trip.hackathon.engine.model;

import lombok.Data;

import java.util.List;

/**
 * 母路线
 */
@Data
public class PlannedRoute {

    /**
     * 子路线集
     */
    List<SubRoute> subRouteSequence;

    /**
     * 子路线
     */
    @Data
    static class SubRoute {
        List<PlannedPath> plannedPathSequence;
    }
}
