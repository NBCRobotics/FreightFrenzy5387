package org.firstinspires.ftc.teamcode.OpenCV;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class HubDetector extends OpenCvPipeline{
    private Telemetry telemetry;
    private Mat mat = new Mat();

    private Scalar lowHSV;
    private Scalar highHSV;

    private Scalar colorNotFound = new Scalar(255, 0, 0);
    private Scalar colorFound = new Scalar(0, 255, 0);

    private boolean aligned = false;

    static final Rect MIDDLE_ROI = new Rect(new Point(0,0), new Point(40,40));

    public HubDetector(String color) {
        if (color.toLowerCase().equals("blue")) {
            //set the color we look for to blue
        }
        if (color.toLowerCase().equals("red")) {
            //set the color we look for to red
        }
        else {
            //uhhhhhhh
        }
    }

    public Mat processFrame(Mat input) {
        aligned = false;
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat middle = mat.submat(MIDDLE_ROI);
        double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area() / 255;
        middle.release();

        if (middleValue > 0.4) {
            aligned = true;
            telemetry.addData("Aligned: ", "true");
        }
        telemetry.update();

        Imgproc.rectangle(mat, MIDDLE_ROI, middleValue > 0.4 ? colorFound : colorNotFound);

        return mat;
    }

    public boolean isAligned() {
        return aligned;
    }
}
