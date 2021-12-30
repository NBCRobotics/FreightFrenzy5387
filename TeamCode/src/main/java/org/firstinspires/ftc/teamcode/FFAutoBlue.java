package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvInternalCamera;

//Made by Andrew Hu

@Autonomous(name="FFAutoBlue", group="LinearOpMode")
@Disabled
//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    // Declare OpMode members.
    //fsadas
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

    OpenCvCamera cam;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

            int camID = hardwareMap.appContext.getResources()
                    .getIdentifier("camID", "id", hardwareMap.appContext.getPackageName());
            cam = OpenCvCameraFactory.getInstance()
                    .createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, camID);



            telemetry.addData("Status", "Runn Time: " + runtime.toString());
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
