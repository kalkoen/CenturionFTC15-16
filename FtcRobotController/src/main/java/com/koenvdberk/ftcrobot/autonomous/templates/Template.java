package com.koenvdberk.ftcrobot.autonomous.templates;

import com.koenvdberk.ftcrobot.autonomous.AutonomousScheduler;
import com.koenvdberk.ftcrobot.hardware.RobotHardware;
import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Template {
    void addActions(AutonomousScheduler scheduler, HardwareMap hardwareMap);
}
