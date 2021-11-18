package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//Made by Andrew Hu 11.15

@TeleOp(name="FFAutoCarouselA", group="LinearOpMode")
//@Disabled

//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            robot.drive(0.5);
            //robot.sleep(100);
            robot.brake();
            robot.strafe(-0.5); //left
            //robot.sleep(2000);
            robot.brake();
            robot.setCarouselPower(-0.5);
            //robot.sleep(1000);
            robot.setCarouselPower(0);




            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", robot.getBackLeftPower(), robot.getBackRightPower());
            telemetry.update();
        }
    }
}
