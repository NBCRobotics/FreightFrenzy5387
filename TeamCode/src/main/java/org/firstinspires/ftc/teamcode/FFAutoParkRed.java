package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name="FFAutoParkRed", group="LinearOpMode")
public class FFAutoParkRed extends LinearOpMode {
    FFRobot robot = new FFRobot();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);


        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {

//            robot.drive(0.5);
//            doFor(500);
//            robot.strafe(0.5);
//            doFor(1900);
//            robot.setCarouselPower(1);
//            doFor(2000);
//            robot.drive(0.5);
//            doFor(1500);
//            robot.strafe(0.5);
//            robot.drive(-0.50);
//            doFor(500);
            robot.drive(-0.5);
            doFor(3000);
            telemetry.addData("Status: ", "Autonomous Terminalized");
            telemetry.update();
            sleep(27000);


        }
        robot.brake();
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