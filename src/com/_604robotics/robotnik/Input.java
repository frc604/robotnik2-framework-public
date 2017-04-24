package com._604robotics.robotnik;

public class Input<T> {
    private final String name;

    private T value;
    private boolean updated = false;
    
    Input (String name, T initialValue) {
        this.name = name;
        value = initialValue;
    }
    
    public String getName () {
        return name;
    }
    
    public T get () {
        return value;
    }

    public void set (T value) {
        this.value = value;
        updated = true;
    }

    public boolean isUpdated () {
        return updated;
    }

    void clearUpdated () {
        updated = false;
    }
}