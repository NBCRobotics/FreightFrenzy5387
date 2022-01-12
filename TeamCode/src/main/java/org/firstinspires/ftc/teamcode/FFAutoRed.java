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
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvViewport;

//Made by Andrew Hu

@Autonomous(name="FFAutoRed", group="LinearOpMode")
@Disabled
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
    private int setStage = 0;

    OpenCvCamera cam;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);

        int camID = hardwareMap.appContext.getResources()
                .getIdentifier("camID", "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, camID);

        BarcodeDetector detector = new BarcodeDetector(telemetry); //barcode
        cam.setPipeline(detector);
        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cam.startStreaming(1280, 720, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {

            }
        });

        waitForStart();
        runtime.reset();

        switch (detector.getLocation()) {
            case RIGHT:
                setStage = 1;
                break;
            case MIDDLE:
                setStage = 2;
                break;
            case LEFT:
                setStage = 3;
                break;
            case UNKNOWN:
                setStage = 2;
                break;
        }


        raiseAndDrop(setStage, 0.7);


        telemetry.addData("Status: ", "Autonomous Terminalized");
        telemetry.update();
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


    public void basketToHeight(double pow, int height)
    {

    }

    public void raiseAndDrop(int stage, double pow)
    {
        double currentHeight = robot.getSlideEncoder(); //assuming it starts at ground level remember neg is up
        double targetHeight = 0; //where the linear slide wants to go

        switch(stage) {
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
