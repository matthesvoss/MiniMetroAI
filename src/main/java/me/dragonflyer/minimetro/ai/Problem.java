package me.dragonflyer.minimetro.ai;

import me.dragonflyer.minimetro.gui.model.entities.Station;

import java.util.List;

public class Problem {

    private List<Station> stations;
    private int numberOfLines, numberOfCarriages, numberOfTunnels;

    public Problem(List<Station> stations, int numberOfLines, int numberOfCarriages, int numberOfTunnels) {
        this.stations = stations;
        this.numberOfLines = numberOfLines;
        this.numberOfCarriages = numberOfCarriages;
        this.numberOfTunnels = numberOfTunnels;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public int getNumberOfCarriages() {
        return numberOfCarriages;
    }

    public int getNumberOfTunnels() {
        return numberOfTunnels;
    }

}
