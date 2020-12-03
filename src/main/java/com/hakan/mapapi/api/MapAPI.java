package com.hakan.mapapi.api;

import com.hakan.mapapi.HMap;
import com.hakan.mapapi.Main;
import com.hakan.mapapi.utils.MapImage;
import com.hakan.mapapi.utils.MapText;
import org.bukkit.plugin.Plugin;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapAPI {

    public MapAPI() {
    }

    public static MapManager getMapManager() {
        return new MapManager();
    }

    public static void setup(Plugin plugin) {
        Main.setInstance(plugin);
    }

    public static class MapManager {

        private final List<MapImage> mapImageList = new ArrayList<>();
        private final List<MapText> mapTextList = new ArrayList<>();

        public MapManager setImage(MapImage mapImage) {
            this.mapImageList.add(mapImage);
            return this;
        }

        public MapManager setImage(BufferedImage bufferedImage, int x, int y, boolean fullSized) {
            setImage(new MapImage(bufferedImage, x, y, fullSized));
            return this;
        }

        public MapManager setImage(Image image, int x, int y, boolean fullSized) {
            setImage(new MapImage(toBufferedImage(image), x, y, fullSized));
            return this;
        }

        public MapManager setImage(String weburl, int x, int y, boolean fullSized) {
            setImage(new MapImage(getImage(weburl), x, y, fullSized));
            return this;
        }

        public MapManager setImage(File file, int x, int y, boolean fullSized) {
            setImage(new MapImage(getImage(file), x, y, fullSized));
            return this;
        }

        public MapManager addText(MapText mapText) {
            this.mapTextList.add(mapText);
            return this;
        }

        public MapManager addText(String text, Color color, int x, int y) {
            addText(new MapText(x, y, false, text, color));
            return this;
        }

        public MapManager addText(String text, Color color, boolean centeredX, int y) {
            addText(new MapText(0, y, centeredX, text, color));
            return this;
        }

        public MapManager addText(String text, int r, int g, int b, int x, int y) {
            addText(new MapText(x, y, false, text, r, g, b));
            return this;
        }

        public MapManager addText(String text, int r, int g, int b, boolean centeredX, int y) {
            addText(new MapText(0, y, centeredX, text, r, g, b));
            return this;
        }

        private BufferedImage getImage(String webUrl) {
            try {
                return ImageIO.read(new URL(webUrl));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private BufferedImage getImage(File file) {
            try {
                return ImageIO.read(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private BufferedImage toBufferedImage(Image img) {
            if (img instanceof BufferedImage) {
                return (BufferedImage) img;
            }
            BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = bimage.createGraphics();
            bGr.drawImage(img, 0, 0, null);
            bGr.dispose();
            return bimage;
        }

        public HMap create() {
            return new HMap(this.mapImageList, this.mapTextList);
        }
    }
}