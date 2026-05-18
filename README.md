# 🇬🇷 AngelosBase

This is a simple code library for the Greek National Robotics Team. It keeps the robot running fast and makes it easy to change buttons.

---

# 🤖 TankDrive Subsystem

Controls the wheels and driving of the robot. It includes speed adjustments and smooth motor physics.

## ⚙️ Settings (Variables at the top of TankDrive.java)
* **`TELEMETRY_ENABLED`** (`boolean` | Default: `true`): Shows drive information on the screen.
### Motor Configuration
* **`LEFT_MOTOR_NAME` / `RIGHT_MOTOR_NAME`** (`String`): The names of the motors matching driver hub configuration.
* **`LEFT_MOTOR_DIRECTION` / `RIGHT_MOTOR_DIRECTION`** (`Direction` | Default: `FORWARD`): Change to `REVERSE` if you want to reverse a motor.
* **`MOTOR_ZERO_POWER_BEHAVIOR`** (`ZeroPowerBehavior` | Default: `BRAKE`): Stops the robot instantly when you let go of the stick. Use `FLOAT` if you want it to glide to a stop.
> [!Note]
> * Usually one of the 2 motors of the Tank-Style Drivebase is reversed because it is in the mirror position on the robot.
> * In most cases `BRAKE` is better for drivetrains
### FeedForward
Feedforward control calculates the required motor power beforehand based on physics and target speeds, correcting for factors like friction before errors even happen.
* **`LEFT_FEEDFORWARD` / `RIGHT_FEEDFORWARD`** (`double[]` | Default: `{0.05, 1.0}`): Physics settings `{KS, KV}`. `KS` helps push through gear friction. `KV` controls straight-line speed precision.
  *  **`KS`** - Gives a little kick to surpass static friction in the beginning of the movement (higher values give a more a aggressive kickstart usual values are 0 - 0.12)
  * **`KV`** - Is the linear multiplier of the motor in order to match (left - right motor speeds usual values are around 0.95-1.05)
* **`KS_THETA`** (`double` | Default: `0.0`): Extra power kick like **`KS`** but in the turning movement

> [!Note]
Experiment with `KS` values while the wheel is off the ground. The correct value is the one right before motor moves. So lets say you play around with values like 0.03, 0.05. 0.06 and the wheel starts moving at 0.07. You should use a value of 0.06.

### Power Mapping
A nice feature is using the back trigger of the gamepad to map the top speed of the robot. The driver might sometimes need small speeds for accuracy or high speeds for time efficiency. While holding the trigger on the gamepad the max speed changes linearly from `DEFAULT_POWER` to `MAX_POWER`.
* **`DEFAULT_POWER`** (`double` | Default: `0.6`): Regular driving speed.
* **`MAX_POWER`** (`double` | Default: `1.0`): Max driving speed (Turbo mode) when holding the trigger down completely.

>[!NOTE]
If you don't prefer using the mapping set both `DEFAULT_POWER` and `MAX_POWER` to 1.0
TeamCode\src\main\java\org\firstinspires\ftc\teamcode\AngelosBase\Samples
## 🚀 Sample Implementation ([Sample_TankDrive](TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AngelosBase/Samples/Samples/Sample_TankDrive.java))
```java
package org.firstinspires.ftc.teamcode.AngelosBase.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AngelosBase.Subsystems.TankDrive;
import org.firstinspires.ftc.teamcode.AngelosBase.Util.GamepadEx;

@Disabled // Remove this to see it on the Driver Station
@TeleOp(name="Sample_TankDrive", group="Samples")
public class Sample_TankDrive extends LinearOpMode {
    private GamepadEx controller;
    private TankDrive drivetrain;

    @Override
    public void runOpMode() {
        // 1. Initialize Gamepad Wrapper
        controller = new GamepadEx(gamepad1);

        // 2. Initialize Drivetrain using the "Functional Philosophy"
        // We map the sticks and triggers directly here.
        drivetrain = new TankDrive(
                hardwareMap,
                telemetry,
                controller::getLeftStickY,  // Forward/Backward (Inverted for FTC)
                controller::getRightStickX,  // Turning
                controller::getRightTrigger  // Acceleration/Turbo mapping
        );

        telemetry.addLine("Drivetrain Sample Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            // 3. The Core Loop
            controller.update(); // Update joystick/button values

            drivetrain.update(); // Calculations: Feedforward + Power Mapping

            telemetry.update();  // Send data to Driver Station / Dashboard
        }
    }
}
```

