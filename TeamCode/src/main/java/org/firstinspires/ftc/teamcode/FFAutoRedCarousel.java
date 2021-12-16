package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

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

        robot.drive(-0.5);
        doFor();
        //cant turn if backed up against wall

        robot.drive(1,-1);
        doFor(); //should be a 90 degree right turn

        //robot.drive(0.5); //backwards actually
        //doFor(FieldMeasurements.TIME_FOR_TILE);


        robot.setCarouselPower(0.5);
        sleep(3000);
        robot.setCarouselPower(0);

        robot.strafe(-1); //left
        doFor(FieldMeasurements.TIME_FOR_TILE);
        //done

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
