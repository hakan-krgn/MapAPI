package com.hakan.mapapi.utils.nms;

import com.hakan.mapapi.utils.nms.mappack.*;
import org.bukkit.Bukkit;

public class SetupNMS {

    public static MapPacket mapPacket;

    public void setup() {
        String serverVersion = Bukkit.getServer().getClass().getName().split("\\.")[3];
        switch (serverVersion) {
            case "v1_8_R3":
                mapPacket = new MapPacket_v1_8_R3();
                break;
            case "v1_9_R1":
                mapPacket = new MapPacket_v1_9_R1();
                break;
            case "v1_9_R2":
                mapPacket = new MapPacket_v1_9_R2();
                break;
            case "v1_10_R1":
                mapPacket = new MapPacket_v1_10_R1();
                break;
            case "v1_11_R1":
                mapPacket = new MapPacket_v1_11_R1();
                break;
            case "v1_12_R1":
                mapPacket = new MapPacket_v1_12_R1();
                break;
            case "v1_13_R1":
                mapPacket = new MapPacket_v1_13_R1();
                break;
            case "v1_13_R2":
                mapPacket = new MapPacket_v1_13_R2();
                break;
            case "v1_14_R1":
                mapPacket = new MapPacket_v1_14_R1();
                break;
            case "v1_15_R1":
                mapPacket = new MapPacket_v1_15_R1();
                break;
            case "v1_16_R1":
                mapPacket = new MapPacket_v1_16_R1();
                break;
            case "v1_16_R2":
                mapPacket = new MapPacket_v1_16_R2();
                break;
            case "v1_16_R3":
                mapPacket = new MapPacket_v1_16_R3();
                break;
        }
    }
}