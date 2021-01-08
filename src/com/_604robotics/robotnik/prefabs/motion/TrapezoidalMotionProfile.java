package com._604robotics.robotnik.prefabs.motion;

public class TrapezoidalMotionProfile {
  private MotionState initial;
  private MotionState goal;
  private MotionConstraints constraints;

  private double maxVel;
  private double maxAccel;

  private double areaAccel;
  private double areaDeccel;

  private double t_1;
  private double t_3;
  private double t_2 = 0.0;

  public TrapezoidalMotionProfile() {}

  public void setParams(MotionState initial, MotionState goal, MotionConstraints constraints) {
    this.initial = initial;
    this.goal = goal;
    this.constraints = constraints;

    this.maxVel = Math.abs(constraints.maxVelocity);
    this.maxAccel = Math.abs(constraints.maxAcceleration);

    calculateParams();
  }

  private void calculateParams() {
    double displacement = goal.position - initial.position;

    if (goal.position < initial.position) {
      maxAccel = -maxAccel;
      maxVel = -maxVel;
    }

    t_1 = (maxVel - initial.velocity) / maxAccel;
    t_3 = (maxVel - goal.velocity) / maxAccel;

    areaAccel = areaTrapezoid(initial.velocity, maxVel, t_1);
    areaDeccel = areaTrapezoid(goal.velocity, maxVel, t_3);

    if ((Math.abs(areaAccel) + Math.abs(areaDeccel)) > Math.abs(displacement)) {
      // Triangular
      maxVel =
          Math.sqrt(
                  Math.abs(
                      (Math.pow(initial.velocity, 2)
                              + 2 * maxAccel * displacement
                              + Math.pow(goal.velocity, 2))
                          / 2))
              * Math.signum(maxVel);

      areaAccel = areaTrapezoid(initial.velocity, maxVel, t_1);
      areaDeccel = areaTrapezoid(goal.velocity, maxVel, t_3);

      t_1 = (maxVel - initial.velocity) / (maxAccel);
      t_3 = (maxVel - goal.velocity) / (maxAccel);
    } else {
      // Trapezoidal
      t_2 =
          Math.abs(
              (Math.abs(displacement) - (Math.abs(areaAccel) + Math.abs(areaDeccel))) / (maxVel));
    }

    if (t_1 < 0 || t_2 < 0 || t_3 < 0) {
      throw new RuntimeException("TrapezoidalMotionProfile parameters are unachievable");
    }
  }

  public MotionState sample(double t) {
    this.checkAndThrow();

    double position;
    double velocity;

    if (t < 0) {
      throw new RuntimeException("TrapezoidalMotionProfile cannot sample at time less than zero");
    } else if (t <= t_1) {
      // Acceleration segment
      velocity = maxAccel * t + initial.velocity;
      position = areaTrapezoid(initial.velocity, velocity, t) + initial.position;
    } else if (t <= (t_1 + t_2)) {
      // Cruise segment
      velocity = maxVel;
      position = areaAccel + (velocity * (t - t_1)) + initial.position;
    } else if (t <= getTotalTime()) {
      // Deceleration segment
      velocity = -maxAccel * (t - (t_1 + t_2)) + maxVel;
      position =
          areaTrapezoid(maxVel, velocity, t - (t_1 + t_2))
              + areaTrapezoid(initial.velocity, maxVel, t_1)
              + (maxVel * t_2)
              + initial.position;
    } else {
      throw new RuntimeException(
          "TrapezoidalMotionProfile cannot sample at time greater than totalTime");
    }

    return new MotionState(position, velocity);
  }

  public double getTotalTime() {
    this.checkAndThrow();
    return t_2 + t_1 + t_3;
  }

  public double getAccelPeriod() {
    this.checkAndThrow();
    return t_1;
  }

  public double getCruisePeriod() {
    this.checkAndThrow();
    return t_2;
  }

  public double getDeccelPeriod() {
    this.checkAndThrow();
    return t_3;
  }

  public boolean getInverted() {
    this.checkAndThrow();
    return maxVel < 0.0;
  }

  private double areaTrapezoid(double b, double b_1, double h) {
    return 0.5 * (b + b_1) * h;
  }

  private void checkAndThrow() {
    if (this.initial == null || this.goal == null || this.constraints == null)
      throw new RuntimeException("TrapezoidalMotionProfile has no params set");
  }
}
