package me.dragonflyer.minimetro.gui.view;

import me.dragonflyer.minimetro.gui.controller.Controller;
import me.dragonflyer.minimetro.gui.controller.MouseEventListener;
import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.model.entities.Line;
import me.dragonflyer.minimetro.gui.model.entities.LineSection;
import me.dragonflyer.minimetro.gui.model.entities.Station;
import me.dragonflyer.minimetro.gui.model.geom.DoublePoint;
import me.dragonflyer.minimetro.gui.model.geom.IntPoint;
import me.dragonflyer.minimetro.resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;

public class View extends JPanel {

    private static final long serialVersionUID = 1L;

    private Controller controller;
    private Model model;

    private JFrame frame;
    private JLabel linesLabel, carriagesLabel, tunnelsLabel;
    private JSpinner linesSpinner, carriagesSpinner, tunnelsSpinner;
    private JButton goButton;

    private Image stationSelectionImage;

    public View(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    public void createAndShow() {
        createFrame();
        createPanel();

        addResizeListener();
        addChangeListeners();
        addActionListeners();
        addMouseListeners();

        loadImages();

        frame.setVisible(true);
    }

    private void createFrame() {
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
    }

    private void createPanel() {
        setLayout(null);
        setBackground(Color.BLACK);

        linesLabel = new JLabel("Lines:", SwingConstants.CENTER);
        linesLabel.setBackground(Color.BLACK);
        linesLabel.setForeground(Color.WHITE);
        linesLabel.setOpaque(true);
        linesLabel.setBorder(null);
        linesLabel.setFont(linesLabel.getFont().deriveFont(30f));
        add(linesLabel);
        linesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 7, 1));
        linesSpinner.setBackground(Color.BLACK);
        linesSpinner.setForeground(Color.WHITE);
        linesSpinner.setOpaque(true);
        linesSpinner.setBorder(null);
        linesSpinner.setFont(linesSpinner.getFont().deriveFont(30f));
        add(linesSpinner);

        carriagesLabel = new JLabel("Carriages:", SwingConstants.CENTER);
        carriagesLabel.setBackground(Color.BLACK);
        carriagesLabel.setForeground(Color.WHITE);
        carriagesLabel.setOpaque(true);
        carriagesLabel.setBorder(null);
        carriagesLabel.setFont(carriagesLabel.getFont().deriveFont(30f));
        add(carriagesLabel);
        carriagesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        carriagesSpinner.setBackground(Color.BLACK);
        carriagesSpinner.setForeground(Color.WHITE);
        carriagesSpinner.setOpaque(true);
        carriagesSpinner.setBorder(null);
        carriagesSpinner.setFont(carriagesSpinner.getFont().deriveFont(30f));
        add(carriagesSpinner);

        tunnelsLabel = new JLabel("Tunnels:", SwingConstants.CENTER);
        tunnelsLabel.setBackground(Color.BLACK);
        tunnelsLabel.setForeground(Color.WHITE);
        tunnelsLabel.setOpaque(true);
        tunnelsLabel.setBorder(null);
        tunnelsLabel.setFont(tunnelsLabel.getFont().deriveFont(30f));
        add(tunnelsLabel);
        tunnelsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        tunnelsSpinner.setBackground(Color.BLACK);
        tunnelsSpinner.setForeground(Color.WHITE);
        tunnelsSpinner.setOpaque(true);
        tunnelsSpinner.setBorder(null);
        tunnelsSpinner.setFont(tunnelsSpinner.getFont().deriveFont(30f));
        add(tunnelsSpinner);

