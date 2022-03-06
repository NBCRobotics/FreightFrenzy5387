package org.firstinspires.ftc.teamcode.EncodersAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FFRobot;
import org.firstinspires.ftc.teamcode.OpenCV.BarcodeDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name = "Auto Cam Tester", group = "Autonomous")
@Disabled
public class FFAutoCamTester2 extends OpMode {
    FFRobot robot = new FFRobot();
    OpenCvCamera cam;
    int stage = 3;
    BarcodeDetector detector = new BarcodeDetector(telemetry); //barcode
    @Override
    public void init() {
        robot.init(hardwareMap);
        int camID = hardwareMap.appContext.getResources()
                .getIdentifier("camID", "id", hardwareMap.appContext.getPackageName());
        cam = OpenCvCameraFactory.getInstance()
                .createInternalCamera(OpenCvInternalCamera.CameraDirection.FRONT, camID);


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
    }

    public void loop() {
        if (detector.getLocation() == BarcodeDetector.Location.RIGHT) {
            stage = 1;
        } else if (detector.getLocation() == BarcodeDetector.Location.MIDDLE) {
            stage = 2;
        } else if (detector.getLocation() == BarcodeDetector.Location.LEFT) {
            stage = 3;
        } else {
            stage = 3;
        }
        robot.driveTo(500);
    }
}
