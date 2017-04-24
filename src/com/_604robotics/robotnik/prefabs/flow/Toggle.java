package com._604robotics.robotnik.prefabs.flow;

public class Toggle {
    private boolean lastInput = false;
    private boolean state;

    public Toggle (boolean initialState) {
        state = initialState;
    }

    public boolean get () {
        return state;
    }

    public void update (boolean input) {
        if (!lastInput && input) {
            state = !state;
        }

        lastInput = input;
    }
}