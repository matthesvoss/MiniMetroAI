package me.dragonflyer.minimetro.gui.model.entities;

import java.awt.*;

public class CircleLine extends Line {//TODO

    private Direction dir;

    public CircleLine(Station[] stations) {
        super(stations);
    }

    public Direction getDirection() {
        return dir;
    }

    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    public enum Direction {
        FORWARDS, BACKWARDS
    }

}
