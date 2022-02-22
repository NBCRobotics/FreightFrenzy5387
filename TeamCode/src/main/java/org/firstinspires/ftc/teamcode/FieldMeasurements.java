package org.firstinspires.ftc.teamcode;


public class FieldMeasurements {
    public static final double TILE_WIDTH = 610; //3728.44037 in encoder ticks //2.15 per two tiles

    private static final int ticksPerRotation = 1440;
    private static final int diameter = 100; //in mm
    //2 feet = 609.6mm, length of tile
    //diameter * 3.14 = C, 609.6mm*1440 ticks/C (mm) = ticks per tile

    private static final double ticksPerTile  = (609.6 * 1440) / (diameter * 3.14);

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

    public static int getTicksPerTile() { return (int)ticksPerTile; }

}
