package com._604robotics.robotnik.prefabs.controller;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;

/**
 * Subclass of PIDController that has a feedforward for auto aligning of a limelight angle. This
 * subclass requires an double as the PIDSource and uses only continuous error.
 */
public class AutoTargetPIDController extends ProfiledPIDController {
  private SimpleMotorFeedforward m_feedforward;

  public AutoTargetPIDController(
      double Kp,
      double Ki,
      double Kd,
      TrapezoidProfile.Constraints constraints,
      SimpleMotorFeedforward feedforward,
      DoubleSupplier source,
      DoubleConsumer output) {
    super(Kp, Ki, Kd, constraints, source, output);

    m_feedforward = feedforward;
  }

  public AutoTargetPIDController(
      double Kp,
      double Ki,
      double Kd,
      TrapezoidProfile.Constraints constraints,
      SimpleMotorFeedforward feedforward,
      DoubleSupplier source,
      DoubleConsumer output,
      double period) {
    super(Kp, Ki, Kd, constraints, period, source, output);

    m_feedforward = feedforward;
  }

  /**
   * Overridden feed forward part of PIDController.
   *
   * @return the feed forward value
   */
  @Override
  protected double calculateFeedForward(double setpoint) {
    return m_feedforward.calculate(setpoint);
  }
}
