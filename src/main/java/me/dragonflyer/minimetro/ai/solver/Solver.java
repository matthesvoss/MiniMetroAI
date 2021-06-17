package me.dragonflyer.minimetro.ai.solver;

import me.dragonflyer.minimetro.ai.Problem;
import me.dragonflyer.minimetro.ai.Solution;
import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.Station;

import java.util.List;

public interface Solver {

    Solution solve(Problem problem);

}
