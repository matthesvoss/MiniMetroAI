package me.dragonflyer.minimetro.gui.model;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

public class Line {
    private Station[] stations;
    private ArrayList<LineSection> lineSections = new ArrayList<>();

    public Line(Station[] stations) {
        this.stations = stations;
    }

    public Station[] getStations() {
        return stations;
    }

    public ArrayList<LineSection> getLineSections() {
        return lineSections;
    }

    public boolean addLineSection(Station station1, Station station2, int station1Direction, int station2Direction, Color color) {
        return addLineSection(station1, station2, station1Direction, station2Direction, null, color);
    }

    public boolean addLineSection(Station station1, Station station2, int station1Direction, int station2Direction, Point inflectionLocation, Color color) {
        if (station1.hasFreePlatform(station1Direction) && station2.hasFreePlatform(station2Direction)) {
            lineSections.add(new LineSection(station1, station2, station1Direction, station1.addPlatform(station1Direction), station2Direction, station2.addPlatform(station2Direction), inflectionLocation, color));
            return true;
        }
        return false;
    }

    Station getFirstStation() {
        return stations[0];
    }

    Station getLastStation() {
        return stations[stations.length - 1];
    }

    boolean isCircle() {
        return getFirstStation().equals(getLastStation());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        Station[] stationsReversed = Arrays.copyOf(stations, stations.length);
        for (int i = 0; i < stationsReversed.length / 2; i++) {
            Station temp = stationsReversed[i];
            int index = stationsReversed.length - i - 1;
            stationsReversed[i] = stationsReversed[index];
            stationsReversed[index] = temp;
        }
        result = prime * result + Arrays.hashCode(stations) + Arrays.hashCode(stationsReversed);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Line)) {
            return false;
        }
        Line other = (Line) obj;
        Station[] stationsReversed = Arrays.copyOf(stations, stations.length);
        for (int i = 0; i < stationsReversed.length / 2; i++) {
            Station temp = stationsReversed[i];
            int index = stationsReversed.length - i - 1;
            stationsReversed[i] = stationsReversed[index];
            stationsReversed[index] = temp;
        }
        if (!(Arrays.equals(stations, other.stations) || Arrays.equals(stationsReversed, other.stations))) {
            return false;
        }
        return true;
    }
}
