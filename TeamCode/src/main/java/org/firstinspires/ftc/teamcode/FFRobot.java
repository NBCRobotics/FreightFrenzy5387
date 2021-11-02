package org.firstinspires.ftc.teamcode;

import android.graphics.Path;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
//created by Sucheth Seethella
public class FFRobot {
    //note that the motors are now all in a single row
    private DcMotor blDrive = null;
    private DcMotor brDrive = null;
    private DcMotor flDrive = null;
    private DcMotor frDrive = null;

    private DcMotor intake = null; //Intake Motor Chain Drive

    //COmments

    private DcMotor[] motors; //array of motors

    DcMotorSimple.Direction motF = DcMotorSimple.Direction.FORWARD; //setting directions
    DcMotorSimple.Direction motR = DcMotorSimple.Direction.REVERSE; //i.e., motR is now the reverse direction
    Servo.Direction serR = Servo.Direction.REVERSE;
    Servo.Direction serF = Servo.Direction.FORWARD;


    public void init(HardwareMap hwdMap){
        this.motors = new DcMotor[] {blDrive,brDrive,flDrive,frDrive};

        this.blDrive = hwdMap.get(DcMotor.class, "blDrive");
        this.brDrive = hwdMap.get(DcMotor.class, "brDrive");
        this.flDrive = hwdMap.get(DcMotor.class, "flDrive");
        this.frDrive = hwdMap.get(DcMotor.class, "frDrive");

        this.intake = hwdMap.get(DcMotor.class, "intake");

//        this.blDrive.setDirection(motF);
//        this.brDrive.setDirection(motR);
//        this.flDrive.setDirection(motF);
//        this.frDrive.setDirection(motR);

        this.blDrive.setDirection(motF); //as declared before motF is forward motR is reverse
        this.brDrive.setDirection(motR);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motR);
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

    public void drive(double bothPow){ //override of drive(double, double)
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


        double nor = 0.0; //normal drive power

        double frontLeftPower = (drive + turn + strafe);
        double backLeftPower = (drive - turn + strafe);
        double frontRightPower = (drive - turn - strafe);
        double backRightPower = (drive + turn - strafe);

        double y = gp.left_stick_y; // Remember, this is reversed!
        double x = -gp.left_stick_x * 1.1;
        double rx = -gp.right_stick_x;
        //double deno = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1); //denominator

        double newflPower = y + x + rx;
        double newblPower = y - x + rx;
        double newfrPower = y - x - rx;
        double newbrPower = y + x - rx;

        if(Math.abs(backRightPower) > 1 || Math.abs(backLeftPower) > 1 ||
                Math.abs(frontRightPower) > 1 || Math.abs(frontLeftPower) > 1) {

            //sets normal speed to greatest magnitude of drive power level(to normalize all power)
            nor = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            nor = Math.max(Math.abs(frontRightPower), nor);
            nor = Math.max(Math.abs(backRightPower), nor);



            /*nor = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower),
                             Math.abs(frontRightPower), Math.abs(backRightPower),
                             nor); can we use this instead for lines 96-98
                             if any one motor has their magnitude of power go over 1,
                             the normal drive power will be set to the greatest power
                             of the 4 motors, or nor if nor is greater(in this version of the code nor
                             will never be greater)
             */

        }
        this.flDrive.setPower(newflPower);
        this.blDrive.setPower(newblPower);
        this.frDrive.setPower(newfrPower);
        this.brDrive.setPower(newbrPower);
        this.intake.setPower(intakePower(gp));


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
    public double intakePower(Gamepad gp){
        if(gp.left_bumper == true){
            return 1;
        }
        else{
            return 0;
        }
    }



}
