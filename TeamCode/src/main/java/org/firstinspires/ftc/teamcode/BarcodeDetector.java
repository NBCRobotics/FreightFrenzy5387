package org.firstinspires.ftc.teamcode;

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
    public enum Location {
        RIGHT, //stage one
        MIDDLE, //stage two
        LEFT, //stage three
        UNKNOWN
    }
    private Location location;
    static final Rect RIGHT_ROI = new Rect(new Point(0, 20), new Point(40, 60));
    static final Rect MIDDLE_ROI = new Rect(new Point(80, 20), new Point(120, 60));
    static final Rect LEFT_ROI = new Rect(new Point(200, 20), new Point(240, 60)); //CHANGE THIS ONCE CAMERA IS MOUTNED!
    /*
    This is prob misplaced so i moved it to processFrame
    Mat right = mat.submat(RIGHT_ROI);
    Mat middle = mat.submat(MIDDLE_ROI);
    Mat left = mat.submat(LEFT_ROI);
    */

    //looks for team shipping element pos
    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, mat, Imgproc.COLOR_RGB2HSV);
        Scalar lowHSV = new Scalar(226, 43, 20);  //lowest value for blue
        Scalar highHSV = new Scalar(250, 255, 255); //highest value for blue (sorta not really actually at all)

        Core.inRange(mat, lowHSV, highHSV, mat);

        Mat right = mat.submat(RIGHT_ROI);
        Mat middle = mat.submat(MIDDLE_ROI);
        Mat left = mat.submat(LEFT_ROI);

        double rightValue = Core.sumElems(right).val[0] / RIGHT_ROI.area() / 255;
        double middleValue = Core.sumElems(middle).val[0] / MIDDLE_ROI.area() / 255;
        double leftValue = Core.sumElems(left).val[0] / LEFT_ROI.area() / 255;        //how much of the area is the desired color

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

        return mat;
    }

    public Location getLocation() {
        return location;
    }
}
