package com._604robotics.robotnik.prefabs.flow;

public class PulseToggle {
    private final Pulse pulse = new Pulse();
    private boolean state;

    public PulseToggle (boolean initialState) {
        state = initialState;
    }

    public boolean on () {
        return state && pulse.get();
    }

    public boolean off () {
        return !state && pulse.get();
    }

    public void update (boolean input) {
        pulse.update(input);
        if (pulse.get()) {
            state = !state;
        }
    }
}