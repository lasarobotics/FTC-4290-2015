package com.qualcomm.ftcrobotcontroller.opmodes;

import com.lasarobotics.library.util.MathUtil;
import com.qualcomm.ftcrobotcontroller.Hardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


public class NavXTest extends LinearOpMode {
    private static final int TOLERANCE_DEGREES = 2;

    Hardware config;


    @Override
    public void runOpMode() throws InterruptedException {
        config = new Hardware(hardwareMap);

        waitForStart();
        turnToDegNavX(45, .5);
        config.navx.stop();
    }

    public  float convertDegNavX(float deg) {
        if (deg < 0)
            deg = 360 - Math.abs(deg);
        return deg;
    }

    public void turnToDegNavX(int deg, double power) throws InterruptedException {
        config.navx.reset();
        waitOneFullHardwareCycle();
        config.leftWheel.setPower(-power);
        config.rightWheel.setPower(power);

        float yaw;

        boolean arrived = false;

        while(!arrived) {
            yaw = convertDegNavX(config.navx.getRotation().x);
            telemetry.addData("Yaw", yaw);
            telemetry.addData("Target Yaw", deg);
            if (MathUtil.inBounds(deg - TOLERANCE_DEGREES, deg + TOLERANCE_DEGREES, yaw))
                arrived = true;
        }

        config.leftWheel.setPower(0);
        config.rightWheel.setPower(0);
    }
}
