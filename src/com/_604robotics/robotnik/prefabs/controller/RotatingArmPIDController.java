package com._604robotics.robotnik.prefabs.controller;

import com._604robotics.robotnik.prefabs.devices.AbsoluteEncoder;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 * <p>Subclass of PIDController that has a feedforward for rotating arms.</p>
 * This subclass requires an AbsoluteEncoder as the PIDSource and uses only continuous error.
 * Zero is assumed to be horizontal. Users are responsible for properly zeroing the AbsoluteEncoder beforehand.
 */
public class RotatingArmPIDController extends PIDController {

    public RotatingArmPIDController(double Kp, double Ki, double Kd, AbsoluteEncoder source, PIDOutput output) {
        super(Kp, Ki, Kd, source, output);
        setContinuous();
    }

    public RotatingArmPIDController(double Kp, double Ki, double Kd, AbsoluteEncoder source, PIDOutput output,
            double period) {
        super(Kp, Ki, Kd, source, output, period);
        setContinuous();
    }

    public RotatingArmPIDController(double Kp, double Ki, double Kd, double Kf, AbsoluteEncoder source, PIDOutput output) {
        super(Kp, Ki, Kd, Kf, source, output);
        setContinuous();
    }

    public RotatingArmPIDController(double Kp, double Ki, double Kd, double Kf, AbsoluteEncoder source, PIDOutput output,
            double period) {
        super(Kp, Ki, Kd, Kf, source, output, period);
        setContinuous();
    }

    @Override
    public synchronized void setContinuous(boolean continuous) {
        if (!continuous) {
            throw new IllegalArgumentException("RotatingArmPIDController must have continuous error!");
        }
        super.setContinuous(continuous);
    }

    @Override
    public synchronized void setContinuous() {
        // TODO Auto-generated method stub
        super.setContinuous();
    }

    /**
     * <p>Overriden feed forward part of PIDController.</p>
     * 
     * This is a physically based model which multiplies feed forward coefficient by cosine.
     * The feedforward calculates the expected torque needed to hold an arm steady, scaled to motor power.
     * 
     *  @return the feed forward value
     */
    @Override
    protected double calculateFeedForward() {
        // Calculate cosine for torque factor
        double angle;
        double fValue;
        synchronized (this) {
            angle = m_pidInput.pidGet();
            fValue = getF();
        }
        // Cosine is periodic so sawtooth wraparound is not a concern
        double cosine = Math.cos(Math.toRadians(angle));
        return fValue * cosine;
    }

}
