package org.firstinspires.ftc.teamcode.SimpleAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.FFRobot;
import org.firstinspires.ftc.teamcode.FieldMeasurements;

import java.lang.reflect.Field;



@Autonomous(name="FFAutoHubParkR", group="LinearOpMode")
public class FFAutoHubParkR extends LinearOpMode {
    FFRobot robot = new FFRobot();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);


        waitForStart();
        runtime.reset();

        robot.setArmPos(0.5);
        robot.drive(-0.5);
        doFor(100);
        robot.strafe(1);
        doFor(FieldMeasurements.TIME_FOR_TILE);
        robot.drive(-0.5);
        doFor(FieldMeasurements.TIME_FOR_TILE);

        robot.setLinearPower(-1);
        sleep(2550);
        robot.setLinearPower(0);
        robot.turnIntake(1);
        sleep(1000);
        robot.turnIntake(0);


        robot.drive(0.5);
        doFor(FieldMeasurements.TIME_FOR_TILE);
        robot.drive(1, -1);
        doFor(FieldMeasurements.TIME_FOR_TILE);

        robot.drive(0.5); //going backwards now
        doFor(2000);
        robot.setCarouselPower(0.7);
        sleep(1000);
        robot.strafe("left",0.5);
        doFor(FieldMeasurements.TIME_FOR_TILE);

    }

    public void doFor(long ms) {
        if (opModeIsActive()) {
            sleep(ms);
            robot.brake();
        } else {

        }
    }

    public void doFor() {
        this.doFor(1000);
    }


}
