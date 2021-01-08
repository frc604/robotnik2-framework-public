package com._604robotics.robotnik.prefabs.motorcontrol;

import com._604robotics.robotnik.Module;
import com._604robotics.robotnik.prefabs.managers.PowerMonitor;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DriverStation;

public class QuixSparkMAX extends MotorController {

  public CANSparkMax controller;

  private String name;

  private Motor motor;

  public QuixSparkMAX(int port, String name, Motor motor, Module module) {
    super(module);
    this.motor = motor;
    if (this.motor == Motor.kNEO || this.motor == Motor.kNEO550) {
      controller = new CANSparkMax(port, MotorType.kBrushless);
    } else {
      controller = new CANSparkMax(port, MotorType.kBrushed);
    }

    controller.enableVoltageCompensation(12);

    PowerMonitor.getInstance().addController(this, name);
  }

  public String getName() {
    return name;
  }

  @Override
  public void set(double power) {
    controller.set(power);
  }

  @Override
  public void enableCurrentLimit(boolean enable) {
    super.isLimiting = enable;

    if (isCurrentLimiting()) {
      controller.setSmartCurrentLimit(getCurrentLimit());
    }
  }

  public void setVoltageCompSaturation(double volts, boolean enable) {
    if (enable) {
      controller.enableVoltageCompensation(volts);
    } else {
      controller.disableVoltageCompensation();
    }
  }

  public void follow(QuixSparkMAX master, boolean inverted) {
    controller.follow(master.controller, inverted);
  }

  @Override
  public double getOutputCurrent() {
    return controller.getOutputCurrent();
  }

  @Override
  public double get() {
    return controller.get();
  }

  @Override
  public void setInverted(boolean inverted) {
    controller.setInverted(inverted);
  }

  @Override
  public boolean getInverted() {
    return controller.getInverted();
  }

  @Override
  public void disable() {
    controller.disable();
  }

  @Override
  public void stopMotor() {
    controller.stopMotor();
  }

  @Override
  public double getOutputVoltage() {
    return controller.get() * getInputVoltage();
  }

  public void burnFlashConditionally(boolean force) {
    if (DriverStation.getInstance().isFMSAttached() || force) {
      controller.burnFlash();
    }
  }

  public void resetParams() {
    controller.restoreFactoryDefaults();
  }

  @Override
  public double getInputVoltage() {
    return controller.getBusVoltage();
  }
}
