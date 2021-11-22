package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//Made by Andrew Hu 11.15

@TeleOp(name="FFAutoRed", group="LinearOpMode")
//@Disabled
//RED
public class FFAutoRed extends LinearOpMode {
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

            //delivering the duck
            robot.drive(0.5);
            doFor(1000);


            sleep(400);
            robot.strafe(0.5); //Right
            doFor(2000);
            sleep(400);
            robot.setCarouselPower(0.5);
            doFor(2000);
            robot.setCarouselPower(0);

            //preload box completely on randomized level
            robot.strafe(-0.5);
            doFor(1500);
            sleep(400);
            robot.drive(0.5);
            doFor(750);
            sleep(400);
            /*

            if(duckPos == left) {
               robot.setLinearPower(0.1); //this changes. lol.
            doFor(300);
            }

            if(duckPos == middle) {
               robot.setLinearPower(0.2); //this changes. lol.
            doFor(300);
            }

            if(duckPos == right) {
               robot.setLinearPower(0.3); //this changes. lol.
            doFor(300);
            just use a switch function rithvik lol
            }

             */
            robot.setLinearPower(0.3); //this changes. lol.
            doFor(300);

            sleep(200);
            robot.setBasketAngle(0.2);


            robot.setLinearPower(-0.3);
            doFor(300);
            robot.setBasketAngle(0.25);
            robot.drive(-0.5,-1);
            doFor(1000);



            do {
                robot.drive(1);
                doFor(1000);
                robot.turnIntake();
            } while(runtime.time()<=25000);

            //return to station







            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", robot.getBackLeftPower(), robot.getBackRightPower());
            telemetry.update();
        }
    }

    public void doFor(long ms)
    {
        sleep(ms);
        robot.brake();
    }

    public void doFor()
    {
        this.doFor(1000);
    }
}
