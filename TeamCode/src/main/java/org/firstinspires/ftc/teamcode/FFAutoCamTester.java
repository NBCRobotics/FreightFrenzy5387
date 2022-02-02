package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="CamTester", group="LinearOpMode")
public class FFAutoCamTester extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    int stage;
    OpenCvCamera cam;

    @Override
    public void runOpMode() throws InterruptedException {
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
    }
}
