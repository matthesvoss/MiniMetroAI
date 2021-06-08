package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.Model;

import java.awt.*;

public class LineSection {

    private Line line;
    private Station station1, station2;
    private Direction platform1Dir, platform2Dir;
    private Platform platform1, platform2;
    private Point platform1Loc, platform2Loc;
    private Point inflectionLoc;

    LineSection(Line line, Station station1, Station station2, Direction platform1Dir, Direction platform2Dir, Platform platform1, Platform platform2, Point inflectionLoc) {
        this.line = line;
        this.station1 = station1;
        this.station2 = station2;
        this.platform1Dir = platform1Dir;
        this.platform2Dir = platform2Dir;
        this.platform1 = platform1;
        this.platform2 = platform2;
        this.platform1Loc = station1.getLocation();
        this.platform2Loc = station2.getLocation();
        this.inflectionLoc = inflectionLoc;
    }

    public void applyPlatformOffsets() {
        Model model = Model.getInstance();
        double lineWidth = model.getLineWidth();
        int straightOffset = (int) Math.round(lineWidth);
        int diagonalOffset = (int) Math.round(lineWidth / Math.sqrt(2d));

        // calculate platform1Loc and platform2Loc offsets
        Point platform1Offset = station1.getPlatformDirection(platform1Dir).getPlatformOffset(platform1, straightOffset, diagonalOffset);
        Point platform2Offset = station2.getPlatformDirection(platform2Dir).getPlatformOffset(platform2, straightOffset, diagonalOffset);

        platform1Loc.translate(platform1Offset.x, platform1Offset.y);
        platform2Loc.translate(platform2Offset.x, platform2Offset.y);

        if (inflectionLoc != null) {
            // calculate inflectionLoc offset
            //TODO test if correct
            inflectionLoc.translate(platform1Offset.x, platform1Offset.y);
            inflectionLoc.translate(platform2Offset.x, platform2Offset.y);
        }
    }

    public Station getStation1() {
        return station1;
    }

    public Station getStation2() {
        return station2;
    }

    public Direction getPlatform1Direction() {
        return platform1Dir;
    }

    public Direction getPlatform2Direction() {
        return platform2Dir;
    }

    public Platform getPlatform1() {
        return platform1;
    }

    public Platform getPlatform2() {
        return platform2;
    }

    public Point getPlatform1Loc() {
        return platform1Loc;
    }

    public Point getPlatform2Loc() {
        return platform2Loc;
    }

    public boolean hasInflectionLocation() {
        return inflectionLoc != null;
    }

    public Point getInflectionLocation() {
        return inflectionLoc;
    }

}
