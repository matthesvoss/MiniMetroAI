package me.dragonflyer.minimetro.gui.controller;

import me.dragonflyer.minimetro.gui.model.Model;
import me.dragonflyer.minimetro.gui.view.View;

public class Controller {

    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
    }

    public void startUp() {
        view = new View(this, model);
    }
}
