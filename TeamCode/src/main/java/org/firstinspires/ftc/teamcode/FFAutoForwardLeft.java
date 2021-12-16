package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="FFAutoForwardLeft", group="LinearOpMode")
public class FFAutoForwardLeft extends LinearOpMode {
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
        doFor(FieldMeasurements.TIME_FOR_TILE);
        //
        robot.strafe(-1);
        doFor(FieldMeasurements.TIME_FOR_TILE);

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
