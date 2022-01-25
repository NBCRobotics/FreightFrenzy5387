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
    private int max = 6700;
    private int min = 500;

    private boolean slideBusy = false;
    private boolean[] currentState = {false, false, false};
    private boolean[] lastState = {false, false, false};
    private long[] lastPressed = {0, 0, 0};

    private int stageTwo = -1*FieldMeasurements.getStageTwoHeight();
    private int stageOne = -1*FieldMeasurements.getStageOneHeight();
    private int stageThree = -1*FieldMeasurements.getStageThreeHeight();

    DcMotorSimple.Direction motF = DcMotorSimple.Direction.FORWARD;
    DcMotorSimple.Direction motR = DcMotorSimple.Direction.REVERSE;
    Servo.Direction serR = Servo.Direction.REVERSE;
    Servo.Direction serF = Servo.Direction.FORWARD;



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
        this.blDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.brDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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

        if(abs(backRightPower) > 1 || abs(backLeftPower) > 1 ||
                abs(frontRightPower) > 1 || abs(frontLeftPower) > 1) {

            //sets normal speed to greatest magnitude of drive power level(to normalize all power)
            nor = Math.max(abs(frontLeftPower), abs(backLeftPower));
            nor = Math.max(abs(frontRightPower), nor);
            nor = Math.max(abs(backRightPower), nor);

        }
        intake(gp2);
        linearPower(gp2);
        armPower(gp2);

        carouselPower(gp);
        int slowDown = gp.left_bumper ? 2 : 1;
        this.flDrive.setPower(frontLeftPower/slowDown);
        this.blDrive.setPower(backLeftPower/slowDown);
        this.frDrive.setPower(frontRightPower/slowDown);
        this.brDrive.setPower(backRightPower/slowDown);
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
        linearSlide.setPower(pow);
    }

    public void turnIntake(double pow){
        intake.setPower(pow);
    }

    public void turnIntake(){
        this.turnIntake(1);
    }

    public void brake(){
        this.drive(0.0);
    }

   // public void setArmPower(double pow) { arm.setPower(pow); }

    //Gamepad 1 Methods
    public void carouselPower(Gamepad gp){ //Blue - Left Trigger || Red - Right Trigger
        if(gp.right_trigger > 0)
            carousel.setPower(-(gp.right_trigger)/2);
        else if(gp.left_trigger > 0){
            carousel.setPower((gp.left_trigger)/2);
        }
        else
            carousel.setPower(zero);
    }


    //GamePad 2 Methods
    public void intake(Gamepad gp) { //neg is push out
        if (gp.right_stick_y != 0)
            intake.setPower(gp.right_stick_y);
        else
            intake.setPower(0);
    }

    public void armPower(Gamepad gp){
        if (gp.left_bumper)
            arm.setPosition(0.45);
        else if (gp.right_bumper)
            arm.setPosition(0.55);
        else
            arm.setPosition(0.5);
    }
    public void setArm(double pos){
        arm.setPosition(pos);

    }

    public void linearPower(Gamepad gp){
         //(*-1 bc up is down rn)
        double pow = 0;
        currentState[0] = gp.y;
        currentState[1] = gp.b;
        currentState[2] = gp.x;

        if (currentState[0] && !lastState[0]) {
            slideBusy = true;
            linearUpStageX(stageThree);
        } else if (currentState[1] && !lastState[1]) {
            slideBusy = true;
            linearUpStageX(stageTwo);
        } else if (currentState[2] && !lastState[2]) {
            slideBusy = true;
            linearUpStageX(stageOne);
        }

        for (int i = 0; i < 3; i++)
            lastState[i] = currentState[i];

        //Up - Power negative, since Encoder is negative1
        if (!slideBusy) {
            if (linearSlide.getCurrentPosition() >= max && gp.left_stick_y < 0)
                pow = 0;
            else if (linearSlide.getCurrentPosition() <= min && gp.left_stick_y > 0)
                pow = 0;
            else
                pow = (gp.left_stick_y);
            linearSlide.setPower(pow);
            //Left Stick has values from -1 - 1
            //DcMotor power is -1 - 1
        }
    }
    public void linearUpStageX(int dis){
        //we are at the right spot if:
        if(getSlideEncoder() >= dis-50 && getSlideEncoder() <= dis+50){
            setLinearPower(zero);
            slideBusy = false;
        }
        else if(getSlideEncoder() > max)  //uh oh!
            setLinearPower(0);
        else if(getSlideEncoder() < min)  //uh oh*2!
            setLinearPower(0);
        else if(getSlideEncoder() < dis-100)
            setLinearPower(-0.5); //go up!
        else
            setLinearPower(0.5);  //else, go down!
    }


    public boolean isMax(){
        if(getSlideEncoder() >= max){
            return true;
        }
        else{
            return false;
        }
    }
    public int getMax(){
        return max;
    }

    public boolean isMin(){
        if(getSlideEncoder() <= min){
            return true;
        }
        else{
            return false;
        }
    }
    public int getMin(){
        return min;
    }

    public double getSlideEncoder(){    //because rn the "max" and "min" are too counterintuitive
        return -1*linearSlide.getCurrentPosition();
    }


}
