package com._604robotics.robotnik;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.tables.ITable;

public abstract class Action {
    private final Module parent;
    private final String name;
    
    @SuppressWarnings("rawtypes")
    private final List<Input> inputs = new ArrayList<>();
    private String inputListValue = "";
    
    public Action (Module parent, String name) {
        this.parent = parent;
        this.name = name;
    }
    
    public String getName () {
        return name;
    }
    
    protected <T> Input<T> addInput (String name, T initialValue) {
        return addInput(new Input<T>(name, initialValue));
    }
    
    protected <T> Input<T> addInput (Input<T> input) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("Input names may not be empty");
        }
        if (name.contains(",")) {
            throw new IllegalArgumentException("Input names may not contain commas");
        }

        inputs.add(input);
        inputListValue = inputListValue.isEmpty() ? input.getName() : inputListValue + "," + input.getName();
        return input;
    }
    
    public void activate () {
        parent.activate(this);
    }
    
    void updateActiveAction (ITable activeActionTable) {
        activeActionTable.putString("name", getName());
        activeActionTable.putString("inputList", inputListValue);
    }
    
    void updateInputs (ITable activeActionInputsTable) {
        for (@SuppressWarnings("rawtypes") Input input : inputs) {
            activeActionInputsTable.putValue(input.getName(), input.get());
        }
    }
    
    protected void begin () {}
    protected void run () {}
    protected void end () {}
}