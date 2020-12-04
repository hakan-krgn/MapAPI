package com.hakan.mapapi.utils;

import java.awt.*;

public class MapText {

    private int x = 0;
    private int y = 0;
    private boolean centered = false;
    private String text = "";
    private Color color = Color.BLACK;

    public MapText() {

    }

    public MapText(int x, int y, boolean centered, String text, Color color) {
        if (!centered) {
            setX(x);
        } else {
            setX(0);
        }
        setY(y);
        setCentered(centered);
        setText(text);
        setColor(color);
    }

    public MapText(int x, int y, boolean centered, String text, int r, int g, int b) {
        if (!centered) {
            setX(x);
        } else {
            setX(0);
        }
        setY(y);
        setCentered(centered);
        setText(text);
        setColor(new Color(r, g, b));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCentered() {
        return centered;
    }

    public void setCentered(boolean centered) {
        this.centered = centered;
        if (centered) {
            setX(0);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (!this.centered) {
            this.x = x;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (!this.centered) {
            this.y = y;
        }
    }
}