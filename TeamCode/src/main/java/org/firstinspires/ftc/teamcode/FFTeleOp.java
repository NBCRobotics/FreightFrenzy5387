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

        //testung more


    }

    @Override
    public void loop() {
        robot.mechanumPov(gamepad1);
        telemetry.addData("Drive Motors: ", "Front Left: " +robot.getFrontLeftPower()+
                " Front Right: " + robot.getFrontRightPower() + "\n Back Left: " + robot.getBackLeftPower() +
                " Back Right: " + robot.getBackRightPower());
        telemetry.update();
    }
    //TEST ONE


}
