package me.dragonflyer.minimetro.gui.model;

import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.Station;
import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;
import me.dragonflyer.minimetro.gui.view.View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Model {

    private static Model model;

    private View view;
    private int width, height;

    private List<Station> stations = new ArrayList<>();
    private List<Line> lines = new ArrayList<>();
    private Color[] lineColors = new Color[] {
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
    private Point2D.Double center = new Point2D.Double(22, 13);
    private double border = 3d;

    private boolean locationSelected = false, stationSelected = false;
    private Point clickedLocation; //TODO: create location class
    public Point selectedLocation;
    private Station selectedStation;

    private Point lastDragPoint;

    private Model() {
    }

    public static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public void registerView(View view) {
        this.view = view;
    }

    public void calculateStuff(int numberOfLines, int numberOfCarriages, int numberOfTunnels) {
        // TODO: waggons, tunnel, flüsse, übersprungene stationen, scharfe kurven (zug muss anhalten)
//				if (!stations.isEmpty()) {
//					for (int i = 0; i < (int) linesSpinner.getValue(); i++)
//						lines.add(new Line());
//					for (Station station : stations) {
//						// A, B, C, D -> (A, B), (B, C), (C, D) + (A, B), (A, B), (A, B, C, D)
//					}
//	    			}

// (1, 2)

// (1, 2), (1, 3), (2, 3), (1, 2, 3), (2, 3, 1), (3, 1, 2), (1, 2, 3, 1)

// (1, 2), (1, 3), (2, 1), (2, 3), (3, 1), (3, 2), (1, 2, 3), (1, 3, 2), (2, 1, 3), (2, 3, 1), (3, 1, 2), (3, 2, 1)

//		(1, 2),
//		(1, 3),
//		(1, 4),
//		(2, 3),
//		(2, 4),
//		(3, 4),
//		(1, 2, 3),
//		(1, 2, 4),
//		(1, 3, 4),
//		(1, 4, 2),
//		(2, 1, 3),
//		(2, 3, 1),
//		(2, 3, 4),
//		(3, 2, 4),
//		(3, 4, 1),
//		(3, 4, 2),
//		(4, 1, 2),
//		(4, 1, 3),
//			(1, 2, 3, 1),
//		(1, 2, 3, 4),
//		(2, 3, 4, 1),
//			(2, 3, 4, 2),
//		(3, 4, 1, 2),
//			(3, 4, 1, 3),
//		(4, 1, 2, 3),
//			(4, 1, 2, 4),
//			(1, 2, 3, 4, 1)
//			(1, 2, 4, 3, 1)
//			(1, 3, 2, 4, 1)

// current state:
//		1,2
//		1,3
//		3,2
//		3,4
//		4,1
//		4,2
//		1,2,4
//		1,3,2
//		2,4,1
//		2,4,3
//		3,1,2
//		3,1,4
//		3,2,1
//		3,2,4
//		3,4,1
//		4,1,2
//		4,3,1
//		4,3,2
//		1,2,3,4
//		1,2,4,3
//		1,3,4,2
//		1,4,3,2
//		2,1,3,4
//		3,1,2,4
//		3,1,4,2
//		3,2,1,4
//		3,2,4,1
//		3,4,1,2
//		4,1,3,2
//		4,2,3,1

        // 1,2,3,4
        // 1,2,4,3
        // 1,3,2,4
        // 1,3,4,2
        // 1,4,2,3
        // 1,4,3,2
        // 2,1,3,4
        // 2,1,4,3
        // 2,3,1,4
        // 2,3,4,1x
        // 2,4,1,3
        // 2,4,3,1x
        // 3,1,2,4
        // 3,1,4,2x
        // 3,2,1,4
        // 3,2,4,1x
        // 3,4,1,2x
        // 3,4,2,1x
        // 4,1,2,3x
        // 4,1,3,2x
        // 4,2,1,3x
        // 4,2,3,1x
        // 4,3,1,2x
        // 4,3,2,1x
        lines.clear();

//		ArrayList<ArrayList<Station>> possibleLines = new ArrayList<>();
//		for (int i = 1; i <= numberOfLines; i++) {// lines zur verfügung
//		    for (int j = 0; j < stations.size(); j++) {// startstation index
//			for (int k = 0; k < stations.size(); k++) {
//
//			}
//		    }
//		}
//		HashSet<ArrayList<Station>> possibleLines = new HashSet<>();
//		HashSet<ArrayList<Station>> permutations = generatePermutationsNoRepetition(stations);
//		for (ArrayList<Station> permutation : permutations) {
//		    ArrayList<Station> reversePermutation = new ArrayList<>(permutation);
//		    Collections.reverse(reversePermutation);
//		    if (!possibleLines.contains(reversePermutation)) {
//			possibleLines.add(permutation);
//		    }
//		    while (permutation.size() >= 2) {
//			permutation = new ArrayList<>(permutation.subList(0, permutation.size() - 1));
//			reversePermutation = new ArrayList<>(permutation);
//			Collections.reverse(reversePermutation);
//			if (!possibleLines.contains(reversePermutation)) {
//			    possibleLines.add(permutation);
//			}
//		    }
//		}

//		static <T> List<T> reverse(final List<T> list) {
//		    final int size = list.size();
//		    final int last = size - 1;
//
//		    // create a new list, with exactly enough initial capacity to hold the (reversed) list
//		    final List<T> result = new ArrayList<>(size);
//
//		    // iterate through the list in reverse order and append to the result
//		    for (int i = last; i >= 0; --i) {
//		        final T element = list.get(i);
//		        result.add(element);
//		    }
//
//		    // result now holds a reversed copy of the original list
//		    return result;
//		}

/*
        HashSet<Station> testStations = new HashSet<>();
        testStations.add(new Station(1, null, null, null));
        testStations.add(new Station(2, null, null, null));
        testStations.add(new Station(3, null, null, null));
        testStations.add(new Station(4, null, null, null));
        HashSet<ArrayList<Station>> permutations = generatePermutationsNoRepetition(testStations);
        HashSet<ArrayList<Station>> subPermutations = new HashSet<>();
        for (ArrayList<Station> permutation : permutations) {
            while (permutation.size() > 2) {
                permutation = new ArrayList<>(permutation.subList(0, permutation.size() - 1)); // remove last station
                subPermutations.add(permutation);
            }
        }
        permutations.addAll(subPermutations);
        // TODO

        HashSet<Line> possibleLines = new HashSet<>();
        for (ArrayList<Station> permutation : permutations) {
            possibleLines.add(new Line(permutation.toArray(new Station[0]), null));
        }
        for (Line line : possibleLines) {
            Station[] stations = line.getStations();
            System.out.println(Arrays.stream(stations).map(Station::getId).map(Objects::toString).collect(Collectors.joining(",")));
//            for (int i = 0; i < stations.length; i++) {
//                Station station = stations[i];
//                System.out.print(station.getTest() + (i != stations.length - 1 ? "," : ""));
//            }
//            System.out.println();
        }*/


//        Line connectingLine = new Line(stations.toArray(new Station[0]), lineColors[0]);
//        lines.add(connectingLine);

        stations.clear();
        Station s1 = new Station(1, new Point(0, 0), Station.Type.CIRCLE);
        Station s2 = new Station(2, new Point(4, 2), Station.Type.CROSS);
        Station s3 = new Station(3, new Point(3, 4), Station.Type.STAR);
        stations.add(s1);
        stations.add(s2);
        stations.add(s3);

        Line l1 = new Line(new Station[] { s1, s2, s3 }, lineColors[0]);
        lines.add(l1);
        Line l2 = new Line(new Station[] { s1, s2}, lineColors[1]);
        lines.add(l2);
        Line l3 = new Line(new Station[] { s1, s2, s3 }, lineColors[2]);
        lines.add(l3);
        Line l4= new Line(new Station[] { s1, s3 }, lineColors[3]);
        lines.add(l4);


        for (Line line : lines) {
            try {
                line.calculateLineSections();
            } catch (NoFreePlatformException e) {
                e.printStackTrace();
            }
        }

        view.repaint();
    }

    private HashSet<ArrayList<Station>> generatePermutationsNoRepetition(HashSet<Station> availableStations) {
        HashSet<ArrayList<Station>> permutations = new HashSet<>();
        for (Station station : availableStations) {
            HashSet<Station> stations = new HashSet<>(availableStations);
            stations.remove(station);
            if (!stations.isEmpty()) {
                for (ArrayList<Station> childPermutation : generatePermutationsNoRepetition(stations)) {
                    ArrayList<Station> permutation = new ArrayList<>();
                    permutation.add(station);
                    permutation.addAll(childPermutation);
                    permutations.add(permutation);
                }
            } else {
                ArrayList<Station> permutation = new ArrayList<>();
                permutation.add(station);
                permutations.add(permutation);
            }
        }
        return permutations;
    }
    /*
     * private HashSet<ArrayList<Station>>
     * generateNonreversablePermutationPowerset(HashSet<ArrayList<Station>>
     * permutations) { HashSet<ArrayList<Station>> possibleLines = new HashSet<>();
     * for (ArrayList<Station> permutation : permutations) { ArrayList<Station>
     * reversePermutation = new ArrayList<>(permutation);
     * Collections.reverse(reversePermutation); if
     * (!possibleLines.contains(reversePermutation)) {
     * possibleLines.add(permutation); }
     *
     * possibleLines.addAll(generateNonreversablePermutationPowerset(permutations));
     * } return possibleLines; }
     */

    public List<Station> getStations() {
        return stations;
    }

    public List<Line> getLines() {
        return lines;
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
                if (selectedStation == null) {
                    stationSelected = false;
                } else {
                    stationSelected = true;
                }

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

    private Point getClosestLocation(Point point) {
        Point2D.Double loc = pointToLocation(point);
        return new Point((int) Math.round(loc.x), (int) Math.round(loc.y));
    }

    private boolean isLocationFree(Point p) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -2; y <= 2; y++) {
                if ((x == 0 && y == 0) || (x != 0 && y == -2) || (x == 2 && y != 0) || (x != 0 && y == 2) || (x == -2 && y != 0)) {
                    continue;
                }
                if (isStationOnLocation(new Point(p.x + x, p.y + y))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isStationOnLocation(Point p) {
        return getStationOnLocation(p) != null;
    }

    private Station getStationOnLocation(Point p) {
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

    public Point locationToPoint(Point loc) {
        return locationToPoint(new Point2D.Double(loc.x, loc.y));
    }

    public Point locationToPoint(Point2D.Double loc) {
        double x = width / 2d + (loc.x - center.x) * gridSize;
        double y = height / 2d + (loc.y - center.y) * gridSize;
        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    private Point2D.Double pointToLocation(Point p) {
        double x = center.x + (p.x - width / 2d) / gridSize;
        double y = center.y + (p.y - height / 2d) / gridSize;
        return new Point2D.Double(x, y);
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

    private int clamp(int x, int min, int max) {
        return Math.max(min, Math.min(x, max));
    }

    private double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(x, max));
    }
}
