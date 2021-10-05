package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FFRobot {
    private DcMotor blDrive = null;
    private DcMotor brDrive = null;
    private DcMotor flDrive = null;
    private DcMotor frDrive = null;
    private Servo leftHook = null;
    //COmments

    private DcMotor[] motors;

    DcMotorSimple.Direction motF = DcMotorSimple.Direction.FORWARD;
    DcMotorSimple.Direction motR = DcMotorSimple.Direction.REVERSE;
    Servo.Direction serR = Servo.Direction.REVERSE;
    Servo.Direction serF = Servo.Direction.FORWARD;


    public void init(HardwareMap hwdMap){
        this.motors = new DcMotor[] {blDrive,brDrive,flDrive,frDrive};

        this.blDrive = hwdMap.get(DcMotor.class, "blDrive");
        this.brDrive = hwdMap.get(DcMotor.class, "brDrive");
        this.flDrive = hwdMap.get(DcMotor.class, "flDrive");
        this.frDrive = hwdMap.get(DcMotor.class, "frDrive");
        this.leftHook = hwdMap.get(Servo.class, "leftHook");

//        this.blDrive.setDirection(motF);
//        this.brDrive.setDirection(motR);
//        this.flDrive.setDirection(motF);
//        this.frDrive.setDirection(motR);

        this.blDrive.setDirection(motF);
        this.brDrive.setDirection(motR);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motR);
        this.leftHook.setDirection(serR);
    }
    public void leftPow(double pow){
        this.blDrive.setPower(pow);
        this.brDrive.setPower(pow);
    }
    public void rightPow(double pow){
        this.flDrive.setPower(pow);
        this.frDrive.setPower(pow);
    }
    public void drive(double lPow, double rPow){
        this.leftPow(lPow);
        this.rightPow(rPow);
    }
    public void drive(double bothPow){
        this.drive(bothPow,bothPow);
    }

    public void strafe(double pow){
        blDrive.setPower(-pow);
        flDrive.setPower(pow);
        brDrive.setPower(pow);
        frDrive.setPower(-pow);
    }

    public void mechanumPov(Gamepad gp){
        double drive = (double) (gp.left_stick_y);
        double turn = (double) ((gp.left_stick_x) * -1);
        double strafe = (double) ((gp.right_stick_x) * -1);


        double nor = 0.0;

        double frontLeftPower = (drive + turn + strafe);
        double backLeftPower = (drive - turn + strafe);
        double frontRightPower = (drive - turn - strafe);
        double backRightPower = (drive + turn - strafe);

        double y = gp.left_stick_y; // Remember, this is reversed!
        double x = -gp.left_stick_x * 1.1;
        double rx = -gp.right_stick_x;
        double deno = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double newflPower = y + x + rx;
        double newblPower = y - x + rx;
        double newfrPower = y - x - rx;
        double newbrPower = y + x - rx;

        if(Math.abs(backRightPower) > 1 || Math.abs(backLeftPower) > 1 ||
                Math.abs(frontRightPower) > 1 || Math.abs(frontLeftPower) > 1) {
            nor = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            nor = Math.max(Math.abs(frontRightPower), nor);
            nor = Math.max(Math.abs(backRightPower), nor);

        }
        foundHooks(gp);
        this.flDrive.setPower(newflPower);
        this.blDrive.setPower(newblPower);
        this.frDrive.setPower(newfrPower);
        this.brDrive.setPower(newbrPower);


//        this.brDrive.setPower(frontLeftPower);
//        this.brDrive.setPower(backLeftPower);
//        this.brDrive.setPower(frontRightPower);
//        this.brDrive.setPower(backRightPower);

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
    public void brake(){
        this.drive(0.0);

    }

    public void hookDown()
    {
        leftHook.setPosition(0.4);
        //.position = 0.72
    }

    public void hookUp()
    {
        leftHook.setPosition(0.01);
       // this.rightHook?.position = 0.21
    }

    public void foundHooks(Gamepad gp) {
        if (gp.right_bumper) { //hook down
            hookDown();
        } else { //default position
            hookUp();
        }

    }
    public double getServo(){
        return leftHook.getPosition();
    }


}
