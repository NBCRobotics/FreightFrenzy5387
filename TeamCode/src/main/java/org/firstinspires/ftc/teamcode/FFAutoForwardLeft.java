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


        robot.setLinearPower(-0.1);
        sleep(500);
        robot.setLinearPower(0);

            robot.drive(-0.5);//f
            doFor(1250);
            robot.strafe(-0.5);
            doFor(2000);




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
