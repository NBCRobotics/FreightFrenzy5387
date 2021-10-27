package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.FFRobot;

@Autonomous(name = "FFCarouselAuto", group = "Autonomous")



public class FFAutoCarouselB extends LinearOpMode{
   FFRobot robot = new FFRobot();


   @Override
   public void runOpMode() throws InterruptedException
        {
            robot.init(hardwareMap);
            //robot.foundHooks();  so it will be more efficient if the robot grabs the foundation and puts it on the thingy first
            robot.drive(0.5);   //move off the wall
            //robot.sleep(500); //as of now it will just go to the carousel.
            robot.brake();
            robot.strafe(0.5);  //strafe right
            //robot.sleep(2000);
            robot.brake();
            robot.drive(-0.5);



        }


}
