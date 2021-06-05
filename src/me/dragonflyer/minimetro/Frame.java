package me.dragonflyer.minimetro;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import me.dragonflyer.minimetro.Station.Type;

public class Frame extends JPanel {
    private static final long serialVersionUID = 1L;
    private JFrame frame;
    private BufferedImage stationSelection;
    private int gridSize = 39, maxGridSize = 150, gridWidth = 44, gridHeight = 26;// 48x30 3 gleise pro ausgang, 8 ausgänge, ubersprungene stationen kennzeichnen
    private Point2D.Double oldCenter = new Point2D.Double(), center = new Point2D.Double(22, 13);
    private Point p, currentLocation;
    private boolean mouseDragged;
    private HashSet<Station> stations = new HashSet<>();
    private HashSet<Line> lines = new HashSet<>();
    private Color[] lineColors = new Color[] { new Color(225, 23, 30), new Color(81, 39, 132), new Color(0, 118, 187), new Color(1, 150, 99), new Color(218, 74, 141), new Color(121, 67, 31), new Color(156, 188, 75) };

    public static void main(String[] args) {
        new Frame();
    }

    private Frame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame = new JFrame("Mini Metro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 900);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(null);
        frame.setContentPane(this);
        setLayout(null);
        setBackground(Color.BLACK);

        JLabel linesLabel = new JLabel("Lines:", SwingConstants.CENTER);
        linesLabel.setBackground(Color.BLACK);
        linesLabel.setForeground(Color.WHITE);
        linesLabel.setOpaque(true);
        linesLabel.setBorder(null);
        linesLabel.setFont(linesLabel.getFont().deriveFont(30f));
        add(linesLabel);
        JSpinner linesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 7, 1));
        linesSpinner.setBackground(Color.BLACK);
        linesSpinner.setForeground(Color.WHITE);
        linesSpinner.setOpaque(true);
        linesSpinner.setBorder(null);
        linesSpinner.setFont(linesSpinner.getFont().deriveFont(30f));
        add(linesSpinner);

        JLabel carriagesLabel = new JLabel("Carriages:", SwingConstants.CENTER);
        carriagesLabel.setBackground(Color.BLACK);
        carriagesLabel.setForeground(Color.WHITE);
        carriagesLabel.setOpaque(true);
        carriagesLabel.setBorder(null);
        carriagesLabel.setFont(carriagesLabel.getFont().deriveFont(30f));
        add(carriagesLabel);
        JSpinner carriagesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        carriagesSpinner.setBackground(Color.BLACK);
        carriagesSpinner.setForeground(Color.WHITE);
        carriagesSpinner.setOpaque(true);
        carriagesSpinner.setBorder(null);
        carriagesSpinner.setFont(carriagesSpinner.getFont().deriveFont(30f));
        add(carriagesSpinner);

        JLabel tunnelsLabel = new JLabel("Tunnels:", SwingConstants.CENTER);
        tunnelsLabel.setBackground(Color.BLACK);
        tunnelsLabel.setForeground(Color.WHITE);
        tunnelsLabel.setOpaque(true);
        tunnelsLabel.setBorder(null);
        tunnelsLabel.setFont(tunnelsLabel.getFont().deriveFont(30f));
        add(tunnelsLabel);
        JSpinner tunnelsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        tunnelsSpinner.setBackground(Color.BLACK);
        tunnelsSpinner.setForeground(Color.WHITE);
        tunnelsSpinner.setOpaque(true);
        tunnelsSpinner.setBorder(null);
        tunnelsSpinner.setFont(tunnelsSpinner.getFont().deriveFont(30f));
        add(tunnelsSpinner);

        JButton go = new JButton("Go");
        go.setBackground(Color.BLACK);
        go.setForeground(Color.WHITE);
        go.setContentAreaFilled(false);
        go.setOpaque(true);
        go.setBorder(null);
        go.setFont(go.getFont().deriveFont(30f));
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {// waggons, tunnel, flüsse, übersprungene stationen, scharfe kurven (zug muss
                // anhalten)
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
                lines.clear();

                int numberOfLines = (int) linesSpinner.getValue();
                int numberOfCarriages = (int) carriagesSpinner.getValue();
                int numberOfTunnels = (int) tunnelsSpinner.getValue();

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

                stations.add(new Station(1));
                stations.add(new Station(2));
                stations.add(new Station(3));
                stations.add(new Station(4));
                HashSet<ArrayList<Station>> permutations = generatePermutationsNoRepetition(stations);
                HashSet<ArrayList<Station>> subPermutations = new HashSet<>();
                for (ArrayList<Station> permutation : permutations) {
                    while (permutation.size() > 2) {
                        permutation = new ArrayList<>(permutation.subList(0, permutation.size() - 1));
                        subPermutations.add(permutation);
                    }
                }
                permutations.addAll(subPermutations);
                // TODO

                HashSet<Line> possibleLines = new HashSet<>();
                for (ArrayList<Station> permutation : permutations) {
                    possibleLines.add(new Line(permutation.toArray(new Station[permutation.size()])));
                }
                for (Line line : possibleLines) {
                    Station[] stations = line.getStations();
                    for (int i = 0; i < stations.length; i++) {
                        Station station = stations[i];
                        System.out.print(station.getTest() + (i != stations.length - 1 ? "," : ""));
                    }
                    System.out.println();
                }
                System.exit(0);

                ArrayList<Line> calcLines = new ArrayList<>();
                calcLines.add(new Line(stations.toArray(new Station[stations.size()])));
                int colorIndex = 0;
                for (Line calcLine : calcLines) {
                    Station[] stations = calcLine.getStations();
                    Line line = new Line(stations);
                    Color color = lineColors[colorIndex];

                    for (int j = 0; j < stations.length - 1; j++) {
                        Station station1 = stations[j], station2 = stations[j + 1];
                        int x1 = station1.getX(), y1 = station1.getY(), x2 = station2.getX(), y2 = station2.getY();
                        if (x1 == x2) {
                            if (y1 < y2) {
                                line.addLineSection(station1, station2, 4, 0, color);
                            } else {
                                line.addLineSection(station1, station2, 0, 4, color);
                            }
                        } else if (y1 == y2) {
                            if (x1 < x2) {
                                line.addLineSection(station1, station2, 2, 6, color);
                            } else {
                                line.addLineSection(station1, station2, 6, 2, color);
                            }
                        } else {
                            int xDiff = x2 - x1, yDiff = y2 - y1, absXDiff = Math.abs(xDiff), absYDiff = Math.abs(yDiff);
                            if (absXDiff == absYDiff) {
                                int station1Platform, station2Platform;
                                if (xDiff > 0) {
                                    if (yDiff < 0) {
                                        station1Platform = 1;
                                        station2Platform = 5;
                                    } else {
                                        station1Platform = 3;
                                        station2Platform = 7;
                                    }
                                } else {
                                    if (yDiff > 0) {
                                        station1Platform = 5;
                                        station2Platform = 1;
                                    } else {
                                        station1Platform = 7;
                                        station2Platform = 3;
                                    }
                                }
                                line.addLineSection(station1, station2, station1Platform, station2Platform, color);
                            } else {
                                int diagonalDiff = Math.min(absXDiff, absYDiff);
                                if (absXDiff < absYDiff) {
                                    if (xDiff > 0) {
                                        if (yDiff < 0) {
                                            if (!line.addLineSection(station1, station2, 0, 5, new Point(x1, y2 + diagonalDiff), color)) {
                                                line.addLineSection(station1, station2, 1, 4, new Point(x2, y1 - diagonalDiff), color);
                                            }
                                        } else {
                                            if (!line.addLineSection(station1, station2, 3, 0, new Point(x2, y1 + diagonalDiff), color)) {
                                                line.addLineSection(station1, station2, 4, 7, new Point(x1, y2 - diagonalDiff), color);
                                            }
                                        }
                                    } else {
                                        if (yDiff > 0) {
                                            if (!line.addLineSection(station1, station2, 4, 1, new Point(x1, y2 - diagonalDiff), color)) {
                                                line.addLineSection(station1, station2, 5, 0, new Point(x2, y1 + diagonalDiff), color);
                                            }
                                        } else {
                                            if (!line.addLineSection(station1, station2, 7, 4, new Point(x2, y1 - diagonalDiff), color)) {
                                                line.addLineSection(station1, station2, 0, 3, new Point(x1, y2 + diagonalDiff), color);
                                            }
                                        }
                                    }
                                } else {
                                    if (xDiff > 0) {
                                        if (yDiff < 0) {
                                            if (!line.addLineSection(station1, station2, 1, 6, new Point(x1 + diagonalDiff, y2), color)) {// &&?
                                                line.addLineSection(station1, station2, 2, 5, new Point(x2 - diagonalDiff, y1), color);
                                            }
                                        } else {
                                            if (!line.addLineSection(station1, station2, 2, 7, new Point(x2 - diagonalDiff, y1), color)) {// &&?
                                                line.addLineSection(station1, station2, 3, 6, new Point(x1 + diagonalDiff, y2), color);
                                            }
                                        }
                                    } else {
                                        if (yDiff > 0) {
                                            if (!line.addLineSection(station1, station2, 5, 2, new Point(x1 - diagonalDiff, y2), color)) {// &&?
                                                line.addLineSection(station1, station2, 6, 1, new Point(x2 + diagonalDiff, y1), color);
                                            }
                                        } else {
                                            if (!line.addLineSection(station1, station2, 6, 3, new Point(x2 + diagonalDiff, y1), color)) {// &&?
                                                line.addLineSection(station1, station2, 7, 2, new Point(x1 - diagonalDiff, y2), color);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    lines.add(line);
                    colorIndex++;
                }
                repaint();
            }
        });
        add(go);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                checkGridSize();
                checkBounds();
                int width = getWidth(), height = getHeight();
                linesLabel.setBounds(width / 2 - 290, height - 50, 100, 40);
                linesSpinner.setBounds(width / 2 - 190, height - 50, 50, 40);
                carriagesLabel.setBounds(width / 2 - 130, height - 50, 140, 40);
                carriagesSpinner.setBounds(width / 2 + 10, height - 50, 50, 40);
                tunnelsLabel.setBounds(width / 2 + 70, height - 50, 120, 40);
                tunnelsSpinner.setBounds(width / 2 + 190, height - 50, 50, 40);
                go.setBounds(width / 2 + 250, height - 50, 40, 40);
            }
        });

        try {
            stationSelection = ImageIO.read(getClass().getClassLoader().getResource("stationselection.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    p = e.getPoint();
                    oldCenter.setLocation(center);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!mouseDragged) {
                    if (currentLocation == null) {
                        p = getClosestLocation(e.getPoint());
                        if (e.getButton() == 1 && rangeCheck(p)) {
                            currentLocation = p;
                            repaint();
                        } else if (e.getButton() == 3) {
                            Station station = getStationOnLocation(p);
                            if (station != null) {
                                stations.remove(station);
                                repaint();
                            }
                        }
                    } else {
                        Point currentPoint = locationToPoint(currentLocation);
                        double distance = p.distance(currentPoint);
                        if (e.getButton() == 1 && distance >= 1.16d * gridSize && distance <= 3d * gridSize) {
                            Station station = getStationOnLocation(currentLocation);
                            Type type = getTypeForDegree(Math.toDegrees(Math.atan2(p.y - currentPoint.y, p.x - currentPoint.x)));
                            if (station == null) {
                                stations.add(new Station(currentLocation, type));
                            } else if (station.getType() != type) {
                                station.setType(type);
                            }
                        }
                        currentLocation = null;
                        repaint();
                    }
                }
                mouseDragged = false;
            }

            private Point getClosestLocation(Point p) {
                Point2D.Double loc = pointToLocation(p);
                return new Point((int) Math.round(loc.x), (int) Math.round(loc.y));
            }

            private boolean rangeCheck(Point p) {
                for (int x = -2; x <= 2; x++) {
                    for (int y = -2; y <= 2; y++) {
                        if (!(x == 0 && y == 0) && !(x != 0 && y == -2) && !(x == 2 && y != 0) && !(x != 0 && y == 2) && !(x == -2 && y != 0) && isStationOnLocation(new Point(p.x + x, p.y + y))) {
                            return false;
                        }
                    }
                }
                return true;
            }

            private Station getStationOnLocation(Point p) {
                for (Station station : stations) {
                    if (station.getLocation().equals(p)) {
                        return station;
                    }
                }
                return null;
            }

            private boolean isStationOnLocation(Point p) {
                return getStationOnLocation(p) != null;
            }

            private Type getTypeForDegree(double degree) {
                if (degree > 0.72d && degree <= 38.44d) {
                    return Type.CROSS;
                } else if (degree > 38.44d && degree <= 69.94d) {
                    return Type.RHOMBUS;
                } else if (degree > 69.94d && degree <= 105.99d) {
                    return Type.LEAF;
                } else if (degree > 105.99d && degree <= 141.81d) {
                    return Type.DIAMOND;
                } else if (degree > 141.81d && degree <= 180d) {
                    return Type.PENTAGON;
                } else if (degree > -180d && degree <= -145.26d) {
                    return Type.STAR;
                } else if (degree > -145.26d && degree <= -108.9d) {
                    return Type.DROP;
                } else if (degree > -108.9d && degree <= -68.88d) {
                    return Type.CIRCLE;
                } else if (degree > -68.88d && degree <= -35.2d) {
                    return Type.TRIANGLE;
                } else if (degree > -35.2d && degree <= 0.72d) {
                    return Type.SQUARE;
                }
                return null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    mouseDragged = true;
                    double xDiff = e.getX() - p.x, yDiff = e.getY() - p.y;
                    center.setLocation(oldCenter.x - xDiff / gridSize, oldCenter.y - yDiff / gridSize);
                    checkBounds();
                    repaint();
                }
            }
        });

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int prevGridSize = gridSize;
                gridSize -= e.getUnitsToScroll();
                checkGridSize();
                if (prevGridSize - gridSize < 0) {
                    Point scrollPoint = e.getPoint();
                    double xDiff = scrollPoint.x - getWidth() / 2d, yDiff = scrollPoint.y - getHeight() / 2d;
                    double xLoc = center.x + xDiff / prevGridSize, yLoc = center.y + yDiff / prevGridSize;
                    center.setLocation(xLoc - xDiff / gridSize, yLoc - yDiff / gridSize);
                }
                checkBounds();
                repaint();
            }
        });

        frame.setVisible(true);
