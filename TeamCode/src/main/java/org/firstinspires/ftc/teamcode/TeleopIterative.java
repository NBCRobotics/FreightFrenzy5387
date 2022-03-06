/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Iterative OpMode", group="Teleop")

public class TeleopIterative extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //note that the motors are now all in a single row
    private DcMotor blDrive = null;
    private DcMotor brDrive = null;
    private DcMotor flDrive = null;
    private DcMotor frDrive = null;
    private DcMotor intake = null;

    private DcMotor linearSlide = null;//Tesrng btiyuc
    private DcMotor carousel = null;
    private Servo arm = null;

    private int zero = 0;   //linear slide encoders
    private final int MAX = 5000;
    private final int MIN = 0;
    private int currentMax = MAX;
    private int currentMin = MIN;
    private int lastPos = 0;

    private int stageTwo = FieldMeasurements.getStageHeight(1);
    private int stageOne = FieldMeasurements.getStageHeight(2);
    private int stageThree = FieldMeasurements.getStageHeight(3);

    DcMotorSimple.Direction motF = DcMotorSimple.Direction.FORWARD;
    DcMotorSimple.Direction motR = DcMotorSimple.Direction.REVERSE;
    Servo.Direction serR = Servo.Direction.REVERSE;
    Servo.Direction serF = Servo.Direction.FORWARD;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");


        this.blDrive = hardwareMap.get(DcMotor.class, "blDrive");
        this.brDrive = hardwareMap.get(DcMotor.class, "brDrive");
        this.flDrive = hardwareMap.get(DcMotor.class, "flDrive");
        this.frDrive = hardwareMap.get(DcMotor.class, "frDrive");
        this.intake = hardwareMap.get(DcMotor.class, "intake");
        this.linearSlide = hardwareMap.get(DcMotor.class, "linearSlide");
        this.carousel = hardwareMap.get(DcMotor.class, "carousel");
        this.arm = hardwareMap.get(Servo.class, "arm");

        //intake.resetDeviceConfigurationForOpMode();

        this.blDrive.setDirection(motF);
        this.brDrive.setDirection(motR);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motR);
        this.linearSlide.setDirection(motR);
        this.arm.setDirection(serR);
        this.linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.linearSlide.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.blDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.brDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        double drive = (-gamepad1.left_stick_y);
        double strafe = (gamepad1.left_stick_x);
        double turn = (gamepad1.right_stick_x);

        //drive measures forward and backwards - is unchanged for all motors
        //strafe is subtracted from the front right and the back left
        //turn needs to affect the right if the stick is moved right, left if moved left
        //right stick right is positive
        //so subtract turn from the right, and add to left

        double frontLeftPower = (drive + strafe + turn);
        double frontRightPower = (drive - strafe - turn);
        double backLeftPower = (drive - strafe + turn);
        double backRightPower = (drive + strafe - turn);

        //to slow down the drive if needed
        double slowDown = gamepad1.left_bumper ? 8.0 : 1.0;
        slowDown = gamepad1.right_bumper ? 2.0 : slowDown;

        flDrive.setPower((frontLeftPower)/(slowDown));
        blDrive.setPower((backLeftPower)/(slowDown));
        frDrive.setPower((frontRightPower)/(slowDown));
        brDrive.setPower((backRightPower)/(slowDown));
        intake(gamepad2);
        linearPower(gamepad2);
        armPower(gamepad2);
        carouselPower(gamepad1);


        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());

        telemetry.addData("Motors",
                "back left (%.2f), back right (%.2f), front left (%.2f), front right (%.2f)",
                (backLeftPower)/(slowDown), (backRightPower)/(slowDown), (frontLeftPower)/(slowDown), (frontRightPower)/(slowDown));
        telemetry.addData("Game Pad 1: ", gamepad1.left_stick_y + " " +gamepad1.left_stick_x+ " "
                +gamepad1.right_stick_x+ " " +gamepad1.right_stick_y + "\n\n");
        telemetry.addData("Right bumper GP1: ", gamepad1.right_bumper);
        telemetry.addData("Left bumper GP1: ", gamepad1.left_bumper);
        telemetry.addData("GP2 a: ", gamepad2.a);
        telemetry.addData("GP2 b: ", gamepad2.b);
        telemetry.addData("GP2 y: ", gamepad2.y);
        telemetry.addData("Intake: ", intake.getCurrentPosition() + "\n");
        telemetry.addData("LinearSlide: ", +linearSlide.getPower() + "\n");
        telemetry.addData("LinearSlide Encoder: ", +getSlideEncoder() + "\n");
        if (isMax()) telemetry.addData("LINEAR SLIDE ERROR", "MAX REACHED");
        if (isMin()) telemetry.addData("LINEAR SLIDE ERROR", "MIN REACHED");
        telemetry.addData("Carousel: ", carousel.getPower() + "\n");
        telemetry.addData("Current Max: ", currentMax);
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {

    }
    //GamePad 2 Methods
    public void intake(Gamepad gp) { //negative is push out || positive is take in
        if (gp.right_stick_y != 0)
            intake.setPower(gp.right_stick_y);
        else
            intake.setPower(0);
    }

    public void armPower(Gamepad gp) { //right is raise || left is lower
        if (gp.right_bumper)
            arm.setPosition(0.45);
        else if (gp.left_bumper)
            arm.setPosition(0.55);
        else
            arm.setPosition(0.5);
    }

    public boolean isMin(){
        return getSlideEncoder() <= MIN;
    }
    public boolean isMax(){
        return getSlideEncoder() >= MAX;
    }

    public int getSlideEncoder(){    //the "max" and "min" are too counterintuitive
        return -1*linearSlide.getCurrentPosition();
    }

    public void linearPower(Gamepad gp){ //dynamically will set a "max" - stage one, two or three, or above all stages
        if (gp.y)
            currentMax = stageThree;
        else if(gp.b)
            currentMax = stageTwo;
        else if (gp.a)
            currentMax = stageOne;
        else{
            currentMax = MAX;
        }

//        if (getSlideEncoder() > currentMax || getSlideEncoder() < currentMin) {
//
//        }

        //else {
        if (gp.left_stick_y == 0) {
//                double offsetPow;
//                if (getSlideEncoder() < lastPos) {
//                    offsetPow = lastPos - getSlideEncoder() * 0.0015;
//                } else if (getSlideEncoder() > lastPos) {
//                    offsetPow = lastPos - getSlideEncoder() * 0.0005;
//                } else {
//                    offsetPow = 0;
//                }
            linearSlide.setPower(0);
        }
        else {
            if(getSlideEncoder() >= currentMax && gp.left_stick_y < 0)
                linearSlide.setPower(0);
            else if (isMin() && gp.left_stick_y > 0)
                linearSlide.setPower(0);
            else
                linearSlide.setPower(gp.left_stick_y);
        }
//        }

        lastPos = getSlideEncoder();

    }
    public void carouselPower(Gamepad gp) { //Blue - Left Trigger || Red - Right Trigger
        if(gp.right_trigger > 0)
            carousel.setPower(-(gp.right_trigger)/1);
        else if(gp.left_trigger > 0)
            carousel.setPower((gp.left_trigger)/1);
        else
            carousel.setPower(zero);
    }



}
