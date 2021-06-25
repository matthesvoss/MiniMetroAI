package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;

import java.util.List;

public class CircleLine extends Line {

    private Station firstAndLastStation;

    public CircleLine(List<Station> stations) {
        super(stations);
        this.firstAndLastStation = super.getFirstStation();
    }

    @Override
    public void calculateLineSections() throws NoFreePlatformException {
        super.calculateLineSections();
        super.calculateLineSection(super.getLastStation(), super.getFirstStation());
    }

    @Override
    public Station getLastStation() {
        return firstAndLastStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CircleLine other = (CircleLine) o;
        List<Station> stations = getStations();
        List<Station> otherStations = other.getStations();
        int stationsSize = stations.size();
        int otherStationsSize = otherStations.size();
        if (otherStationsSize != stationsSize) {
            return false;
        }
        // check if otherStations is rotated version of stations
        int offset = otherStations.indexOf(firstAndLastStation);
        if (offset == -1) {
            return false;
        }
        for (int i = 1; i < stationsSize; i++) {
            Station station = stations.get(i);
            Station otherStation = otherStations.get((i + offset) % otherStationsSize);
            if (!station.equals(otherStation)) {
                return false;
            }
        }
        return true;
    }

}
