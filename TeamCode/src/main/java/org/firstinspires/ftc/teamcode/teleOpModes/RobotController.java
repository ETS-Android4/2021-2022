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
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class RobotController extends LinearOpMode {

    // Get left/right motors
    public DcMotor leftMotor;
    public DcMotor rightMotor;
    
    // Get motor for carousel
    public DcMotor carouselMotor;
    
    // Var to store inputted power
    public double motorPower;
    public double rawSteeringInput;

    public void ControlDrivetrain()
    {
        // Get motor power
        motorPower = 0;
        if(gamepad1.right_trigger > 0.9)
            motorPower = 1;
        else if(gamepad1.left_trigger > 0.9)
            motorPower = -1;

        // Get steering input
        rawSteeringInput = this.gamepad1.left_stick_x;
            
        // Convert steering input to multipliers for the left and right motor
        double leftMultiplier = 1 - rawSteeringInput;
        double rightMultiplier = 1 + rawSteeringInput;

        // Get power
        double lMotorPower = -(motorPower * leftMultiplier);
        double rMotorPower = motorPower * rightMultiplier;

        // Set raw telemetry
        telemetry.addData("Raw Power Input", motorPower);
        telemetry.addData("Raw Steering Input", rawSteeringInput);

        DriveDrivetrain(lMotorPower, rMotorPower);
    }

    public void DriveDrivetrain(double l, double r)
    {
        // Apply power (one is inverted)
        leftMotor.setPower(l);
        rightMotor.setPower(r);

        // Set motor power telemetry
        telemetry.addData("Left Motor Power", leftMotor.getPower());
        telemetry.addData("Right Motor Power", rightMotor.getPower());
    }
    
    boolean isCarouselActive = false;
    int carouselPower = 1;
    
    boolean canChangeActive = true;
    boolean canChangeDirection = true;
    public void ControlCarousel()
    {
        // Use Y button to toggle carousel
        if(this.gamepad1.y){
            if(canChangeActive)
            {
                isCarouselActive = !isCarouselActive;
                canChangeActive = false;
            }
        }else{
            canChangeActive = true;
        }
        
        // Reverse motor direction with B button just in case
        if(this.gamepad1.b){
            if(canChangeDirection)
            {
                carouselPower *= -1;
                canChangeDirection = false;
            }
        }else{
            canChangeDirection = true;
        }
        
        // Set power to the motor
        if(isCarouselActive)
        {
            carouselMotor.setPower(carouselPower);
        }else{
            carouselMotor.setPower(0);
        }
        
        // Add telemetry
        telemetry.addData("Carousel", isCarouselActive);
        telemetry.addData("Carousel Power", carouselMotor.getPower());
    }
    
    @Override
    public void runOpMode() {
        // Get motors as defined in Robot Config.
        leftMotor = hardwareMap.get(DcMotor.class, "lMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rMotor");
        carouselMotor = hardwareMap.get(DcMotor.class, "cMotor");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Control drivetrain
            ControlDrivetrain();
            
            // Control carousel
            ControlCarousel();
            
            telemetry.addData("Status", "Running");
            telemetry.update();
        }
    }
}
