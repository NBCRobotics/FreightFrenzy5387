package org.firstinspires.ftc.teamcode;


public class FieldMeasurements {
    public static final double TILE_WIDTH = 610; //3728.44037 in encoder ticks //2.15 per two tiles
    //1.0.75 for 1 tile  at speed 0.5
    public static final long TIME_FOR_TILE = 1075; //in MS
    private static final double STAGEONE_HEIGHT = -3300;    //IN TICKS
    private static final double STAGETWO_HEIGHT = -5300;   //IN TICKS
    private static final double STAGETHREE_HEIGHT = -6700;

    public static int getStageOneHeight() {
        return (int)STAGEONE_HEIGHT;
    }

    public static int getStageTwoHeight() {
        return (int)STAGETWO_HEIGHT;
    }

    public static int getStageThreeHeight() {
        return (int)STAGETHREE_HEIGHT;
    }

}
