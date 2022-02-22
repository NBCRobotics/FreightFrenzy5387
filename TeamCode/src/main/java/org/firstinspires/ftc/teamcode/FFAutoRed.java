package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OpenCV.BarcodeDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

//Made by Andrew Hu

@Autonomous(name="FFAutoRed", group="LinearOpMode")
//@Disabled
//RED
    public class FFAutoRed extends LinearOpMode {
    FFRobot robot = new FFRobot();
    private ElapsedTime runtime = new ElapsedTime();

    //RED means the carousel is on the LEFT

    final int MAXSLIDEHEIGHT = robot.getMax();
    final int MINSLIDEHEIGHT = robot.getMin();

    final int tickspertile = FieldMeasurements.getTicksPerTile();

    int stage;

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
                cam.startStreaming(240, 320, OpenCvCameraRotation.UPSIDE_DOWN);
            }

            @Override
            public void onError(int errorCode) {
                telemetry.addData("Error: ", errorCode + "Camera not started");
            }
        });


        waitForStart();
        runtime.reset();

        if (detector.getLocation() == BarcodeDetector.Location.RIGHT) {
            stage = 1;
        } else if (detector.getLocation() == BarcodeDetector.Location.MIDDLE) {
            stage = 2;
        } else if (detector.getLocation() == BarcodeDetector.Location.LEFT) {
            stage = 3;
        } else {
            stage = 2;
        }

        telemetry.addData("Status: ", "Autonomous Initialized");
        telemetry.addData("Status: ", "Stage is set to" + stage);
        telemetry.update();



    }

    public void doFor(long ms) {
        sleep(ms);
        robot.brake();
    }

    public void doFor() {
        this.doFor(1000);
    }

}


