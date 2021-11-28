package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//Made by Andrew Hu

@Autonomous(name="FFAutoRed", group="LinearOpMode")
//@Disabled
//RED
    public class FFAutoRed extends LinearOpMode {
    FFRobot robot = new FFRobot();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    final int MAXSLIDEHEIGHT = robot.getMax();
    final int MINSLIDEHEIGHT = robot.getMin();

    DcMotor blDrive = null;
    DcMotor brDrive = null;

    final int ticksPerRev = 1440;
    //0.2103 mm per tick
    //if ticksPerRev = 1440, 0.1635 mm per tick
    //609.6 millimeters per square
    //3728.44037 ticks per square

    final int carouselPos = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();



        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Without encoders
            //delivering the duck
            robot.drive(0.5);
            doFor(400);
            robot.strafe(-0.5);
            doFor(2000);
            robot.drive(0.5,-0.5);
            doFor(1000);
            robot.setCarouselPower(0.7);
            doFor(2000);
            robot.setCarouselPower(0);

            //preload box completely on randomized level
            robot.strafe(-0.5);
            doFor(1500);
            sleep(400);
            robot.drive(0.5);
            doFor(750);
            sleep(400);
//
//
//            raiseAndDrop(2, 0.5);
//
//            robot.setBasketAngle(0.25);
            robot.drive(-0.5,-1);
            doFor(1000);
//            /*do {
//                robot.drive(1);
//                doFor(1000);
//                robot.turnIntake();
//                robot.drive(-1);
//                doFor(1000);
//                robot.strafe(-.5);
//                doFor(300);
//                robot.drive(0.5,-0.5);
//                doFor(500);
//                robot.setLinearPower(0.3); //this changes. lol.
//                sleep(300);
//                robot.setLinearPower(0);
//
//                sleep(200);
//                robot.setBasketAngle(0.2);
//
//
//                robot.setLinearPower(-0.3);
//                sleep(300);
//                robot.setLinearPower(0);
//                robot.setBasketAngle(0.25);
//                robot.drive(-0.5,-1);
//                doFor(500);
//
//            } while(runtime.time()<=25000);
//            */
//            //return to storage unit
//
            robot.drive(-0.6);
            robot.strafe(-0.4);
            doFor(1000);

            //With encoders

//            driveDistance(0.5, 300);
//            strafeDistance(0.5, 3728);
//            robot.setCarouselPower(0.9);
//            sleep(2000);
//            robot.setCarouselPower(0);
//
//            strafeDistance(0.5, 7457);
//            driveDistance(0.5, 5592);
//
//            raiseAndDrop(2, 0.5);


            //idk if we will use time but yea here
            telemetry.addData("Status: ", "Autonomous Terminalized");
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", robot.getBackLeftPower(), robot.getBackRightPower());
            telemetry.update();
        }
    }

    public void doFor(long ms)
    {
        sleep(ms);
        robot.brake();
    }

    public void doFor()
    {
        this.doFor(1000);
    }

    public void driveDistance(double pow, int dis) //drive some distance with encoders
    {
        blDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //after resetting, set runmode to to pos
        blDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);



        blDrive.setTargetPosition(-dis);   //the strafe method sets the power of bl to negtative
        brDrive.setTargetPosition(dis);


        //two of the pos is needed for the others the motors will "travel" the same amount
        //then based on the time and power needed to travel to that position, set all other motors

        robot.drive(pow);
        while (blDrive.isBusy() && brDrive.isBusy())
        {

        }

        robot.brake();
        blDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        brDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void strafeDistance(double pow, int dis)
    {
        blDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        brDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //after resetting, set runmode to to pos
        blDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        brDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        blDrive.setTargetPosition(-dis);   //the strafe method sets the power of bl to negtative
        brDrive.setTargetPosition(dis);
        //two of the pos is needed of the motors is needed all of them will "travel" the same amount
        //then based on the time and power needed to travel to that position, set all other motors

        robot.strafe(pow);
        while (blDrive.isBusy() && brDrive.isBusy())
        {

        }

        robot.brake();
        blDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        brDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //i totally did not copy and paste the driveDistance method
    }

    public void turnAngle(int angle)
    {

    }

    public int determineLevel()
    {
        int level = 0;
        return level;
    }

    public void basketToHeight(double pow, int height)
    {

    }

    public void raiseAndDrop(int level, double pow)
    {
        double currentHeight = robot.getSlideEncoder(); //assuming it starts at ground level
        double targetHeight = 0; //where the linear slide wants to go

        switch(level) {
            case 1:
                targetHeight = MAXSLIDEHEIGHT / 5.0;
                while (currentHeight > targetHeight) {
                    robot.setLinearPower(-pow);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                sleep(500);
                robot.turnIntake(-1);
                sleep(500);
                robot.turnIntake(0);
                while (currentHeight < MINSLIDEHEIGHT)
                {
                    robot.setLinearPower(pow);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                break;
            case 2:
                targetHeight = MAXSLIDEHEIGHT / 2.0;
                while (currentHeight > targetHeight) {
                    robot.setLinearPower(-pow);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                sleep(500);
                robot.turnIntake(-1);
                sleep(500);
                robot.turnIntake(0);
                while (currentHeight < MINSLIDEHEIGHT)
                {
                    robot.setLinearPower(pow);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                break;
            case 3:
                targetHeight = MAXSLIDEHEIGHT;
                while (currentHeight > targetHeight) {
                    robot.setLinearPower(pow);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                sleep(500);
                robot.turnIntake(-1);
                sleep(500);
                robot.turnIntake(0);
                while (currentHeight < MINSLIDEHEIGHT)
                {
                    robot.setLinearPower(-pow);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                break;
        }


    }
}
