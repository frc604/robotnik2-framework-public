package com._604robotics.robotnik;

class OutputProxy<T> implements Output<T> {
    private final String name;
    private final Output<T> source;

    private T value;

    public OutputProxy (String name, Output<T> source) {
        if (name.contains(",")) {
            throw new IllegalArgumentException("Output names may not contain commas");
        }
        if( source == null )
        	System.out.println("Source is null.");
        else
        	System.out.println("Source isn't null.\nName is " + name);
        this.name = name;
        this.source = source;
    }

    public String getName () {
        return name;
    }

    @Override
    public T get () {
        return value;
    }

    void update () {
        value = source.get();
    }
}