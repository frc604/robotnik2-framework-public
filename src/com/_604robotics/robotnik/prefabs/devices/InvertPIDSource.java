package com._604robotics.robotnik.prefabs.devices;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class InvertPIDSource implements PIDSource {
    private final PIDSource wrappedSource;

    public InvertPIDSource(PIDSource Source) {
        this.wrappedSource = Source;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        wrappedSource.setPIDSourceType(pidSource);

    }

    @Override
    public PIDSourceType getPIDSourceType() {
        // TODO Auto-generated method stub
        return wrappedSource.getPIDSourceType();
    }

    @Override
    public double pidGet() {
        // TODO Auto-generated method stub
        return -wrappedSource.pidGet();
    }

}
