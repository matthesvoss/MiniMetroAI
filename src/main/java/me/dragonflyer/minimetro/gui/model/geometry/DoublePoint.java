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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void translate(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void translate(DoublePoint d) {
        translate(d.x, d.y);
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
