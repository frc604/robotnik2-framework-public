package com._604robotics.robotnik.prefabs.motion;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;

public class TrapezoidalMotionProfileTest {
  private TrapezoidalMotionProfile profile;

  @BeforeEach
  void setUp() {
    this.profile = new TrapezoidalMotionProfile();
  }

  @Test
  void graph() {
    var setupStart = System.nanoTime();
    MotionConstraints constraints = new MotionConstraints(15.0, 50);
    this.profile.setParams(new MotionState(10.0, 5.0), new MotionState(24.0, 5.0), constraints);
    System.out.println(System.nanoTime() - setupStart);

    ArrayList<Double> xData = new ArrayList<Double>();
    ArrayList<Double> yData = new ArrayList<Double>();
    ArrayList<Double> yPrimeData = new ArrayList<Double>();
    ArrayList<Double> yDoublePrimeData = new ArrayList<Double>();

    ArrayList<Long> times = new ArrayList<Long>();

    var startLONG = System.nanoTime();
    for (double i = 0; i <= this.profile.getTotalTime(); i = i + 0.005) {
      xData.add(i);
      var start = System.nanoTime();
      MotionState state = this.profile.sample(i);
      times.add(System.nanoTime() - start);

      yData.add(state.velocity);
      yPrimeData.add(state.position);

      var factor = this.profile.getInverted() ? -1.0 : 1.0;

      if (i <= this.profile.getAccelPeriod()) {
        yDoublePrimeData.add(constraints.maxAcceleration * factor);
      } else if (this.profile.getAccelPeriod() < i
          && i <= (this.profile.getAccelPeriod() + this.profile.getCruisePeriod())) {
        yDoublePrimeData.add(0.0);
      } else if ((this.profile.getAccelPeriod() + this.profile.getCruisePeriod()) < i
          && i <= (this.profile.getTotalTime())) {
        yDoublePrimeData.add(-constraints.maxAcceleration * factor);
      }
    }
    System.out.println(System.nanoTime() - startLONG);

    long sum = 0;
    for (Long d : times) sum += d;

    System.out.println("AVERAGE" + sum / times.size());

    XYChart chart =
        new XYChartBuilder()
            .width(800)
            .height(600)
            .title(getClass().getSimpleName())
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

    // Customize Chart
    chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
    chart.getStyler().setAxisTitlesVisible(false);
    chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

    // Customize Chart
    chart.getStyler().setPlotBackgroundColor(new Color(40, 42, 54));
    chart.getStyler().setPlotGridLinesColor(new Color(68, 71, 90));
    chart.getStyler().setChartBackgroundColor(new Color(46, 49, 62));
    chart.getStyler().setPlotBorderColor(new Color(68, 71, 90));
    chart.getStyler().setLegendBackgroundColor(new Color(68, 71, 90));
    chart.getStyler().setAxisTickLabelsColor(new Color(248, 248, 242));
    chart.getStyler().setAxisTickMarksColor(new Color(68, 71, 90));
    chart.getStyler().setChartFontColor(new Color(248, 248, 242));
    chart.getStyler().setChartTitleBoxVisible(false);
    chart.getStyler().setPlotGridLinesVisible(true);

    // Series
    var series1 = chart.addSeries("velocity", xData, yData);
    series1.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
    series1.setLineColor(new Color(139, 233, 253));
    series1.setMarkerColor(new Color(139, 233, 253));
    series1.setFillColor(new Color(139, 233, 253, 100));

    var series2 = chart.addSeries("position", xData, yPrimeData);
    series2.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
    series2.setLineColor(new Color(255, 184, 108));
    series2.setMarkerColor(new Color(255, 184, 108));

    var series3 = chart.addSeries("acceleration", xData, yDoublePrimeData);
    series3.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
    series3.setLineColor(new Color(80, 250, 123));
    series3.setMarkerColor(new Color(80, 250, 123));

    // new SwingWrapper(chart).displayChart();

    // try {
    //   Thread.sleep(2000000);
    // } catch (InterruptedException e) {
    //   e.printStackTrace();
    // }
  }

