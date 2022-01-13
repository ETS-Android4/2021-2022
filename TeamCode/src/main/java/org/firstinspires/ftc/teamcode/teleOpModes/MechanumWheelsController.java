package org.firstinspires.ftc.teamcode.teleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class MechanumWheelsController extends LinearOpMode {

    // Different Motors
    public DcMotor topLeft;
    public DcMotor topRight;
    public DcMotor bottomLeft;
    public DcMotor bottomRight;

    double MovementTheta()
    {
        float leftJoystickX = gamepad1.left_stick_x;
        float leftJoystickY = gamepad1.left_stick_y;
        telemetry.addData("Move X", leftJoystickX);
        telemetry.addData("Move Y", leftJoystickY);

        float theta = 0;
        float tangent = leftJoystickY / leftJoystickX;
        float additionalAngle = (float) Math.abs(Math.toDegrees(Math.atan(tangent)));
        if(leftJoystickX <= 0 && leftJoystickY > 0)
        {
            theta = 90;
            theta += 90 - additionalAngle;
        }
        else if(leftJoystickX < 0 && leftJoystickY <= 0)
        {
            theta = 180;
            theta += additionalAngle;
        }
        else if(leftJoystickX >= 0 && leftJoystickY < 0)
        {
            theta = 270;
            theta += 90 - additionalAngle;
        }else{
            theta = additionalAngle;
        }

        telemetry.addData("Theta", theta);
        telemetry.update();
        return theta;
    }

    @Override
    public void runOpMode() {
        // Get motors as defined in Robot Config.
        topLeft = hardwareMap.get(DcMotor.class, "ltMotor");
        topRight = hardwareMap.get(DcMotor.class, "rtMotor");
        bottomLeft = hardwareMap.get(DcMotor.class, "lbMotor");
        bottomRight = hardwareMap.get(DcMotor.class, "rbMotor");

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Get joystick direction
            double theta = MovementTheta();

        }
    }
}
