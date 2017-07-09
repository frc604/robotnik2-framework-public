package com._604robotics.robotnik;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import com._604robotics.robotnik.prefabs.utils.Pair;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Robot extends SampleRobot {
    public static double DEFAULT_REPORT_INTERVAL = 5;

    private static final Logger logger = new Logger(Robot.class);

    private final ITable table = NetworkTable.getTable("robotnik");

    private final List<Module> modules = new ArrayList<>();
    private final IterationTimer iterationTimer;

    private final List<Pair<String, Coordinator>> systems = new ArrayList<>();

    private Coordinator autonomousMode;
    private Coordinator teleopMode;
    private Coordinator testMode;

    public Robot () {
        this(DEFAULT_REPORT_INTERVAL);
    }

    public Robot (double reportInterval) {
        iterationTimer = new IterationTimer(reportInterval);
        updateModuleList();
    }

    protected <T extends Module> T addModule (T module) {
        modules.add(module);
        updateModuleList();
        return module;
    }

    private void updateModuleList () {
        table.putString("moduleList", modules.stream()
                .map(Module::getName)
                .collect(Collectors.joining(",")));
    }

    protected void addSystem (String name, Coordinator system) {
        systems.add(new Pair<>(name, system));
    }

    protected void addSystem (Class klass, Coordinator system) {
        addSystem(klass.getSimpleName(), system);
    }

    protected void setAutonomousMode (Coordinator autonomousMode) {
        this.autonomousMode = autonomousMode;
    }

    protected void setTeleopMode (Coordinator teleopMode) {
        this.teleopMode = teleopMode;
    }

    protected void setTestMode (Coordinator testMode) {
        this.testMode = testMode;
    }

    @Override
    protected void robotInit () {
        printBanner();
    }

    @Override
    public void autonomous () {
        loop("Autonomous", autonomousMode, () -> isEnabled() && isAutonomous());
    }

    @Override
    public void operatorControl () {
        loop("Teleop", teleopMode, () -> isEnabled() && isOperatorControl());
    }

    @Override
    public void test () {
        loop("Test", testMode, () -> isEnabled() && isTest());
    }

    @Override
    protected void disabled () {
        loop("Disabled", null, this::isDisabled);
    }

    private void loop (String name, Coordinator mode, Supplier<Boolean> active) {
        logger.info(name + " mode begin");

        for (Module module : modules) {
            Reliability.swallowThrowables(module::begin, "Error in begin() of module " + module.getName());
        }

        if (mode != null) {
            Reliability.swallowThrowables(mode::start, "Error starting " + name + " mode");
            iterationTimer.start();
        }

        for (Pair<String, Coordinator> system : systems) {
            Reliability.swallowThrowables(system.getValue()::start, "Error starting system " + system.getKey());
        }

        while (active.get()) {
            for (Module module : modules) {
                module.prepare();
            }

            if (mode != null) {
                Reliability.swallowThrowables(mode::execute, "Error executing " + name + " mode");
            }

            for (Pair<String, Coordinator> system : systems) {
                Reliability.swallowThrowables(system.getValue()::execute, "Error executing system " + system.getKey());
            }

            for (Module module : modules) {
                module.update();

                if (mode != null) {
                    module.execute();
                }

                Reliability.swallowThrowables(module::run, "Error in run() of module " + module.getName());
            }

            if (mode != null) {
                iterationTimer.sample(
                        loopTime -> logger.info("Loop time: " + loopTime * 1000 + " ms"));
            }
        }

        if (mode != null) {
            iterationTimer.stop();
            Reliability.swallowThrowables(mode::stop, "Error stopping " + name + " mode");
        }

        for (Pair<String, Coordinator> system : systems) {
            Reliability.swallowThrowables(system.getValue()::stop, "Error stopping system " + system.getKey());
        }

        for (Module module : modules) {
            if (mode != null) {
                module.terminate();
            }

            Reliability.swallowThrowables(module::end, "Error in end() of module " + module.getName());
        }

        logger.info(name + " mode end");
    }

    private void printBanner () {
        byte[] logodata = Base64.getDecoder().decode("DQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgYDo6YDotICAgICAgICAgICAgICA6LS06LWANCiAgICAgICAgICAgICAgICAgICAgICAgYDo6Oi0gICAgLi8uICAgICAgICAgIDovYCAgIGAtOjotYA0KICAgICAgICAgICAgICAgICAgICAgLzotICAgICAgICAgIDotLS0uLi4tLS0vLiAgICAgICAgIGAtOjoNCiAgICAgICAgICAgICAgICAgICAgICsgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGAvDQogICAgICBgLSAgICAgICAgICAgICA6LiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA6YCAgICAgICAgICAgIGAtDQogICAgICBgbzo6OmAgICAgICAgICAuLyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICArICAgICAgICAgIC4vOjpvDQogICAgICAgLTogYDo6Oi4gICAgIC8vICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAuLy0gICAgIC06Oi0gICtgDQogICAgICAgIC8uICAgIC06Oi0tK2AgICAgICAgYDotLS0tOmAgICAgICAgIGA6LS0tLTpgICAgICAgIC0rYDo6Oi4gICAgLToNCiAgICAgICAgIG8gICAgICAgYC0gICAgICAgIC86ICAgICBgOi8gICAgIGArLSAgICAgYC86ICAgICAgIGA6YCAgICAgIGArDQogICAgYC4tLTovLiAgICAgICAgICAgICAgIC9gICAgICAgICAtOiAgICArYCAgICAgICAgOi0gICAgICAgICAgICAgICA6LS0tLS4NCmBvOi0uYCAgICAgICAgICAgICAgICAgICAgLyAgICAgICAgIGAvICAgICsgICAgICAgICAuOiAgICAgICAgICAgICAgICAgICAgYC0tOisNCiBgOi8uICAgICAgICAgICAgICAgICAgICAgYCtgICAgICAgLitgICAgIC4rYCAgICAgIC4rICAgICAgICAgICAgICAgICAgICAgIC0vOmANCiAgICAtLzpgICAgICAgICAgICAgICAgICAgICA6Oi0tLS06OiAgICAgICAgOjotLS06Oi0gICAgICAgICAgICAgICAgICAgIC4vLy4NCiAgICAgIGBzLiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgOm8NCiAgICAgLStgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIC4rLg0KICAgYC8tICAgICAgICAgICAgIC4tLS4gLi0tLS0tLyA6eWAgICAgICAgICAgLnkgICstLi0tOmAgLS0tYCAgICAgICAgICAgICA6L2ANCiAgK28tOi0tLS0uLS4tLyAgIGBvYCAvIDpgICAgIC8gOjorYCAgICAgICAgLSsvICAvICAgIDpgIC8gLW8gICAtOi0uLS4tLS0tLS1vOg0KICBgYCAgICAgICAgICA6LiAgICArLS8gOmAgICAgLyA6OiAvL2AgICAgLi86IC8gIC8gICAgOmAgLzovICAgICsgICAgICAgICAgIGBgDQogICAgICAgICAgICAgICBvICAgICA6KyA6YCAgICAvIDo6ICAgLS8gYCstICAgLyAgLyAgICA6YCBvLiAgICBgLw0KICAgICAgICAgICAgICAgLTogICAgICAgOi4gICAgLyA6OiAgICAvIGA6ICAgIC8gIC8gICAgOmAgICAgICAgbw0KICAgICAgICAgICAgICAgIC8tICAgICAgYDo6OmAgLyA6OiAgICAvIGA6ICAgIC8gIC8gYDovOiAgICAgICArLg0KICAgICAgICAgICAgICAgICA6OiAgICAgICAgYDo6LyA6OiAgICAvIGA6ICAgIC8gIC86LSAgICAgICAgYG8uDQogICAgICAgICAgICAgICAgICAtK2AgICAgICAgICAgIGAtLS0tLSsgYC8tLS0tLSAgICAgICAgICAgIC4rYA0KICAgICAgICAgICAgICAgICAgIGAvOiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYCs6DQogICAgICAgICAgICAgICAgICAgICAuLzpgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLi86DQogICAgICAgICAgICAgICAgICAgICAgIGA6Oi0gICAgICAgICAgIC0tLS0gICAgICAgICAgYDovLQ0KICAgICAgICAgICAgICAgICAgICAgICAgICBgOjo6LWAgICAgIC06ICA6LSAgICAgLjo6Oi0NCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGAtLS0tLSA6ICAgIDogLS0tLS4NCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLi0nJy0uDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAuJyAuLS4gICkNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLyAuJyAgLyAvDQogICAgICAgICAgICAgICAgICAgICAgICAgICBfICAgICAgICAgICBfICAgICAgICAgXyBfICAgKF8vICAgLyAvDQogICAgICAgICAgICAgICAgIF8gX18gX19fIHwgfF9fICAgX19fIHwgfF8gXyBfXyAoXykgfCBfXyAgICAvIC8NCiAgICAgICAgICAgICAgICB8ICdfXy8gXyBcfCAnXyBcIC8gXyBcfCBfX3wgJ18gXHwgfCB8LyAvICAgLyAvDQogICAgICAgICAgICAgICAgfCB8IHwgKF8pIHwgfF8pIHwgKF8pIHwgfF98IHwgfCB8IHwgICA8ICAgLiAnDQogICAgICAgICAgICAgICAgfF98ICBcX19fL3xfLl9fLyBcX19fLyBcX198X3wgfF98X3xffFxfXCAvIC8gICAgXy4tJykNCiAgICAgICAgICAgICAgICBmcmFtZXdvcmsgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAuJyAnICBfLicuLScnDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAvICAvLi0nXy4nDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIC8gICAgXy4nDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICggXy4tJw0KDQoNCg==");
        System.out.println(new String(logodata));
    }
}