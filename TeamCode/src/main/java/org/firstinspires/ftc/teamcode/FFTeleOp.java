package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FFTeleOp", group = "TeleOp")
public class FFTeleOp extends OpMode {
    FFRobot robot = new FFRobot();

    @Override
    public void init() {
        //WHY
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robot.init(hardwareMap);

        //testing more


    }

    @Override
    public void loop() {
        robot.mechanumPov(gamepad1);
        telemetry.addData("Drive Motors: ", "\nFront Left: " +robot.getFrontLeftPower()+
                "\n Back Left: " + robot.getBackLeftPower() + "\nFront Right" + robot.getFrontRightPower() +
                "\nBack Right: " + robot.getBackRightPower());
        telemetry.addData("Game Pad: ", gamepad1.left_stick_y + " " +gamepad1.left_stick_x+ " "
                +gamepad1.right_stick_x+ " " +gamepad1.right_stick_y);
        telemetry.addData("Left Trigger: ", gamepad1.left_trigger);

        telemetry.addData("Intake: ", gamepad1.left_bumper);
        telemetry.addData("Carousel: ", gamepad1.right_trigger);
        telemetry.addData("Linear Slide: ", gamepad1.left_trigger);
        //above labels all motors, servos, etc.
        telemetry.update();
    }
    //TEST ONE


}