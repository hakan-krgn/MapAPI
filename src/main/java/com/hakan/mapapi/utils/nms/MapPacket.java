package com.hakan.mapapi.utils.nms;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MapPacket {

    void sendPacket(Player player, ItemStack mapItem, byte[] bytes);

}