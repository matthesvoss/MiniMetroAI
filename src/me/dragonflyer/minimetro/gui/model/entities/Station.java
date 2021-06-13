package me.dragonflyer.minimetro.gui.model.entities;

import me.dragonflyer.minimetro.gui.model.geom.IntPoint;
import me.dragonflyer.minimetro.resources.ResourceManager;

import java.awt.*;
import java.awt.geom.Point2D;

public class Station {

    public int id;
    private IntPoint loc;
    private Type type;
    private Image icon;
    private PlatformDirection[] platforms = new PlatformDirection[8];

    public Station(int id, IntPoint loc, Type type) {
        this(loc, type);
        this.id = id;
    }

    public Station(IntPoint loc, Type type) {
        this.loc = loc;
        setType(type);

        platforms[0] = new PlatformDirection(Direction.N);
        platforms[1] = new PlatformDirection(Direction.NE);
        platforms[2] = new PlatformDirection(Direction.E);
        platforms[3] = new PlatformDirection(Direction.SE);
        platforms[4] = new PlatformDirection(Direction.S);
        platforms[5] = new PlatformDirection(Direction.SW);
        platforms[6] = new PlatformDirection(Direction.W);
        platforms[7] = new PlatformDirection(Direction.NW);
    }

    public int getId() {
        return id;
    }

    public IntPoint getLocation() {
        return loc;
    }

    public int getX() {
        return loc.x;
    }

    public int getY() {
        return loc.y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        updateIcon();
    }

    public Image getIcon() {
        return icon;
    }

    private void updateIcon() {
        ResourceManager resourceManager = ResourceManager.getInstance();
        this.icon = resourceManager.getImage(type.getName());
    }

    public PlatformDirection getPlatformDirection(Direction dir) {
        return platforms[dir.ordinal()];
    }

    public enum Type {
        CIRCLE("circle"), TRIANGLE("triangle"), SQUARE("square"), CROSS("cross"), RHOMBUS("rhombus"), LEAF("leaf"), DIAMOND("diamond"), PENTAGON("pentagon"), STAR("star"), DROP("drop");

        private String name;

        Type(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    //    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + test/* (location == null ? 0 : location.hashCode()) */;
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object other) {
//        if (this == other) {
//            return true;
//        }
//        if (!(other instanceof Station)) {
//            return false;
//        }
//        Station otherStation = (Station) other;
//        if (test != otherStation.getTest()/*
//         * location == null && otherStation.location != null ||
//         * !location.equals(otherStation.location)
//         */) {
//            return false;
//        }
//        return true;
//    }

}
