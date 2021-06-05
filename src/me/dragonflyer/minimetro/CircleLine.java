package me.dragonflyer.minimetro;

public class CircleLine extends Line {

    CircleLine(Station[] stations) {
	super(stations);
    }

    private Direction direction;

    private enum Direction {
	FORWARDS, BACKWARDS;
    }

    Direction getDirection() {
	return direction;
    }

    void setDirection(Direction direction) {
	this.direction = direction;
    }
}
