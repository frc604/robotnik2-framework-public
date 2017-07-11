package com._604robotics.robotnik;

import java.util.regex.Pattern;

public class Input<T> {

    private final Module parent;

    private final String name;

    private final T defaultValue;
    private T value;
    private long valueEpoch = -1;

    Input (Module parent, String name, T defaultValue) {
        this.parent = parent;
        this.name = name;
        this.defaultValue = defaultValue;
    }

    public String getName () {
        return name;
    }

    public T get () {
        if (valueEpoch == parent.getEpoch()) {
            return value;
        } else {
            return defaultValue;
        }
    }

    public void set (T value) {
        this.value = value;
        valueEpoch = parent.getEpoch();
    }

    public boolean isFresh () {
        final long currentEpoch = parent.getEpoch();
        return valueEpoch == currentEpoch || valueEpoch == currentEpoch - 1;
    }
}