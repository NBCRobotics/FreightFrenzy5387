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

@Autonomous(name="FFAutoBlue", group="LinearOpMode")
//@Disabled
//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    //BLUE means the carousel is to the RIGHT of the robot
    private ElapsedTime runtime = new ElapsedTime();

    final int MAXSLIDEHEIGHT = robot.getMax();
    final int MINSLIDEHEIGHT = robot.getMin();

    final int tickspertile = FieldMeasurements.getTicksPerTile();

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
                cam.startStreaming(320, 240, OpenCvCameraRotation.UPSIDE_DOWN);
            }
            @Override
            public void onError(int errorCode) {

            }
        });

        waitForStart();
        runtime.reset();


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