# 🌀 Active Intake Subsystem

Active Intake uses a constant spinning motion to intake game elements. Uses automated states (`STOPPED`, `FORWARD`, `REVERSE`) so you only have to tap a button once instead of holding it down.

### ⚙️ Settings (Variables at the top of ActiveIntake.java)
* **`TELEMETRY_ENABLED`** (`boolean` | Default: `true`): Shows whether the intake is spinning forward, backward, or stopped.
* **`INTAKE_MOTOR_NAME`** (`String` | Default: `"intake"`): The name of the intake motor in your configuration list.
* **`FORWARD_VELOCITY`** (`double` | Default: `1.0`): Spinning power when pulling objects in.
* **`REVERSE_VELOCITY`** (`double` | Default: `-1.0`): Spinning power when pushing objects out (spitting out a jam).
* **`INTAKE_DIRECTION`** (`Direction` | Default: `FORWARD`): Flips the direction the axle spins.
* **`INTAKE_ZERO_POWER_BEHAVIOR`** (`ZeroPowerBehavior` | Default: `BRAKE`): Locks the motor immediately when turned off to keep elements from rolling out.

## 🚀 Sample Implementation ([Sample_ActiveIntake](TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AngelosBase/Samples/Samples/Sample_ActiveIntake.java))
```java
package org.firstinspires.ftc.teamcode.AngelosBase.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AngelosBase.Subsystems.ActiveIntake;
import org.firstinspires.ftc.teamcode.AngelosBase.Util.GamepadEx;

@Disabled // Remove this to see it on the Driver Station
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
```

# ⏱️ Timer Utility

A simple helper that counts how much time has passed. It helps you build timing sequences without freezing your entire robot program (never use `sleep()` inside active driver loops).

## 🛠️ Functions
* `resetTimer()`: Starts the clock from zero right now.
* `getCurTimeSecs()`: Tells you exactly how many seconds have passed since your last reset (e.g. `2.54` seconds).
* `getCurTime()`: Gives the elapsed time in raw milliseconds.

## 🚀 Sample Implementation ([Sample_Timer](TeamCode/src/main/java/org/firstinspires/ftc/teamcode/AngelosBase/Samples/Samples/Sample_Timer.java))
```java
package org.firstinspires.ftc.teamcode.AngelosBase.Samples;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.AngelosBase.Util.GamepadEx;
import org.firstinspires.ftc.teamcode.AngelosBase.Util.Timer;

@Disabled
@TeleOp(name="Sample_Timer", group="Samples")
public class Sample_Timer extends LinearOpMode {
    private GamepadEx controller;
    private Timer timer;

    @Override
    public void runOpMode() {
        // 1. Initialize Utilities
        controller = new GamepadEx(gamepad1);
        timer = new Timer();

        telemetry.addLine("Timer Sample Initialized");
        telemetry.addLine("Press A to Reset | Check Status at 5s");
        telemetry.update();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            // 2. Refresh Inputs
            controller.update();

            // 3. Timer Reset Logic
            // Using justPressed ensures the timer resets exactly once per click
            if (controller.justPressed(GamepadEx.Button.A)) {
                timer.resetTimer();
            }

            // 4. Time-Based Logic
            double elapsed = timer.getCurTimeSecs();
            boolean isExpired = elapsed > 5.0;

            // 5. Telemetry Feedback
            telemetry.addData("Status", isExpired ? "!!! 5 SECONDS UP !!!" : "Counting...");
            telemetry.addData("Elapsed Time", "%.2f seconds", elapsed);

            telemetry.update();
        }
    }
}