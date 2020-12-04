package com.hakan.mapapi.utils;

import java.awt.image.BufferedImage;

public class MapImage {

    private BufferedImage bufferedImage;
    private int x = 0;
    private int y = 0;
    private boolean fullSized = false;

    public MapImage() {

    }

    public MapImage(BufferedImage bufferedImage, int x, int y, boolean fullSized) {
        setBufferedImage(bufferedImage);
        if (fullSized) {
            setX(0);
            setY(0);
        } else {
            setX(x);
            setY(y);
        }
        setFullSized(fullSized);
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        if (!this.fullSized) {
            this.x = x;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        if (!this.fullSized) {
            this.y = y;
        }
    }

    public boolean isFullSized() {
        return fullSized;
    }

    public void setFullSized(boolean fullSized) {
        this.fullSized = fullSized;
        if (fullSized) {
            setX(0);
            setY(0);
        }
    }
}