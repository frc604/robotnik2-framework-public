package com._604robotics.robotnik.prefabs.auto.angular;

import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.State;

public class TurnInPlaceTrajectory {
  private TrapezoidProfile m_profile;

  public TrapezoidProfile.Constraints m_constraints;

  private Pose2d m_initial;
  private Rotation2d m_end;

  private TrapezoidProfile.State m_prevRef;

  public TurnInPlaceTrajectory(
      Pose2d initialPose, Rotation2d endRotation, AngularMotionConstraint constraint) {
    m_initial = initialPose;
    m_end = endRotation;

    m_constraints =
        new TrapezoidProfile.Constraints(
            constraint.maxAngularVelocity, constraint.maxAngularAcceleration);

    m_profile =
        new TrapezoidProfile(
            m_constraints,
            new State(m_end.getRadians(), 0),
            new State(m_initial.getRotation().getRadians(), 0));

    m_prevRef = new TrapezoidProfile.State(m_initial.getRotation().getRadians(), 0.0);
  }

  public Pose2d getInitialPose() {
    return m_initial;
  }

  public AngularTrajectoryState sample(double seconds, double dt) {
    m_profile = new TrapezoidProfile(m_constraints, new State(m_end.getRadians(), 0), m_prevRef);

    TrapezoidProfile.State state = m_profile.calculate(dt);

    SmartDashboard.putNumber("State", new Rotation2d(state.position).getRadians());

    SmartDashboard.putNumber("Time", m_profile.totalTime());

    m_prevRef = state;

    return new AngularTrajectoryState(
        seconds,
        new Pose2d(m_initial.getTranslation(), new Rotation2d(state.position)),
        state.velocity);
  }

  public double getTotalTimeSeconds() {
    return m_profile.totalTime();
  }
}
