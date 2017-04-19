package com._604robotics.robotnik;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public abstract class Robot extends SampleRobot {
    public static double DEFAULT_REPORT_INTERVAL = 5;
    
    private static final Logger logger = new Logger(Robot.class);
    
    private final ITable table = NetworkTable.getTable("robotnik");

    private final List<Module> modules = new ArrayList<>();
    private final IterationTimer iterationTimer;

    private final List<Controller> systems = new ArrayList<>();

    private Controller autonomousMode;
    private Controller teleopMode;
    private Controller testMode;

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

    protected void addSystem (Controller system) {
        systems.add(system);
    }
	
	protected void setAutonomousMode (Controller autonomousMode) {
	    this.autonomousMode = autonomousMode;
	}
	
	protected void setTeleopMode (Controller teleopMode) {
	    this.teleopMode = teleopMode;
	}
	
	protected void setTestMode (Controller testMode) {
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
    
    private void loop (String name, Controller mode, Supplier<Boolean> active) {
        logger.info(name + " mode begin");

        for (Module module : modules) {
            Reliability.swallowThrowables(module::begin, "Error in begin() of module " + module.getName());
        }

        for (Controller system : systems) {
            Reliability.swallowThrowables(system::begin, "Error in begin() of system");
        }

        if (mode != null) {
            Reliability.swallowThrowables(mode::begin, "Error in begin() of " + name + " mode");
            iterationTimer.start();
        }

        while (active.get()) {
            for (Module module : modules) {
                module.prepare();
            }

            for (Controller system : systems) {
                Reliability.swallowThrowables(system::run, "Error in run() of system");
            }

            if (mode != null) {
                Reliability.swallowThrowables(mode::run, "Error in run() of " + name + " mode");
            }

            for (Module module : modules) {
                module.update();
                Reliability.swallowThrowables(module::run, "Error in run() of module " + module.getName());

                if (mode != null) {
                    module.execute();
                }
            }

            if (mode != null) {
                iterationTimer.sample(
                        loopTime -> logger.info("Loop time: " + loopTime * 1000 + " ms"));
            }
        }

        if (mode != null) {
            iterationTimer.stop();
            Reliability.swallowThrowables(mode::end, "Error in end() of " + name + " mode");
        }

        for (Controller system : systems) {
            Reliability.swallowThrowables(system::end, "Error in end() of system");
        }

        for (Module module : modules) {
            Reliability.swallowThrowables(module::end, "Error in end() of module " + module.getName());

            if (mode != null) {
                module.terminate();
            }
        }
        
        logger.info(name + " mode end");
    }

    private void printBanner () {
        System.out.println(Base64.getDecoder().decode("DQoNCg0KICAgICAgICAgICAgICAgICAgICAgICAgICAgYDo6YDotICAgICAgICAgICAgICA6LS06LWANCiAgICAgICAgICAgICAgICAgICAgICAgYDo6Oi0gICAgLi8uICAgICAgICAgIDovYCAgIGAtOjotYA0KICAgICAgICAgICAgICAgICAgICAgLzotICAgICAgICAgIDotLS0uLi4tLS0vLiAgICAgICAgIGAtOjoNCiAgICAgICAgICAgICAgICAgICAgICsgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGAvDQogICAgICBgLSAgICAgICAgICAgICA6LiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA6YCAgICAgICAgICAgIGAtDQogICAgICBgbzo6OmAgICAgICAgICAuLyAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICArICAgICAgICAgIC4vOjpvDQogICAgICAgLTogYDo6Oi4gICAgIC8vICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAuLy0gICAgIC06Oi0gICtgDQogICAgICAgIC8uICAgIC06Oi0tK2AgICAgICAgYDotLS0tOmAgICAgICAgIGA6LS0tLTpgICAgICAgIC0rYDo6Oi4gICAgLToNCiAgICAgICAgIG8gICAgICAgYC0gICAgICAgIC86ICAgICBgOi8gICAgIGArLSAgICAgYC86ICAgICAgIGA6YCAgICAgIGArDQogICAgYC4tLTovLiAgICAgICAgICAgICAgIC9gICAgICAgICAtOiAgICArYCAgICAgICAgOi0gICAgICAgICAgICAgICA6LS0tLS4NCmBvOi0uYCAgICAgICAgICAgICAgICAgICAgLyAgICAgICAgIGAvICAgICsgICAgICAgICAuOiAgICAgICAgICAgICAgICAgICAgYC0tOisNCiBgOi8uICAgICAgICAgICAgICAgICAgICAgYCtgICAgICAgLitgICAgIC4rYCAgICAgIC4rICAgICAgICAgICAgICAgICAgICAgIC0vOmANCiAgICAtLzpgICAgICAgICAgICAgICAgICAgICA6Oi0tLS06OiAgICAgICAgOjotLS06Oi0gICAgICAgICAgICAgICAgICAgIC4vLy4NCiAgICAgIGBzLiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgOm8NCiAgICAgLStgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIC4rLg0KICAgYC8tICAgICAgICAgICAgIC4tLS4gLi0tLS0tLyA6eWAgICAgICAgICAgLnkgICstLi0tOmAgLS0tYCAgICAgICAgICAgICA6L2ANCiAgK28tOi0tLS0uLS4tLyAgIGBvYCAvIDpgICAgIC8gOjorYCAgICAgICAgLSsvICAvICAgIDpgIC8gLW8gICAtOi0uLS4tLS0tLS1vOg0KICBgYCAgICAgICAgICA6LiAgICArLS8gOmAgICAgLyA6OiAvL2AgICAgLi86IC8gIC8gICAgOmAgLzovICAgICsgICAgICAgICAgIGBgDQogICAgICAgICAgICAgICBvICAgICA6KyA6YCAgICAvIDo6ICAgLS8gYCstICAgLyAgLyAgICA6YCBvLiAgICBgLw0KICAgICAgICAgICAgICAgLTogICAgICAgOi4gICAgLyA6OiAgICAvIGA6ICAgIC8gIC8gICAgOmAgICAgICAgbw0KICAgICAgICAgICAgICAgIC8tICAgICAgYDo6OmAgLyA6OiAgICAvIGA6ICAgIC8gIC8gYDovOiAgICAgICArLg0KICAgICAgICAgICAgICAgICA6OiAgICAgICAgYDo6LyA6OiAgICAvIGA6ICAgIC8gIC86LSAgICAgICAgYG8uDQogICAgICAgICAgICAgICAgICAtK2AgICAgICAgICAgIGAtLS0tLSsgYC8tLS0tLSAgICAgICAgICAgIC4rYA0KICAgICAgICAgICAgICAgICAgIGAvOiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYCs6DQogICAgICAgICAgICAgICAgICAgICAuLzpgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLi86DQogICAgICAgICAgICAgICAgICAgICAgIGA6Oi0gICAgICAgICAgIC0tLi0gICAgICAgICAgYDovLQ0KICAgICAgICAgICAgICAgICAgICAgICAgICBgOjo6LWAgICAgIC06ICArYCAgICAgLjo6Oi0NCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGAtLS0tLSA6ICAgLi5gLS0tLS4NCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAuLScnLS4NCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLicgLi0uICApDQogICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLyAuJyAgLyAvDQogICAgICAgICAgICAgICAgICAgICAgICAgICBfICAgICAgICAgICBfICAgICAgICAgXyBfICAoXy8gICAvIC8NCiAgICAgICAgICAgICAgICAgXyBfXyBfX18gfCB8X18gICBfX18gfCB8XyBfIF9fIChfKSB8IF9fICAgIC8gLw0KICAgICAgICAgICAgICAgIHwgJ19fLyBfIFx8ICdfIFwgLyBfIFx8IF9ffCAnXyBcfCB8IHwvIC8gICAvIC8NCiAgICAgICAgICAgICAgICB8IHwgfCAoXykgfCB8XykgfCAoXykgfCB8X3wgfCB8IHwgfCAgIDwgICAuICcNCiAgICAgICAgICAgICAgICB8X3wgIFxfX18vfF8uX18vIFxfX18vIFxfX3xffCB8X3xffF98XF9cIC8gLyAgICBfLi0nKQ0KICAgICAgICAgICAgICAgIGZyYW1ld29yayAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIC4nICcgIF8uJy4tJycNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIC8gIC8uLSdfLicNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgLyAgICBfLicNCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgKCBfLi0nDQoNCg=="));
    }
}