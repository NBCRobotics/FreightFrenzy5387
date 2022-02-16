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

import java.lang.reflect.Field;

//created by Sucheth Seethella
public class FFRobot {
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
    private final int MAX = 9500;
    private final int MIN = 0;
    private int currentMax = MAX;
    private int currentMin = MIN;
    private int initPos;

    private int stageTwo = FieldMeasurements.getStageTwoHeight();
    private int stageOne = FieldMeasurements.getStageOneHeight();
    private int stageThree = FieldMeasurements.getStageThreeHeight();

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

        this.blDrive.setDirection(motF); //as declared before motF is forward motR is reverse
        this.brDrive.setDirection(motR);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motR);
        this.linearSlide.setDirection(motR);
        this.arm.setDirection(serR);
        this.linearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setDriveMode(DcMotor.RunMode mode) {
        this.blDrive.setMode(mode);
        this.brDrive.setMode(mode);
        this.flDrive.setMode(mode);
        this.frDrive.setMode(mode);
    }

    public void leftPow(double pow){
        this.blDrive.setPower(-pow);
        this.flDrive.setPower(-pow);
    }
    public void rightPow(double pow){
        this.brDrive.setPower(-pow);
        this.blDrive.setPower(-pow);
    }
    public void drive(double lPow, double rPow){
        this.leftPow(lPow);
        this.rightPow(rPow);
    }

    public void drive(double bothPow){ //override of drive(double, double)
        this.drive(bothPow,bothPow);
    }

    public void driveTo(int pos) {
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        setDriveMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        setDriveMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (flDrive.getCurrentPosition() < pos) {
            flDrive.setTargetPosition(pos);
            frDrive.setTargetPosition(pos);
            blDrive.setTargetPosition(pos);
            brDrive.setTargetPosition(pos);
        }
        setDriveMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void turn() {

    }

    public void strafe(double pow){ //Pos = Right , Neg = Left
        blDrive.setPower(pow); //MOTF
        flDrive.setPower(-pow); //MOTF
        brDrive.setPower(-pow); //MOTR
        frDrive.setPower(pow); //MOTR       //A:WAYS INPUT 1 LMAO
    }

    public void strafe(String s, double pow) {
        pow = Math.abs(pow);
        if (s.equals("right"))
            this.strafe(pow);
        else if (s.equals("left"))
            this.strafe(-pow);
    }

    public void mechanumPov(Gamepad gp, Gamepad gp2){
//        double drive = (double) (gp.left_stick_y);
//        double turn = (double) ((gp.left_stick_x) * -1.5);
//        double strafe = (double) ((gp.right_stick_x) * -1);
//
//        double nor = 0.0; //normal drive power
//
//        double frontLeftPower = (drive + turn + strafe);
//        double backLeftPower = (drive - turn + strafe);
//        double frontRightPower = (drive - turn - strafe);
//        double backRightPower = (drive + turn - strafe);
//

//        if(abs(backRightPower) > 1 || abs(backLeftPower) > 1 ||
//                abs(frontRightPower) > 1 || abs(frontLeftPower) > 1) {
//
//            //sets normal speed to greatest magnitude of drive power level(to normalize all power)
//            nor = Math.max(abs(frontLeftPower), abs(backLeftPower));
//            nor = Math.max(abs(frontRightPower), nor);
//            nor = Math.max(abs(backRightPower), nor);
//        }


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

        this.flDrive.setPower((frontLeftPower)/(slowDown));
        this.blDrive.setPower((backLeftPower)/(slowDown));
        this.frDrive.setPower((frontRightPower)/(slowDown));
        this.brDrive.setPower((backRightPower)/(slowDown));

        intake(gp2);
        linearPower(gp2);
        armPower(gp2);
        carouselPower(gp);
        dpadDrive(gp);
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

    public void setCarouselPower(double pow){
        carousel.setPower(pow);
    }

    public void setLinearPower(double pow)
    {
        linearSlide.setPower(-pow); //the motor is orientated as up is negative - this makes it more intuitive with our min and max values
    }

    public void setLinearPower(String direction, double pow) { //what is this
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

    public void brake(){
        this.drive(0.0);
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

        //if that doesn't work uncomment this stuff cause i think this might work
//        drive = gp.dpad_up ? -1.0 : gp.dpad_down ? 1.0 : 0.0;
//        strafe = gp.dpad_left ? -1.0 : gp.dpad_right ? 1.0 : 0.0;
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

        if(getSlideEncoder() >= currentMax && gp.left_stick_y < 0)
            linearSlide.setPower(0);
        else if (isMin() && gp.left_stick_y > 0)
            linearSlide.setPower(0);
        else
            linearSlide.setPower(gp.left_stick_y);
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
