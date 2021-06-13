package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.geom.DoublePoint;

public class Platform {

    private Direction dir;
    private Lane lane;
    private boolean free = true;

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

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public DoublePoint getPlatformOffset(double lineWidth) {
        DoublePoint offset = new DoublePoint();
        if (this.lane == Lane.LEFT) {
            offset = getPlatformOffsetLeft(lineWidth);
        } else if (this.lane == Lane.RIGHT) {
            offset = getPlatformOffsetRight(lineWidth);
        }
        return offset;
    }

    private DoublePoint getPlatformOffsetLeft(double lineWidth) {
        double straightOffset = lineWidth;
        double diagonalOffset = lineWidth / Math.sqrt(2d);

        DoublePoint offset = new DoublePoint();
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

    private DoublePoint getPlatformOffsetRight(double lineWidth) {
        DoublePoint offset = getPlatformOffsetLeft(lineWidth);
        offset.x = -offset.x;
        offset.y = -offset.y;
        return offset;
    }

    public DoublePoint getInflectionOffset(double lineWidth, double absXDiff, double absYDiff, LineSection.Turn turn) {
        DoublePoint offset = new DoublePoint();
        if (this.lane == Lane.LEFT) {
            offset = getInflectionOffsetLeft(lineWidth, absXDiff, absYDiff, turn);
        } else if (this.lane == Lane.RIGHT) {
            offset = getInflectionOffsetRight(lineWidth, absXDiff, absYDiff, turn);
        }
        return offset;
    }

    private DoublePoint getInflectionOffsetLeft(double lineWidth, double absXDiff, double absYDiff, LineSection.Turn turn) {
        double straightOffset = lineWidth;
        double diagonalOffset = lineWidth * Math.sqrt(2d);

        DoublePoint offset = new DoublePoint();
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

    private DoublePoint getInflectionOffsetRight(double lineWidth, double absXDiff, double absYDiff, LineSection.Turn turn) {
        DoublePoint offset = getInflectionOffsetLeft(lineWidth, absXDiff, absYDiff, turn);
        offset.x = -offset.x;
        offset.y = -offset.y;
        return offset;
    }

    public enum Lane {
        LEFT, MIDDLE, RIGHT
    }

}
