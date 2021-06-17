package me.dragonflyer.minimetro.gui.controller;

import me.dragonflyer.minimetro.ai.Problem;
import me.dragonflyer.minimetro.ai.Solution;
import me.dragonflyer.minimetro.ai.solver.BasicSolver;
import me.dragonflyer.minimetro.ai.solver.Solver;
import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.view.View;

import javax.swing.*;
import java.awt.event.*;

public class Controller {

    private Model model;
    private View view;
    private Solver solver;

    public Controller(Model model) {
        this.model = model;
        this.solver = new BasicSolver(model);
    }

    public void startUp() {
        view = new View(this, model);
        model.registerView(view);
        view.createAndShow();
    }

    public void numberOfLinesChanged(int numberOfLines) {
        model.setNumberOfLines(numberOfLines);
    }

    public void numberOfCarriagesChanged(int numberOfCarriages) {
        model.setNumberOfCarriages(numberOfCarriages);
    }

    public void numberOfTunnelsChanged(int numberOfTunnels) {
        model.setNumberOfTunnels(numberOfTunnels);
    }

    public void goButtonClicked() {
        if (model.getStations().size() < 2) {
            return;
        }

        Problem problem = new Problem(model.getStations(), model.getNumberOfLines(), model.getNumberOfCarriages(), model.getNumberOfTunnels());
        Solution solution = solver.solve(problem);
        model.setLines(solution.getLines());
        view.repaint();
    }

    public void mouseClicked(MouseEvent e) {
        model.mouseClicked(e.getButton(), e.getPoint());
    }

    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            model.leftMouseButtonPressed(e.getPoint());
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            model.leftMouseButtonDragged(e.getPoint());
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        model.mouseWheelMoved(e.getUnitsToScroll(), e.getPoint());
    }

    public void frameResized(int width, int height) {
        model.frameResized(width, height);
    }
}
