package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//Made by Andrew Hu 11.15

@TeleOp(name="FFAutoBlue", group="LinearOpMode")
//@Disabled

//RED
public class FFAutoBlue extends LinearOpMode {
    FFRobot robot = new FFRobot();
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    final int MAXSLIDEHEIGHT = robot.getMax();
    final int MINSLIDEHEIGHT = robot.getMin();

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();



        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //carousel
            robot.drive(0.5);
            doFor(500);
            sleep(400);
            robot.strafe(-0.5); //left
            doFor(2000);
            sleep(400);
            robot.setCarouselPower(-0.5);
            doFor(2000);
            robot.setCarouselPower(0);

            //pre load box on specified level
            robot.strafe(0.5);
            doFor(1500);
            sleep(400);
            robot.drive(0.5);
            doFor(750);

            raiseAndDrop(determineLevel());

            robot.drive(-1,-0.5);
            doFor(1000);



            /*do {
                robot.drive(1);
                doFor(1000);
                robot.turnIntake();
            } while(runtime.time()<=25000);

             */
            //return to station

            robot.drive(-0.6);
            robot.strafe(0.4);
            doFor(1000);

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
        this.doFor(400);
    }

    public int determineLevel()
    {
        int level = 0;
        return level;
    }

    public void raiseAndDrop(int level)
    {
        double currentHeight = robot.getSlideEncoder(); //assuming it starts at ground level
        double targetHeight = 0; //where the linear slide wants to go
        switch(level) {
            case 1:
                targetHeight = MAXSLIDEHEIGHT / 5.0;
                while (currentHeight < targetHeight) {
                    robot.setLinearPower(-0.5);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                sleep(500);
                robot.turnIntake(-1);
                sleep(500);
                robot.turnIntake(0);
                while (currentHeight > MINSLIDEHEIGHT)
                {
                    robot.setLinearPower(0.5);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                break;
            case 2:
                targetHeight = MAXSLIDEHEIGHT / 2.0;
                while (currentHeight < targetHeight) {
                    robot.setLinearPower(0.5);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                sleep(500);
                robot.turnIntake(-1);
                sleep(500);
                robot.turnIntake(0);
                while (currentHeight > MINSLIDEHEIGHT)
                {
                    robot.setLinearPower(0.5);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                break;
            case 3:
                targetHeight = MAXSLIDEHEIGHT;
                while (currentHeight < targetHeight) {
                    robot.setLinearPower(0.5);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                sleep(500);
                robot.turnIntake(-1);
                sleep(500);
                robot.turnIntake(0);
                while (currentHeight > MINSLIDEHEIGHT)
                {
                    robot.setLinearPower(0.5);
                    currentHeight = robot.getSlideEncoder();
                }
                robot.setLinearPower(0);
                break;
        }


    }
}
