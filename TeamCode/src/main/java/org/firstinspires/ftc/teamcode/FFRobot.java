package org.firstinspires.ftc.teamcode;

import static java.lang.Math.abs;

import android.graphics.Path;

import com.qualcomm.hardware.motors.RevRoboticsCoreHexMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Field;

//created by Sucheth Seethella
public class FFRobot {
    //note that the motors are now all in a single row
    private DcMotor blDrive = null;
    private DcMotor brDrive = null;
    private DcMotor flDrive = null;
    private DcMotor frDrive = null;
    private DcMotor intake = null;
    public double leftPower;
    public double rightPower;

    private DcMotor linearSlide = null;//Tesrng btiyuc
    private DcMotor carousel = null;
    private Servo arm = null;

    private int zero = 0;   //linear slide encoders
    private final int MAX = 4100;
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

    private double drive;
    private double turn;
    private double strafe;
    private double frontLeftPower;
    private double frontRightPower;
    private double backLeftPower;
    private double backRightPower;
    private double lastDrivePos;

    public void init(HardwareMap hwdMap){

        this.blDrive = hwdMap.get(DcMotor.class, "blDrive");
        this.brDrive = hwdMap.get(DcMotor.class, "brDrive");
        this.flDrive = hwdMap.get(DcMotor.class, "flDrive");
        this.frDrive = hwdMap.get(DcMotor.class, "frDrive");
        this.intake = hwdMap.get(DcMotor.class, "intake");
        this.linearSlide = hwdMap.get(DcMotor.class, "linearSlide");
        this.carousel = hwdMap.get(DcMotor.class, "carousel");
        this.arm = hwdMap.get(Servo.class, "arm");

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

    //auto drive methods
    public void leftPow(double pow){
        this.blDrive.setPower(-pow);
        this.flDrive.setPower(-pow);
    }
    public void rightPow(double pow){
        this.brDrive.setPower(-pow);
        this.frDrive.setPower(-pow);
    }

    public void drive(double lPow, double rPow){
        this.leftPow(lPow);
        this.rightPow(rPow);
    }

    public void drive(double bothPow){ //override of drive(double, double)
        drive(bothPow, bothPow);
    }

    public void strafe(double pow){ //Pos = right , Neg = left
        blDrive.setPower(-pow);
        flDrive.setPower(pow);
        brDrive.setPower(pow);
        frDrive.setPower(-pow);
    }

    public void strafe(String s, double pow) {
        pow = Math.abs(pow);
        if (s.equals("right"))
            this.strafe(pow);
        else if (s.equals("left"))
            this.strafe(-pow);
    }

    public void driveTo(int pos) {
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        setTargetPos(pos);
        if (pos > 0) {
            drive(0.5);
            while (flDrive.getCurrentPosition() < pos) {
                double diff = ((flDrive.getCurrentPosition() - frDrive.getCurrentPosition()) * 0.0015);
                frDrive.setPower(0.5 + diff);
                diff = ((flDrive.getCurrentPosition() - blDrive.getCurrentPosition()) * 0.0015);
                blDrive.setPower(0.5 + diff);
                diff = ((flDrive.getCurrentPosition() - brDrive.getCurrentPosition()) * 0.0015);
                brDrive.setPower(0.5 + diff);
            }
        } else {
            drive(-0.5);
            while (flDrive.getCurrentPosition() > pos) {
                double diff = ((flDrive.getCurrentPosition() - frDrive.getCurrentPosition()) * 0.0015);
                frDrive.setPower(-0.5 + diff);
                diff = ((flDrive.getCurrentPosition() - blDrive.getCurrentPosition()) * 0.0015);
                blDrive.setPower(-0.5 + diff);
                diff = ((flDrive.getCurrentPosition() - brDrive.getCurrentPosition()) * 0.0015);
                brDrive.setPower(-0.5 + diff);
            }
        }
        brake();
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void strafeTo(int pos) {
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_TO_POSITION);
        setTargetPos(pos);
        if (pos > 0) {
            strafe(0.5);
            while(flDrive.getCurrentPosition() < pos) {
                double diff = (Math.abs((flDrive.getCurrentPosition()) - Math.abs(frDrive.getCurrentPosition())) * 0.0015);
                frDrive.setPower(-0.5 + diff);
                diff = (Math.abs((flDrive.getCurrentPosition()) - Math.abs(brDrive.getCurrentPosition())) * 0.0015);
                brDrive.setPower(0.5 + diff);
                diff = (Math.abs((flDrive.getCurrentPosition()) - Math.abs(blDrive.getCurrentPosition())) * 0.0015);
                blDrive.setPower(-0.5 + diff);
            }
        }
        if (pos < 0) {
            strafe(-0.5);
            while(flDrive.getCurrentPosition() > pos) {
                double diff = (Math.abs((flDrive.getCurrentPosition()) - Math.abs(frDrive.getCurrentPosition())) * 0.0015);
                frDrive.setPower(0.5 + diff);
                diff = (Math.abs((flDrive.getCurrentPosition()) - Math.abs(brDrive.getCurrentPosition())) * 0.0015);
                brDrive.setPower(-0.5 + diff);
                diff = (Math.abs((flDrive.getCurrentPosition()) - Math.abs(blDrive.getCurrentPosition())) * 0.0015);
                blDrive.setPower(0.5 + diff);
            }
        }
        brake();
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int timeForTurn(int angle) {
        return (angle*1700/180);
    }

    public void setTargetPos(int pos) {
        flDrive.setTargetPosition(pos);
        frDrive.setTargetPosition(pos);
        blDrive.setTargetPosition(pos);
        brDrive.setTargetPosition(pos);
    }
    public void setDriveMode(DcMotor.RunMode mode) {
        this.blDrive.setMode(mode);
        this.brDrive.setMode(mode);
        this.flDrive.setMode(mode);
        this.frDrive.setMode(mode);
    }

    public void brake() {
        this.drive(0.0);
    }

    public void reverse() {
        double flPow = flDrive.getPower();
        double frPow = frDrive.getPower();
        double blPow = blDrive.getPower();
        double brPow = brDrive.getPower();
        flDrive.setPower(-flPow);
        frDrive.setPower(-frPow);
        brDrive.setPower(-brPow);
        blDrive.setPower(-blPow);
    }

    //teleop methods

    public void mechanumPov(Gamepad gp, Gamepad gp2) {
        drive = (-gp.left_stick_y);
        strafe = (gp.left_stick_x);
        turn = (gp.right_stick_x);

        //drive measures forward and backwards - is unchanged for all motors
        //strafe is subtracted from the front right and the back left
        //turn needs to affect the right if the stick is moved right, left if moved left
        //right stick right is positive
        //so subtract turn from the right, and add to left

        frontLeftPower = (drive + strafe + turn);
        frontRightPower = (drive - strafe - turn);
        backLeftPower = (drive - strafe + turn);
        backRightPower = (drive + strafe - turn);

        //to slow down the drive if needed
        double slowDown = gp.left_bumper ? 8.0 : 1.0;
        slowDown = gp.right_bumper ? 2.0 : slowDown;

        this.flDrive.setPower((frontLeftPower)/(slowDown));
        this.blDrive.setPower((backLeftPower)/(slowDown));
        this.frDrive.setPower((frontRightPower)/(slowDown));
        this.brDrive.setPower((backRightPower)/(slowDown));



    }

    public double getFrontLeftPower(){
        return flDrive.getPower();
    }
    public double getBackLeftPower(){
        return blDrive.getPower();
    }
    public double getBackRightPower(){
        return brDrive.getPower();
    }
    public double getFrontRightPower(){
        return frDrive.getPower();
    }
    public double getIntakePower(){return intake.getCurrentPosition();}
    public double getLinearPower(){return linearSlide.getPower();}
    public double getCarouselPower(){return carousel.getPower();}

    //auto methods for control
    public void setCarouselPower(double pow){
        carousel.setPower(pow);
    }

    public void setLinearPower(double pow)
    {
        linearSlide.setPower(-pow); //the motor is orientated as up is negative - this makes it more intuitive with our min and max values
    }

    public void setLinearPower(String direction, double pow) {
        pow = abs(pow);
        if (direction.equals("up")) {
            linearSlide.setPower(-pow);
        }
        if (direction.equals("down")) {
            linearSlide.setPower(pow);
        }
    }

    public void turnIntake(double pow){
        intake.setPower(pow);
    }

    public void setArmPos(double pow) { arm.setPosition(pow); }

    //Gamepad 1 Methods
    public void carouselPower(Gamepad gp) { //Blue - Left Trigger || Red - Right Trigger
        if(gp.right_trigger > 0)
            carousel.setPower(-(gp.right_trigger)/2);
        else if(gp.left_trigger > 0)
            carousel.setPower((gp.left_trigger)/2);
        else
            carousel.setPower(zero);
    }

    public void dpadDrive(Gamepad gp) {    //use if you simple want to move straight in cardinal directions.
        if (gp.dpad_down) {
            drive(-0.5);
        } else if (gp.dpad_left) {
            strafe("left", 0.5);
        } else if (gp.dpad_right) {
            strafe("right", 0.5);
        } else if (gp.dpad_up) {
            drive(0.5);
        } else {
            brake();
        }
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


    public boolean isMax(){
        return getSlideEncoder() >= MAX;
    }
    public int getMax(){
        return MAX;
    }

    public boolean isMin(){
        return getSlideEncoder() <= MIN;
    }
    public int getMin(){
        return MIN;
    }

    public int getSlideEncoder(){    //the "max" and "min" are too counterintuitive
        return -1*linearSlide.getCurrentPosition();
    }

    public int getCurrentMax() {
        return currentMax;
    }


}
