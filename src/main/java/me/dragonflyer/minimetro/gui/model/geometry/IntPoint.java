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

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void translate(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void translate(IntPoint d) {
        translate(d.x, d.y);
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
