/*
    @author Declan J. Scott
*/

package org.firstinspires.ftc.teamcode.teleOpModes;

import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp
public class MechanumWheelsController extends LinearOpMode {

    // Different Motors
    public DcMotor topLeft;
    public DcMotor topRight;
    public DcMotor bottomLeft;
    public DcMotor bottomRight;
    public DcMotor slider; // Slider
    public DcMotor carousel; // Carousel

    // Motor states
    public enum MotorState{
        FORWARD,
        BACKWARD,
        DISABLED
    }
    public MotorState rightSlantState = MotorState.DISABLED;
    public MotorState leftSlantState = MotorState.DISABLED;

    // Servos for grabber
    public Servo leftGrabber;
    public Servo rightGrabber;
    // Create offset for servos so they can be reset correctly
    public float GOffset = 0.75f;

    // Touch sensor
    public RevTouchSensor bottomLimit;

    // Speed
    public float speed = 1;
    public float sliderSpeed = 0.75f;

    double MovementTheta()
    {
        float leftJoystickX = gamepad1.left_stick_x;
        float leftJoystickY = -gamepad1.left_stick_y;
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
        return theta;
    }

    @Override
    public void runOpMode() {
        // Get motors as defined in Robot Config.
        topLeft = hardwareMap.get(DcMotor.class, "ltMotor");
        topRight = hardwareMap.get(DcMotor.class, "rtMotor");
        bottomLeft = hardwareMap.get(DcMotor.class, "lbMotor");
        bottomRight = hardwareMap.get(DcMotor.class, "rbMotor");
        slider = hardwareMap.get(DcMotor.class, "slider");
        carousel = hardwareMap.get(DcMotor.class, "carousel");

        // Get digital touch sensor
        bottomLimit = hardwareMap.get(RevTouchSensor.class, "bLimit");

        // Get servos
        leftGrabber = hardwareMap.get(Servo.class, "lGrab");
        rightGrabber = hardwareMap.get(Servo.class, "rGrab");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Initialize Servos
        leftGrabber.setDirection(Servo.Direction.REVERSE);
        leftGrabber.setPosition(GOffset);
        rightGrabber.setPosition(GOffset);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Initialize slider at bottom
//        while(!bottomLimit.isPressed())
//        {
//            telemetry.addData("Status", "Initializing");
//            telemetry.addData("Bottom Limit Status", bottomLimit.isPressed());
//            telemetry.update();
//            slider.setPower(sliderSpeed);
//        }
//        slider.setPower(0);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
            MasterControls();
    }

    public void MasterControls()
    {
            // Get joystick direction
            double theta = MovementTheta();

            speed = gamepad1.right_bumper ? 1f : 0.4f;

            // control steering or driving
            if(!Double.isNaN(theta))
            {
                if(gamepad1.left_bumper)
                    Steer((float) theta);
                else
                    Drive((float) theta);
            }else{
                // Set power
                topRight.setPower(0);
                bottomLeft.setPower(0);
                topLeft.setPower(0);
                bottomRight.setPower(0);
            }

            SliderMovement();
            ControlCarousel();

            // Control grabber with right trigger
            float grabberPower = gamepad1.right_trigger;
            leftGrabber.setPosition(Math.min(grabberPower, 1f));
            rightGrabber.setPosition(Math.min(grabberPower, 1f));
            telemetry.addData("Grabber Target", grabberPower);

            // Motor power telemetry
            telemetry.addData("TRP", topRight.getPower());
            telemetry.addData("BRP", bottomRight.getPower());
            telemetry.addData("TLP", topLeft.getPower());
            telemetry.addData("BLP", bottomLeft.getPower());

            // Slider telemetry
            telemetry.addData("Slider Power", slider.getPower());
            telemetry.addData("Bottom Limit Status", bottomLimit.isPressed());

            telemetry.update();
    }

    public void Drive(float theta)
    {
        // Configure slant states
        // Right slant
        if(theta >= 85 && theta <= 185)
            rightSlantState = MotorState.FORWARD;
        else if(theta >= 265 && theta <= 360)
            rightSlantState = MotorState.BACKWARD;
        else
            rightSlantState = MotorState.DISABLED;

        // Left slant
        if(theta >= 0 && theta <= 95)
            leftSlantState = MotorState.FORWARD;
        else if(theta >= 175 && theta <= 275)
            leftSlantState = MotorState.BACKWARD;
        else
            leftSlantState = MotorState.DISABLED;

        // Lazy, but I got to do it
        if(Math.round(theta / 10) == 0)
        {
            leftSlantState = MotorState.FORWARD;
            rightSlantState = MotorState.BACKWARD;
        }

        // Configure motor power
        float rightSlantPower = rightSlantState == MotorState.FORWARD ? speed : -speed;
        if(rightSlantState == MotorState.DISABLED)
            rightSlantPower = 0;

        float leftSlantPower = leftSlantState == MotorState.FORWARD ? speed : -speed;
        if(leftSlantState == MotorState.DISABLED)
            leftSlantPower = 0;

        telemetry.addData("Right Slant State", rightSlantState.toString());
        telemetry.addData("Left Slant State", leftSlantState.toString());

        // Set power
        topRight.setPower(rightSlantPower);
        bottomLeft.setPower(-rightSlantPower);
        topLeft.setPower(-leftSlantPower);
        bottomRight.setPower(leftSlantPower);
    }

    public void Steer(float theta)
    {
        float leftWheelPower = 0;
        float rightWheelPower = 0;
        if(theta >= 90 && theta <= 270)
        {
            leftWheelPower = -speed;
            rightWheelPower = speed;
        }else {
            leftWheelPower = speed;
            rightWheelPower = -speed;
        }

        // Set Power
        bottomLeft.setPower(-leftWheelPower);
        topRight.setPower(rightWheelPower);
        bottomRight.setPower(rightWheelPower);
        topLeft.setPower(-leftWheelPower);
    }

    // Map right-joystick y-axis to slider movement while accounting for limit switches
    void SliderMovement()
    {
        float yPower = this.gamepad1.right_stick_y * sliderSpeed;

        // If pressed, move up until not pressed anymore
        if(!bottomLimit.isPressed())
            slider.setPower(yPower);
        else
            slider.setPower(-0.5f);
    }

    boolean isCarouselActive = false;
    float carouselPower = 0.75f;

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
            carousel.setPower(carouselPower);
        }else{
            carousel.setPower(0);
        }

        // Add telemetry
        telemetry.addData("Carousel", isCarouselActive);
        telemetry.addData("Carousel Power", carousel.getPower());
    }
}
