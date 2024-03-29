package org.firstinspires.ftc.teamcode.OpenCV;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;


public class BarcodeDetector extends OpenCvPipeline {
    Telemetry telemetry;
    Mat mat = new Mat();
    public BarcodeDetector (Telemetry t) {
        telemetry = t;
    }

    Scalar colorNotFound = new Scalar(255, 0, 0);
    Scalar colorFound = new Scalar(0, 255, 0);
    public enum Location {
        RIGHT, //stage one
        MIDDLE, //stage two
        LEFT, //stage three
        UNKNOWN //also unknwon
    }
    private Location location;
    static final Rect RIGHT_ROI = new Rect(new Point(150, 80), new Point(190, 120));
    static final Rect MIDDLE_ROI = new Rect(new Point(50, 80), new Point(90, 120));
    static final Rect LEFT_ROI = new Rect(new Point(0, 80), new Point(20, 120));

    //looks for team shipping element pos
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);

        Scalar lowHSV = new Scalar(90, 150, 0);
        Scalar highHSV = new Scalar(140, 255, 255);

        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat right = mat.submat(RIGHT_ROI);
        Mat middle = mat.submat(MIDDLE_ROI);
        Mat left = mat.submat(LEFT_ROI);

        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;
        double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area() / 255;
        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;        //how much of the area is the desired color

        right.release();
        middle.release();
        left.release();

        boolean onRight = rightValue > 0.4; // is percent that it blue so 40%
        boolean onMiddle = middleValue > 0.4;
        boolean onLeft = leftValue > 0.4;

        if (onRight) {
            location = Location.RIGHT;
            telemetry.addData("Barcode location: ", "right square");
        } else if (onMiddle) {
            location = Location.MIDDLE;
            telemetry.addData("Barcode location: ", "middle square");
        } else if (onLeft) {
            location = Location.LEFT;
            telemetry.addData("Barcode location", "left square");
        } else {
            location = Location.UNKNOWN;
            telemetry.addData("Barcode location: ", "not found");
        }
        telemetry.update();
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_GRAY2RGB);

        //add rectangles
        Imgproc.rectangle(mat, RIGHT_ROI, onRight ? colorFound : colorNotFound);
        Imgproc.rectangle(mat, MIDDLE_ROI, onMiddle ? colorFound : colorNotFound);
        Imgproc.rectangle(mat, LEFT_ROI, onLeft ? colorFound : colorNotFound);

        return mat;
    }

    public Location getLocation() {
        return location;
    }

    public String getAnalysis() {
        String text = "";
        if (location == Location.LEFT) {
            text += "Shipping element on the left square";
        } else if (location == Location.MIDDLE) {
            text += "Shipping element on the middle square";
        } else if (location == Location.RIGHT) {
            text += "Shipping element on the right square";
        } else {
            text += "unknown pos";
        }
        return text;
    }
}
