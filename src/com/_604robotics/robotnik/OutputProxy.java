package com._604robotics.robotnik;

import java.util.regex.Pattern;

class OutputProxy<T> implements Output<T> {
    private final String name;
    private final Output<T> source;

    private static final String javaLang = "java\\.lang\\..+";
    private final boolean isEnum;

    private T value;

    public OutputProxy (String name, Output<T> source) {
        if (name.contains(",")) {
            throw new IllegalArgumentException("Output names may not contain commas");
        }

        this.name = name;
        this.source = source;
        System.out.println("OutputProxy:");
        System.out.println("The class contained is:"+source.get().getClass().getName());
        isEnum=!(Pattern.matches(javaLang, source.get().getClass().getName()));
        System.out.println("Contains enum="+isEnum);
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