//	ArrayList<Station> availableStations = new ArrayList<>();
//	availableStations.add(new Station(new Point(1, 1), Type.CIRCLE));
//	availableStations.add(new Station(new Point(1, 2), Type.TRIANGLE));
//	availableStations.add(new Station(new Point(2, 1), Type.SQUARE));
//	availableStations.add(new Station(new Point(2, 2), Type.DIAMOND));
//	ArrayList<ArrayList<Station>> stationPermutations = generatePermutationsNoRepetition(availableStations);
//	for (ArrayList<Station> permutation : stationPermutations) {
//	    for (Station station : permutation) {
//		station.print();
//		System.out.print(",");
//	    }
//	    System.out.println();
//	}
    }

    private Point locationToPoint(Point p) {
        return locationToPoint(new Point2D.Double(p.x, p.y));
    }

    private Point locationToPoint(Point2D.Double p) {
        return new Point((int) Math.round(getWidth() / 2d + (p.x - center.x) * gridSize), (int) Math.round(getHeight() / 2d + (p.y - center.y) * gridSize));
    }

    private Point2D.Double pointToLocation(Point p) {
        return new Point2D.Double(center.x + (p.x - getWidth() / 2d) / gridSize, center.y + (p.y - getHeight() / 2d) / gridSize);
    }

    private void checkGridSize() {
        int minGridSize = Math.max((int) Math.ceil((double) getWidth() / (gridWidth + 6)), (int) Math.ceil((double) getHeight() / (gridHeight + 6)));
        gridSize = Math.max(minGridSize, Math.min(gridSize, maxGridSize));
    }

    private void checkBounds() {
        double minX = getWidth() / 2d / gridSize - 3d, minY = getHeight() / 2d / gridSize - 3d;
        center.setLocation(Math.max(minX, Math.min(center.x, gridWidth - minX)), Math.max(minY, Math.min(center.y, gridHeight - minY)));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        // draw grid
        g2.setColor(Color.DARK_GRAY);
        Point topLeft = locationToPoint(new Point(0, 0)), bottomRight = locationToPoint(new Point(gridWidth, gridHeight));
        for (int x = 0; x <= gridWidth * gridSize; x += gridSize) {
            g2.drawLine(topLeft.x + x, topLeft.y, topLeft.x + x, bottomRight.y);
        }
        for (int y = 0; y <= gridHeight * gridSize; y += gridSize) {
            g2.drawLine(topLeft.x, topLeft.y + y, bottomRight.x, topLeft.y + y);
        }

        // draw station background
        g2.setColor(Color.BLACK);
        for (Station station : stations) {
            Point stationPoint = locationToPoint(new Point(station.getX() - 2, station.getY() - 2));
            g2.fillOval(stationPoint.x, stationPoint.y, 4 * gridSize, 4 * gridSize);
        }

        // draw linesections
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(gridSize / 10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Line line : lines) {
            for (LineSection lineSection : line.getLineSections()) {
                Point station1Point = locationToPoint(lineSection.getStation1().getLocation()), station2Point = locationToPoint(lineSection.getStation2().getLocation());
                int x1 = station1Point.x, y1 = station1Point.y, x2 = station2Point.x, y2 = station2Point.y;
                int[] station1Offsets = new int[2], station2Offsets = new int[2];
                int offset = (int) Math.ceil(gridSize / 10d);
                int diagonalOffset = (int) Math.ceil(gridSize / (10d * Math.sqrt(2d)));

                int station1Direction = lineSection.getStation1Direction();
                int station1Platform = lineSection.getStation1Platform();
                int station1UsedPlatforms = lineSection.getStation1().getUsedPlatforms(station1Direction);
                if (station1UsedPlatforms == 2 && station1Platform == 1 || station1UsedPlatforms == 3 && station1Platform == 2) {
                    station1Offsets = getOffsetsForDirection(station1Direction, offset, diagonalOffset, false);
                } else if (station1UsedPlatforms == 3 && station1Platform == 0) {
                    station1Offsets = getOffsetsForDirection(station1Direction, offset, diagonalOffset, true);
                }

                int station2Direction = lineSection.getStation2Direction();
                int station2Platform = lineSection.getStation2Platform();
                int station2UsedPlatforms = lineSection.getStation2().getUsedPlatforms(station2Direction);
                if (station2UsedPlatforms == 2 && station2Platform == 1 || station2UsedPlatforms == 3 && station2Platform == 2) {
                    station2Offsets = getOffsetsForDirection(station2Direction, offset, diagonalOffset, false);
                } else if (station2UsedPlatforms == 3 && station2Platform == 0) {
                    station2Offsets = getOffsetsForDirection(station2Direction, offset, diagonalOffset, true);
                }

                g2.setColor(lineSection.getColor());
                if (!lineSection.hasInflectionLocation()) {
                    g2.drawLine(x1 + station1Offsets[0], y1 + station1Offsets[1], x2 + station2Offsets[0], y2 + station2Offsets[1]);
                } else {
                    Point inflectionPoint = locationToPoint(lineSection.getInflectionLocation());
                    g2.drawPolyline(new int[] { x1 + station1Offsets[0], inflectionPoint.x, x2 + station2Offsets[0] }, new int[] { y1 + station1Offsets[1], inflectionPoint.y, y2 + station2Offsets[1] }, 3);
                }
            }
        }

        // draw station icons
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        for (Station station : stations) {
            Point stationPoint = locationToPoint(new Point2D.Double(station.getX() - 0.4d, station.getY() - 0.4d));
            g2.drawImage(station.getIcon(), stationPoint.x, stationPoint.y, (int) Math.round(0.8d * gridSize), (int) Math.round(0.8d * gridSize), null);
        }
        // draw station selection
        if (currentLocation != null) {
            Point currentPoint = locationToPoint(new Point(currentLocation.x - 3, currentLocation.y - 3));
            g2.drawImage(stationSelection, currentPoint.x, currentPoint.y, 6 * gridSize, 6 * gridSize, null);
        }
    }

    private int[] getOffsetsForDirection(int direction, int offset, int diagonalOffset, boolean inverted) {
        int[] offsets = new int[2];
        switch (direction) {
            case 0:
                offsets[0] = offset;
                break;
            case 1:
                offsets[0] = diagonalOffset;
                offsets[1] = diagonalOffset;
                break;
            case 2:
                offsets[1] = offset;
                break;
            case 3:
                offsets[0] = -diagonalOffset;
                offsets[1] = diagonalOffset;
                break;
            case 4:
                offsets[0] = -offset;
                break;
            case 5:
                offsets[0] = -diagonalOffset;
                offsets[1] = -diagonalOffset;
                break;
            case 6:
                offsets[1] = -offset;
                break;
            case 7:
                offsets[0] = diagonalOffset;
                offsets[1] = -diagonalOffset;
                break;
        }
        if (inverted) {
            offsets[0] = -offsets[0];
            offsets[1] = -offsets[1];
        }
        return offsets;
    }

    private HashSet<ArrayList<Station>> generatePermutationsNoRepetition(HashSet<Station> availableStations) {
        HashSet<ArrayList<Station>> permutations = new HashSet<>();
        for (Station station : availableStations) {
            HashSet<Station> stations = new HashSet<>(availableStations);
            stations.remove(station);
            if (!stations.isEmpty()) {
                for (ArrayList<Station> childPermutation : generatePermutationsNoRepetition(stations)) {
                    ArrayList<Station> permutation = new ArrayList<>();
                    permutation.add(station);
                    permutation.addAll(childPermutation);
                    permutations.add(permutation);
                }
            } else {
                ArrayList<Station> permutation = new ArrayList<>();
                permutation.add(station);
                permutations.add(permutation);
            }
        }
        return permutations;
    }

    /*
     * private HashSet<ArrayList<Station>>
     * generateNonreversablePermutationPowerset(HashSet<ArrayList<Station>>
     * permutations) { HashSet<ArrayList<Station>> possibleLines = new HashSet<>();
     * for (ArrayList<Station> permutation : permutations) { ArrayList<Station>
     * reversePermutation = new ArrayList<>(permutation);
     * Collections.reverse(reversePermutation); if
     * (!possibleLines.contains(reversePermutation)) {
     * possibleLines.add(permutation); }
     *
     * possibleLines.addAll(generateNonreversablePermutationPowerset(permutations));
     * } return possibleLines; }
     */
}

