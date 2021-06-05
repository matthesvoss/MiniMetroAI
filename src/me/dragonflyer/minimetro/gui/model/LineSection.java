package me.dragonflyer.minimetro.gui.model;

import java.awt.Color;
import java.awt.Point;

public class LineSection {
    private Station station1, station2;
    private int station1Direction, station1Platform, station2Direction, station2Platform;
    private Point inflectionLocation;
    private Color color;

    LineSection(Station station1, Station station2, int station1Direction, int station1Platform, int station2Direction, int station2Platform, Point inflectionLocation, Color color) {
	this.station1 = station1;
	this.station2 = station2;
	this.station1Platform = station1Platform;
	this.station2Platform = station2Platform;
	this.inflectionLocation = inflectionLocation;
	this.color = color;
    }

    public Station getStation1() {
	return station1;
    }

    public Station getStation2() {
	return station2;
    }

    public int getStation1Direction() {
	return station1Direction;
    }

    public int getStation1Platform() {
	return station1Platform;
    }

    public int getStation2Direction() {
	return station2Direction;
    }

    public int getStation2Platform() {
	return station2Platform;
    }

    public boolean hasInflectionLocation() {
	return inflectionLocation != null;
    }

    public Point getInflectionLocation() {
	return inflectionLocation;
    }

    public Color getColor() {
	return color;
    }
}
