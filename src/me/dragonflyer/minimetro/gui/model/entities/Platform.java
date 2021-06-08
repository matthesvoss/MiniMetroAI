package me.dragonflyer.minimetro.gui.model.entities;

public enum Platform {

    LEFT, MIDDLE, RIGHT;

    private boolean used = false;

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

}
