package org.firstinspires.ftc.teamcode.EncodersAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.FFRobot;
import org.firstinspires.ftc.teamcode.FieldMeasurements;
import org.firstinspires.ftc.teamcode.OpenCV.BarcodeDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

//Made by Andrew Hu

@Autonomous(name="FFAutoBlue", group="LinearOpMode")
//@Disabled
//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    //BLUE means the carousel is to the RIGHT of the robot
    //Drop at proper stage, go to carousel, park in blue square
    private ElapsedTime runtime = new ElapsedTime();
    int stage;
    final int tickspertile = FieldMeasurements.getTicksPerTile();

    OpenCvCamera cam;

    @Override
    public void runOpMode() {
        int camID = hardwareMap.appContext.getResources()
                .getIdentifier("camID", "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, camID);

        BarcodeDetector detector = new BarcodeDetector(telemetry); //barcode
        cam.setPipeline(detector);
        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                cam.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN);
            }
            @Override
            public void onError(int errorCode) {

            }
        });

        if (detector.getLocation() == BarcodeDetector.Location.RIGHT) {
            stage = 1;
        } else if (detector.getLocation() == BarcodeDetector.Location.MIDDLE) {
            stage = 2;
        } else if (detector.getLocation() == BarcodeDetector.Location.LEFT) {
            stage = 3;
        } else {
            stage = 3;
        }

        waitForStart();
        runtime.reset();

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);

        robot.driveTo(100);
        robot.strafe(-0.5);
        doFor(700);
        robot.driveTo((tickspertile/2)-200);


        sleep(100);
        robot.setLinearPower(1);
        while (robot.getSlideEncoder() < FieldMeasurements.getStageHeight(stage)) {

        }
        sleep(100);
        robot.setLinearPower(0);

        robot.driveTo(100);
        //aligned with hub
        robot.turnIntake(1);
        sleep(2000);
        robot.turnIntake(0);

        robot.driveTo(-200);
        sleep(100);
        robot.setLinearPower(-1);
        while(robot.getSlideEncoder() > robot.getMin()) {

        }
        sleep(100);
        robot.setLinearPower(0);
        robot.brake();
        //slightly off the hub


        //at carousel

        //parked
        telemetry.addData("Status: ", "Terminated");


    }

    public void doFor(long ms) {
        sleep(ms);
        robot.brake();
    }

    public void doFor()
    {
        this.doFor(400);
    }



}
