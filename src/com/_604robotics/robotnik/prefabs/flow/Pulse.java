package com._604robotics.robotnik.prefabs.flow;

public class Pulse {
    private boolean pulseActive = false;
    private boolean lastInput = false;

    public boolean get () {
        return pulseActive;
    }

    public void update (boolean input) {
        pulseActive = !lastInput && input;
        lastInput = input;
    }
}