package me.dragonflyer.minimetro.gui.model.entities;

import java.awt.*;

public class PlatformDirection {

    private Direction dir;
    private Platform leftPlatform = Platform.LEFT;
    private Platform middlePlatform = Platform.MIDDLE;
    private Platform rightPlatform = Platform.RIGHT;

    public PlatformDirection(Direction dir) {
        this.dir = dir;
    }

    public Direction getDirection() {
        return dir;
    }

    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    public boolean hasFreePlatform() {
        return !leftPlatform.isUsed() || !middlePlatform.isUsed() || !rightPlatform.isUsed();
    }

    public Platform getFreePlatform() {
        if (!middlePlatform.isUsed()) {
            return middlePlatform;
        }
        if (!leftPlatform.isUsed()) {
            return leftPlatform;
        }
        if (!rightPlatform.isUsed()) {
            return rightPlatform;
        }
        return null;
    }

    public Point getPlatformOffset(Platform platform, int straightOffset, int diagonalOffset) {
        Point offset = new Point();
        if (platform == Platform.LEFT) { //TODO test ==
            offset = getLeftPlatformOffset(straightOffset, diagonalOffset);
        } else if (platform == Platform.RIGHT) {
            offset = getRightPlatformOffset(straightOffset, diagonalOffset);
        }
        return offset;
    }

    private Point getLeftPlatformOffset(int straightOffset, int diagonalOffset) {
        Point offset = new Point();
        switch (dir) {
            case N:
                offset.x = straightOffset;
                break;
            case NE:
                offset.x = diagonalOffset;
                offset.y = diagonalOffset;
                break;
            case E:
                offset.y = straightOffset;
                break;
            case SE:
                offset.x = -diagonalOffset;
                offset.y = diagonalOffset;
                break;
            case S:
                offset.x = -straightOffset;
                break;
            case SW:
                offset.x = -diagonalOffset;
                offset.y = -diagonalOffset;
                break;
            case W:
                offset.y = -straightOffset;
                break;
            case NW:
                offset.x = diagonalOffset;
                offset.y = -diagonalOffset;
                break;
            default:
                break;
        }
        return offset;
    }

    private Point getRightPlatformOffset(int straightOffset, int diagonalOffset) {
        Point offset = getLeftPlatformOffset(straightOffset, diagonalOffset);
        offset.x = -offset.x;
        offset.y = -offset.y;
        return offset;
    }

}
