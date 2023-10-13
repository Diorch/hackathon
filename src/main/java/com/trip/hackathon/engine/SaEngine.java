package com.trip.hackathon.engine;

import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class SaEngine {


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
