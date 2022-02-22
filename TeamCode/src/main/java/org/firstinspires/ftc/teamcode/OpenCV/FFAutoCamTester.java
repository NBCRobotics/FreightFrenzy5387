package org.firstinspires.ftc.teamcode.OpenCV;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FFRobot;
import org.firstinspires.ftc.teamcode.FieldMeasurements;
import org.firstinspires.ftc.teamcode.OpenCV.BarcodeDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;

@Autonomous(name="CamTester", group="LinearOpMode")
public class FFAutoCamTester extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();

    int stage = -1;
    OpenCvCamera cam;
    FFRobot robot = new FFRobot();

    @Override
    public void runOpMode() throws InterruptedException {
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



        if (detector.getLocation() == BarcodeDetector.Location.RIGHT) {
            stage = 1;
        } else if (detector.getLocation() == BarcodeDetector.Location.MIDDLE) {
            stage = 2;
        } else if (detector.getLocation() == BarcodeDetector.Location.LEFT) {
            stage = 3;
        } else {
            stage = 2;
        }



        waitForStart();
        runtime.reset();

        telemetry.addData("Stage set at: ", stage);
        telemetry.update();

        switch(stage) {
            case 1:
                while (robot.getSlideEncoder() < FieldMeasurements.getStageOneHeight()) {
                    robot.setLinearPower(1);
                }
                robot.setLinearPower(0);
                break;
            case 2:
                while (robot.getSlideEncoder() < FieldMeasurements.getStageTwoHeight()) {
                    robot.setLinearPower(1);
                }
                robot.setLinearPower(0);
            case 3:
                while (robot.getSlideEncoder() < FieldMeasurements.getStageThreeHeight()) {
                    robot.setLinearPower(1);
                }
                robot.setLinearPower(0);
        }


    }
}
