package org.firstinspires.ftc.teamcode.EncodersAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.FFRobot;
import org.firstinspires.ftc.teamcode.FieldMeasurements;
import org.firstinspires.ftc.teamcode.OpenCV.BarcodeDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="Hub and Park Red", group="LinearOpMode")
public class FFAutoWarehouseR extends LinearOpMode {
    FFRobot robot = new FFRobot();
    final int tickspertile = FieldMeasurements.getTicksPerTile();
    //OpenCvCamera cam;
    private ElapsedTime runtime = new ElapsedTime();
    int stage = 3;

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);

//        int camID = hardwareMap.appContext.getResources()
//                .getIdentifier("camID", "id", hardwareMap.appContext.getPackageName());
//        cam = OpenCvCameraFactory.getInstance()
//                .createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, camID);
//
//        BarcodeDetector detector = new BarcodeDetector(telemetry); //barcode
//        cam.setPipeline(detector);
//        cam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
//            @Override
//            public void onOpened() {
//                cam.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN);
//            }
//            @Override
//            public void onError(int errorCode) {
//
//            }
//        });

//        if (detector.getLocation() == BarcodeDetector.Location.RIGHT) {
//            stage = 1;
//        } else if (detector.getLocation() == BarcodeDetector.Location.MIDDLE) {
//            stage = 2;
//        } else if (detector.getLocation() == BarcodeDetector.Location.LEFT) {
//            stage = 3;
//        } else {
//            stage = 3;
//        }

        waitForStart();
        runtime.reset();

        robot.setLinearPower(1);
        sleep(100);
        robot.setLinearPower(0);

        robot.driveTo(100);
        robot.strafe(-0.5);
        doFor(1200);
        robot.driveTo((tickspertile / 2) - 250);
        robot.brake();

        sleep(100);
        robot.setLinearPower(1);
        while (robot.getSlideEncoder() < FieldMeasurements.getStageHeight(stage)) {

        }
        sleep(100);
        robot.setLinearPower(0);

        robot.driveTo(100);
        //aligned with hub

        robot.turnIntake(0.7);
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
        //off the hub


        //turns and goes into warehouse
        robot.leftPow(-0.5);
        robot.rightPow(0.5);
        doFor(robot.timeForTurn(90));
        robot.strafe(0.5);
        doFor(1850);
        robot.driveTo(3000);


    }

    public void doFor(long ms) {
        if(opModeIsActive()) {
            sleep(ms);
            robot.brake();
        }
        else {

        }
    }

    public void doFor() {
        this.doFor(1000);

    }
}