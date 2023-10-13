package com.trip.hackathon.engine;

import org.springframework.stereotype.Component;

@Component
public class RoutingEngine {


    public Object run() {
        this.warmUp();
        this.doWork();
        return this.getResult();
    }

    public void warmUp() {

    }

    public void doWork() {

    }

    public Object getResult() {
        return null;
    }
}
