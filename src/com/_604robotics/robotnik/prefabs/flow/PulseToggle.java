package com._604robotics.robotnik.prefabs.flow;

public class PulseToggle {
    private final Pulse pulse = new Pulse();
    private boolean state;

    public PulseToggle (boolean initialState) {
        state = initialState;
    }

    public boolean isInOnState () {
        return state;
    }

    public boolean isInOffState () {
        return !state;
    }

    public boolean isEnteringOnState () {
        return state && pulse.isRisingEdge();
    }

    public boolean isEnteringOffState () {
        return !state && pulse.isRisingEdge();
    }

    public boolean isLeavingOnState () {
        return state && pulse.isFallingEdge();
    }

    public boolean isLeavingOffState () {
        return !state && pulse.isFallingEdge();
    }

    public void update (boolean input) {
        pulse.update(input);
        if (pulse.isRisingEdge()) {
            state = !state;
        }
    }
}