        goButton = new JButton("Go");
        goButton.setBackground(Color.BLACK);
        goButton.setForeground(Color.WHITE);
        goButton.setContentAreaFilled(false);
        goButton.setOpaque(true);
        goButton.setBorder(null);
        goButton.setFont(goButton.getFont().deriveFont(30f));
        add(goButton);
    }

    private void addChangeListeners() {
        linesSpinner.addChangeListener(e -> controller.numberOfLinesChanged((int) linesSpinner.getValue()));
        carriagesSpinner.addChangeListener(e -> controller.numberOfCarriagesChanged((int) carriagesSpinner.getValue()));
        tunnelsSpinner.addChangeListener(e -> controller.numberOfTunnelsChanged((int) tunnelsSpinner.getValue()));
    }

    private void addActionListeners() {
        goButton.addActionListener(e -> controller.goButtonClicked());
    }

    private void addResizeListener() {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth(), height = getHeight();
                linesLabel.setBounds(width / 2 - 290, height - 50, 100, 40);
                linesSpinner.setBounds(width / 2 - 190, height - 50, 50, 40);
                carriagesLabel.setBounds(width / 2 - 130, height - 50, 140, 40);
                carriagesSpinner.setBounds(width / 2 + 10, height - 50, 50, 40);
                tunnelsLabel.setBounds(width / 2 + 70, height - 50, 120, 40);
                tunnelsSpinner.setBounds(width / 2 + 190, height - 50, 50, 40);
                goButton.setBounds(width / 2 + 250, height - 50, 40, 40);

                controller.frameResized(width, height);
            }
        });
    }

    private void addMouseListeners() {
        MouseEventListener mouseEventListener = new MouseEventListener(controller);
        addMouseListener(mouseEventListener);
        addMouseMotionListener(mouseEventListener);
        addMouseWheelListener(mouseEventListener);
    }

    private void loadImages() {
        ResourceManager resourceManager = ResourceManager.getInstance();
        resourceManager.loadImages();
        stationSelectionImage = resourceManager.getImage("stationselection");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // create own copy of graphics object
        final Graphics2D g2d = (Graphics2D) g.create();

        try {
            drawGrid(g2d);
            drawStationBackgrounds(g2d);
            drawLineSections(g2d);
            drawStationIcons(g2d);
            drawStationSelection(g2d);
        } finally {
            g2d.dispose();
        }
    }

    private void drawGrid(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.DARK_GRAY);
        g2d.setStroke(new BasicStroke(model.gridSize / 30f));
        Point topLeft = model.locationToPoint(new IntPoint(0, 0));
        Point bottomRight = model.locationToPoint(new IntPoint(model.gridWidth, model.gridHeight));
        for (int x = 0; x <= model.gridWidth * model.gridSize; x += model.gridSize) {
            g2d.drawLine(topLeft.x + x, topLeft.y, topLeft.x + x, bottomRight.y);
        }
        for (int y = 0; y <= model.gridHeight * model.gridSize; y += model.gridSize) {
            g2d.drawLine(topLeft.x, topLeft.y + y, bottomRight.x, topLeft.y + y);
        }
    }

    private void drawStationBackgrounds(Graphics2D g2d) {
        g2d.setColor(Color.BLACK);
        for (Station station : model.getStations()) {
            Point stationBgTopLeft = model.locationToPoint(new IntPoint(station.getX() - 2, station.getY() - 2));
            g2d.fillOval(stationBgTopLeft.x, stationBgTopLeft.y, 4 * model.gridSize, 4 * model.gridSize);
        }
    }

    private void drawLineSections(Graphics2D g2d) {
        double lineWidth = model.gridSize / 10d;
        g2d.setStroke(new BasicStroke((float) lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for (Line line : model.getLines()) {
            g2d.setColor(line.getColor());
            for (LineSection lineSection : line.getLineSections()) {
                Point platform1 = model.locationToPoint(lineSection.getPlatform1Loc());
                Point platform2 = model.locationToPoint(lineSection.getPlatform2Loc());

                if (lineSection.hasInflectionLocation()) {
                    Point inflection = model.locationToPoint(lineSection.getInflectionLocation());
                    g2d.drawPolyline(new int[] { platform1.x, inflection.x, platform2.x }, new int[] {platform1.y, inflection.y, platform2.y }, 3);
                } else {
                    g2d.drawLine(platform1.x, platform1.y, platform2.x, platform2.y);
                }
            }
        }
    }

    private void drawStationIcons(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        for (Station station : model.getStations()) {
            Point stationIconTopLeft = model.locationToPoint(new DoublePoint(station.getX() - 0.4d, station.getY() - 0.4d));
            g2d.drawImage(station.getIcon(), stationIconTopLeft.x, stationIconTopLeft.y, (int) Math.round(0.8d * model.gridSize), (int) Math.round(0.8d * model.gridSize), null);
        }
    }

    private void drawStationSelection(Graphics2D g2d) {
        if (model.isLocationSelected()) {
            Point stationSelectionTopLeft = model.locationToPoint(new IntPoint(model.selectedLocation.x - 3, model.selectedLocation.y - 3));
            g2d.drawImage(stationSelectionImage, stationSelectionTopLeft.x, stationSelectionTopLeft.y, 6 * model.gridSize, 6 * model.gridSize, null);

            if (!model.isStationSelected()) {
                g2d.setColor(Color.WHITE);
                Point selectedPoint = model.locationToPoint(model.selectedLocation);
                int radius = Math.round(model.gridSize / 20f);
                fillCircle(g2d, selectedPoint.x, selectedPoint.y, radius);
            }
        }
    }

    private void fillCircle(Graphics2D g2d, int x, int y, int radius) {
        g2d.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

}
