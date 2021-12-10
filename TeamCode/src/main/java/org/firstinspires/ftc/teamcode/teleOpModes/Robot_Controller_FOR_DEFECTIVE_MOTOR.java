/*
Copyright 2021 FIRST Tech Challenge Team FTC
Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute,
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
    @author Declan J. Scott
*/

package org.firstinspires.ftc.teamcode.teleOpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

// Exact replica of RobotController.java, but the left motor receives less power
@TeleOp
public class Robot_Controller_FOR_DEFECTIVE_MOTOR extends RobotController {

    @Override
    public void ControlDrivetrain()
    {
        // Set power
        motorPower = -this.gamepad1.left_stick_y;
        rawSteeringInput = this.gamepad1.left_stick_x;
            
        // Set motor power on the right motor to only a percentage of it's power
        double rightMotorCompensation = 0.95f;
        double rightMotorPower = motorPower * rightMotorCompensation;
        
        // Convert steering input to multipliers for the left and right motor
        double leftMultiplier = 1 + rawSteeringInput;
        double rightMultiplier = 1 - rawSteeringInput;
        
        // Apply power (one is inverted)
        leftMotor.setPower(-(motorPower * leftMultiplier));
        rightMotor.setPower(rightMotorPower * rightMultiplier);
            
        telemetry.addData("Raw Power Input", motorPower);
        telemetry.addData("Raw Steering Input", rawSteeringInput);
            
        // Set motor power telemetry
        telemetry.addData("Left Motor Power (UNCHANGED)", leftMotor.getPower());
        telemetry.addData("Right Motor Power (COMPENSATED)", rightMotor.getPower());
    }
}
