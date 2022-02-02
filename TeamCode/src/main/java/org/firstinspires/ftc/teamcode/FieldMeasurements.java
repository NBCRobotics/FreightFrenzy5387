package org.firstinspires.ftc.teamcode;


public class FieldMeasurements {
    public static final double TILE_WIDTH = 610; //3728.44037 in encoder ticks //2.15 per two tiles
    //1.0.75 for 1 tile  at speed 0.5
    //1440
    public static final long TIME_FOR_TILE = 1075; //in MS
    private static final int STAGEONE_HEIGHT = 3300;    //IN TICKS
    private static final int STAGETWO_HEIGHT = 5300;   //IN TICKS
    private static final int STAGETHREE_HEIGHT = 9000;
    private static final double circumfrence = 3.61;

    public static int getStageOneHeight() {
        return STAGEONE_HEIGHT;
    }

    public static int getStageTwoHeight() {
        return STAGETWO_HEIGHT;
    }

    public static int getStageThreeHeight() {
        return STAGETHREE_HEIGHT;
    }

}
