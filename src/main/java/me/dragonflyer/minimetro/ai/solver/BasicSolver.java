package me.dragonflyer.minimetro.ai.solver;

import me.dragonflyer.minimetro.ai.Problem;
import me.dragonflyer.minimetro.ai.Solution;
import me.dragonflyer.minimetro.ai.util.PowerSet;
import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.Station;
import me.dragonflyer.minimetro.gui.model.exceptions.NoFreePlatformException;
import me.dragonflyer.minimetro.gui.model.geometry.IntPoint;

import java.util.*;

public class BasicSolver implements Solver {

    private final Model model;

    public BasicSolver(Model model) {
        this.model = model;
    }

    @Override
    public Solution solve(Problem problem) {
        // TODO: waggons, tunnel, flüsse, übersprungene stationen, scharfe kurven (zug muss anhalten)

        List<Station> stations = problem.getStations();

        // test stations
        stations.clear();
        Station s1 = new Station(1, new IntPoint(0, 0), Station.Type.CIRCLE);
        Station s2 = new Station(2, new IntPoint(4, 2), Station.Type.CROSS);
        Station s3 = new Station(3, new IntPoint(3, 4), Station.Type.STAR);
        stations.add(s1);
        stations.add(s2);
        stations.add(s3);

        //calculate all possible lines for numberOfLines = 1 (List<Line>)
        //zwischen den berechnungen alle platforms von allen linesections wieder free machen
        //calculate combinations of 2 possible lines
        //...
        //calculate best result
        //alle platforms von allen linesections des results wieder auf nicht free setzen

        // iterate over possible line lengths
        PowerSet<Station> stationPowerSet = new PowerSet<>(new HashSet<>(stations), 2, stations.size());
        for (Set<Station> stationSet : stationPowerSet) {
            Line line = new Line(stationSet.toArray(new Station[0]));
        }
        for (int i = 1; i < problem.getNumberOfLines(); i++) {

        }


        ArrayList<Line> lines = new ArrayList<>();
        Line l1 = new Line(new Station[]{s1, s2, s3});
        l1.setColor(model.lineColors[0]);
        lines.add(l1);
        Line l2 = new Line(new Station[]{s1, s2});
        l1.setColor(model.lineColors[1]);
        lines.add(l2);
        Line l3 = new Line(new Station[]{s1, s2, s3});
        l1.setColor(model.lineColors[2]);
        lines.add(l3);
        Line l4 = new Line(new Station[]{s1, s3});
        l1.setColor(model.lineColors[3]);
        lines.add(l4);


        for (Line line : lines) {
            try {
                line.calculateLineSections();
            } catch (NoFreePlatformException e) {
                e.printStackTrace();
            }
        }
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

        return new Solution(lines);
    }

    private Set<List<Station>> generatePermutationsNoRepetition(List<Station> availableStations) {
        Set<List<Station>> permutations = new HashSet<>();
        for (Station station : availableStations) {
            List<Station> stations = new ArrayList<>(availableStations);
            stations.remove(station);
            if (!stations.isEmpty()) {
                for (List<Station> childPermutation : generatePermutationsNoRepetition(stations)) {
                    List<Station> permutation = new ArrayList<>();
                    permutation.add(station);
                    permutation.addAll(childPermutation);
                    permutations.add(permutation);
                }
            } else {
                List<Station> permutation = new ArrayList<>();
                permutation.add(station);
                permutations.add(permutation);
            }
        }
        return permutations;
    }

    private Set<List<Station>> generateNonreversablePermutationPowerset(Set<List<Station>> permutations) {
        Set<List<Station>> possibleLines = new HashSet<>();
        for (List<Station> permutation : permutations) {
            List<Station> reversePermutation = new ArrayList<>(permutation);
            Collections.reverse(reversePermutation);
            if (!possibleLines.contains(reversePermutation)) {
                possibleLines.add(permutation);
            }
            possibleLines.addAll(generateNonreversablePermutationPowerset(permutations));
        }
        return possibleLines;
    }

}
