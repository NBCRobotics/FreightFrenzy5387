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

@Autonomous(name = "FFAutoRedCarousel", group = "Carousel")
public class FFAutoRedCarousel extends LinearOpMode{
    FFRobot robot = new FFRobot();

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robot.init(hardwareMap);

        waitForStart();
        runtime.reset();

        robot.strafe(-1);
        doFor(200);

        robot.drive(0.5);
        doFor(FieldMeasurements.TIME_FOR_TILE);
        //cant turn if backed up against wall



        //robot.drive(0.5); //backwards actually
        //doFor(FieldMeasurements.TIME_FOR_TILE);


        robot.setCarouselPower(0.5);
        sleep(6000);
        robot.setCarouselPower(0);

        robot.strafe(-1); //left
        doFor(FieldMeasurements.TIME_FOR_TILE);
        //done
        robot.drive(0.5);
        doFor(300);

        telemetry.addData("Status: ", "Autonomous Finished");
        telemetry.update();

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
