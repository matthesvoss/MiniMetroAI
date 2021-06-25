package me.dragonflyer.minimetro.gui.model.geometry;

import java.util.Objects;

public class IntPoint extends Point {

    public int x, y;

    public IntPoint() {
        this(0, 0);
    }

    public IntPoint(IntPoint p) {
        this(p.x, p.y);
    }

    public IntPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public IntPoint setLocation(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public IntPoint translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        return this;
    }

    public IntPoint translate(IntPoint d) {
        return translate(d.x, d.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        IntPoint other = (IntPoint) o;
        return x == other.x && y == other.y;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
