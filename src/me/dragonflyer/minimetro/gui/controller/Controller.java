package me.dragonflyer.minimetro.gui.controller;

import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.view.View;

import javax.swing.*;
import java.awt.event.*;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
    }

    public void startUp() {
        view = new View(this, model);
        model.registerView(view);
        view.createAndShow();
    }

    public void goButtonClicked() {
        model.calculateStuff(view.getNumberOfLines(), view.getNumberOfCarriages(), view.getNumberOfTunnels());
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
