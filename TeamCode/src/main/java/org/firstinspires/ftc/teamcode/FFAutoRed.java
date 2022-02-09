package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvViewport;
import org.openftc.easyopencv.OpenCvWebcam;

//Made by Andrew Hu

@Autonomous(name="FFAutoRed", group="LinearOpMode")
//@Disabled
//RED
    public class FFAutoRed extends LinearOpMode {
    FFRobot robot = new FFRobot();
    private ElapsedTime runtime = new ElapsedTime();

    final int MAXSLIDEHEIGHT = robot.getMax();
    final int MINSLIDEHEIGHT = robot.getMin();

    final int ticksPerRev = 1440;
    //0.2103 mm per tick
    //if ticksPerRev = 1440, 0.1635 mm per tick
    //609.6 millimeters per square
    //3728.44037 ticks per square
    final int carouselPos = 0;
    private int stage = 0;

    OpenCvInternalCamera cam;

    //OpenCvWebcam cam;
    @Override
    public void runOpMode() {
        robot.init(hardwareMap);


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);

        BarcodeDetector detector = new BarcodeDetector(telemetry);
        cam.setPipeline(detector);
        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cam.startStreaming(320, 240, OpenCvCameraRotation.SIDEWAYS_RIGHT);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error: ", errorCode + "Camera not started");
            }
        });




        waitForStart();
        runtime.reset();

        switch (detector.getLocation()) {
            case RIGHT:
                stage = 1;
                break;
            case MIDDLE:
                stage = 2;
                break;
            case LEFT:
                stage = 3;
                break;
            case UNKNOWN:
                stage = 2;
                break;
        }

        telemetry.addData("Status: ", "Autonomous Initialized");
        telemetry.addData("Status: ", "Stage is set to" + stage);
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
