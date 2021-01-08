package com._604robotics.robotnik.prefabs.devices;

import com._604robotics.robotnik.prefabs.motorcontrol.QuixSparkMAX;
import com._604robotics.robotnik.prefabs.motorcontrol.gearing.CalculableRatio;
import com.revrobotics.CANEncoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NEOEncoder implements Encoder {
  private final CANEncoder m_encoder;

  private CalculableRatio m_ratio;

  private boolean inverted = false;

  public NEOEncoder(QuixSparkMAX spark) {
    m_encoder = spark.controller.getEncoder();
    m_ratio = null;
  }

  public NEOEncoder(QuixSparkMAX spark, CalculableRatio ratio) {
    m_encoder = spark.controller.getEncoder();
    m_ratio = ratio;
  }

  public boolean getInverted() {
    return inverted;
  }

  public void setInverted(boolean inverted) {
    this.inverted = inverted;
  }

  @Override
  public double getValue() {
    return getPosition();
  }

  public void setdistancePerRotation(double distancePerRotation) {
    if (m_ratio == null) {
      m_encoder.setPositionConversionFactor(distancePerRotation);
      m_encoder.setVelocityConversionFactor(distancePerRotation * (1.0 / 60.0));
    } else {
      m_encoder.setPositionConversionFactor(m_ratio.calculate(distancePerRotation));
      SmartDashboard.putNumber("REUDCC", m_ratio.calculate(1.0));
      System.out.println(
          "Soarks are dumb " + (m_ratio.calculate(distancePerRotation) * (1.0 / 60.0)));
      m_encoder.setVelocityConversionFactor(m_ratio.calculate(distancePerRotation) * (1.0 / 60.0));
    }
  }

  public double getdistancePerRotation() {
    return m_encoder.getPositionConversionFactor();
  }

  public double getPosition() {
    return getPos();
  }

  public double getVelocity() {
    return getVel();
  }

  public void zero() {
    m_encoder.setPosition(0.0);
  }

  public void zero(double value) {
    m_encoder.setPosition(value);
  }

  private double getPos() {
    int factor;

    if (inverted) {
      factor = -1;
    } else {
      factor = 1;
    }

    return m_encoder.getPosition() * factor;
  }

  private double getVel() {
    int factor;

    if (inverted) {
      factor = -1;
    } else {
      factor = 1;
    }

    return m_encoder.getVelocity() * factor;
  }
}
