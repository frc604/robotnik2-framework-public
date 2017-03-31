package com._604robotics.robotnik;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public abstract class Module {
    private final ITable table;

    private final ITable inputsTable;
    private final TableIndex inputsTableIndex;

    private final ITable outputsTable;
    private final TableIndex outputsTableIndex;
    
    private final ITable activeActionTable;
    private final ITable activeActionInputsTable;

    private final String name;
    
    private Action defaultAction;
    private Action lastAction;
    private Action activeAction;

    @SuppressWarnings("rawtypes")
    private final List<Input> inputs = new ArrayList<>();
    @SuppressWarnings("rawtypes")
    private final List<OutputProxy> outputs = new ArrayList<>();
    
    protected void begin () {}
    protected void end () {} 

    public Module (String name) {
        this.name = name;

        table = NetworkTable.getTable("robotnik")
                .getSubTable("modules")
                .getSubTable(name);

        inputsTable = table.getSubTable("inputs");
        inputsTableIndex = new TableIndex(table, "inputList");

        outputsTable = table.getSubTable("outputs");
        outputsTableIndex = new TableIndex(table, "outputList");

        activeActionTable = table.getSubTable("activeAction");
        activeActionTable.putString("name", "");
        activeActionTable.putString("inputList", "");
        
        activeActionInputsTable = activeActionTable.getSubTable("inputs");
    }
    
    public String getName () {
        return name;
    }
    
    protected void setDefaultAction (Action action) {
        this.defaultAction = action;
    }
    
    protected <T> Input<T> addInput (String name, T initialValue) {
        return addInput(new Input<T>(name, initialValue));
    }
    
    protected <T> Input<T> addInput (Input<T> input) {
        inputs.add(input);
        inputsTableIndex.add("Input", input.getName());
        return input;
    }
    
    protected <T> Output<T> addOutput (String name, Output<T> output) {
        final OutputProxy<T> proxy = new OutputProxy<>(name, output);
        outputs.add(proxy);
        outputsTableIndex.add("Output", name);
        return proxy;
    }
    
    void update () {
        for (@SuppressWarnings("rawtypes") Input input : inputs) {
            inputsTable.putValue(input.getName(), input.get());
        }

        for (@SuppressWarnings("rawtypes") OutputProxy output : outputs) {
            Reliability.swallowThrowables(output::update,
                    "Error updating output " + output.getName() + " of module " + getName());
            outputsTable.putValue(output.getName(), output.get());
        }
    }
    
    void reset () {
        activeAction = defaultAction;
    }

    void activate (Action action) {
        activeAction = action;
    }
    
    void execute () {
        if (activeAction != lastAction) {
            if (lastAction != null) {
                Reliability.swallowThrowables(lastAction::end,
                        "Error in end() of action " + lastAction.getName() + " of module " + getName());
            }

            lastAction = activeAction;

            if (activeAction == null) {
                activeActionTable.putString("name", "");
                activeActionTable.putString("inputList", "");
            } else {
                activeAction.updateActiveAction(activeActionTable);
                Reliability.swallowThrowables(activeAction::begin,
                        "Error in begin() of action " + activeAction.getName() + " of module " + getName());
            }
        }
        
        if (activeAction != null) {
            activeAction.updateInputs(activeActionInputsTable);
            Reliability.swallowThrowables(activeAction::run,
                    "Error in run() of action " + activeAction.getName() + " of module " + getName());
        }
    }
    
    void terminate () {
        if (lastAction != null) {
            Reliability.swallowThrowables(lastAction::end,
                    "Error in end() of action " + lastAction.getName() + " of module " + getName());
        }
        lastAction = null;

        activeActionTable.putString("name", "");
        activeActionTable.putString("inputList", "");

        Reliability.swallowThrowables(this::end,
                "Error in end() of module " + getName());
    }
    
    private static class OutputProxy<T> implements Output<T> {
        private final String name;
        private final Output<T> source;

        private T value;

        public OutputProxy (String name, Output<T> source) {
            if (name.contains(",")) {
                throw new IllegalArgumentException("Output names may not contain commas");
            }

            this.name = name;
            this.source = source;
        }
        
        public String getName (){
            return name;
        }

        @Override
        public T get () {
            return value;
        }
        
        private void update () {
            value = source.get();
        }
    }
}