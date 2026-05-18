package org.firstinspires.ftc.teamcode.AngelosBase.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@Disabled // Remove this line to show up in driver hub
@TeleOp(name="[NAME SHOWN ON ROBOT]", group="[PROGRAM CATEGORY (shown in driver hub)]")
public class Sample_Blank extends LinearOpMode {
    // Declare subsystems and utilities here

    @Override
    public void runOpMode() {
        // Initialize subsystems and utilities here

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            // Update subsystems and utilities here
        }
    }
}
