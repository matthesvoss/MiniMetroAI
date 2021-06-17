package me.dragonflyer.minimetro.ai;

import me.dragonflyer.minimetro.gui.model.entities.Line;

import java.util.List;

public class Solution {

    private List<Line> lines;

    public Solution(List<Line> lines) {
        this.lines = lines;
    }

    public List<Line> getLines() {
        return lines;
    }

}
