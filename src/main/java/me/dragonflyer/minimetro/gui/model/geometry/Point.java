package me.dragonflyer.minimetro.gui.model.geometry;

public abstract class Point {

    public abstract double getX();

    public abstract double getY();

    public final double distance(double x, double y) {
        x -= getX();
        y -= getY();
        return Math.sqrt(x * x + y * y);
    }

    public final double distance(Point p) {
        return distance(p.getX(), p.getY());
    }

}
