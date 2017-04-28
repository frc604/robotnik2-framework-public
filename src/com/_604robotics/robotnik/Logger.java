package com._604robotics.robotnik;

import com._604robotics.robotnik.prefabs.flow.SmartTimer;
import edu.wpi.first.wpilibj.DriverStation;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Logger {
    private static SmartTimer bootTimer = new SmartTimer();

    static {
        bootTimer.start();
    }

    private final String name;

    public Logger (String name) {
        this.name = name;
    }

    public Logger (String parentName, String name) {
        this.name = parentName + ":" + name;
    }

    public Logger (@SuppressWarnings("rawtypes") Class c) {
        this(c.getSimpleName());
    }

    public Logger (@SuppressWarnings("rawtypes") Class c, String name) {
        this(c.getSimpleName(), name);
    }

    public Logger (@SuppressWarnings("rawtypes") Class c, Class c2) {
        this(c.getSimpleName(), c2.getSimpleName());
    }

    public void log (String level, String message) {
        System.out.println(
                "* [Boot Time: " + bootTimer.get() +
                "]\t[Match Time: " + DriverStation.getInstance().getMatchTime() +
                "]\n- [" + level +
                "]\t[" + name +
                "]\t" + message +
                "\n");
    }

    public void info (String message) {
        log("INFO", message);
    }

    public void error (String message, Throwable t) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        pw.println(message);
        t.printStackTrace(pw);

        log("ERROR", sw.toString());
    }
}