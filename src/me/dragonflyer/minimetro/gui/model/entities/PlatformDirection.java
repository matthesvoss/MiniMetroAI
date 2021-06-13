package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;

public class PlatformDirection {

    private Direction dir;
    private Platform leftPlatform, middlePlatform, rightPlatform;

    public PlatformDirection(Direction dir) {
        this.dir = dir;
        leftPlatform = new Platform(dir, Platform.Lane.LEFT);
        middlePlatform = new Platform(dir, Platform.Lane.MIDDLE);
        rightPlatform = new Platform(dir, Platform.Lane.RIGHT);
    }

    public Platform getFreePlatform() throws NoFreePlatformException {
        if (middlePlatform.isFree()) {
            return middlePlatform;
        }
        if (leftPlatform.isFree()) {
            return leftPlatform;
        }
        if (rightPlatform.isFree()) {
            return rightPlatform;
        }
        throw new NoFreePlatformException();
    }

    public Platform getOppositePlatform(Platform platform) {
        switch (platform.getLane()) {
            case LEFT:
                if (rightPlatform.isFree()) {
                    return rightPlatform;
                }
                break;
            case MIDDLE:
                if (middlePlatform.isFree()) {
                    return middlePlatform;
                }
                break;
            case RIGHT:
                if (leftPlatform.isFree()) {
                    return leftPlatform;
                }
                break;
            default:
                break;
        }
        return null;
    }

}
