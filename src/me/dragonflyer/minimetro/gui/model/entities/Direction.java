package me.dragonflyer.minimetro.gui.model.entities;

public enum Direction {
    N, NE, E, SE, S, SW, W, NW;

    public boolean isOrdinal() {
        return this == NE || this == SE || this == SW || this == NW;
    }
}
