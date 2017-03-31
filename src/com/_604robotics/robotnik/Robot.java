package com._604robotics.robotnik;

import java.util.ArrayList;
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
    
    void begin () {
        for (Module module : modules) {
            Reliability.swallowThrowables(module::begin,
                    "Error in begin() of module " + module.getName());
        }
    }
    
    void update () {
        for (Module module : modules) {
            module.update();
        }
    }
    
    void reset () {
        for (Module module : modules) {
            module.reset();
        }
    }
    
    void execute () {
        for (Module module : modules) {
            module.execute();
        }
    }
    
    void terminate () {
        for (Module module : modules) {
            module.terminate();
        }
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

        if (mode == null) {
            while (active.get()) {
                update();
            }
        } else {
            begin();
            mode.begin();
            iterationTimer.start();

            while (active.get()) {
                update();
                reset();
                Reliability.swallowThrowables(mode::run, "Error in run() of " + name + " mode");
                execute();
                
                iterationTimer.sample(
                        loopTime -> logger.info("Loop time: " + loopTime*1000 + " ms"));
            }

            iterationTimer.stop();
            mode.end();
            terminate();
        }
        
        logger.info(name + " mode end");
    }

    private void printBanner () {
        System.out.println(
            "\n\n\n" +
            "                       ..            .`\n" +
            "                   .:oyhho.        .ohhy+:\n" +
            "                 +yhhhhhhhhoossssoohhhhhhhhy/\n" +
            "                 ohhhhhhhhhhhhhhhhhhhhhhhhhh+\n" +
            "      :/.        :hhhhhhhhhhhhhhhhhhhhhhhhhh-        ./-\n" +
            "       yhy+-    /hhhhhhhhhhhhhhhhhhhhhhhhhhhy:    -+yhs\n" +
            "       .hhhhho:shhhhhy/.``./yhhhhy/.``./yhhhhho:ohhhhy`\n" +
            "     `.-shhhhhhhhhhhh`      .hhhh`      .hhhhhhhhhhhho-.`\n" +
            "  /shhhhhhhhhhhhhhhhy       `hhhy       `hhhhhhhhhhhhhhhhys/\n" +
            "   ./yhhhhhhhhhhhhhhhs-    -shhhhs-    -shhhhhhhhhhhhhhhy/`\n" +
            "      :hhhhhhhhhhhhhhhhhyyhhhhhhhhhhyyhhhhhhhhhhhhhhhhh:\n" +
            "     /hhhhhhhhhhhhhhhhyyhyhhhhhhhhhhyhyyhhhhhhhhhhhhhhhy:\n" +
            "   -yhhhyysshhh+  ho`  `ho:hhhhhhhh-sh   `sh  +hhhosyyhhhs-\n" +
            " .  ``      shhy- h+   `ho .shhhhs. sh    oh -hhh+      ``.\n" +
            "            :hhhhoh+   `ho   `ys`   sh    ohohhhh.\n" +
            "             +hhhhhs`  `ho    ys    sh   .shhhhh:\n" +
            "              +hhhhhhs/:ho    ys    sh-/shhhhhh/\n" +
            "               :yhhhhhhhhhs+/:yy:/+shhhhhhhhhy-\n" +
            "                `+hhhhhhhhhhhhhhhhhhhhhhhhhy/\n" +
            "                  `:shhhhhhhhyyyhhhhhhhhho-\n" +
            "                     `:+syhhh:  /hhhyo/-\n" +
            "                          `.-`  `..\n" +
            "\n" +
            "                      _           _         _ _\n" +
            "                     | |         | |       (_) |\n" +
            "            _ __ ___ | |__   ___ | |_ _ __  _| | __\n" +
            "           | '__/ _ \\| '_ \\ / _ \\| __| '_ \\| | |/ /\n" +
            "           | | | (_) | |_) | (_) | |_| | | | |   <\n" +
            "           |_|  \\___/|_.__/ \\___/ \\__|_| |_|_|_|\\_\\\n" +
            "           framework\n" +
            "\n\n"
        );
    }
}