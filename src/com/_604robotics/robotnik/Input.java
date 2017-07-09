package com._604robotics.robotnik;

import java.util.regex.Pattern;

public class Input<T> {

    private static final String javaLang = "java\\.lang\\..+";
    private final Module parent;

    private final String name;

    private final T defaultValue;
    private T value;
    private long valueEpoch = -1;
    private final boolean isEnum;

    Input (Module parent, String name, T defaultValue) {
        this.parent = parent;
        this.name = name;
        this.defaultValue = defaultValue;
        System.out.println("Input:");
        System.out.println("The class contained is:"+defaultValue.getClass().getName());
        isEnum=!(Pattern.matches(javaLang, defaultValue.getClass().getName()));
        System.out.println("Contains enum="+isEnum);
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