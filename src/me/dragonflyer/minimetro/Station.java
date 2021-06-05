package me.dragonflyer.minimetro;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Station {
    private Point location;
    private Type type;
    private BufferedImage icon;
    private int[] platforms = new int[8];
    private int test;

    enum Type {
        CIRCLE("circle.png"), TRIANGLE("triangle.png"), SQUARE("square.png"), CROSS("cross.png"), RHOMBUS("rhombus.png"), LEAF("leaf.png"), DIAMOND("diamond.png"), PENTAGON("pentagon.png"), STAR("star.png"), DROP("drop.png");

        private String iconName;

        private Type(String iconName) {
            this.iconName = iconName;
        }

        String getIconName() {
            return iconName;
        }
    }

    Station(int test) {
        this.test = test;
    }

    Station(Point location, Type type) {
        this.location = location;
        this.type = type;
        try {
            icon = ImageIO.read(getClass().getClassLoader().getResource(type.getIconName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Point getLocation() {
        return location;
    }

    int getX() {
        return location.x;
    }

    int getY() {
        return location.y;
    }

    Type getType() {
        return type;
    }

    void setType(Type type) {
        this.type = type;
        try {
            icon = ImageIO.read(getClass().getClassLoader().getResource(type.getIconName()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BufferedImage getIcon() {
        return icon;
    }

    boolean hasFreePlatform(int direction) {
        return platforms[direction] < 3;
    }

    int getUsedPlatforms(int direction) {
        return platforms[direction];
    }

    int addPlatform(int direction) {
        return platforms[direction]++;// ++...?
    }

    int getTest() {
        return test;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + test/* (location == null ? 0 : location.hashCode()) */;
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Station)) {
            return false;
        }
        Station otherStation = (Station) other;
        if (test != otherStation.getTest()/*
         * location == null && otherStation.location != null ||
         * !location.equals(otherStation.location)
         */) {
            return false;
        }
        return true;
    }
}