  void testSegmentTimes(double total, double accel, double cruise, double decel) {
    assertAll(
        "All segment times are correct",
        () -> assertEquals(this.profile.getTotalTime(), total, 0.001),
        () -> assertEquals(this.profile.getAccelPeriod(), accel, 0.001),
        () -> assertEquals(this.profile.getCruisePeriod(), cruise, 0.001),
        () -> assertEquals(this.profile.getDeccelPeriod(), decel, 0.001));
  }

  void testCrucialPoints(MotionState a, MotionState b, MotionState c, MotionState d) {
    double t1 = this.profile.getAccelPeriod();
    double t2 = t1 + this.profile.getCruisePeriod();
    double t3 = t2 + this.profile.getDeccelPeriod();

    assertAll(
        "Crucial transition points are correct",
        () -> assertTrue(this.profile.sample(0).almostEqual(a, 0.001)),
        () -> assertTrue(this.profile.sample(t1).almostEqual(b, 0.001)),
        () -> assertTrue(this.profile.sample(t2).almostEqual(c, 0.001)),
        () -> assertTrue(this.profile.sample(t3).almostEqual(d, 0.001)));

    // Throws if time is less than zero
    assertThrows(RuntimeException.class, () -> this.profile.sample(-0.001));

    // Throws if time is greater than total time
    assertThrows(
        RuntimeException.class, () -> this.profile.sample(this.profile.getTotalTime() + 0.001));
  }

  void testConstraints(
      MotionState startState, MotionState endState, MotionConstraints constraints) {
    // Check that constraints are respected
    double dt = 0.02;
    MotionState lastState = new MotionState(startState.position, startState.velocity);
    MotionState state;
    for (double t = dt; t < this.profile.getTotalTime(); t += dt) {
      state = this.profile.sample(t);

      double velocity = Math.abs(state.position - lastState.position) / dt;
      double acceleration = Math.abs(state.velocity - lastState.velocity) / dt;
      assertTrue(velocity - constraints.maxVelocity < 0.001);
      assertTrue(acceleration - constraints.maxAcceleration < 0.001);

      lastState.position = state.position;
      lastState.velocity = state.velocity;
    }
  }

  @Test
  void uninitialized() {
    assertAll(
        "An uninitialized motion profile throws a RuntimeError on all public methods",
        () -> assertThrows(RuntimeException.class, () -> this.profile.sample(0)),
        () -> assertThrows(RuntimeException.class, () -> this.profile.getTotalTime()),
        () -> assertThrows(RuntimeException.class, () -> this.profile.getAccelPeriod()),
        () -> assertThrows(RuntimeException.class, () -> this.profile.getCruisePeriod()),
        () -> assertThrows(RuntimeException.class, () -> this.profile.getDeccelPeriod()),
        () -> assertThrows(RuntimeException.class, () -> this.profile.getInverted()));
  }

  @Test
  void unachievableTrajectorys() {
    // Impossible to achieve trajectory with given constraints
    // Max acceleration too low
    assertThrows(
        RuntimeException.class,
        () ->
            this.profile.setParams(
                new MotionState(0.0, 0.0),
                new MotionState(1.0, 10.0),
                new MotionConstraints(100.0, 1.0)));

    // Max acceleration too low inverted
    assertThrows(
        RuntimeException.class,
        () ->
            this.profile.setParams(
                new MotionState(0.0, 0.0),
                new MotionState(-1.0, -10.0),
                new MotionConstraints(100.0, 1.0)));

    // Max velocity too low
    assertThrows(
        RuntimeException.class,
        () ->
            this.profile.setParams(
                new MotionState(0.0, 0.0),
                new MotionState(1.0, 10.0),
                new MotionConstraints(1.0, 100.0)));

    // Max velocity too low inverted
    assertThrows(
        RuntimeException.class,
        () ->
            this.profile.setParams(
                new MotionState(0.0, 0.0),
                new MotionState(-1.0, -10.0),
                new MotionConstraints(1.0, 100.0)));
  }

