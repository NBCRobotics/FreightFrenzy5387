package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//Made by Andrew Hu

@Autonomous(name="FFAutoBlue", group="LinearOpMode")
//@Disabled
//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    final int MAXSLIDEHEIGHT = robot.getMax();
    final int MINSLIDEHEIGHT = robot.getMin();

    DcMotor blDrive = robot.getBlDrive();
    DcMotor brDrive = robot.getBrDrive();

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
        robot.init(hardwareMap);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            //carousel
            robot.drive(0.5);
            doFor(500);
            sleep(400);
            robot.strafe(0.5); //right
            doFor(2000);
            sleep(400);
            robot.setCarouselPower(-0.5);
            doFor(2000);
            robot.setCarouselPower(0);

            //pre load box on specified level
            robot.strafe(-0.5);
            doFor(1750);
            sleep(400);
            robot.drive(0.5);
            doFor(750);


            robot.drive(-1,-0.5);   //ok this is kinda random rn
            doFor(1000);                   //doesn't guarantee that the robot will be fully in the parking spot





            /*do {
                robot.drive(1);
                doFor(1000);
                robot.turnIntake();
            } while(runtime.time()<=25000);

             */
            //return to station

            robot.drive(-0.6);
            robot.strafe(0.4);
            doFor(1000);

            //with encoders

//            driveDistance(0.5, 300);
//            strafeDistance(0.5, 3728);

            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            break;
        }
    }

    public void doFor(long ms) {
        sleep(ms);
        robot.brake();
    }

    public void doFor()
    {
        this.doFor(400);
    }

    public void driveDistance(double pow, int dis)
    {
        this.blDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.brDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //after resetting, set runmode to to pos
        this.blDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.brDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        this.blDrive.setTargetPosition(-dis);   //the strafe method sets the power of bl to negtative
        this.brDrive.setTargetPosition(dis);


        //two of the pos is needed for the others the motors will "travel" the same amount
        //then based on the time and power needed to travel to that position, set all other motors

        robot.drive(pow);
        while (blDrive.isBusy() && brDrive.isBusy())
        {

        }
        this.blDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.brDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.brake();
    }

    public void strafeDistance(double pow, int dis)
    {
        this.blDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.brDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //after resetting, set runmode to to pos
        this.blDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.brDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);


        this.blDrive.setTargetPosition(-dis);   //the strafe method sets the power of bl to negtative
        this.brDrive.setTargetPosition(dis);
        //two of the pos is needed of the motors is needed all of them will "travel" the same amount
        //then based on the time and power needed to travel to that position, set all other motors

        robot.strafe(pow);
        while (blDrive.isBusy() && brDrive.isBusy())
        {

        }

        robot.brake();
        this.blDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.brDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //i totally did not copy and paste the driveDistance method
    }

    public int determineLevel() {
        int level = 0;
        return level;
    }

    public void raiseAndDrop(int level, double pow) //using the encoder for dropping freight onto the tower
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
                while (currentHeight < MINSLIDEHEIGHT)   //we dont want it to break obv
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
                    robot.setLinearPower(-pow);     //negative is up
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
        }


    }
}
