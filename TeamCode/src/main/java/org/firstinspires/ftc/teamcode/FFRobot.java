package org.firstinspires.ftc.teamcode;

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
    private Servo basket = null;


    private int zero = 0;
    private int max = -6700;
    private int min = 0;

    private int stageTwo = FieldMeasurements.getStageTwoHeight();
    private int stageOne = FieldMeasurements.getStageOneHeight();
    private int stageThree = FieldMeasurements.getStageThreeHeight();
    //COmment



    DcMotorSimple.Direction motF = DcMotorSimple.Direction.FORWARD; //setting directions
    DcMotorSimple.Direction motR = DcMotorSimple.Direction.REVERSE; //i.e., motR is now the reverse direction
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
        this.basket = hwdMap.get(Servo.class, "basket");

        //intake.resetDeviceConfigurationForOpMode();


        this.blDrive.setDirection(motF); //as declared before motF is forward motR is reverse
        this.brDrive.setDirection(motR);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motR);

        this.linearSlide.setDirection(motR);

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

    public DcMotor getBlDrive(){
        return blDrive;
    }

    public DcMotor getBrDrive(){
        return brDrive;
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

        if(Math.abs(backRightPower) > 1 || Math.abs(backLeftPower) > 1 ||
                Math.abs(frontRightPower) > 1 || Math.abs(frontLeftPower) > 1) {

            //sets normal speed to greatest magnitude of drive power level(to normalize all power)
            nor = Math.max(Math.abs(frontLeftPower), Math.abs(backLeftPower));
            nor = Math.max(Math.abs(frontRightPower), nor);
            nor = Math.max(Math.abs(backRightPower), nor);





        }
        intake(gp2);
        linearPower(gp2);
        carouselPower(gp2);
        linearUpStageTwo(stageTwo, gp2);
        linearUpStageThree(stageThree, gp2);


        this.flDrive.setPower(frontLeftPower/(gp.left_trigger+1));
        this.blDrive.setPower(backLeftPower/(gp.left_trigger+1));
        this.frDrive.setPower(frontRightPower/(gp.left_trigger+1));
        this.brDrive.setPower(backRightPower/(gp.left_trigger+1));

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

    public void setBasketAngle(double angle){
        basket.setPosition(angle);
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




    //GamePad 2 Methods
    public void intake(Gamepad gp){
        if (gp.right_stick_y > 0)
            intake.setPower(gp.right_stick_y);
        else if (gp.right_stick_y < 0)
            intake.setPower(gp.right_stick_y);
        else
            intake.setPower(0);

    }
    public void linearPower(Gamepad gp){
         //(*-1 bc up is down rn)
        double pow = 0;
        if (gp.y) {
            setLinearLevel(linearSlide.getCurrentPosition(), stageThree);
        }
        else if (gp.b) {
            setLinearLevel(linearSlide.getCurrentPosition(), stageTwo);
        }
        else if (gp.a) {
            setLinearLevel(linearSlide.getCurrentPosition(), stageOne);
        }

        //Up - Power negative Encoder is negative
        if(linearSlide.getCurrentPosition() <= max && gp.left_stick_y < 0)//NEGATIVE IS UP
            pow = 0;
        else if(linearSlide.getCurrentPosition() >= min && gp.left_stick_y > 0)//POS IS DOWN
            pow = 0;
        else
            pow = (gp.left_stick_y);
        linearSlide.setPower(pow);
         //Left Stick has values from -1 - 1
                                                //DcMotor power is -1 - 1
    }

    public void setLinearLevel(double pos, double target) {
        if (pos >= target-50 && pos <= target-50) {
            //do nothing, its in range;
            linearSlide.setPower(0.0);
        }
        else if (pos < target) {
            linearSlide.setPower(0.5);
        }
        else if (pos > target) {
            linearSlide.setPower(-0.5);
        }
    }

    public boolean isMax(){
        if(linearSlide.getCurrentPosition() <= max){
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
        if(linearSlide.getCurrentPosition() >= min){
            return true;
        }
        else{
            return false;
        }
    }
    public int getMin(){
        return min;
    }

    public double getSlideEncoder(){
        return linearSlide.getCurrentPosition();
    }

    public void carouselPower(Gamepad gp){ //Blue - Right Trigger || Red - Left Trigger
        if(gp.right_trigger > 0)
            carousel.setPower(-(gp.right_trigger));
        else if(gp.left_trigger > 0){
            carousel.setPower((gp.left_trigger));
        }
        else
            carousel.setPower(zero);
    }

    public void linearUpStageTwo(int dis, Gamepad gp){
        if(gp.b){
            linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlide.setTargetPosition(dis);
            if(getSlideEncoder() < dis)
                setLinearPower(-0.2);
            else
                setLinearPower(0.2);
        }
        if(getSlideEncoder() == dis){
            setLinearPower(0.0);
        }
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void linearUpStageThree(int dis, Gamepad gp) {
        if (gp.y) {
            linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlide.setTargetPosition(dis);
            if (getSlideEncoder() < dis)
                setLinearPower(-0.4);
            else
                setLinearPower(0.4);
        }
        if (getSlideEncoder() == dis) {
            setLinearPower(0.0);
        }
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void linearUpStageOne(int dis, Gamepad gp) {
        if (gp.a) {
            linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            linearSlide.setTargetPosition(dis);
            if (getSlideEncoder() < dis)
                setLinearPower(-0.4);
            else
                setLinearPower(0.4);
        }
        if (getSlideEncoder() == dis) {
            setLinearPower(0.0);
        }
        linearSlide.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}
