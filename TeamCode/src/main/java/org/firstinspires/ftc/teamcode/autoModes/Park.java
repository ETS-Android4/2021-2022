package org.firstinspires.ftc.teamcode.autoModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.teleOpModes.MechanumWheelsController;

// LAST LEGS FOR AUTO

@Autonomous
public class Park extends MechanumWheelsController {

    @Override
    public void MasterControls()
    {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        speed = 0.5f;
        Drive(270);
        try {
            Thread.sleep(1200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Set power
        topRight.setPower(0);
        bottomLeft.setPower(0);
        topLeft.setPower(0);
        bottomRight.setPower(0);
        stop();
    }
}
