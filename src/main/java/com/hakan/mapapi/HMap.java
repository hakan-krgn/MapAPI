package com.hakan.mapapi;

import com.hakan.mapapi.utils.MapImage;
import com.hakan.mapapi.utils.MapText;
import com.hakan.mapapi.utils.nms.SetupNMS;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

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
        BufferedImage bufferedImage = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        if (mapImageList.size() != 0) {
            for (MapImage mapImage : new ArrayList<>(mapImageList)) {
                BufferedImage mainImage = mapImage.getBufferedImage();
                BufferedImage resizedImage = mapImage.isFullSized() ? MapPalette.resizeImage(mainImage.getScaledInstance(128, 128, Image.SCALE_DEFAULT)) : mainImage;
                graphics2D.drawImage(resizedImage, null, mapImage.getX(), mapImage.getY());
            }
        }
        if (mapTextList.size() != 0) {
            for (MapText mapText : new ArrayList<>(mapTextList)) {
                String text = mapText.getText();
                Font font = mapText.getFont();
                graphics2D.setFont(font);
                graphics2D.setColor(mapText.getColor());
                int x = mapText.isCentered() ? (int) (64 - (graphics2D.getFontMetrics().stringWidth(text) / 2.0)) : mapText.getX();
                int y = mapText.getY();
                graphics2D.drawString(text, x, y);
            }
        }
        graphics2D.dispose();
        byte[] bytesCanvas = new byte[16384];
        byte[] bytes = MapPalette.imageToBytes(bufferedImage);
        for (int x2 = 0; x2 < bufferedImage.getWidth(); x2++) {
            for (int y2 = 0; y2 < bufferedImage.getHeight(); y2++) {
                bytesCanvas[y2 * 128 + x2] = bytes[y2 * bufferedImage.getWidth() + x2];
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