package com._604robotics.robotnik.prefabs.flow;

public class Pulse {
    private boolean lastInput = false;

    private boolean risingEdge = false;
    private boolean fallingEdge = false;

    public boolean isRisingEdge () {
        return risingEdge;
    }

    public boolean isFallingEdge () {
        return fallingEdge;
    }

    public void update (boolean input) {
        risingEdge = !lastInput && input;
        fallingEdge = lastInput && !input;

        lastInput = input;
    }
}