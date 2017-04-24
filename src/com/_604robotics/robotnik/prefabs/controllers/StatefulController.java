package com._604robotics.robotnik.prefabs.controllers;

import com._604robotics.robotnik.Controller;
import com._604robotics.robotnik.Logger;

public abstract class StatefulController extends Controller {
    private final Logger logger;

    private State initialState;
    private State state;

    public StatefulController (String name) {
        logger = new Logger(StatefulController.class, name);
    }

    protected void setInitialState (State initialState) {
        this.initialState = initialState;
    }

    @Override
    protected void begin () {
        logger.info("Begin");
        setState(initialState);
    }

    @Override
    protected void run () {
        if (state != null) {
            setState(state.run());
        }
    }

    @Override
    protected void end () {
        stopState();
        logger.info("End");
    }

    private void setState (State state) {
        if (state == this.state) {
            return;
        }

        stopState();
        this.state = state;

        if (state == null) {
            logger.info("Null State");
        } else {
            logger.info("Begin State: " + state.name);
            state.begin();
        }
    }

    private void stopState () {
        if (state == null) {
            return;
        }

        state.end();
        logger.info("End State: " + state.name);
        state = null;
    }

    public static abstract class State {
        private final String name;

        public State (String name) {
            this.name = name;
        }

        protected void begin () {}
        protected State run () { return this; }
        protected void end () {}
    }
}