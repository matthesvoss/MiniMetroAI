package me.dragonflyer.minimetro.gui.model;

import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.Station;
import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;
import me.dragonflyer.minimetro.gui.model.geom.DoublePoint;
import me.dragonflyer.minimetro.gui.model.geom.IntPoint;
import me.dragonflyer.minimetro.gui.view.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Model {

    private View view;
    private int width, height;

    private List<Station> stations = new ArrayList<>();
    private int numberOfLines, numberOfCarriages, numberOfTunnels;
    private List<Line> lines = new ArrayList<>();
    public final Color[] lineColors = new Color[] {
            new Color(225, 23, 30),
            new Color(81, 39, 132),
            new Color(0, 118, 187),
            new Color(1, 150, 99),
            new Color(218, 74, 141),
            new Color(121, 67, 31),
            new Color(156, 188, 75)
    };

    public int gridSize = 39;
    public int gridWidth = 44, gridHeight = 26;// 48x30 3 gleise pro ausgang, 8 ausgänge, ubersprungene stationen kennzeichnen
    private DoublePoint center = new DoublePoint(22, 13);
    private double border = 3d;

    private boolean locationSelected = false, stationSelected = false;
    private IntPoint clickedLocation;
    public IntPoint selectedLocation;
    private Station selectedStation;

    private Point lastDragPoint;

    public void registerView(View view) {
        this.view = view;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public int getNumberOfCarriages() {
        return numberOfCarriages;
    }

    public void setNumberOfCarriages(int numberOfCarriages) {
        this.numberOfCarriages = numberOfCarriages;
    }

    public int getNumberOfTunnels() {
        return numberOfTunnels;
    }

    public void setNumberOfTunnels(int numberOfTunnels) {
        this.numberOfTunnels = numberOfTunnels;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public boolean isLocationSelected() {
        return locationSelected;
    }

    public boolean isStationSelected() {
        return stationSelected;
    }

    public void mouseClicked(int button, Point point) {
        if (isLocationSelected()) {
            if (button == MouseEvent.BUTTON1) {
                Point selectedLocationPoint = locationToPoint(selectedLocation);
                double distance = point.distance(selectedLocationPoint);
                if (distance >= 1.16d * gridSize && distance <= 3d * gridSize) { // point is in selection circle
                    double degree = Math.toDegrees(Math.atan2(point.y - selectedLocationPoint.y, point.x - selectedLocationPoint.x));
                    Station.Type selectedType = getStationTypeForDegree(degree);

                    if (!isStationSelected()) { // add new station of selected type
                        Station newStation = new Station(selectedLocation, selectedType);
                        stations.add(newStation);
                    } else if (selectedStation.getType() != selectedType) { // change station type and icon
                        selectedStation.setType(selectedType);
                    }
                }
            }
            locationSelected = false;
            stationSelected = false;
            view.repaint();
        } else {
            clickedLocation = getClosestLocation(point);
            if (button == MouseEvent.BUTTON1 && isLocationFree(clickedLocation)) { // select clicked location
                locationSelected = true;
                selectedLocation = clickedLocation;

                selectedStation = getStationOnLocation(selectedLocation);
                stationSelected = selectedStation != null;

                view.repaint();
            } else if (button == MouseEvent.BUTTON3) { // try to remove station
                Station station = getStationOnLocation(clickedLocation);
                if (station != null) {
                    stations.remove(station);
                    view.repaint();
                }
            }
        }
    }

    private IntPoint getClosestLocation(Point point) {
        DoublePoint loc = pointToLocation(point);
        return new IntPoint((int) Math.round(loc.x), (int) Math.round(loc.y));
    }

    private boolean isLocationFree(IntPoint p) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                if ((x == 0 && y == 0) || (x != 0 && y == -2) || (x == 2 && y != 0) || (x != 0 && y == 2) || (x == -2 && y != 0)) {
                    continue;
                }
                if (isStationOnLocation(new IntPoint(p.x + x, p.y + y))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isStationOnLocation(IntPoint p) {
        return getStationOnLocation(p) != null;
    }

    private Station getStationOnLocation(IntPoint p) {
        for (Station station : stations) {
            if (station.getLocation().equals(p)) {
                return station;
            }
        }
        return null;
    }

    private Station.Type getStationTypeForDegree(double degree) {
        if (degree > 0.72d && degree <= 38.44d) {
            return Station.Type.CROSS;
        } else if (degree > 38.44d && degree <= 69.94d) {
            return Station.Type.RHOMBUS;
        } else if (degree > 69.94d && degree <= 105.99d) {
            return Station.Type.LEAF;
        } else if (degree > 105.99d && degree <= 141.81d) {
            return Station.Type.DIAMOND;
        } else if (degree > 141.81d && degree <= 180d) {
            return Station.Type.PENTAGON;
        } else if (degree > -180d && degree <= -145.26d) {
            return Station.Type.STAR;
        } else if (degree > -145.26d && degree <= -108.9d) {
            return Station.Type.DROP;
        } else if (degree > -108.9d && degree <= -68.88d) {
            return Station.Type.CIRCLE;
        } else if (degree > -68.88d && degree <= -35.2d) {
            return Station.Type.TRIANGLE;
        } else if (degree > -35.2d && degree <= 0.72d) {
            return Station.Type.SQUARE;
        }
        return null;
    }

    public void leftMouseButtonPressed(Point point) {
        lastDragPoint = point;
    }

    public void leftMouseButtonDragged(Point point) {
        double xDiff = point.x - lastDragPoint.x;
        double yDiff = point.y - lastDragPoint.y;
        center.setLocation(center.x - xDiff / gridSize, center.y - yDiff / gridSize);
        clampCenter();
        lastDragPoint = point;

        locationSelected = false;
        stationSelected = false;

        view.repaint();
    }

    public void mouseWheelMoved(int unitsToScroll, Point point) {
        int prevGridSize = gridSize;
        gridSize -= unitsToScroll;
        clampGridSize();
        if (prevGridSize - gridSize < 0) { // zoomed in
            zoomInOnCursor(point, prevGridSize);
        }
        clampCenter();

        locationSelected = false;
        stationSelected = false;

        view.repaint();
    }

    private void zoomInOnCursor(Point point, int prevGridSize) {
        double centerDiffX = point.x - width / 2d;
        double centerDiffY = point.y - height / 2d;

        double centerMoveX = centerDiffX / prevGridSize - centerDiffX / gridSize;
        double centerMoveY = centerDiffY / prevGridSize - centerDiffY / gridSize;

        center.setLocation(center.x + centerMoveX, center.y + centerMoveY);
    }

    public Point locationToPoint(me.dragonflyer.minimetro.gui.model.geom.Point loc) {
        double x = width / 2d + (loc.getX() - center.x) * gridSize;
        double y = height / 2d + (loc.getY() - center.y) * gridSize;
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    private DoublePoint pointToLocation(Point p) {
        double x = center.x + (p.x - width / 2d) / gridSize;
        double y = center.y + (p.y - height / 2d) / gridSize;
        return new DoublePoint(x, y);
    }

    public void frameResized(int width, int height) {
        this.width = width;
        this.height = height;

        clampGridSize();
        clampCenter();
    }

    private void clampGridSize() {
        int minWidth = (int) Math.ceil(width / (gridWidth + 2 * border));
        int minHeight = (int) Math.ceil(height / (gridHeight + 2 * border));
        int minGridSize = Math.max(minWidth, minHeight);

        double minSquares = 8d;
        int maxWidth = (int) Math.floor(width / minSquares);
        int maxHeight = (int) Math.floor(height / minSquares);
        int maxGridSize = Math.max(maxWidth, maxHeight);

        gridSize = clamp(gridSize, minGridSize, maxGridSize);
    }

    private void clampCenter() {
        double minX = width / 2d / gridSize - border;
        double maxX = gridWidth - minX;

        double minY = height / 2d / gridSize - border;
        double maxY = gridHeight - minY;

        double clampedX = clamp(center.x, minX, maxX);
        double clampedY = clamp(center.y, minY, maxY);

        center.setLocation(clampedX, clampedY);
    }

    private int clamp(int i, int min, int max) {
        return Math.max(min, Math.min(i, max));
    }

    private double clamp(double d, double min, double max) {
        return Math.max(min, Math.min(d, max));
    }
}
