package com.hakan.mapapi.utils.nms.mappack;

import com.hakan.mapapi.utils.nms.MapPacket;
import net.minecraft.server.v1_8_R3.PacketPlayOutMap;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MapPacket_v1_8_R3 implements MapPacket {

    @Override
    public void sendPacket(Player player, ItemStack mapItem, byte[] bytes) {
        PacketPlayOutMap packet = new PacketPlayOutMap(mapItem.getDurability(), (byte) 0, new ArrayList<>(), bytes, 0, 0, 128, 128);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}