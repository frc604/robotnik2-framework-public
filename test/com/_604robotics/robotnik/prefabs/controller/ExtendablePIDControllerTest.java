/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/* This is 604's custom version to test the re-written multithreaded          */
/* controllers                                                                */
/*----------------------------------------------------------------------------*/
package com._604robotics.robotnik.prefabs.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendablePIDControllerTest {
  private ExtendablePIDController m_controller;
  private double m_measurement = 0.0;
  private double m_output = 0.0;

  @BeforeEach
  void setUp() {
    m_controller =
        new ExtendablePIDController(0, 0, 0, () -> m_measurement, (output) -> m_output = output);
  }

  @Test
  void continuousInputTest() {
    m_controller.setP(1);
    m_controller.enableContinuousInput(-180, 180);

    m_controller.setSetpoint(179);
    m_measurement = -179;

    m_controller.setEnabled(true);
    m_controller.m_controlLoop.cancel();

    m_controller.calculate();

    assertTrue(m_output < 0.0);
    m_controller.setEnabled(false);
  }

  @Test
  void proportionalGainOutputTest() {
    m_controller.setP(4);

    m_controller.setSetpoint(0);
    m_measurement = 0.025;

    m_controller.setEnabled(true);
    m_controller.m_controlLoop.cancel();

    m_controller.calculate();

    assertEquals(-0.1, m_output, 1e-5);
    m_controller.setEnabled(false);
  }

  @Test
  void integralGainOutputTest() {
    m_controller.setI(4);

    m_controller.setSetpoint(0);
    m_measurement = 0.025;

    m_controller.setEnabled(true);
    m_controller.m_controlLoop.cancel();

    for (int i = 0; i < 5; i++) {
      m_controller.calculate();
    }

    // Larger delta due to a time issue with unit tests.
    assertEquals(-0.5 * m_controller.getPeriod(), m_output, 0.002);
    m_controller.setEnabled(false);
  }

  @Test
  void derivativeGainOutputTest() {
    m_controller.setD(4);

    m_controller.setEnabled(true);
    m_controller.m_controlLoop.cancel();

    m_controller.setSetpoint(0);
    m_measurement = 0;

    m_controller.calculate();

    m_controller.setSetpoint(0);
    m_measurement = 0.0025;

    m_controller.calculate();

    assertEquals(-0.01 / m_controller.getPeriod(), m_output, 1e-5);
    m_controller.setEnabled(false);
  }

  @Test
  void outputClampTest() {
    m_controller.setPID(1, 0, 0);

    m_controller.setOutputRange(-12, 12);

    m_controller.setEnabled(true);
    m_controller.m_controlLoop.cancel();

    m_controller.setSetpoint(20);
    m_measurement = 1;

    m_controller.calculate();

    assertEquals(12, m_output, 1e-5);
    m_controller.setEnabled(false);
  }
}
