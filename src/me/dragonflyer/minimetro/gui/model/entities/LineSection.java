package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.geom.DoublePoint;
import me.dragonflyer.minimetro.gui.model.geom.IntPoint;

import java.awt.*;
import java.awt.geom.Point2D;

public class LineSection {

    private Line line;

    private Station station1, station2;
    private Direction platform1Dir, platform2Dir;
    private Platform platform1, platform2;

    private DoublePoint platform1Loc, platform2Loc;

    private DoublePoint inflectionLoc;
    private boolean hasInflectionLoc = false;
    private Turn turn;

    private int absXDiff, absYDiff;

    public LineSection(Line line, Station station1, Station station2, int absXDiff, int absYDiff) {
        this.line = line;
        this.station1 = station1;
        this.station2 = station2;
        this.absXDiff = absXDiff;
        this.absYDiff = absYDiff;

        IntPoint station1Loc = station1.getLocation();
        this.platform1Loc = new DoublePoint(station1Loc.getX(), station1Loc.getY());
        IntPoint station2Loc = station2.getLocation();
        this.platform2Loc = new DoublePoint(station2Loc.getX(), station2Loc.getY());
    }

    public void applyOffsets() {
        double lineWidth = 0.1d;

        applyPlatformOffsets(lineWidth);
        if (hasInflectionLoc) {
            applyInflectionOffset(lineWidth);
        }
    }

    private void applyPlatformOffsets(double lineWidth) {
        // calculate platform1Loc and platform2Loc offsets
        DoublePoint platform1Offset = platform1.getPlatformOffset(lineWidth);
        DoublePoint platform2Offset = platform2.getPlatformOffset(lineWidth);

        platform1Loc.x += platform1Offset.x;
        platform1Loc.y += platform1Offset.y;
        platform2Loc.x += platform2Offset.x;
        platform2Loc.y += platform2Offset.y;
    }

    private void applyInflectionOffset(double lineWidth) {
        DoublePoint inflectionOffset1 = platform1.getInflectionOffset(lineWidth, absXDiff, absYDiff, turn);
        DoublePoint inflectionOffset2 = platform2.getInflectionOffset(lineWidth, absXDiff, absYDiff, turn.getOpposite());

        inflectionLoc.x += inflectionOffset1.x + inflectionOffset2.x;
        inflectionLoc.y += inflectionOffset1.y + inflectionOffset2.y;
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

    public void setPlatform1Dir(Direction platform1Dir) {
        this.platform1Dir = platform1Dir;
    }

    public Direction getPlatform2Direction() {
        return platform2Dir;
    }

    public void setPlatform2Dir(Direction platform2Dir) {
        this.platform2Dir = platform2Dir;
    }

    public Platform getPlatform1() {
        return platform1;
    }

    public void setPlatform1(Platform platform1) {
        this.platform1 = platform1;
    }

    public Platform getPlatform2() {
        return platform2;
    }

    public void setPlatform2(Platform platform2) {
        this.platform2 = platform2;
    }

    public DoublePoint getPlatform1Loc() {
        return platform1Loc;
    }

    public DoublePoint getPlatform2Loc() {
        return platform2Loc;
    }

    public boolean hasInflectionLocation() {
        return hasInflectionLoc;
    }

    public void setInflectionLoc(IntPoint inflectionLoc) {
        this.inflectionLoc = new DoublePoint(inflectionLoc.getX(), inflectionLoc.getY());
        hasInflectionLoc = true;
    }

    public DoublePoint getInflectionLocation() {
        return inflectionLoc;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public enum Turn {
        LEFT, RIGHT;

        public Turn getOpposite() {
            return switch (this) {
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
                default -> null;
            };
        }
    }
}
