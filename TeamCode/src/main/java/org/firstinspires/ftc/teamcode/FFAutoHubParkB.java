package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.lang.reflect.Field;

//DIDNT FINISH OOPS MY BAD
@Autonomous(name="FFAutoHubParkB", group="LinearOpMode")
public class FFAutoHubParkB extends LinearOpMode {
    FFRobot robot = new FFRobot();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);


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
