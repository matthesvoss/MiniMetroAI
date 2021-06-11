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
        if (!middlePlatform.isUsed()) {
            return middlePlatform;
        }
        if (!leftPlatform.isUsed()) {
            return leftPlatform;
        }
        if (!rightPlatform.isUsed()) {
            return rightPlatform;
        }
        throw new NoFreePlatformException();
    }

    public Platform getOppositePlatform(Platform platform) throws NoFreePlatformException {
        switch (platform.getLane()) {
            case LEFT:
                if (!rightPlatform.isUsed()) {
                    return rightPlatform;
                }
                break;
            case MIDDLE:
                if (!middlePlatform.isUsed()) {
                    return middlePlatform;
                }
                break;
            case RIGHT:
                if (!leftPlatform.isUsed()) {
                    return leftPlatform;
                }
                break;
            default:
                break;
        }
        throw new NoFreePlatformException();
    }

}