  @Test
  void standardTrapezoid() {
    // 0 start and stop velocities, full trapezoid
    MotionState startState = new MotionState(0.0, 0.0);
    MotionState endState = new MotionState(10.0, 0.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 1.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(6.333, 3.0, 0.333, 3.0);
    this.testCrucialPoints(
        startState, new MotionState(4.5, 3.0), new MotionState(5.5, 3.0), endState);
    this.testConstraints(startState, endState, constraints);
    assertFalse(this.profile.getInverted());
  }

  @Test
  void standardTrapezoidInverted() {
    // 0 start and stop velocities, full inverted trapezoid
    MotionState startState = new MotionState(0.0, 0.0);
    MotionState endState = new MotionState(-10.0, 0.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 1.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(6.333, 3.0, 0.333, 3.0);
    this.testCrucialPoints(
        startState, new MotionState(-4.5, -3.0), new MotionState(-5.5, -3.0), endState);
    this.testConstraints(startState, endState, constraints);
    assertTrue(this.profile.getInverted());
  }

  @Test
  void standardTrangle() {
    // 0 start and stop velocities, triangular profile
    MotionState startState = new MotionState(0.0, 0.0);
    MotionState endState = new MotionState(5.0, 0.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 1.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(4.472, 2.236, 0.0, 2.236);
    this.testCrucialPoints(
        startState, new MotionState(2.5, 2.236), new MotionState(2.5, 2.236), endState);
    this.testConstraints(startState, endState, constraints);
    assertFalse(this.profile.getInverted());
  }

  @Test
  void standardTrangleInverted() {
    // 0 start and stop velocities, inverted triangular profile
    MotionState startState = new MotionState(0.0, 0.0);
    MotionState endState = new MotionState(-5.0, 0.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 1.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(4.472, 2.236, 0.0, 2.236);
    this.testCrucialPoints(
        startState, new MotionState(-2.5, -2.236), new MotionState(-2.5, -2.236), endState);
    this.testConstraints(startState, endState, constraints);
    assertTrue(this.profile.getInverted());
  }

  @Test
  void nonZeroStartEndTrapezoidA() {
    // velocity/acceleration: +/+ -> +/+
    MotionState startState = new MotionState(5.0, 2.0);
    MotionState endState = new MotionState(10.0, 2.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 3.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(1.777, 0.333, 1.111, 0.333);
    this.testCrucialPoints(
        startState, new MotionState(5.833, 3.0), new MotionState(9.166, 3.0), endState);
    this.testConstraints(startState, endState, constraints);
    assertFalse(this.profile.getInverted());
  }

  @Test
  void nonZeroStartEndTrapezoidB() {
    // velocity/acceleration: +/+ -> +/-
    MotionState startState = new MotionState(5.0, 2.0);
    MotionState endState = new MotionState(10.0, -2.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 3.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(3.111, 0.333, 1.111, 1.666);
    this.testCrucialPoints(
        startState, new MotionState(5.833, 3.0), new MotionState(9.166, 3.0), endState);
    this.testConstraints(startState, endState, constraints);
    assertFalse(this.profile.getInverted());
  }

  @Test
  void nonZeroStartEndTrapezoidC() {
    // velocity/acceleration: +/+ -> -/+
    MotionState startState = new MotionState(5.0, 2.0);
    MotionState endState = new MotionState(-10.0, 2.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 3.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(7.777, 1.666, 4.444, 1.666);
    this.testCrucialPoints(
        startState, new MotionState(4.166, -3.0), new MotionState(-9.166, -3.0), endState);
    this.testConstraints(startState, endState, constraints);
    assertTrue(this.profile.getInverted());
  }

  @Test
  void nonZeroStartEndTrapezoidD() {
    // velocity/acceleration: +/+ -> -/-
    MotionState startState = new MotionState(5.0, 2.0);
    MotionState endState = new MotionState(-10.0, -2.0);
    MotionConstraints constraints = new MotionConstraints(3.0, 3.0);
    this.profile.setParams(startState, endState, constraints);

    this.testSegmentTimes(6.444, 1.666, 4.444, 0.333);
    this.testCrucialPoints(
        startState, new MotionState(4.166, -3.0), new MotionState(-9.166, -3.0), endState);
    this.testConstraints(startState, endState, constraints);
    assertTrue(this.profile.getInverted());
  }
}
