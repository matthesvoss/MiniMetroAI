package me.dragonflyer.minimetro.gui.model.geometry;

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
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
