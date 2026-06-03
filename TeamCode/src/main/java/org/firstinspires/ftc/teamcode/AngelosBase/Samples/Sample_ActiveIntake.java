package org.firstinspires.ftc.teamcode.AngelosBase.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AngelosBase.Subsystems.ActiveIntake;
import org.firstinspires.ftc.teamcode.AngelosBase.Util.GamepadEx;

//@Disabled // Remove this to see it on the Driver Station
@TeleOp(name="Sample_ActiveIntake", group="Samples")
public class Sample_ActiveIntake extends LinearOpMode {
    private GamepadEx controller;
    private ActiveIntake intake;

    @Override
    public void runOpMode() {
        // Initialize Gamepad Wrapper
        controller = new GamepadEx(gamepad1);

        // Initialize Intake
        // Mapping:
        // A -> Forward (Intake)
        // B -> Stop
        // X -> Reverse (Outtake)
        intake = new ActiveIntake(
                hardwareMap,
                telemetry,
                () -> controller.justPressed(GamepadEx.Button.A),      // forwardButton supplier
                () -> controller.justPressed(GamepadEx.Button.B),      // stopButton supplier
                () -> controller.justPressed(GamepadEx.Button.Y)       // reverseButton supplier
        );

        telemetry.addLine("Intake Sample Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            controller.update(); // Refresh button states

            intake.update();     // Runs the state logic and sets motor power

            telemetry.update();  // Sends [Intake] State to the driver hub
        }
    }
}