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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Point) {
            Point other = (Point) obj;
            double epsilon = 1E-6d;
            double xDiff = Math.abs(this.getX() - other.getX());
            double yDiff = Math.abs(this.getY() - other.getY());
            return xDiff < epsilon && yDiff < epsilon;
        }
        return false;
    }

}
