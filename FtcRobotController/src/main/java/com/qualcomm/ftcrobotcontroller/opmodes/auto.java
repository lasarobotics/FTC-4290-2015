package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Ethan on 1/12/16.
 */

public class auto extends LinearOpMode {
    int wheelPosition;

    public void declare() throws InterruptedException {
        hardware.leftWheel = hardwareMap.dcMotor.get("l");
        hardware.rightWheel = hardwareMap.dcMotor.get("r");
        hardware.rightWheel.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch1 = hardwareMap.dcMotor.get("w1");
        hardware.winch1.setDirection(DcMotor.Direction.REVERSE);
        hardware.winch2 = hardwareMap.dcMotor.get("w2");

        hardware.angler = hardwareMap.dcMotor.get("a");
        hardware.angler.setDirection(DcMotor.Direction.REVERSE);

        hardware.climberLeft = hardwareMap.servo.get("cl");
        hardware.climberLeft.setPosition(hardware.leftServoTop);

        hardware.climberRight = hardwareMap.servo.get("cr");
        hardware.climberRight.setPosition(hardware.rightServoTop);

        hardware.stopper = hardwareMap.servo.get("s");
        hardware.stopper.setPosition(hardware.stopperOff);

        hardware.climber = hardwareMap.servo.get("c");
        hardware.climber.setPosition(hardware.climberBottom);

        hardware.winch2.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        hardware.angler.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);

        while (hardware.rightWheel.getCurrentPosition() != 0){
            hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives hardware the time to reset
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
    }
    public void driveTo(int ticks, double powerLeft, double powerRight) throws InterruptedException {
        double wheelPosition =  0;
        while(wheelPosition != ticks) {
            wheelPosition = hardware.rightWheel.getCurrentPosition();
            telemetry.addData("1", "wheel at: " + wheelPosition + " ticks.");
            if(wheelPosition < ticks) {
                telemetry.addData("2", "tick goal: " + (ticks));
                telemetry.addData("3", "near");
                hardware.leftWheel.setPower(powerLeft);
                hardware.rightWheel.setPower(powerRight);
            }
            else if (wheelPosition > ticks) {
                telemetry.addData("2", "tick goal: " + (ticks));
                telemetry.addData("3", "far");
                hardware.leftWheel.setPower(-powerLeft);
                hardware.rightWheel.setPower(-powerRight);
            } else {
                break;
            }
        }
        stopDrive();
        while (hardware.rightWheel.getCurrentPosition() != 0){
            hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }//gives hardware the time to reset
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitOneFullHardwareCycle();
    }
    public void driveAng(int angle, double leftPower, double rightPower) throws InterruptedException {
        double mod = 34;
        driveTo((int) (angle * mod), leftPower, rightPower);
    }
    public void driveDist(int inches, double leftPower, double rightPower) throws InterruptedException {
        double mod = 80;
        driveTo((int) (inches * mod), leftPower, rightPower);
    }
    public void stopDrive(){
        hardware.rightWheel.setPower(0);
        hardware.leftWheel.setPower(0);
    }
    public static void moveArm() {
        for(double i  = hardware.climberBottom; i > hardware.climberTop; i-=.01){
            hardware.climber.setPosition(i);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        declare();
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        waitOneFullHardwareCycle();
        waitForNextHardwareCycle();
        hardware.rightWheel.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        waitForStart();
        moveArm();
    }

}
