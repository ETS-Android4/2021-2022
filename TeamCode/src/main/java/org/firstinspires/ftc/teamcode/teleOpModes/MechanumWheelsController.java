/*
    @author Declan J. Scott
*/

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

    public enum MotorState{
        FORWARD,
        BACKWARD,
        DISABLED
    }
    public MotorState rightSlantState = MotorState.DISABLED;
    public MotorState leftSlantState = MotorState.DISABLED;

    double MovementTheta()
    {
        float leftJoystickX = gamepad1.left_stick_x;
        float leftJoystickY = gamepad1.left_stick_y;
        telemetry.addData("Move X", leftJoystickX);
        telemetry.addData("Move Y", leftJoystickY);

        // Get the direction of the joystick (this has to be the worst code I've ever written)
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

            // control steering or driving
            if(gamepad1.left_bumper)
                Steer((float) theta);
            else
                Drive((float) theta);
        }
    }

    void Drive(float theta)
    {
        // Configure slant states
        // Right slant
        if(theta >= 90 && theta <= 180)
            rightSlantState = MotorState.FORWARD;
        else if(theta >= 270 && theta <= 360)
            rightSlantState = MotorState.BACKWARD;
        else
            rightSlantState = MotorState.DISABLED;

        // Left slant
        if(theta >= 90 && theta <= 180)
            rightSlantState = MotorState.FORWARD;
        else if(theta >= 270 && theta <= 360)
            rightSlantState = MotorState.BACKWARD;
        else
            rightSlantState = MotorState.DISABLED;

        // Configure motor power
        float rightSlantPower = rightSlantState == MotorState.FORWARD ? 1 : -1;
        if(rightSlantState == MotorState.DISABLED)
            rightSlantPower = 0;

        float leftSlantPower = leftSlantState == MotorState.FORWARD ? 1 : -1;
        if(leftSlantState == MotorState.DISABLED)
            leftSlantPower = 0;

        // Set power
        topRight.setPower(rightSlantPower);
        bottomLeft.setPower(rightSlantPower);
        topLeft.setPower(leftSlantPower);
        bottomRight.setPower(leftSlantPower);
    }

    void Steer(float theta)
    {
        float leftWheelPower = 0;
        float rightWheelPower = 0;
        if(theta >= 90 && theta <= 270)
        {
            leftWheelPower = -1;
            rightWheelPower = 1;
        }else {
            leftWheelPower = 1;
            rightWheelPower = -1;
        }

        // Set Power
        topRight.setPower(rightWheelPower);
        bottomRight.setPower(rightWheelPower);
        topLeft.setPower(leftWheelPower);
        bottomLeft.setPower(leftWheelPower);
    }
}
