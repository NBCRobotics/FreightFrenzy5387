package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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

        robot.drive(-0.5);
        doFor(100);
        robot.strafe(1);
        doFor(FieldMeasurements.TIME_FOR_TILE);
        robot.drive(-0.5);
        doFor(FieldMeasurements.TIME_FOR_TILE);
        robot.linearUpStageX(FieldMeasurements.getStageThreeHeight());
        robot.turnIntake(-0.5);
        sleep(500);
        robot.turnIntake(0);
        robot.linearUpStageX(-50);

        robot.drive(0.5);
        doFor(FieldMeasurements.TIME_FOR_TILE);
        robot.drive(-0.5, 0.5);







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
