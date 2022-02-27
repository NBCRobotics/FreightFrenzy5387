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

@Autonomous(name="Carousel and Park Blue", group="LinearOpMode")
//@Disabled
//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    //BLUE means the carousel is to the RIGHT of the robot
    //Drop at proper stage, go to carousel, park in blue square
    private ElapsedTime runtime = new ElapsedTime();
    int stage = 3;
    final int tickspertile = FieldMeasurements.getTicksPerTile();

    OpenCvCamera cam;

    @Override
    public void runOpMode() {
        waitForStart();
        runtime.reset();
        robot.init(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);

        robot.setArmPos(0.5);

        robot.setLinearPower(1);
        sleep(100);
        robot.setLinearPower(0);

        robot.driveTo(100);
        robot.strafe(-0.5);
        doFor(1200);
        robot.driveTo((tickspertile/2)-250);

        //raise up
        sleep(100);
        robot.setLinearPower(1);
        while (robot.getSlideEncoder() < FieldMeasurements.getStageHeight(stage)) {

        }
        sleep(100);
        robot.setLinearPower(0);
        robot.driveTo(100);
        //aligned with hub

        robot.turnIntake(0.65);
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

        robot.driveTo(-1*(tickspertile/2)+450);
        robot.strafe("right", 0.5);
        doFor(3000);
        robot.driveTo(-300);
        robot.setCarouselPower(-0.6);
        sleep(5000);
        robot.setCarouselPower(0);
        //at carousel

        robot.driveTo(tickspertile/2);
        robot.leftPow(0.5);
        robot.rightPow(-0.5);
        doFor(robot.timeForTurn(90));
        robot.strafe("right", 0.5);
        doFor(300);
        robot.driveTo(-600);


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
