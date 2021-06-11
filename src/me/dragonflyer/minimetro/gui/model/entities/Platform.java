package me.dragonflyer.minimetro.gui.model.entities;

import java.awt.geom.Point2D;

public class Platform {

    private Direction dir;
    private Lane lane;
    private boolean used = false;

    Platform(Direction dir, Lane lane) {
        this.dir = dir;
        this.lane = lane;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Point2D.Double getPlatformOffset(double lineWidth) {
        Point2D.Double offset = new Point2D.Double();
        if (this.lane == Lane.LEFT) {
            offset = getPlatformOffsetLeft(lineWidth);
        } else if (this.lane == Lane.RIGHT) {
            offset = getPlatformOffsetRight(lineWidth);
        }
        return offset;
    }

    private Point2D.Double getPlatformOffsetLeft(double lineWidth) {
        double straightOffset = lineWidth;
        double diagonalOffset = lineWidth / Math.sqrt(2d);

        Point2D.Double offset = new Point2D.Double();
        switch (dir) {
            case N:
                offset.x = -straightOffset;
                break;
            case NE:
                offset.x = -diagonalOffset;
                offset.y = -diagonalOffset;
                break;
            case E:
                offset.y = -straightOffset;
                break;
            case SE:
                offset.x = diagonalOffset;
                offset.y = -diagonalOffset;
                break;
            case S:
                offset.x = straightOffset;
                break;
            case SW:
                offset.x = diagonalOffset;
                offset.y = diagonalOffset;
                break;
            case W:
                offset.y = straightOffset;
                break;
            case NW:
                offset.x = -diagonalOffset;
                offset.y = diagonalOffset;
                break;
            default:
                break;
        }
        return offset;
    }

    private Point2D.Double getPlatformOffsetRight(double lineWidth) {
        Point2D.Double offset = getPlatformOffsetLeft(lineWidth);
        offset.x = -offset.x;
        offset.y = -offset.y;
        return offset;
    }

    public Point2D.Double getInflectionOffset(double lineWidth, double absXDiff, double absYDiff, LineSection.Turn turn) {
        Point2D.Double offset = new Point2D.Double();
        if (this.lane == Lane.LEFT) {
            offset = getInflectionOffsetLeft(lineWidth, absXDiff, absYDiff, turn);
        } else if (this.lane == Lane.RIGHT) {
            offset = getInflectionOffsetRight(lineWidth, absXDiff, absYDiff, turn);
        }
        return offset;
    }

    private Point2D.Double getInflectionOffsetLeft(double lineWidth, double absXDiff, double absYDiff, LineSection.Turn turn) {
        double straightOffset = lineWidth;
        double diagonalOffset = lineWidth * Math.sqrt(2d);

        Point2D.Double offset = new Point2D.Double();
        if (dir.isOrdinal()) {
            if (absXDiff < absYDiff) {
                if (dir == Direction.SW || dir == Direction.NW) {
                    offset.y = diagonalOffset;
                } else { // SE || NE
                    offset.y = -diagonalOffset;
                }
            } else {
                if (dir == Direction.SW || dir == Direction.SE) {
                    offset.x = diagonalOffset;
                } else { // NW || NE
                    offset.x = -diagonalOffset;
                }
            }
        } else {
            // idea to simplify
//            if (turn == LineSection.Turn.LEFT) {
//                if (absXDiff < absYDiff) {
//
//                } else {
//
//                }
//            } else { // RIGHT
//                if (absXDiff < absYDiff) {
//
//                } else {
//
//                }
//            }
            //TODO pass turn to linesection, call this method with turn from pov of platform (use opposite)

            if (absXDiff < absYDiff) {
                if (dir == Direction.N) {
                    if (turn == LineSection.Turn.LEFT) {
                        offset.x = -straightOffset;
                        offset.y = -straightOffset;
                    } else { // RIGHT
                        offset.x = -straightOffset;
                        offset.y = straightOffset;
                    }
                } else if (dir == Direction.S) {
                    if (turn == LineSection.Turn.LEFT) {
                        offset.x = straightOffset;
                        offset.y = straightOffset;
                    } else { // RIGHT
                        offset.x = straightOffset;
                        offset.y = -straightOffset;
                    }
                }
            } else {
                if (dir == Direction.E) {
                    if (turn == LineSection.Turn.LEFT) {
                        offset.x = straightOffset;
                        offset.y = -straightOffset;
                    } else { // RIGHT
                        offset.x = -straightOffset;
                        offset.y = -straightOffset;
                    }
                } else if (dir == Direction.W) {
                    if (turn == LineSection.Turn.LEFT) {
                        offset.x = -straightOffset;
                        offset.y = straightOffset;
                    } else { // RIGHT
                        offset.x = straightOffset;
                        offset.y = straightOffset;
                    }
                }
            }
        }
        return offset;
    }

    private Point2D.Double getInflectionOffsetRight(double lineWidth, double absXDiff, double absYDiff, LineSection.Turn turn) {
        Point2D.Double offset = getInflectionOffsetLeft(lineWidth, absXDiff, absYDiff, turn);
        offset.x = -offset.x;
        offset.y = -offset.y;
        return offset;
    }

    public enum Lane {
        LEFT, MIDDLE, RIGHT
    }

}
