package com._604robotics.robotnik.prefabs.motion;

public class MotionState {
  public double position;
  public double velocity;

  public MotionState(double position, double velocity) {
    this.position = position;
    this.velocity = velocity;
  }

  public MotionState() {
    this(0.0, 0.0);
  }

  public boolean almostEqual(MotionState obj, double eps) {
    return (Math.abs(this.position - obj.position) < eps
        && Math.abs(this.velocity - obj.velocity) < eps);
  }

  public String toString() {
    return String.format("(position: %.3f, velocity: %.3f)", this.position, this.velocity);
  }
}
