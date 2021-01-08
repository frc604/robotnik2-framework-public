/*----------------------------------------------------------------------------*/
/* Copyright (c) 2016-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
/* This is 604's custom version to test the re-written multithreaded          */
/* controllers                                                                */
/*----------------------------------------------------------------------------*/

package com._604robotics.robotnik.prefabs.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExtendablePIDControllerToleranceTest {
  private static final double kSetpoint = 50.0;
  private static final double kTolerance = 10.0;
  private static final double kRange = 200;

  private ExtendablePIDController m_controller;

  private double m_measurement = 0.0;

  @SuppressWarnings("unused")
  private double m_output = 0.0;

  @BeforeEach
  void setUp() {
    m_controller =
        new ExtendablePIDController(0, 0, 0, () -> m_measurement, (output) -> m_output = output);
  }

  @Test
  void initialToleranceTest() {
    m_controller.setPID(0.05, 0.0, 0.0);
    m_controller.enableContinuousInput(-kRange / 2, kRange / 2);

    assertTrue(m_controller.atSetpoint());
  }

  @Test
  void absoluteToleranceTest() {
    m_controller.setPID(0.05, 0.0, 0.0);
    m_controller.enableContinuousInput(-kRange / 2, kRange / 2);

    m_controller.setTolerance(kTolerance);
    m_controller.setSetpoint(kSetpoint);

    m_measurement = 0.025;
    m_controller.setEnabled(true);
    m_controller.m_controlLoop.cancel();

    m_controller.calculate();

    assertFalse(
        m_controller.atSetpoint(),
        "Error was in tolerance when it should not have been. Error was "
            + m_controller.getPositionError());

    m_controller.setEnabled(false);

    m_measurement = kSetpoint + kTolerance / 2;
    m_controller.setEnabled(true);

    m_controller.calculate();

    assertTrue(
        m_controller.atSetpoint(),
        "Error was not in tolerance when it should have been. Error was "
            + m_controller.getPositionError());

    m_controller.setEnabled(false);

    m_measurement = kSetpoint + 10 * kTolerance;
    m_controller.setEnabled(true);

    m_controller.calculate();

    assertFalse(
        m_controller.atSetpoint(),
        "Error was in tolerance when it should not have been. Error was "
            + m_controller.getPositionError());
    m_controller.setEnabled(false);
  }
}
