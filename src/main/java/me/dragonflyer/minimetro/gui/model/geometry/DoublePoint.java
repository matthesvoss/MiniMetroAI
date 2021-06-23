package me.dragonflyer.minimetro.gui.model.geometry;

public class DoublePoint extends Point {

    public double x, y;

    public DoublePoint() {
        this(0, 0);
    }

    public DoublePoint(DoublePoint p) {
        this(p.x, p.y);
    }

    public DoublePoint(double x, double y) {
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

    public DoublePoint setLocation(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public DoublePoint translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
        return this;
    }

    public DoublePoint translate(DoublePoint d) {
        return translate(d.x, d.y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
