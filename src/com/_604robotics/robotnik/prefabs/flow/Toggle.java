package com._604robotics.robotnik.prefabs.flow;

public class Toggle {
    private final Pulse pulse = new Pulse();
    private boolean state;

    public Toggle (boolean initialState) {
        state = initialState;
    }

    public boolean get () {
        return state;
    }

    public void update (boolean input) {
        pulse.update(input);
        if (pulse.isRisingEdge()) {
            state = !state;
        }
    }
}