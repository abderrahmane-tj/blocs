package com.jetris.core;

import java.awt.Color;

public class Square {

    public Square(Color color) {
        this.color = color;
    }

    protected Square clone() {
        return new Square(color);
    }

    private Color color;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
