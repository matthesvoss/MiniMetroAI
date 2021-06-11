package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;

import java.awt.*;
import java.awt.geom.Point2D;

public class LineSection {
    private Line line;

    private Station station1, station2;
    private Direction platform1Dir, platform2Dir;
    private Platform platform1, platform2;
    private Turn turn;
    private Point2D.Double platform1Loc, platform2Loc;
    private Point2D.Double inflectionLoc;
    private boolean hasInflectionLoc = false;
    private int absXDiff, absYDiff;

    public LineSection(Line line, Station station1, Station station2, int absXDiff, int absYDiff) {
        this.line = line;
        this.station1 = station1;
        this.station2 = station2;
        this.absXDiff = absXDiff;
        this.absYDiff = absYDiff;

        Point station1Loc = station1.getLocation();
        this.platform1Loc = new Point2D.Double(station1Loc.getX(), station1Loc.getY());
        Point station2Loc = station2.getLocation();
        this.platform2Loc = new Point2D.Double(station2Loc.getX(), station2Loc.getY());
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
        Point2D.Double platform1Offset = platform1.getPlatformOffset(lineWidth);
        Point2D.Double platform2Offset = platform2.getPlatformOffset(lineWidth);

        platform1Loc.x += platform1Offset.x;
        platform1Loc.y += platform1Offset.y;
        platform2Loc.x += platform2Offset.x;
        platform2Loc.y += platform2Offset.y;
    }

    private void applyInflectionOffset(double lineWidth) {
        Point2D.Double inflectionOffset1 = platform1.getInflectionOffset(lineWidth, absXDiff, absYDiff, turn);
        Point2D.Double inflectionOffset2 = platform2.getInflectionOffset(lineWidth, absXDiff, absYDiff, turn);

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

    public Point2D.Double getPlatform1Loc() {
        return platform1Loc;
    }

    public Point2D.Double getPlatform2Loc() {
        return platform2Loc;
    }

    public boolean hasInflectionLocation() {
        return hasInflectionLoc;
    }

    public void setInflectionLoc(Point inflectionLoc) {
        this.inflectionLoc = new Point2D.Double(inflectionLoc.getX(), inflectionLoc.getY());
        hasInflectionLoc = true;
    }

    public Point2D.Double getInflectionLocation() {
        return inflectionLoc;
    }

    public enum Turn {
        LEFT, RIGHT;

        public Turn opposite(Turn turn) {
            switch (turn) {
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                default:
                    return null;
            }
        }
    }
}
