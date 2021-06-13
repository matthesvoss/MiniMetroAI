package me.dragonflyer.minimetro.ai;

import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.Station;

import java.util.List;

public interface Solver {

    List<Line> solve(List<Station> stations, int numberOfLines, int numberOfCarriages, int numberOfTunnels);

}
