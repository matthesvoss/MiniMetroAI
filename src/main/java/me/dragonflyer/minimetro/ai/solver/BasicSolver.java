package me.dragonflyer.minimetro.ai.solver;

import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import me.dragonflyer.minimetro.ai.Problem;
import me.dragonflyer.minimetro.ai.Solution;
import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.Station;
import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;
import me.dragonflyer.minimetro.gui.model.geometry.IntPoint;
import org.paukov.combinatorics.CombinatoricsFactory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BasicSolver implements Solver {

    private final Model model;

    public BasicSolver(Model model) {
        this.model = model;
    }

    @Override
    public Solution solve(Problem problem) {
        // TODO: waggons, tunnel, flüsse, übersprungene stationen, scharfe kurven (zug muss anhalten)

//        List<Station> stations = problem.getStations();

        // test stations
//        stations.clear();
//        Station s1 = new Station(1, new IntPoint(0, 0), Station.Type.CIRCLE);
//        stations.add(s1);
//        Station s2 = new Station(2, new IntPoint(4, 2), Station.Type.CROSS);
//        stations.add(s2);
//        Station s3 = new Station(3, new IntPoint(3, 4), Station.Type.STAR);
//        stations.add(s3);

        // test lines
//        ArrayList<Line> lines = new ArrayList<>();
//        lines.add(new Line(new Station[]{s1, s2, s3}));
//        lines.add(new Line(new Station[]{s1, s2}));
//        lines.add(new Line(new Station[]{s1, s2, s3}));
//        lines.add(new Line(new Station[]{s1, s3}));
//        setLineColors(lines);

        Set<Solution> possibleSolutions = getPossibleSolutions(problem);
        Solution solution = getSolution(problem, possibleSolutions);

//        return new Solution(lines);
        return solution;
    }

    private Set<Solution> getPossibleSolutions(Problem problem) {
        List<Station> stations = problem.getStations();

        // calculate possible lines
        Set<Line> possibleLines = new HashSet<>();
        // TODO use depth first or breadth first search

        System.out.println("possible lines " + possibleLines.size());

        // calculate possible line combinations
        Set<Solution> possibleSolutions = new HashSet<>();
        ICombinatoricsVector<Line> vector = CombinatoricsFactory.createVector(possibleLines);
        for (int i = 1; i <= problem.getNumberOfLines(); i++) {
            Generator<Line> gen = CombinatoricsFactory.createMultiCombinationGenerator(vector, i);
            for (ICombinatoricsVector<Line> combination : gen) {
                Solution possibleSolution = new Solution(combination.getVector());
                possibleSolutions.add(possibleSolution);
            }
        }

        System.out.println("possible solutions " + possibleSolutions.size());
        return possibleSolutions;
    }

    private Solution getSolution(Problem problem, Set<Solution> possibleSolutions) {
        //TODO select best possible solution
        Solution solution = possibleSolutions.iterator().next();
        setLineColors(solution.getLines());
        solution.getLines().forEach(line -> line.setPlatformsFree(false));
        return solution;
    }

    private void setLineColors(List<Line> lines) {
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            line.setColor(model.lineColors[i]);
        }
    }

}
