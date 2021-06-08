package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;

import java.awt.*;

public class Line {

    private Station[] stations;
    private LineSection[] lineSections;
    private Color color;

    public Line(Station[] stations, Color color) {
        this.stations = stations;
        this.lineSections = new LineSection[stations.length - 1];
        this.color = color;
    }

    public Station[] getStations() {
        return stations;
    }

    public LineSection[] getLineSections() {
        return lineSections;
    }

    public void setLineSection(int index, Direction station1Dir, Direction station2Dir, Point inflectionLoc) throws NoFreePlatformException {
        if (index > lineSections.length) {
            throw new IllegalArgumentException("Invalid line section index");
        }

        Station station1 = stations[index];
        Station station2 = stations[index + 1];

        PlatformDirection platformDir1 = station1.getPlatformDirection(station1Dir);
        PlatformDirection platformDir2 = station2.getPlatformDirection(station2Dir);

        if (platformDir1.hasFreePlatform() && platformDir2.hasFreePlatform()) {
            Platform platform1 = platformDir1.getFreePlatform();
            Platform platform2 = platformDir1.getFreePlatform();

            LineSection lineSection = new LineSection(this, station1, station2, station1Dir, station2Dir, platform1, platform2, inflectionLoc);
            lineSection.applyPlatformOffsets();
            lineSections[index] = lineSection;
        } else {
            throw new NoFreePlatformException();
        }
    }

    public void calculateLineSections() throws NoFreePlatformException {
        for (int j = 0; j < stations.length - 1; j++) {
            Station station1 = stations[j], station2 = stations[j + 1];
            int x1 = station1.getX(), y1 = station1.getY(), x2 = station2.getX(), y2 = station2.getY();

            if (x1 == x2) { // stations are on same vertical line
                if (y1 < y2) { // station1 is above station2
                    setLineSection(j, Direction.S, Direction.N, null);
                } else { // station1 is below station2
                    setLineSection(j, Direction.N, Direction.S, null);
                }
            } else if (y1 == y2) { // stations are on same horizontal line
                if (x1 < x2) { // station1 is left of station2
                    setLineSection(j, Direction.E, Direction.W, null);
                } else { // station1 is right of station2
                    setLineSection(j, Direction.W, Direction.E, null);
                }
            } else {
                int xDiff = x2 - x1, yDiff = y2 - y1;
                int absXDiff = Math.abs(xDiff), absYDiff = Math.abs(yDiff);

                if (absXDiff == absYDiff) { // no inflection point needed
                    Direction station1Dir, station2Dir;
                    if (xDiff > 0) {
                        if (yDiff < 0) {
                            station1Dir = Direction.NE;
                            station2Dir = Direction.SW;
                        } else {
                            station1Dir = Direction.SE;
                            station2Dir = Direction.NW;
                        }
                    } else {
                        if (yDiff > 0) {
                            station1Dir = Direction.SW;
                            station2Dir = Direction.NE;
                        } else {
                            station1Dir = Direction.NW;
                            station2Dir = Direction.SE;
                        }
                    }

                    setLineSection(j, station1Dir, station2Dir, null);
                } else { // inflection point needed
                    int diagonalLength = Math.min(absXDiff, absYDiff);

                    if (absXDiff < absYDiff) {
                        if (xDiff > 0) {
                            if (yDiff < 0) {
                                try {
                                    setLineSection(j, Direction.N, Direction.SW, new Point(x1, y2 + diagonalLength));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.NE, Direction.S, new Point(x2, y1 - diagonalLength));
                                }
                            } else {
                                try {
                                    setLineSection(j, Direction.SE, Direction.N, new Point(x2, y1 + diagonalLength));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.S, Direction.NW, new Point(x1, y2 - diagonalLength));
                                }
                            }
                        } else {
                            if (yDiff > 0) {
                                try {
                                    setLineSection(j, Direction.S, Direction.NE, new Point(x1, y2 - diagonalLength));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.SW, Direction.N, new Point(x2, y1 + diagonalLength));
                                }
                            } else {
                                try {
                                    setLineSection(j, Direction.NW, Direction.S, new Point(x2, y1 - diagonalLength));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.N, Direction.SE, new Point(x1, y2 + diagonalLength));
                                }
                            }
                        }
                    } else {
                        if (xDiff > 0) {
                            if (yDiff < 0) {
                                try {
                                    setLineSection(j, Direction.NE, Direction.W, new Point(x1 + diagonalLength, y2));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.E, Direction.SW, new Point(x2 - diagonalLength, y1));
                                }
                            } else {
                                try {
                                    setLineSection(j, Direction.E, Direction.NW, new Point(x2 - diagonalLength, y1));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.SE, Direction.W, new Point(x1 + diagonalLength, y2));
                                }
                            }
                        } else {
                            if (yDiff > 0) {
                                try {
                                    setLineSection(j, Direction.SW, Direction.E, new Point(x1 - diagonalLength, y2));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.W, Direction.NE, new Point(x2 + diagonalLength, y1));
                                }
                            } else {
                                try {
                                    setLineSection(j, Direction.W, Direction.SE, new Point(x2 + diagonalLength, y1));
                                } catch (NoFreePlatformException e) {
                                    setLineSection(j, Direction.NW, Direction.E, new Point(x1 - diagonalLength, y2));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Color getColor() {
        return color;
    }

    public Station getFirstStation() {
        return stations[0];
    }

    public Station getLastStation() {
        return stations[stations.length - 1];
    }

    public boolean isCircle() {
        return getFirstStation().equals(getLastStation());
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        Station[] stationsReversed = Arrays.copyOf(stations, stations.length);
//        for (int i = 0; i < stationsReversed.length / 2; i++) {
//            Station temp = stationsReversed[i];
//            int index = stationsReversed.length - i - 1;
//            stationsReversed[i] = stationsReversed[index];
//            stationsReversed[index] = temp;
//        }
//        result = prime * result + Arrays.hashCode(stations) + Arrays.hashCode(stationsReversed);
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (!(obj instanceof Line)) {
//            return false;
//        }
//        Line other = (Line) obj;
//        Station[] stationsReversed = Arrays.copyOf(stations, stations.length);
//        for (int i = 0; i < stationsReversed.length / 2; i++) {
//            Station temp = stationsReversed[i];
//            int index = stationsReversed.length - i - 1;
//            stationsReversed[i] = stationsReversed[index];
//            stationsReversed[index] = temp;
//        }
//        if (!(Arrays.equals(stations, other.stations) || Arrays.equals(stationsReversed, other.stations))) {
//            return false;
//        }
//        return true;
//    }

}