//if (absXDiff < absYDiff) {
//    if (xDiff > 0) {
//    	if (yDiff < 0) {
//    	    inflectionLocation = new Point(x1, y2 + diagonalDiff);(x1, y2 + xDiff)
//    	    inflectionLocation = new Point(x2, y1 - diagonalDiff);(x2, y1 - xDiff)
//    	} else {
//    	    inflectionLocation = new Point(x2, y1 + diagonalDiff);(x2, y1 + xDiff)
//    	    inflectionLocation = new Point(x1, y2 - diagonalDiff);(x1, y2 - xDiff)
//    	}
//    } else {
//    	if (yDiff > 0) {
//    	    inflectionLocation = new Point(x1, y2 - diagonalDiff);(x1, y2 + xDiff)
//    	    inflectionLocation = new Point(x2, y1 + diagonalDiff);(x2, y1 - xDiff)
//    	} else {
//    	    inflectionLocation = new Point(x2, y1 - diagonalDiff);(x2, y2)
//    	    inflectionLocation = new Point(x1, y2 + diagonalDiff);(x1, y2)
//    	}
//    }
//    } else {
//    if (xDiff > 0) {
//    	if (yDiff < 0) {
//    	    inflectionLocation = new Point(x1 + diagonalDiff, y2);(x1, y2)
//    	    inflectionLocation = new Point(x2 - diagonalDiff, y1);(x1, y1)
//    	} else {
//    	    inflectionLocation = new Point(x2 - diagonalDiff, y1);(x1, y1)
//    	    inflectionLocation = new Point(x1 + diagonalDiff, y2);(x1, y2)
//    	}
//    } else {
//    	if (yDiff > 0) {
//    	    inflectionLocation = new Point(x1 - diagonalDiff, y2);(x1, y2)
//    	    inflectionLocation = new Point(x2 + diagonalDiff, y1);(x1, y1)
//    	} else {
//    	    inflectionLocation = new Point(x2 + diagonalDiff, y1);(x1, y1)
//    	    inflectionLocation = new Point(x1 - diagonalDiff, y2);(x1, y2)
//    	}
//    }
//}