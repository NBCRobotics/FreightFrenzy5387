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
    private DcMotor intake = null;

    private DcMotor linearSlide = null;//Tesrng btiyuc
    private DcMotor carousel = null;
    private Servo basket = null;


    private int zero = 0;
    private int max = 1200;
    private int min = 0;
    //COmment

    private DcMotor[] motors; //array of motors

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


        this.blDrive.setDirection(motF); //as declared before motF is forward motR is reverse
        this.brDrive.setDirection(motR);
        this.flDrive.setDirection(motF);
        this.frDrive.setDirection(motR);
        this.intake.setDirection(motR);
        this.linearSlide.setDirection(motR);

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
        blDrive.setPower(-pow);
        flDrive.setPower(pow);
        brDrive.setPower(pow);
        frDrive.setPower(-pow);
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
        setBasket(gp2);
//        this.flDrive.setPower(newflPower);
//        this.blDrive.setPower(newblPower);
//        this.frDrive.setPower(newfrPower);
//        this.brDrive.setPower(newbrPower);
        //this.intake.setPower(gp.left_stick_y);


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



    public double getIntakePower(){return intake.getPower();}
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
        if (gp.left_bumper){
            intake.setPower(1);
        }
        else
            intake.setPower(0);

    }
    public void linearPower(Gamepad gp){
         //(*-1 bc up is down rn)
        double pow = 0;
        if(linearSlide.getCurrentPosition() <= min){ //If the current position is all the way down
            if(gp.left_stick_y < 0){ // If I make the joystick point down
                pow = 0;
                linearSlide.setPower(pow); //Just set it to 0 beacuse I dont want to go more down
            }
        }
        else if(linearSlide.getCurrentPosition() >= max){ //If current pos is at the max
            if(gp.left_stick_y > 0){ //And I move joystick up more
                pow = 0;
                linearSlide.setPower(pow); //Set pow to 0 because I dont want to go to max
            }
        }
        else{
            pow = (gp.left_stick_y)/3.0;
            linearSlide.setPower(pow);
        }
         //Left Stick has values from -1 - 1
                                                //DcMotor power is -1 - 1
    }
    public boolean isMax(){
        if(linearSlide.getCurrentPosition() >= max){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isMin(){
        if(linearSlide.getCurrentPosition() <= min){
            return true;
        }
        else{
            return false;
        }
    }

    public double getSlideEncoder(){
        return linearSlide.getCurrentPosition();
    }
    public void carouselPower(Gamepad gp){ //Blue - Right Trigger || Red - Left Trigger
        if(gp.right_trigger > 0)
            carousel.setPower(gp.right_trigger);
        else if(gp.left_trigger > 0){
            carousel.setPower(-(gp.left_trigger));
        }
        else
            carousel.setPower(zero);
    }

    public void setBasket(Gamepad gp){
        double pos = 0.0;
        while (Math.abs(gp.left_stick_x) > 0)
        {
            if (gp.left_stick_x > 0){
                pos += 0.1;

            } else if (gp.left_stick_y > 0){
                pos -= 0.1;
            }
            basket.setPosition(pos);
        }


    }


}
