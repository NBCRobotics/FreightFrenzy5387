package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class FFAutoParkBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            robot.drive(0.5);
            doFor(2000);
            robot.strafe(0.75);
            doFor(3000);

        }
    }

    public void doFor(long ms) {
        sleep(ms);
        robot.brake();
    }

    public void doFor() {
        this.doFor(1000);

    }
}
