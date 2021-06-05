package me.dragonflyer.minimetro;

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

    Station getStation1() {
	return station1;
    }

    Station getStation2() {
	return station2;
    }

    int getStation1Direction() {
	return station1Direction;
    }

    int getStation1Platform() {
	return station1Platform;
    }

    int getStation2Direction() {
	return station2Direction;
    }

    int getStation2Platform() {
	return station2Platform;
    }

    boolean hasInflectionLocation() {
	return inflectionLocation != null;
    }

    Point getInflectionLocation() {
	return inflectionLocation;
    }

    Color getColor() {
	return color;
    }
}
