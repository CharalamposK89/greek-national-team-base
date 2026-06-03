package org.firstinspires.ftc.teamcode.AngelosBase.Tuners;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

// This TEST FILE: tunes the feedforward values for the drivebase motors
// (++ FTCDashboard Config Variables)

@Disabled // TEST FILE: It is Disabled the OpMode so it doesnt show up in the driver station
@Config
@TeleOp(name="FeedForwardTuner", group="Tuners")
public class FeedForwardTuner extends LinearOpMode {
    private DcMotorEx leftMotor, rightMotor;
    public static double KSL = 0.0, KSR=0.0, KVL = 1.0, KVR = 1.0, power=0;
    private Telemetry dash_tele;

    @Override
    public void runOpMode() {
        leftMotor = hardwareMap.get(DcMotorEx.class, "left_drive");
        rightMotor = hardwareMap.get(DcMotorEx.class, "right_drive");

        leftMotor.setDirection(DcMotorEx.Direction.REVERSE);
        leftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        dash_tele = FtcDashboard.getInstance().getTelemetry();

        waitForStart();

        while (opModeIsActive()) {
            power = Range.clip(power, 0, 1);
            leftMotor.setPower(KSL * Math.signum(power+0.00000000000000000000001) + KVL * power);
            rightMotor.setPower(KSR * Math.signum(power+0.00000000000000000000001) + KVR * power);

            dash_tele.addData("Target Vel: ", power*(28*500));
            dash_tele.addData("Left Actual Vel: ", leftMotor.getVelocity());
            dash_tele.addData("Right Actual Vel: ", rightMotor.getVelocity());
            dash_tele.update();
        }
    }
}
