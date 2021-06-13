package me.dragonflyer.minimetro.gui.model.geom;

public abstract class Point {

    public abstract double getX();

    public abstract double getY();

    public double distance(double x, double y) {
        x -= getX();
        y -= getY();
        return Math.sqrt(x * x + y * y);
    }

    public double distance(Point p) {
        return distance(p.getX(), p.getY());
    }

}
