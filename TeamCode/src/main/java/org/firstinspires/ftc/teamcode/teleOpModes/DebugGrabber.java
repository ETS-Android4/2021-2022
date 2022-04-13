package org.firstinspires.ftc.teamcode.teleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class DebugGrabber extends LinearOpMode {

    // Servos for grabber
    public Servo leftGrabber;
    public Servo rightGrabber;
    // Create offset for servos so they can be reset correctly
    public float leftGOffset = 0.1f;
    public float rightGOffset = 0.1f;

    @Override
    public void runOpMode() {
        // Get servos
        leftGrabber = hardwareMap.get(Servo.class, "lGrab");
        rightGrabber = hardwareMap.get(Servo.class, "rGrab");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Initialize Servos
        rightGrabber.setDirection(Servo.Direction.REVERSE);
        leftGrabber.setPosition(leftGOffset);
        rightGrabber.setPosition(rightGOffset);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
            Debug();
    }

    public void Debug()
    {
        telemetry.addData("HOLD LEFT BUMPER AND MOVE LEFT/RIGHT JOYSTICKS TO CHANGE GRABBER OFFSETS", 0);
        // Change offsets from left and right joysticks
        if (gamepad1.left_bumper)
        {
            leftGOffset = gamepad1.left_stick_y;
            rightGOffset = gamepad1.right_stick_y;
        }

        // Control grabber with right trigger
        float grabberPower = gamepad1.right_trigger;
        leftGrabber.setPosition(grabberPower + leftGOffset);
        rightGrabber.setPosition(grabberPower + rightGOffset);
        telemetry.addData("Grabber Target", grabberPower);

        telemetry.addData("LEFT GRABBER:", 0);
        telemetry.addData("Grabber Position", leftGrabber.getPosition());
        telemetry.addData("Grabber Offset", leftGOffset);

        telemetry.addData("RIGHT GRABBER:", 0);
        telemetry.addData("Grabber Position", rightGrabber.getPosition());
        telemetry.addData("Grabber Offset", rightGOffset);
        telemetry.update();
    }
}
