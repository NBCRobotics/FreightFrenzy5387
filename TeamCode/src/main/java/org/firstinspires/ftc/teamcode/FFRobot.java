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

    private DcMotor[] motors;

    DcMotorSimple.Direction motF = DcMotorSimple.Direction.FORWARD;
    DcMotorSimple.Direction motR = DcMotorSimple.Direction.REVERSE;

    public void init(HardwareMap hwdMap){
        this.motors = new DcMotor[] {blDrive,brDrive,flDrive,frDrive};

        this.blDrive = hwdMap.get(DcMotor.class, "blDrive");
        this.brDrive = hwdMap.get(DcMotor.class, "brDrive");
        this.flDrive = hwdMap.get(DcMotor.class, "flDrive");
        this.frDrive = hwdMap.get(DcMotor.class, "frDrive");

        this.blDrive.setDirection(motF);
        this.brDrive.setDirection(motF);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motF);
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

    public void mechanumPov(Gamepad gp){
        double drive = (double) (gp.left_stick_y);
        double turn = (double) ((gp.left_stick_x) * -1);
        double strafe = (double) ((gp.right_stick_x) * -1);

        double nor = 0.0;

        double frontLeftPower = (drive + turn + strafe);
        double backLeftPower = (drive - turn + strafe);
        double frontRightPower = (drive - turn - strafe);
        double backRightPower = (drive + turn - strafe);

        if(Math.abs(backRightPower) > 1 || Math.abs(backLeftPower) > 1 ||
                Math.abs(frontRightPower) > 1 || Math.abs(frontLeftPower) > 1) {
            nor = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            nor = Math.max(Math.abs(frontRightPower), nor);
            nor = Math.max(Math.abs(backRightPower), nor);

        }

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

}
