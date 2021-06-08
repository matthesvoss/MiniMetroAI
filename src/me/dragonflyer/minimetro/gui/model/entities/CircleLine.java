package me.dragonflyer.minimetro.gui.model.entities;

import java.awt.*;

public class CircleLine extends Line {

    private Direction dir;

    CircleLine(Station[] stations, Color color) {
        super(stations, color);
    }

    Direction getDirection() {
        return dir;
    }

    void setDirection(Direction dir) {
        this.dir = dir;
    }

    private enum Direction {
        FORWARDS, BACKWARDS
    }

}
