package me.dragonflyer.minimetro;

import me.dragonflyer.minimetro.gui.controller.Controller;
import me.dragonflyer.minimetro.gui.model.Model;

public class Main {

    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);
        controller.startUp();
    }

}
