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
        robot.mechanumPov(gamepad1,gamepad2);
        telemetry.addData("Drive Motors: ", "\nFront Left: " +robot.getFrontLeftPower()+
                "\n Back Left: " + robot.getBackLeftPower() + "\nFront Right" + robot.getFrontRightPower() +
                "\nBack Right: " + robot.getBackRightPower());
     //   telemetry.addData("Game Pad 1: ", gamepad1.left_stick_y + " " +gamepad1.left_stick_x+ " "
          //      +gamepad1.right_stick_x+ " " +gamepad1.right_stick_y + "\n\n");
        telemetry.addData("Right bumper: ", gamepad2.right_bumper);
        telemetry.addData("Left bumper: ", gamepad2.left_bumper);
        telemetry.addData("Intake: ", robot.getIntakePower() + "\n");
        telemetry.addData("LinearSlide: ", +robot.getLinearPower() + "\n");
        telemetry.addData("LinearSlide Encoder: ", +robot.getSlideEncoder() + "\n");
        if (robot.isMax()) telemetry.addData("LINEAR SLIDE ERROR", "MAX REACHED");
        if (robot.isMin()) telemetry.addData("LINEAR SLIDE ERROR", "MIN REACHED");
        telemetry.addData("Carousel: ", robot.getCarouselPower() + "\n");
        //above labels all motors, servos, etc.
        telemetry.update();
    }
    //TEST ONE


}