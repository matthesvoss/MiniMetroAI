package me.dragonflyer.minimetro.gui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseEventListener extends MouseAdapter {

    private Controller controller;

    public MouseEventListener(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.mouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        controller.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        controller.mouseWheelMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        controller.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

}
