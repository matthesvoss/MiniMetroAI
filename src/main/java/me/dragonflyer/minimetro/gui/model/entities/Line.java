package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.enums.Direction;
import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;
import me.dragonflyer.minimetro.gui.model.geometry.IntPoint;
import org.checkerframework.checker.units.qual.A;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Line {

    private List<Station> stations;
    private List<LineSection> lineSections;
    private Color color;

    public Line(List<Station> stations) {
        if (stations == null || stations.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.stations = stations;
        this.lineSections = new ArrayList<>();
    }

    public void calculateLineSections() throws NoFreePlatformException {
        for (int i = 0; i < stations.size() - 1; i++) {
            calculateLineSection(stations.get(i), stations.get(i + 1));
        }
    }

    protected void calculateLineSection(Station station1, Station station2) throws NoFreePlatformException {
        int x1 = station1.getX(), y1 = station1.getY(), x2 = station2.getX(), y2 = station2.getY();
        int xDiff = x2 - x1, yDiff = y2 - y1;
        int absXDiff = Math.abs(xDiff), absYDiff = Math.abs(yDiff);

        LineSection lineSection = new LineSection(this, station1, station2, absXDiff, absYDiff);

        if (x1 == x2) { // stations are on same vertical line
            if (y1 < y2) { // station1 is above station2
                setupLineSection(lineSection, Direction.S, Direction.N, null, null);
            } else { // station1 is below station2
                setupLineSection(lineSection, Direction.N, Direction.S, null, null);
            }
        } else if (y1 == y2) { // stations are on same horizontal line
            if (x1 < x2) { // station1 is left of station2
                setupLineSection(lineSection, Direction.E, Direction.W, null, null);
            } else { // station1 is right of station2
                setupLineSection(lineSection, Direction.W, Direction.E, null, null);
            }
        } else {
            if (absXDiff == absYDiff) { // no inflection point needed (diagonal)
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

                setupLineSection(lineSection, station1Dir, station2Dir, null, null);
            } else { // inflection point needed
                int diagonalLength = Math.min(absXDiff, absYDiff);

                if (absXDiff < absYDiff) {
                    if (xDiff > 0) {
                        if (yDiff < 0) {
                            try {
                                setupLineSection(lineSection, Direction.N, Direction.SW, new IntPoint(x1, y2 + diagonalLength), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.NE, Direction.S, new IntPoint(x2, y1 - diagonalLength), LineSection.Turn.LEFT);
                            }
                        } else {
                            try {
                                setupLineSection(lineSection, Direction.SE, Direction.N, new IntPoint(x2, y1 + diagonalLength), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.S, Direction.NW, new IntPoint(x1, y2 - diagonalLength), LineSection.Turn.LEFT);
                            }
                        }
                    } else {
                        if (yDiff > 0) {
                            try {
                                setupLineSection(lineSection, Direction.S, Direction.NE, new IntPoint(x1, y2 - diagonalLength), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.SW, Direction.N, new IntPoint(x2, y1 + diagonalLength), LineSection.Turn.LEFT);
                            }
                        } else {
                            try {
                                setupLineSection(lineSection, Direction.NW, Direction.S, new IntPoint(x2, y1 - diagonalLength), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.N, Direction.SE, new IntPoint(x1, y2 + diagonalLength), LineSection.Turn.LEFT);
                            }
                        }
                    }
                } else {
                    if (xDiff > 0) {
                        if (yDiff < 0) {
                            try {
                                setupLineSection(lineSection, Direction.NE, Direction.W, new IntPoint(x1 + diagonalLength, y2), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.E, Direction.SW, new IntPoint(x2 - diagonalLength, y1), LineSection.Turn.LEFT);
                            }
                        } else {
                            try {
                                setupLineSection(lineSection, Direction.E, Direction.NW, new IntPoint(x2 - diagonalLength, y1), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.SE, Direction.W, new IntPoint(x1 + diagonalLength, y2), LineSection.Turn.LEFT);
                            }
                        }
                    } else {
                        if (yDiff > 0) {
                            try {
                                setupLineSection(lineSection, Direction.SW, Direction.E, new IntPoint(x1 - diagonalLength, y2), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.W, Direction.NE, new IntPoint(x2 + diagonalLength, y1), LineSection.Turn.LEFT);
                            }
                        } else {
                            try {
                                setupLineSection(lineSection, Direction.W, Direction.SE, new IntPoint(x2 + diagonalLength, y1), LineSection.Turn.RIGHT);
                            } catch (NoFreePlatformException e) {
                                setupLineSection(lineSection, Direction.NW, Direction.E, new IntPoint(x1 - diagonalLength, y2), LineSection.Turn.LEFT);
                            }
                        }
                    }
                }
            }
        }
        lineSections.add(lineSection);
    }

    private void setupLineSection(LineSection lineSection, Direction platform1Dir, Direction platform2Dir, IntPoint inflectionLoc, LineSection.Turn turn) throws NoFreePlatformException {
        PlatformDirection platformDir1 = lineSection.getStation1().getPlatformDirection(platform1Dir);
        PlatformDirection platformDir2 = lineSection.getStation2().getPlatformDirection(platform2Dir);

        boolean hasInflectionLoc = inflectionLoc != null;
        Platform platform1 = platformDir1.getFreePlatform();
        Platform platform2 = platformDir2.getOppositePlatform(platform1);
        if (platform2 == null) {
            if (hasInflectionLoc) {
                platform2 = platformDir2.getFreePlatform();
            } else {
                throw new NoFreePlatformException();
            }
        }

        lineSection.setPlatform1(platform1);
        platform1.setFree(false);

        lineSection.setPlatform2(platform2);
        platform2.setFree(false);

        lineSection.setPlatform1Dir(platform1Dir);
        lineSection.setPlatform2Dir(platform2Dir);

        if (hasInflectionLoc) {
            lineSection.setInflectionLoc(inflectionLoc);
            lineSection.setTurn(turn);
        }

        lineSection.applyOffsets();
    }

    public void setPlatformsFree(boolean free) {
        for (LineSection lineSection : lineSections) {
            lineSection.getPlatform1().setFree(free);
            lineSection.getPlatform2().setFree(free);
        }
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<LineSection> getLineSections() {
        return lineSections;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Station getFirstStation() {
        return stations.get(0);
    }

    public Station getLastStation() {
        return stations.get(stations.size() - 1);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        List<Station> stationsReversed = new ArrayList<>(stations);
        Collections.reverse(stationsReversed);
        result = prime * result + stations.hashCode() + stationsReversed.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Line other = (Line) o;
        if (other.stations.equals(stations)) {
            return true;
        }
        List<Station> stationsReversed = new ArrayList<>(stations);
        Collections.reverse(stationsReversed);
        return other.stations.equals(stationsReversed);
    }

}
