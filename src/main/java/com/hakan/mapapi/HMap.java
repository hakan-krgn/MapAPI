package com.hakan.mapapi;

import com.hakan.mapapi.utils.MapImage;
import com.hakan.mapapi.utils.MapText;
import com.hakan.mapapi.utils.nms.SetupNMS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class HMap {

    private final List<MapImage> mapImageList;
    private final List<MapText> mapTextList;

    public HMap(List<MapImage> mapImageList, List<MapText> mapTextList) {
        this.mapImageList = mapImageList;
        this.mapTextList = mapTextList;
    }

    public void setImage(Player player, ItemStack mapItem, MapImage mapImage, MapImage newMapImage) {
        int index = mapImageList.indexOf(mapImage);

        mapImage.setBufferedImage(newMapImage.getBufferedImage());
        mapImage.setFullSized(newMapImage.isFullSized());
        mapImage.setX(newMapImage.getX());
        mapImage.setY(newMapImage.getY());

        if (index == -1) {
            this.mapImageList.add(mapImage);
        } else {
            this.mapImageList.set(index, mapImage);
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> SetupNMS.mapPacket.sendPacket(player, mapItem, getTotalBytes(this.mapImageList, this.mapTextList)));
    }

    public void setText(Player player, ItemStack mapItem, MapText mapText, MapText newMapText) {
        int index = mapTextList.indexOf(mapText);

        mapText.setText(newMapText.getText());
        mapText.setColor(newMapText.getColor());
        mapText.setCentered(newMapText.isCentered());
        mapText.setX(newMapText.getX());
        mapText.setY(newMapText.getY());

        if (index == -1) {
            this.mapTextList.add(mapText);
        } else {
            this.mapTextList.set(index, mapText);
        }

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> SetupNMS.mapPacket.sendPacket(player, mapItem, getTotalBytes(this.mapImageList, this.mapTextList)));
    }

    public List<MapImage> getMapImageList() {
        return this.mapImageList;
    }

    public List<MapText> getMapTextList() {
        return this.mapTextList;
    }

    public ItemStack open(Player player, int slot) {
        player.getInventory().setHeldItemSlot(slot);

        ItemStack cloneItem = player.getItemInHand();

        ItemStack map = new ItemStack(getMap(), 1);

        player.setItemInHand(map);

        sendPacket(player, map, player.getInventory().getHeldItemSlot());

        return cloneItem;
    }

    public void sendPacket(Player player, ItemStack mapItem, int slot) {

        if (mapItem == null) {
            return;
        }

        MapView mapView = Bukkit.createMap(player.getWorld());
        for (MapRenderer mapRenderer : mapView.getRenderers()) {
            mapView.removeRenderer(mapRenderer);
        }
        mapView.setCenterX(player.getLocation().getBlockX());
        mapView.setCenterZ(player.getLocation().getBlockZ());
        mapView.setScale(MapView.Scale.CLOSEST);
        mapItem.setDurability(mapView.getId());

        player.getInventory().setItem(slot, mapItem);

        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), () -> SetupNMS.mapPacket.sendPacket(player, mapItem, getTotalBytes(this.mapImageList, this.mapTextList)));
    }

    private byte[] getTotalBytes(List<MapImage> mapImageList, List<MapText> mapTextList) {
        byte[] imageBytes = getImageBytes(mapImageList);
        byte[] textBytes = getTextBytes(mapTextList);
        if (textBytes.length > 0) {
            int m = 0;
            for (byte textbyte : textBytes) {
                if (textbyte != 0) {
                    imageBytes[m] = textBytes[m];
                }
                m++;
            }
        }
        return imageBytes;
    }

    private byte[] getImageBytes(List<MapImage> mapImageList) {
        if (mapImageList.size() == 0) {
            return new byte[0];
        }
        byte[] bytesCanvas = new byte[16384];
        for (MapImage mapImage : new ArrayList<>(mapImageList)) {
            BufferedImage mainImage = mapImage.getBufferedImage();
            BufferedImage bufferedImage = mapImage.isFullSized() ? MapPalette.resizeImage(mainImage.getScaledInstance(128, 128, Image.SCALE_DEFAULT)) : mainImage;
            byte[] bytes = MapPalette.imageToBytes(bufferedImage);
            for (int x2 = mapImage.getX(); x2 < bufferedImage.getWidth() + mapImage.getX(); ++x2) {
                for (int y2 = mapImage.getY(); y2 < bufferedImage.getHeight() + mapImage.getY(); ++y2) {
                    bytesCanvas[y2 * 128 + x2] = bytes[(y2 - mapImage.getY()) * bufferedImage.getWidth() + x2 - mapImage.getX()];
                }
            }
        }
        return bytesCanvas;
    }

    private byte[] getTextBytes(List<MapText> mapTextList) {
        if (mapTextList.size() == 0) {
            return new byte[0];
        }
        MapFont mapFont = MinecraftFont.Font;
        byte[] bytesCanvas = new byte[16384];
        for (MapText mapText : new ArrayList<>(mapTextList)) {
            String text = mapText.getText();
            int x = mapText.isCentered() ? 64 - (mapFont.getWidth(text) / 2) : mapText.getX();
            int y = mapText.getY();
            byte color = MapPalette.matchColor(mapText.getColor());
            for (int i = 0; i < text.length(); ++i) {
                char ch = text.charAt(i);
                if (ch == '\u00A7') {
                    int j = text.indexOf(';', i);
                    if (j >= 0) {
                        color = Byte.parseByte(text.substring(i + 1, j));
                        i = j;
                        continue;
                    }
                }
                MapFont.CharacterSprite sprite = mapFont.getChar(text.charAt(i));
                for (int r = 0; r < mapFont.getHeight(); ++r) {
                    for (int c = 0; c < sprite.getWidth(); ++c) {
                        if (sprite.get(r, c)) {
                            int i1 = (y + r) * 128 + x + c;
                            if (!sprite.get(r, c)) bytesCanvas[i1] = 0;
                            else bytesCanvas[i1] = color;
                        }
                    }
                }
                x += sprite.getWidth() + 1;
            }
        }
        return bytesCanvas;
    }

    private Material getMap() {
        Material material = null;
        String serverVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
        switch (serverVersion) {
            case "v1_8_R3":
            case "v1_9_R1":
            case "v1_9_R2":
            case "v1_10_R1":
            case "v1_11_R1":
            case "v1_12_R1":
                material = Material.valueOf("MAP");
                break;
            case "v1_13_R1":
            case "v1_13_R2":
            case "v1_14_R1":
            case "v1_15_R1":
            case "v1_16_R1":
            case "v1_16_R2":
            case "v1_16_R3":
                material = Material.valueOf("FILLED_MAP");
                break;
        }
        return material;
    }
}